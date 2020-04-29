(ns ring-app.db.core
  (:require
   [next.jdbc :as sql]
   ;;[clojure.java.jdbc :as sql]
   [hugsql.core :as hugsql]
   [hugsql.adapter.next-jdbc :as next-adapter]
   [clojure.edn :as edn]))

;; Get the hugsql queries
(hugsql/def-db-fns "queries.sql")

;;(hugsql/set-adapter! (next-adapter/hugsql-adapter-next-jdbc))

(def db
  (try
    (edn/read-string (slurp "config.edn"))
    (catch java.io.FileNotFoundException e
      (println "--- DB Configuration file not found! ---"))))

;;TODO: shutdown server if no file found

(def ds
  (if (nil? db)
    (println "ERROR: Unable to setup DB configruation. Please provide 'config.edn' file and restart the server!")
    (sql/get-datasource db)))

;; create a 'users' table if it does not exist.
;;TODO: Prettify; add verbosity.
(create-users-table-if-not-exists! db)


;; ----------- SQL QUERY FUNCTIONS -----------
;;TODO: REMOVE PLAINTEXT PASSWORDS
;;TODO: Unify username and password check (less queries)

(declare register-user!)
(declare add-user-db!)

(defn user-exists? [{:keys [username]}]
  (not (nil? (get-login-data db {:username username}))))

(defn pass-correct? [login-details] ;; very basic pass validation. 
  (= (:pass (get-login-data db login-details)) (:pass login-details)))

(defn get-user-db [id-map]
  (let [user (get-user-id db id-map)]
    (if (nil? user)
      {:result "User not found!"}
      user)))

(defn add-user-db! [user-details] ;; add/register a new user
  (if (user-exists? (select-keys user-details [:username]))
    {:result "Username already exists!"}
    (add-user! db user-details)))

(defn remove-user-db! [id-map] ;; remove a user from DB
  (if (remove-user! db id-map)
    {:result "Success."}
    {:result "Fail."}))

(defn get-all-users-db [] ;; test function to get all the users from DB
  (get-all-users db))

(defn register-user! [user-details]
  (if (= 1 (add-user-db! user-details))
    {:result "Success"}
    {:result "Fail. Username already exists?"}))

(defn login [login-details] ;; debug login test data. sends all available user data on success (excludes password).
  (if (and (user-exists? login-details) (pass-correct? login-details))
    (get-user-details db login-details)
    {:result "Incorrect username or password!"}))

(defn debug-login [login-details]
  (get-login-data db login-details)) ;;check DB functionality

;; (defn add-users-db! [user]
;;   (add-users! db))
