-- :name add-user! :! :n
-- :doc adds a new user to the DB
INSERT INTO [User]
(Name, Surname, Address, ResidenceCountry, Nationality, Sex, Email, Password, PhoneNumber, BirthdayDate, RoleID)
VALUES (:Name, :Surname, :Address, :ResidenceCountry, :Nationality, :Sex, :Email, :Password, :PhoneNumber, :BirthdayDate, :RoleID)

-- :name get-user-by-id :? :1
-- :doc find the user with a matching ID
SELECT *
FROM [User]
WHERE userid = :userid

-- :name get-user-by-email :? :1
-- :doc find the user with a matching EMAIL
SELECT *
FROM [User]
WHERE email = :email

-- :name get-user-details :? :*
-- :doc selects all available details for selected user (excludes password)
SELECT * INTO #TempTable
FROM [User]
WHERE Email = :Email
ALTER TABLE #TempTable
DROP COLUMN pass
SELECT * FROM #TempTable
DROP TABLE #TempTable

-- :name get-user-details-DEBUG :? :*
-- :doc //DEBUG// selects all available details for selected user (excludes password) //DEBUG//
SELECT Name, Surname, Address, ResidenceCountry, Nationality, Sex, Email, PhoneNumber, BirthdayDate, RegistrationDate, RoleID 
FROM [User]
WHERE Email = :Email

-- :name get-login-data :? :1
-- :doc Get user account login details
SELECT UserID, Email, Name, Surname
FROM [User]
WHERE Email = :Email

-- :name get-login-data-with-password :? :1
-- :doc Get login details for password checking
SELECT UserID, Email, Name, Surname, Password
FROM [User]
WHERE Email = :Email

-- :name get-all-users :? :*
-- :doc selects all available users from the database
SELECT * FROM [User]

-- :name remove-user! :!
-- :doc removes user by specifying his ID
DELETE FROM [User] 
WHERE UserID = :UserID