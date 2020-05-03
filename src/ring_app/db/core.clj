(ns ring-app.db.core
  (:require
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

;;TODO: shutdown server if no file found

;; (def ds
;;   (if (nil? db)
;;     (println "ERROR: Unable to setup DB configruation. Please provide 'config.edn' file and restart the server!")
;;     (sql/get-datasource db)))

;; create a 'users' table if it does not exist.
;;TODO: Prettify; add verbosity.

(defn create-tables []
  "Populates DB with tables if they do not exist"
  (println "Checking DB tables...")
  (let [status (get (first (first (create-user-table-if-not-exists! db))) 1)] ;;TODO: Rewrite this mess!
    (if (= -1 status) ;; Status 0 means succesful creation. Status -1 means table already exists.
      (println "Found User table.")
      (println "Table User not found. Creating table..."))))


;; ----------- SQL QUERY FUNCTIONS -----------
;;TODO: ENCRYPT PLAINTEXT PASSWORDS
;;TODO: Unify username and password check (less queries)

(declare register-user!)
(declare add-user-db!)

(defn user-exists? [{:keys [Email]}] ;;check if user with such email exists
  (not (nil? (get-login-data db {:Email Email}))))

;; (defn pass-correct? [login-details] ;; very basic pass validation.
;;   (= (:pass (get-login-data db login-details)) (:pass login-details)))

(defn credentials-correct? [{:keys [Email Password]}]
  (when-let [user (get-login-data-with-password db {:Email Email})]
    (= (:password user) Password)))

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
    (add-user! db user-details)))

(defn remove-user-db! [id-map] ;; remove a user from DB
  (let [status (get (first (first (remove-user! db id-map))) 1)] ;;TODO: Rewrite this mess!
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
