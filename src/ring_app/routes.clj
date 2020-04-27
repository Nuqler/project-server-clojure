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
          (response/ok (db/get-user-db {:id id})))}]
    ["/register-user"
     {:post
      (fn [{{:keys [username pass role description]} :body-params}]
        (response/ok (db/register-user! {:username username :pass pass :role role :description description})))}] ;;TODO: change format
    ["/remove-user"
     {:post
      (fn [{{:keys [id]} :body-params}]
        (response/ok (db/remove-user-db! {:id id})))}]
    ["/login"
     {:post
      (fn [{{:keys [username pass]} :body-params}]
        (response/ok (db/login {:username username :pass pass})))}]
    ["/debug-response" ;; return full response
     {:post
      (fn [received-response]
        (response/ok (str received-response)))}]
    ["/get-users"
     {:get
      (fn [_]
        (response/ok (db/get-all-users-db)))}]]])
