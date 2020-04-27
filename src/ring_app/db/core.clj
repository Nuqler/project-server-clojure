(ns ring-app.db.core
  (:require
   [next.jdbc :as sql]
   ;;[clojure.java.jdbc :as sql]
   [hugsql.core :as hugsql]
   [clojure.edn :as edn]))

;; Get the hugsql queries
(hugsql/def-db-fns "queries.sql")

(def db
  (try
    (edn/read-string (slurp "config.edn"))
    (catch java.io.FileNotFoundException e
      (println "--- DB Configuration file not found! ---"))))

;;TODO: shutdown server if no file found

(def ds
  (if (nil? db)
    (println "ERROR: Unable to setup DB configruation.")
    (sql/get-datasource db)))

(defn create-users-table! []
  (try
    (sql/execute! ds
                  ["CREATE TABLE users (id int IDENTITY(1,1) PRIMARY KEY,
username varchar(30),
pass varchar(100),
role varchar(16),
description varchar(255),
registration_date DATETIME NOT NULL DEFAULT GETDATE())"])
    (catch com.microsoft.sqlserver.jdbc.SQLServerException e
    (str "ERROR: Table already exists!"))))

;;TODO: REMOVE PLAINTEXT PASSWORDS
(declare register-user!)
(declare add-user-db!)

(defn user-exists? [username]
  (if (not (nil? (get-login-data db {:username username})))
    true
    false))

(defn pass-correct? [login-details];;----------------------------------------
  (if (= (:pass (get-login-data db login-details)) (:pass login-details))
    true
    false))

(defn get-user-db [id]
  (let [user (get-user-id db id)]
    (if (nil? test)
      (str "User not found!")
      user)))

(defn add-user-db! [user-details]
  (if (user-exists? (select-keys user-details [:username]))
    (str "Username already exists!")
    (add-user! db user-details)))

(defn remove-user-db! [id]
  (remove-user! db {:id id}))

(defn get-all-users-db [_]
  (get-all-users db))

(defn register-user! [user-details]
  (if (= 1 (add-user-db! user-details))
    (str "Success")
    (str "Fail. Username already exists?"))) ;;RIP


(defn login [login-details] ;; debug login test data. sends all available user data on success.
  (if (and (user-exists? login-details) (pass-correct? login-details))
    (get-user-details db login-details)
    (str "Incorrect username or password!")))

(defn debug-login [login-details]
  (get-login-data db login-details)) ;;check DB functionality

;; (defn add-users-db! [user]
;;   (add-users! db))
