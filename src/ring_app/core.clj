(ns ring-app.core
  (:require
   [reitit.ring :as reitit]
   [ring.adapter.jetty :as jetty]
   [muuntaja.middleware :as muuntaja]
   [ring.util.http-response :as response]
   [ring.middleware.reload :refer [wrap-reload]]
   [muuntaja.middleware :refer [wrap-format]]
   [clojure.java.jdbc :as sql]
   [ring-app.db.core :as db]))


(defn response-handler [request]
  (response/ok
   (str "<html><body> your IP is: "
        (:remote-addr request)
        "</body></html>")))

(defn user-getter [request]
  (response/ok
   (db/get-all-users)))

;; (defn db-response-handler [request]
;;   (response/ok
;;    (db/eval-hug-sql request)))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn wrap-formats [handler]
  (-> handler
      (muuntaja/wrap-format)))

(def routes
  [["/" {:get response-handler
         :post response-handler}]
   ["/echo/:id"
    {:get
     (fn [{{:keys [id]} :path-params}]
       (response/ok (str "<p>the value is: " id "</p>")))}]
   ["/api"
    {:middleware [wrap-format]}
    ["/get-user/:id"
     {:get
      (fn [{{:keys [id]} :path-params}]
        (response/ok (db/get-user id)))}]
    ["/add-user"
     {:post
      (fn [{{:keys [id pass]} :path-params}]
        (response/ok (db/add-user! {:id id :pass pass})))}] ;; FIX ME!!!!!!!!!!!!!!!!!
    ["/get-users"
     {:get user-getter}]
    ["/hug-test"
     {:get (fn [_]
             (response/ok
              (db/hug-test)))}]]])

(def handler
  (reitit/routes
   (reitit/ring-handler
    (reitit/router routes))
   (reitit/create-default-handler
    {:not-found
     (constantly (response/not-found "404 - Page not found"))
     :method-not-allowed
     (constantly (response/method-not-allowed "405 - Not allowed"))
     :not-acceptable
     (constantly (response/not-acceptable "406 - Not acceptable"))})))

(defn -main []
  (jetty/run-jetty
   (-> #'handler
       wrap-nocache
       wrap-reload)
   {:port 3000
    :join? false}))


