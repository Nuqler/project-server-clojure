-- :name insert-roles :! :n
-- :doc Insert multiple roles with :tuple* parameter type
insert into [Role] (RoleID, Name)
values :tuple*:Role

-- :name insert-rooms :! :n
-- :doc Insert multiple rooms with :tuple* parameter type
insert into [Room] (Name, Description, RoomType)
values :tuple*:Rooms

-- :name insert-ratings :! :n
-- :doc Insert multiple ratings with :tuple* parameter type
insert into [RatingForDay] (UserID, TimeOnRest, TimeOnWork, WorkFinished, Rating)
values :tuple*:Ratings