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
       (response/ok {:result (str "Connection successful. The received value is: " id)}))}]
   ["/api"
    {:middleware [wrap-format]}
    ["/get-user/:id"
     {:get
      (fn [{{:keys [id]} :path-params}]
        (if-let [user (db/get-user-db {:id id})]
          (response/ok user)
          (response/bad-request {:result "User with such ID does not exist."})))}]
    ["/register-user"
     {:post
      (fn [request]
        (if (= true (db/register-user! (:body-params request)))
            (response/ok {:result "Registration successful."})
            (response/conflict {:result "Registration failed. Selected username already exists."})))}] ;;TODO: change format
    ["/remove-user"
     {:post
      (fn [{{:keys [id]} :body-params}]
        (response/ok (db/remove-user-db! {:id id})))}]
    ["/login"
     {:post
      (fn [{{:keys [username pass]} :body-params}]
        (if-let [status (db/login {:username username :pass pass})]
          (response/ok status)
          (response/bad-request {:result "Incorrect username or login."})))}]
    ["/debug-response" ;; debug
     {:post
      (fn [request]
        (response/ok (str (request :body-params))))}]
    ["/get-users"
     {:get
      (fn [_]
        (response/ok (db/get-all-users-db)))}]]])
