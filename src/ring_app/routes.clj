(ns ring-app.routes
  (:require
   [ring.util.http-response :as response]
   [muuntaja.middleware :refer [wrap-format]]
   [ring-app.db.core :as db]))

;; testing examples
(defn response-handler [request]
  (response/ok
   (str "<html><body> your IP is: "
        (:remote-addr request)
        "</body></html>")))

(defn user-getter [_]
  (response/ok
   (db/get-all-users-db)))

(def routes
  [["/" {:get response-handler
         :post response-handler}]
   ["/echo/:id"
    {:get
     (fn [{{:keys [id]} :path-params}]
       (response/ok (str "<p>Connection successful. Received value is: " id "</p>")))}]
   ["/api"
    {:middleware [wrap-format]}
    ["/get-user/:id"
     {:get
      (fn [{{:keys [id]} :path-params}]
        (if (string? id) ;;redundant? check if string parses to int
          (response/ok (db/get-user-db {:id id}))
          (response/ok (str "Invalid datatype passed. Expected int; received " (type id)))))}]
    ["/register-user"
     {:post
      (fn [{{:keys [username pass role description]} :body-params}]
        (response/ok (db/register-user! {:username username :pass pass :role role :description description})))}] ;; FIX ME!!!
    ["/login"
     {:post
      (fn [{{:keys [username pass]} :body-params}]
        (response/ok (db/login {:username username :pass pass})))}]

    ["/debug-test" ;;post response test; returns
     {:post
      (fn [{{:keys [username pass]} :body-params}]
        (response/ok {:username (type username) :pass (type pass)}))}]
    ["/debug-response" ;; return full response
     {:post
      (fn [received-response]
        (response/ok (str received-response)))}]
    ["/get-users"
     {:get user-getter}]]])

