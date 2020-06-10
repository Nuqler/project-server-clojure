-- :name create-user-group-table! :!
-- :doc check if the 'user-group' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[User_Group]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[User_Group](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[GroupID] [int] NOT NULL,
 CONSTRAINT [PK_User_Group] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-room-table! :!
-- :doc check if the 'room' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[Room]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[Room](
	[RoomID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](32) NOT NULL,
	[Description] [varchar](128) NOT NULL,
	[RoomType] [int] NOT NULL,
 CONSTRAINT [PK_Room] PRIMARY KEY CLUSTERED 
(
	[RoomID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-role-table! :!
-- :doc check if the 'role' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[Role]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[Role](
	[RoleID] [int] NOT NULL,
	[Name] [varchar](32) NOT NULL,
 CONSTRAINT [PK_Role] PRIMARY KEY CLUSTERED 
(
	[RoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-----------------------
-- TEST GENERATION DATE!!!!!!!!!!!!!!
-- :name create-recommendation-table! :!
-- :doc check if the 'recommendation' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[Recommendation]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[Recommendation](
	[RecommendationID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[Text] [varchar](256) NOT NULL,
	[GenerationDate] [date] NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT [PK_Recommendation] PRIMARY KEY CLUSTERED 
(
	[RecommendationID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-ratingday-table! :!
-- :doc check if the 'RatingForDay' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[RatingForDay]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[RatingForDay](
	[RatingID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[TimeOnRest] [float] NULL,
	[TimeOnWork] [float] NULL,
	[WorkFinished] [int] DEFAULT 2,
	[Rating] [int] NULL,
	[Date] [date] NOT NULL DEFAULT GETDATE(),
 CONSTRAINT [PK_RatingForDay] PRIMARY KEY CLUSTERED
(
	[RatingID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-product-table! :!
-- :doc check if the 'product' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[Product]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[Product](
	[ProductID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Description] [varchar](128) NOT NULL,
	[Type] [int] NOT NULL,
	[Category] [varchar](32) NOT NULL,
 CONSTRAINT [PK_Product] PRIMARY KEY CLUSTERED 
(
	[ProductID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-group-table! :!
-- :doc check if the 'group' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[Group]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[Group](
	[GroupID] [int] IDENTITY(1,1) NOT NULL,
	[GroupName] [nvarchar](32) NOT NULL,
	[Description] [nvarchar](128) NULL,
	[RegistrationDate] [date] NOT NULL DEFAULT GETDATE(),
	[GroupLeaderID] [int] NOT NULL,
	[ProjectManagerID] [int] NOT NULL,	
 CONSTRAINT [PK_Group] PRIMARY KEY CLUSTERED 
(
	[GroupID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
--- ALTER TABLE [dbo].[Group] ADD CONSTRAINT [DF_Group_GroupName] DEFAULT (N'New Group') FOR [GroupName]

-- :name create-foodorder-table! :!
-- :doc check if the 'FoodOrder' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[FoodOrder]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[FoodOrder](
	[FoodOrderID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[ProductID] [int] NOT NULL,
	[OrderTime] [datetime] NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT [PK_FoodOrder] PRIMARY KEY CLUSTERED 
(
	[FoodOrderID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]