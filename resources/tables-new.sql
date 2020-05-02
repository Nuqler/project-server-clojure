-- :name create-user-table-if-not-exists! :!
-- :doc check if the 'users' table exists
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[User]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[User](
	[User ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Surname] [nvarchar](50) NOT NULL,
	[Address] [nvarchar](50) NOT NULL,
	[ResidenceCountry] [varchar](58) NOT NULL,
	[Nationality] [varchar](50) NOT NULL,
	[Sex] [varchar](2) NOT NULL,
	[Email] [varchar](50) NOT NULL,
	[Password] [varchar](256) NOT NULL,
	[PhoneNumber] [varchar](50) NULL,
	[BirthdayDate] [date] NOT NULL,
	[RegistrationDate] [datetime] NOT NULL DEFAULT CURRENT_TIMESTAMP,
	[RoleID] [int] NOT NULL,
 CONSTRAINT [PK_users_v2] PRIMARY KEY CLUSTERED 
(
	[User ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-user-group-table-if-not-exists! :!
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

-- :name create-room-table-if-not-exists! :!
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

-- :name create-role-table-if-not-exists! :!
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
-- :name create-recommendation-table-if-not-exists! :!
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

-- :name create-ratingday-table-if-not-exists! :!
-- :doc check if the 'RatingForDay' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[RatingForDay]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[RatingForDay](
	[RatingID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[TimeOnRest] [float] NULL,
	[TimeOnWork] [float] NULL,
	[WorkFinished] [bit] NULL,
	[Rating] [int] NULL,
 CONSTRAINT [PK_RatingForDay] PRIMARY KEY CLUSTERED 
(
	[RatingID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-product-table-if-not-exists! :!
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

-- :name create-group-table-if-not-exists! :!
-- :doc check if the 'group' table exists and create it if it does not
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[Group]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[Group](
	[GroupID] [int] NOT NULL,
	[GroupName] [nvarchar](32) NOT NULL,
	[Description] [nvarchar](128) NULL,
 CONSTRAINT [PK_Group] PRIMARY KEY CLUSTERED 
(
	[GroupID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name create-foodoreder-table-if-not-exists! :!
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