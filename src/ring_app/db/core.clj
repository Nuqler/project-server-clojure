(ns ring-app.db.core
  (:require
   [buddy.hashers :as hashers]
   [next.jdbc :as sql]
   [hugsql.core :as hugsql]
   [hugsql.adapter.next-jdbc :as next-adapter]
   [clojure.edn :as edn]))

;; Evaluatee hug-sql expressions
(dorun (map #(hugsql/def-db-fns %) ["queries.sql" "table-creation.sql"]))

(hugsql/set-adapter! (next-adapter/hugsql-adapter-next-jdbc))

(def ^:private db
  (try
    (edn/read-string (slurp "config.edn"))
    (catch java.io.FileNotFoundException e
      (println "--- DB Configuration file not found! ---"))))


;;TODO: Prettify; add verbosity.
;;TODO: shutdown server if no file found

;; create a 'users' table if it does not exist.
(defn create-tables []
  "Populates DB with tables if they do not exist"
  (println "Checking DB tables...")
  (let [status (get-in (create-user-table-if-not-exists! db) [0 :next.jdbc/update-count])]
    (if (= -1 status) ;; Status 0 means succesful creation. Status -1 means table already exists.
      (println "Found User table.")
      (println "Table User not found. Creating table..."))))


;; ----------- SQL QUERY FUNCTIONS -----------

(declare register-user!)
(declare add-user-db!)

(defn user-exists? [{:keys [Email]}] ;;check if user with such email exists
  (not (nil? (get-login-data db {:Email Email}))))

;; (defn pass-correct? [login-details] ;; very basic pass validation.
;;   (= (:pass (get-login-data db login-details)) (:pass login-details)))

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

;; (defn add-users-db! [user]
;;   (add-users! db))
