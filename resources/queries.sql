-- :name create-users-table-if-not-exists!
-- :command :execute
-- :result :raw
-- :doc check if the 'users' table exists
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[users]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[users] (
id int IDENTITY(1,1) PRIMARY KEY,
username varchar(30),
pass varchar(100),
role varchar(16),
description varchar(255),
registration_date  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
)

-- :name create-users-table!
-- :command :execute
-- :result :raw
-- :doc create a users table
CREATE TABLE users (
id int IDENTITY(1,1) PRIMARY KEY,
username varchar(30),
pass varchar(100),
role varchar(16),
description varchar(255),
registration_date  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
)

-- :name drop-users-table :!
-- :doc Drop users table if exists
DROP TABLE IF EXISTS users

-- :name add-user! :! :n
-- :doc adds a new user
INSERT INTO users
(username, pass, role, description)
VALUES (:username, :pass, :role, :description)

-- :name add-users! :! :n
-- :doc add multiple users
INSERT INTO users
(id, username, pass, role, description)
VALUES :t*:users

-- :name get-user-id :? :1
-- find the user with a matching ID
SELECT *
FROM users
WHERE id = :id

-- :name get-user-username :? :1
-- find the user with a matching USERNAME
SELECT *
FROM users
WHERE username = :username

-- :name get-user-details :? :*
-- :doc selects all available details for selected user (excludes password)
SELECT * INTO #TempTable
FROM users
WHERE username = :username
ALTER TABLE #TempTable
DROP COLUMN pass
SELECT * FROM #TempTable
DROP TABLE #TempTable

-- :name get-login-data :? :1
-- Get login details for testing purposes
SELECT id, username, pass
FROM users
WHERE username = :username

-- :name get-all-users :? :*
-- :doc selects all available users from the database
SELECT * FROM users

-- :name remove-user! :!
-- :doc removes user by specifying his ID
DELETE FROM users 
WHERE id = :id