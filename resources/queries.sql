--- REMOTE RATING RELATED ---

-- :name get-rooms :? :*
-- :doc get all room IDs with types
SELECT RoomID, Name, RoomType
FROM [Room]

-- :name update-rating! :! :n
-- :doc update rating info in table (time on rest, time on work, rating by userid and date)
UPDATE [RatingForDay]
SET TimeOnRest = :TimeOnRest, TimeOnWork = :TimeOnWork, Rating = :Rating
WHERE UserID = :UserID AND [Date] = :Date

--:name add-rating! :! :n
--:doc add a new rating to user in case one does not exist
INSERT INTO [RatingForDay]
(UserID, TimeOnRest, TimeOnWork, Rating, Date)
VALUES
(:UserID, :TimeOnRest, :TimeOnWork, :Rating, :Date)

--:name count-ratings :? :1
--:doc count number of ratings
SELECT COUNT(UserID) AS [Count] FROM [RatingForDay]
WHERE [Date] = :Date AND [UserID] = :UserID

--- USER ---

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

-- :name get-daily-task-status :? :1
-- :doc get daily task completion status for input date | TODO: add validation?
SELECT [RatingForDay].[UserID], Name, Surname, WorkFinished 
FROM [User]
INNER JOIN [RatingForDay]
ON [User].[UserID] = [RatingForDay].[UserID]
WHERE [RatingForDay].[UserID] = :UserID AND [RatingForDay].[Date] = :Date

-- :name count-daily-task :? :1
-- :doc Count daily task status. If 0 => create a new status with 'status #2'.
SELECT COUNT(Name) AS [Count]
FROM [User]
INNER JOIN [RatingForDay]
ON [User].[UserID] = [RatingForDay].[UserID]
WHERE [RatingForDay].[UserID] = :UserID AND [RatingForDay].[Date] = :Date

-- :name get-daily-rating :? :1
-- :doc get daily  rating | TODO: add validation?
SELECT UserID, TimeOnRest, TimeOnWork, Rating, [RatingForDay].[Date]
FROM [User]
INNER JOIN [RatingForDay]
ON [User].[UserID] = [RatingForDay].[UserID]
WHERE [User].[UserID] = :UserID AND [RatingForDay].[Date] = :Date

-- :name get-last-week-rating :? :*
-- :doc get a list of ratings from last week
SELECT TOP(5) UserID, TimeOnRest, TimeOnWork, Rating, [Date]
FROM [RatingForDay]
WHERE UserID = :UserID

-- :name get-last-N-days-rating :? :*
-- :doc get a list of ratings from last week
SELECT TOP(:Number) UserID, TimeOnRest, TimeOnWork, Rating, [Date]
FROM [RatingForDay]
WHERE UserID = :UserID
ORDER BY [Date] DESC

--:name add-daily-question! :! :n
--:doc add ONLY DAILY QUESTION STATUS to [RatingForDay]
INSERT INTO [RatingForDay]
(UserID, WorkFinished, Date)
VALUES
(:UserID, :WorkFinished, :Date)

--:name update-daily-question! :! :n
--:doc update ONLY DAILY QUESTION STATUS to [RatingForDay]. Redundant?
UPDATE [RatingForDay]
SET WorkFinished = :WorkFinished
WHERE UserID = :UserID AND [Date] = :Date

--- GROUP LEADER & MANAGEMENT ---

-- :name create-new-group! :! :n
-- :doc create a new group
INSERT INTO [Group]
(GroupName, Description, GroupLeaderID, ProjectManagerID)
VALUES (:GroupName, :Description, :GroupLeaderID, :ProjectManagerID)

--- UPDATE GROUP DATA -> SEE ADMIN FUNCS ---

-- :name add-group-member! :! :1
-- :doc add new members to a group
INSERT INTO [User_Group]
(UserID, GroupID)
VALUES (:UserID, :GroupID)

-- :name add-group-leader! :! :n
-- :doc temporary crutch to add GR to user_group table.
DECLARE @var1 int; 
SELECT @var1 = GroupID
FROM [Group]
WHERE GroupLeaderID = :UserID
INSERT INTO [User_Group]
(UserID, GroupID)
VALUES (:UserID, @var1)

-- :name remove-group-member! :! :n
-- :doc remove members from a group
DELETE FROM [User_Group]
WHERE UserID = :UserID AND GroupID = :GroupID

-- :name remove-group! :!
-- :doc remove group and all its members
DELETE FROM [Group] 
WHERE GroupID = :GroupID
DELETE FROM [User_Group]
WHERE GroupID = :GroupID

