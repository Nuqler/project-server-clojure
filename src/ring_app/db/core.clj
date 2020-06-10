(ns ring-app.db.core
  (:require
   [buddy.hashers :as hashers]
   [next.jdbc :as sql]
   [hugsql.core :as hugsql]
   [hugsql.adapter.next-jdbc :as next-adapter]
   [clojure.edn :as edn]))

;; Evaluatee hug-sql expressions
(dorun (map #(hugsql/def-db-fns %) ["queries.sql" "table-creation.sql" "sup-tables.sql" "debug-populate-tables.sql"]))

(hugsql/set-adapter! (next-adapter/hugsql-adapter-next-jdbc))

(def ^:private db
  (try
    (edn/read-string (slurp "config.edn"))
    (catch java.io.FileNotFoundException e
      (println "--- DB Configuration file not found! ---"))))


;;TODO: Prettify; add verbosity.
;;TODO: shutdown server if no file found
;;TODO: Change output type in SQL files to eliminate awkward :next.jdbc output!

;; create a 'users' table if it does not exist.

(defn create-tables-debug []
  "Temporary func to initialize all necessary tables."
  (println "Preparing tables....")
  (do
    (create-user-table! db)
    (create-role-table! db)
    (create-room-table! db)
    (create-ratingday-table! db)
    (create-group-table! db)
    (create-user-group-table! db)
    (println "Done.")))

(defn create-tables []
  "Populates DB with tables if they do not exist"
  (println "Checking DB tables...")
  (let [status (get-in (create-user-table! db) [0 :next.jdbc/update-count])]
    (if (= -1 status) ;; Status 0 means succesful creation. Status -1 means table already exists.
      (println "Found User table.")
      (println "Table User not found. Creating table..."))))

;; ----------- SQL QUERY FUNCTIONS -----------

(declare register-user!)
(declare add-user-db!)

(defn user-exists? [{:keys [Email]}] ;;check if user with such email exists
  (not (nil? (get-login-data db {:Email Email}))))

(defn credentials-correct? [{:keys [Email Password]}]
  (when-let [user (get-login-data-with-password db {:Email Email})]
    (hashers/check Password (:password user))))

(defn get-user-db [credentials-map]
  (letfn [(get-by-id [credentials-map]
            (let [user (get-user-by-id db credentials-map)]
              (if-not (nil? user)
                user)))
          (get-by-email [credentials-map]
            (let [user (get-user-by-email db credentials-map)]
              (if-not (nil? user)
                user)))]
    (cond
      (not= 1 (count credentials-map)) {:result "Error. Received JSON size is incorrect. Object should contain only one parameter: ID or EMAIL."}
      (contains? credentials-map :userid) (get-by-id credentials-map)
      (contains? credentials-map :email) (get-by-email credentials-map))))

(defn add-user-db! [user-details] ;; add/register a new user
  (if-not (user-exists? (select-keys user-details [:Email]))
    (add-user! db (update user-details :Password #(hashers/derive %)))))

(defn remove-user-db! [id-map] ;; remove a user from DB
  (let [status (get-in (remove-user! db id-map) [0 :next.jdbc/update-count])]
    (if (= 1 status) ;; Status = 1 found and removed; 0 = not found.
      {:result "Success."}
      {:result "Fail. No user with such UserID found."})))

(defn get-all-users-db [] ;; test function to get all the users from DB
  (get-all-users db))

(defn register-user! [user-details] ;; returns true if the user registration is successful. False if not.
  (= 1 (add-user-db! user-details)))

(defn login [login-details] ;; debug login test data. sends all available user data on success (excludes password).
  (if (credentials-correct? login-details)
    (get-user-details-DEBUG db login-details))) ;;TODO: change debug sql func

(defn get-login-data-db [credentials-map]
  (get-login-data db credentials-map))

;; TODO: Remove all this redundant boilerplate code
;; ----------- ADMIN -----------
;; Some old code does not get to be moved there in order not to break existing code structure.
;; -- CREATE DEFAULT ADMIN ACCOUNT ON FIRST STARTUP --

;; ((let [users (:count (get-number-of-users db))]
;;    (if (= 0 users)
;;      (add-user-db!
;;       {:Name "Admin" :Surname "Admin" :Address "Falling tower 114" :ResidenceCountry "USA" :Nationality "American" :Sex "1" :Email "admin" :Password "admin" :PhoneNumber "+777712345678" :BirthdayDate "1990-10-15" :RoleID "2"}))))

;;(add-user-db! {:Name "Admin" :Surname "Admin" :Address "Falling tower 114" :ResidenceCountry "USA" :Nationality "American" :Sex "1" :Email "admin" :Password "admin" :PhoneNumber "+777712345678" :BirthdayDate "1990-10-15" :RoleID "2"})


(defn get-all-groups-db []
  (get-all-groups db))

(defn update-user-db! [user-details]
 ;; (if (user-exists? (select-keys user-details [:Email])) ;; status 1 = successful update, 0 = fail.
    (update-user-data! db (update user-details :Password #(hashers/derive %))))

(defn update-group-db! [group-details] ;; update group details (name and description); also accessed by GR role
  (update-group-data! db group-details)) ;; status 1 = successful update; 0 = fail.
;; ----------- ADMIN -----------

;; ----------- USER -----------


(defn set-daily-question-db! [user-details]
  "Set first daily question status"
  (let [status (:count (count-daily-task db user-details))
        workfinished (:workfinished (get-daily-task-status db user-details))]
    (if (= 0 status)
      (add-daily-question! db user-details)
      (if (= 2 workfinished)
        (update-daily-question! db user-details)))))

(defn get-task-status-db [task-details]
  "Get daily task status. If there is no record, creates a new record with status 2 = unanswered."
  (let [status (:count (count-daily-task db task-details))]
    (if (= 0 status)
      (do
        (add-daily-question! db {:UserID (:UserID task-details) :WorkFinished 2 :Date (:Date task-details)})
        (get-daily-task-status db task-details))
      (get-daily-task-status db task-details))))

(defn get-last-week-rating-db [user-details]
  (get-last-week-rating db user-details))

(defn get-last-N-days-rating-db [user-details]
  (get-last-N-days-rating db user-details))

;; ----------- USER -----------

;; ----------- GROUPS -----------
(defn create-group-db! [group-details] ;; redundant?
  (create-new-group! db group-details))

(defn add-group-member-db! [user-details]
  (add-group-member! db user-details))

(defn remove-group-member-db! [user-details]
  (remove-group-member! db user-details))

(defn remove-group-db! [group-details] ;; redundant?
  (remove-group! db group-details))

(defn get-group-members-db [group-details]
  (get-group-members db group-details))

(defn get-group-db [group-details]
  (get-group-data db group-details))

;; ----------- GROUPS -----------


;; ----------- PROJECT MANAGER -----------
(defn get-emps-list-db []
  (get-employees-list db))

(defn get-controlled-groups-db [user-details]
  "Receive a ProjectManagerID and get all groups that this person controlls."
  (get-controlled-groups-list db user-details))

(defn promote-to-lead-db! [user-details]
  "Promote employee to a GL role. Requires both Employee and a project manager ID!"
  (let [group (get-group-by-leader db {:GroupLeaderID (:UserID user-details)})]
    (if-not (empty? group)
        (str "Already promoted!")
        (do
          (promote-to-leader! db {:UserID (:UserID user-details)})
          (create-new-group! db {:GroupName "New Group" :Description "None" :GroupLeaderID (:UserID user-details) :ProjectManagerID (:ProjectManagerID user-details)})
          (add-group-leader! db {:UserID (:UserID user-details)})))))

;; TODO: rewrite this change!
(defn demote-to-emp-db! [user-details] ;; demote person back to EMP role. Delete a group and its members.
  (if-let [group (get-group-by-leader db {:GroupLeaderID (:UserID user-details)})]
    (do
      (demote-from-leader! db {:UserID (:UserID user-details)})
      (remove-group-with-leader! db {:UserID (:UserID user-details)}))))
;; ----------- PROJECT MANAGER -----------

;; ----------- REMOTE RATING -----------
(defn get-rooms-db []
  (get-rooms db))

(defn update-rating-db! [user-details]
  "Update RATING record if exists. Otherwise add new."
  (let [rating (:count (count-ratings db user-details))]
    (if (= 0 rating)
      (add-rating! db user-details)
      (update-rating! db user-details))))

;; ----------- REMOTE RATING -----------
