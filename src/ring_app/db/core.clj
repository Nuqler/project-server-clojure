(ns ring-app.db.core
  (:require [clojure.java.jdbc :as sql]
            [hugsql.core :as hugsql]))

;; REMEMBER TO START POSTGRE SERVER SERVICE! or else java.connection exception may be raised.
;;

(hugsql/def-db-fns "queries.sql")


;; TEMPORARY SOLUTION FOR TESTING
(def db {:subprotocol "postgresql"
         :subname "//localhost/reporting"
         :user "admin"
         :password "admin"})

(defn create-users-table! []
  (sql/db-do-commands db
                      (sql/create-table-ddl
                       :users
                       [[:id "varchar(32) PRIMARY KEY"]
                        [:pass "varchar(100)"]])))

;;TODO: CHANGE PLAINTEXT PASSWORDS

(defn get-user [id]
  (first (sql/query db ["select * from users where id = ?" id])))

(defn get-all-users []
  (sql/query db ["select * from users"]))

(defn add-user! [user]
  (sql/insert! db :users user))


(defn add-user! [user]
  (sql/insert! db :users user))