-- :name remove-group-with-leader! :! :n
-- :doc remove group and all its members. GROUP DESIGNATED BY GROUP LEADER.
DECLARE @var1 int; 
SELECT @var1 = GroupID
FROM [Group]
WHERE GroupLeaderID = :UserID
DELETE FROM [Group] 
WHERE GroupLeaderID = :UserID
DELETE FROM [User_Group]
WHERE GroupID = @var1

-- :name get-group-members :? :*
-- :doc get group members list for selected group
SELECT [User].[UserID], Name, Surname, Email
FROM [User]
INNER JOIN [User_Group]
ON [User].[UserID] = [User_Group].[UserID]
WHERE [User_Group].[GroupID] = :GroupID

-- :name get-group-data :? :*
-- :doc get group data (name, description, etc)
SELECT *
FROM [Group]
WHERE GroupID = :GroupID

-- :name get-group-by-leader :? :*
-- :doc Check if group exists. Get grop info by query by groupleaderID.
SELECT *
FROM [Group]
WHERE GroupLeaderID = :GroupLeaderID

--- PROJECT MANAGER ---

-- :name get-employees-list :? :*
-- :doc get list of employees available for promotion
SELECT UserID, Name, Surname, Email
FROM [User]
WHERE RoleID = 1

-- :name get-controlled-groups-list :? :*
-- :doc get list of GR with selection by ProjectManagerID
SELECT GroupID, GroupName, Description
FROM [Group]
WHERE ProjectManagerID = :ProjectManagerID

-- :name promote-to-leader! :! :1
-- :doc promote selected person to a group leader role
UPDATE [User]
SET RoleID = 4
WHERE UserID = :UserID

-- :name demote-from-leader! :! :1
-- :doc demote person to a regular employee status
UPDATE [User]
SET RoleID = 1
WHERE UserID = :UserID

--- ADMIN ---
--- TODO: ADD CASCADE DELETION OF USER DATA ---

-- :name get-all-users :? :*
-- :doc selects all available users and their data from the 'User' table (excluding password)
SELECT UserID, Name, Surname, Address, ResidenceCountry, Nationality, Sex, Email, PhoneNumber, BirthdayDate, RegistrationDate, RoleID
FROM [User]

-- :name remove-user! :!
-- :doc removes user by specifying his ID
DELETE FROM [User] 
WHERE UserID = :UserID

-- :name add-user! :! :n
-- :doc adds a new user to the DB
INSERT INTO [User]
(Name, Surname, Address, ResidenceCountry, Nationality, Sex, Email, Password, PhoneNumber, BirthdayDate, RoleID)
VALUES (:Name, :Surname, :Address, :ResidenceCountry, :Nationality, :Sex, :Email, :Password, :PhoneNumber, :BirthdayDate, :RoleID)

-- :name get-user-by-id :? :1
-- :doc find the user with a matching ID
SELECT Name, Surname, Address, ResidenceCountry, Nationality, Sex, Email, PhoneNumber, BirthdayDate, RegistrationDate, RoleID 
FROM [User]
WHERE userid = :userid

-- :name get-user-by-email :? :1
-- :doc find the user with a matching EMAIL
SELECT Name, Surname, Address, ResidenceCountry, Nationality, Sex, Email, PhoneNumber, BirthdayDate, RegistrationDate, RoleID 
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

--:name update-user-data! :! :n
--:doc update already existing user data
UPDATE [User]
SET Name = :Name, Surname = :Surname, Address = :Address, ResidenceCountry = :ResidenceCountry, Nationality = :Nationality, Sex = :Sex, Email = :Email, Password = :Password, PhoneNumber = :PhoneNumber, BirthdayDate = :BirthdayDate, RoleID = :RoleID
WHERE UserID = :UserID

--:name get-all-groups :? :*
--:doc get a list of all created groups
SELECT GroupID, GroupName, Description, [Group].[RegistrationDate], GroupLeaderID, Name, Surname
FROM [Group]
INNER JOIN [User]
ON [Group].[GroupLeaderID] = [User].[UserID]

--:name update-group-data! :! :n
--:doc update information regarding group name or description
UPDATE [Group]
SET GroupName = :GroupName, Description = :Description
WHERE GroupID = :GroupID

--:name get-number-of-users !? :1
--:doc Get number of rows in [User] table. Used to create first admin account.
SELECT COUNT(*) AS [Count] 
FROM [User]