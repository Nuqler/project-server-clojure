(ns ring-app.routes
  (:require
   [ring.util.http-response :as response]
   [muuntaja.middleware :refer [wrap-format]]
   [ring-app.db.core :as db]
   [ring-app.auth :as auth]
   ;;----
   [ring.util.response :refer [redirect file-response]]
   [ring-app.uploader :as uploader]
   [ring-app.python-shell :as ps]
   [ring.middleware [multipart-params :as mp]]))

;; testing examples
(defn response-handler [request]
  (response/ok
   (str "<html><body> your IP is: "
        (:remote-addr request)
        "</body></html>")))

;; -- TOKEN HANDLING--

;; TODO

;; -- TOKEN HANDLING --

;;TODO: Check routes for redundancy. Due to lack of authorization ATM some routes may be duplicated.

(def routes
  [["/" {:get response-handler
         :post response-handler}]
   ["/echo/:input"
    {:get
     (fn [{{:keys [input]} :path-params}]
       (response/ok (str "<p>Connection successful. Received value is: " input "</p>")))}]
   ["/file"
    {:middleware [mp/wrap-multipart-params]}
    ;;TODO: refactor into 1 consice function.
    ["/upload-user-picture"
     {:post
      (fn [request]
        (let [file (get (:params request) "file")]
          (uploader/upload-file uploader/avatar-path file)
          (redirect (str "/user-picture/" (:filename file)))
          (response/ok  {:result "Upload successful."})))}]
    ["/user-picture/:filename"
     {:get
      (fn [{{:keys [filename]} :path-params}]
        (file-response (str uploader/avatar-path filename)))}]
    ["/upload-training-set"
     ;;TODO: receive ZIPPPED files. Work with that.
     (fn [request]
       (let [file (get (:params request) "file")]
         (uploader/upload-file uploader/training-path file)
         (redirect (str "/training-set/" (:filename file)))
         (println "LOG: training set received.")
         (uploader/unpack-and-remove)
         (ps/train-recognizer)
         (println "LOG: training complete.")
         (response/ok {:result "Upload successful."})))]]
   ["/api"
    {:middleware [wrap-format]}
    ["/rating"
     ["/get-daily-task-status"
      {:post
       (fn [{{:keys [userid date]} :body-params}]
         (response/ok (db/get-task-status-db {:UserID userid :Date date})))}]
     ["/get-rooms"
      {:get
       (fn [_]
         (response/ok (db/get-rooms-db)))}]
     ["/update-rating"
      {:post
       (fn [{{:keys [userid timeonrest timeonwork rating date]} :body-params}]
         (if-not (= 0 (db/update-rating-db! {:UserID userid :TimeOnRest timeonrest :TimeOnWork timeonwork :Rating rating :Date date}))
           (response/ok {:result "Rating update succesful."})
           (response/bad-request {:result "Error. Could not update rating. Incorrect UserID?"})))}]]
    ;; ----- CAMERA -----
    ;; ----- GET ML FILE -----
    ["/camera"
     ["/get-ml-file"
      {:get
       (fn [_]
         (file-response (str uploader/ML-file)))}]]
    ;; ----- USER -----
    ["/user"
     ["/check/:email" ;; Get 'name', 'surname', 'userid' if input email exists. To be used in end application.
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
           (response/bad-request {:result "Incorrect credentials."})))}]
     ["/beta-login-token"
      {:post
       (fn [{{:keys [email password]} :body-params}]
         (if-let [user (auth/login-get-token-test2 {:Email email :Password password})]
           (response/ok user)
           (response/bad-request {:result "Incorrect credentials."})))}]
     ["/beta-logout-token"
      {:post
       (fn [{{:keys [email]} :body-params}]
         (response/ok (auth/logout-token-test {:Email email})))}]
     ["/get-daily-task-status"
      {:post
       (fn [{{:keys [userid date]} :body-params}]
         (response/ok (db/get-task-status-db {:UserID userid :Date date})))}]
     ["/get-last-week-rating"
      {:post
       (fn [{{:keys [userid]} :body-params}]
         (response/ok (db/get-last-week-rating-db {:UserID userid})))}]
     ["/get-last-N-days-rating"
      {:post
       (fn [{{:keys [userid number]} :body-params}]
         (response/ok (db/get-last-N-days-rating-db {:UserID userid :Number number})))}]
     ["/set-daily-question"
      {:post
       (fn [{{:keys [userid workfinished date]} :body-params}]
         (if-not (nil? (db/set-daily-question-db! {:UserID userid :WorkFinished workfinished :Date date}))
           (response/ok {:result "Answer received."})
           (response/bad-request {:result "Error. Answer already received?."})))}]
     ["/group"
      ["/add-member"
       {:post
        (fn [{{:keys [userid groupid]} :body-params}]
          (let [status (db/add-group-member-db! {:UserID userid :GroupID groupid})]
            (if-not (= 0 status)
              (response/ok {:result "Successfuly added a group member."})
              (response/bad-request {:result "Error. Could not add a group member."}))))}]
      ["/remove-member"
       {:post
        (fn [{{:keys [userid groupid]} :body-params}]
          (let [status (db/remove-group-member-db! {:UserID userid :GroupID groupid})]
            (if-not (= 0 status) ;; TODO: FIX STATUS BUG. ALWAYS RETURNS POSITIVE?
              (response/ok {:result "Successfuly removed a member."})
              (response/bad-request {:result "Error. Could not remove a member. Member not found?"}))))}]
      ["/get-employees"
       {:get
        (fn [_]
          (response/ok (db/get-emps-list-db)))}]
      ["/get-group-members/:groupid"
       {:get
        (fn [{{:keys [groupid]} :path-params}]
          (response/ok (db/get-group-members-db {:GroupID groupid})))}]
      ["/edit-group-info"
       {:post
        (fn [{{:keys [groupid groupname description]} :body-params}]
          (let [status (db/update-group-db! {:GroupID groupid :GroupName groupname :Description description})]
            (if-not (= 0 status)
              (response/ok {:result "Edit successful"})
              (response/bad-request {:result "Error. Failed to edit group information."}))))}]
      ["/get-group/:groupid"
       {:get
        (fn [{{:keys [groupid]} :path-params}]
          (response/ok (db/get-group-db {:GroupID groupid})))}]]]
    ;; ----- PROJECT MANAGER -----
    ["/pm"
     ["/get-employees"
      {:get
       (fn [_]
         (response/ok (db/get-emps-list-db)))}]
     ["/get-controlled-groups/:projectmanagerid"
      {:get
       (fn [{{:keys [projectmanagerid]} :path-params}]
         (response/ok (db/get-controlled-groups-db {:ProjectManagerID projectmanagerid})))}]
     ["/promote-to-leader" ;; REQUIRES USERID AND PM ID
      {:post
       (fn [{{:keys [userid projectmanagerid]} :body-params}]
         (let [status (db/promote-to-lead-db! {:UserID userid :ProjectManagerID projectmanagerid})]
           (if (= 1 status)
             (response/ok {:result "Promotion successful."})
             (response/bad-request {:result "Error. Could not make a promotion."}))))}]
     ["/demote-to-employee"
      {:post
       (fn [{{:keys [userid]} :body-params}]
         (let [status (db/demote-to-emp-db! {:UserID userid})]
           (if (= 1 status)
             (response/ok {:result "Succesfully demoted."})
             (response/bad-request {:result "Error. Could not demote user."}))))}]]

    ;; ----- ADMIN -----
    ["/admin"
     ["/register-user"
      {:post
       (fn [{{:keys [name surname address residencecountry nationality sex email password phonenumber birthdaydate roleid]} :body-params}]
         (if (= true (db/register-user!
                      {:Name name :Surname surname :Address address :ResidenceCountry residencecountry :Nationality nationality :Sex sex :Email email :Password password :PhoneNumber phonenumber :BirthdayDate birthdaydate :RoleID roleid})) ;; TODO: Prettify
           (response/ok {:result "Success."})
           (response/conflict {:result "Failed. User with such email already exists."})))}] ;;TODO: change format
     ["/get-user/:parameter"  ;; get user by ID or EMAIL.
      {:get
       (fn [{{:keys [parameter]} :path-params}]
         (if (re-matches #".+\@.+\..+" parameter)
           (if-let [user (db/get-user-db {:email parameter})]
             (response/ok user)
             (response/bad-request {:result "User with such EMAIL does not exist."}))
           (if-let [user (db/get-user-db {:userid parameter})] ;;TODO: rewrite in more concise form
             (response/ok user)
             (response/bad-request {:result "User with such ID does not exist."}))))}]
     ["/get-users"
      {:get
       (fn [_]
         (response/ok (db/get-all-users-db)))}]
     ["/remove-user"
      {:post
       (fn [{{:keys [userid]} :body-params}]
         (response/ok (db/remove-user-db! {:UserID userid})))}]
     ["/update-user-data"
      {:post
       (fn [{{:keys [userid name surname address residencecountry nationality sex email password phonenumber birthdaydate roleid]} :body-params}]
         (if (= 1 (db/update-user-db! {:UserID userid :Name name :Surname surname :Address address :ResidenceCountry residencecountry :Nationality nationality :Sex sex :Email email :Password password :PhoneNumber phonenumber :BirthdayDate birthdaydate :RoleID roleid}))
           (response/ok {:result "User data update successful."})
           (response/bad-request {:result "ERROR. User with such ID not found."})))}]
     ["/get-all-groups"
      {:get
       (fn [_]
         (response/ok (db/get-all-groups-db)))}]
     ["/get-group-members/:groupid"
      {:get
       (fn [{{:keys [groupid]} :path-params}]
         (response/ok (db/get-group-members-db {:GroupID groupid})))}]
     ["/edit-group-info" ;; TODO: replace with contemporary code from USER
      {:post
       (fn [{{:keys [groupid groupname description]} :body-params}]
         (if (= 1 (db/update-group-db! {:GroupID groupid :GroupName groupname :Description description}))
           (response/ok {:result "Group information update succesful."})
           (response/bad-request {:result "Error. No group with such ID."})))}]]]])
