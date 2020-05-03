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
   ["/echo/:input"
    {:get
     (fn [{{:keys [input]} :path-params}]
       (response/ok {:result (str "<p>Connection successful. Received value is: " input "</p>")}))}]
   ["/api"
    {:middleware [wrap-format]}
    ["/get-user/:parameter"  ;; get user by ID or EMAIL. Debug feature.
     {:get
      (fn [{{:keys [parameter]} :path-params}]
        (if (re-matches #".+\@.+\..+" parameter)
          (if-let [user (db/get-user-db {:email parameter})]
            (response/ok user)
            (response/bad-request {:result "User with such EMAIL does not exist."}))
          (if-let [user (db/get-user-db {:userid parameter})] ;;TODO: rewrite in more concise form
            (response/ok user)
            (response/bad-request {:result "User with such ID does not exist."}))))}]
    ["/register-user"
     {:post
      (fn [{{:keys [name surname address residencecountry nationality sex email password phonenumber birthdaydate roleid]} :body-params}]
        (if (= true (db/register-user!
                     {:Name name :Surname surname :Address address :ResidenceCountry residencecountry :Nationality nationality :Sex sex :Email email :Password password :PhoneNumber phonenumber :BirthdayDate birthdaydate :RoleID roleid})) ;; TODO: Prettify
          (response/ok {:result "Success."})
          (response/conflict {:result "Failed. User with such email already exists."})))}] ;;TODO: change format
    ["/remove-user"
     {:post
      (fn [{{:keys [userid]} :body-params}]
        (response/ok (db/remove-user-db! {:UserID userid})))}]
    ["/user/:email" ;; Get 'name', 'surname', 'userid' if input email exists. To be used in end application.
     {:get
      (fn [{{:keys [email]} :path-params}]
        (if-let [user (db/get-login-data-db {:Email email})]
          (response/ok user)
          (response/bad-request {:result "User with specified email not found."})))}]
    ["/login"
     {:post
      (fn [{{:keys [email password]} :body-params}]
        (if-let [user (db/login {:Email email :Password password})]
          (response/ok user)
          (response/bad-request {:result "Incorrect username or password."})))}]
    ["/get-users"
     {:get
      (fn [_]
        (response/ok (db/get-all-users-db)))}]]])
