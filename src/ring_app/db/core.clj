(ns ring-app.db.core
  (:require
   [next.jdbc :as sql]
   [hugsql.core :as hugsql]
;;   [hugsql.adapter.next-jdbc :as next-adapter]
   [clojure.edn :as edn]))

;; Get the hugsql queries
;;(hugsql/def-db-fns "queries-v2.sql")
(hugsql/def-db-fns "queries.sql")

;;(hugsql/set-adapter! (next-adapter/hugsql-adapter-next-jdbc))

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
(create-users-table-if-not-exists! db)


;; ----------- SQL QUERY FUNCTIONS -----------
;;TODO: ENCRYPT PLAINTEXT PASSWORDS
;;TODO: Unify username and password check (less queries)

(declare register-user!)
(declare add-user-db!)

(defn user-exists? [{:keys [username]}]
  (not (nil? (get-login-data db {:username username}))))

;; (defn pass-correct? [login-details] ;; very basic pass validation.
;;   (= (:pass (get-login-data db login-details)) (:pass login-details)))

(defn credentials-correct? [{:keys [username pass]}]
  (if-let [db-data (get-login-data db {:username username})]
    (= (:pass db-data) pass)))

(defn get-user-db [id-map]
  (let [user (get-user-id db id-map)]
    (if-not (nil? user)
      user)))

(defn add-user-db! [user-details] ;; add/register a new user
  (if-not (user-exists? (select-keys user-details [:username]))
    (add-user! db user-details)))

(defn remove-user-db! [id-map] ;; remove a user from DB
  (if (remove-user! db id-map)
    {:result "Success."}
    {:result "Fail."}))

(defn get-all-users-db [] ;; test function to get all the users from DB
  (get-all-users db))

(defn register-user! [user-details] ;; returns true if the user registration is successful. False if not.
  (= 1 (add-user-db! user-details)))

(defn login [login-details] ;; debug login test data. sends all available user data on success (excludes password).
  (if (credentials-correct? login-details)
    (get-user-details db login-details)))

(defn debug-login [login-details]
  (get-login-data db login-details)) ;;check DB functionality

;; (defn add-users-db! [user]
;;   (add-users! db))
