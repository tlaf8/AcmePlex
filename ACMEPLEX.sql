DROP DATABASE IF EXISTS ACMEPLEX;
CREATE DATABASE ACMEPLEX;
USE ACMEPLEX;

CREATE TABLE REGISTEREDUSERS
(
    RegisteredUserID INT AUTO_INCREMENT  NOT NULL PRIMARY KEY,
    RName            VARCHAR(255)        NOT NULL,
    Email            VARCHAR(255) UNIQUE NOT NULL,
    RPassword        VARCHAR(255),
    Address          TEXT
);

CREATE TABLE CREDITCARDS
(
    CardID           INT AUTO_INCREMENT PRIMARY KEY,
    RegisteredUserID INT          NOT NULL,
    CardHolderName   VARCHAR(255) NOT NULL,
    CardNumber       VARCHAR(16)  NOT NULL,
    CSV              VARCHAR(3)   NOT NULL,
    ExpirationDate   VARCHAR(4)   NOT NULL,
    CreditAmount     DECIMAL(5, 2) DEFAULT 500,
    InStoreCredit    DECIMAL(5, 2) DEFAULT 0,
    Expiry           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE THEATERS
(
    TheaterID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    TName     VARCHAR(255)       NOT NULL,
    Location  VARCHAR(255)       NOT NULL
);

CREATE TABLE MOVIES
(
    MovieID   INT AUTO_INCREMENT PRIMARY KEY,
    Title     VARCHAR(255) NOT NULL,
    Duration  INT,
    Genre     VARCHAR(50),
    TheaterID VARCHAR(255)
);

CREATE TABLE SHOWTIMES
(
    ShowtimeID INT AUTO_INCREMENT PRIMARY KEY,
    MovieID    INT  NOT NULL,
    TheaterID  INT  NOT NULL,
    SDate      DATE NOT NULL,
    StartTime  TIME NOT NULL,
    FOREIGN KEY (MovieID) REFERENCES MOVIES (MovieID) ON DELETE CASCADE
);

CREATE TABLE SHOWTIMESEATS
(
    ShowtimeID  INT NOT NULL,
    SeatID      INT NOT NULL,
    IsAvailable BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (ShowtimeID, SeatID),
    FOREIGN KEY (ShowtimeID) REFERENCES SHOWTIMES (ShowtimeID) ON DELETE CASCADE
);

CREATE TABLE TICKETS
(
    TicketID         INT           NOT NULL PRIMARY KEY,
    ShowtimeID       INT           NOT NULL,
    Seats            VARCHAR(255)  NOT NULL,
    OrdinaryUserID   INT           NULL,
    RegisteredUserID INT           NULL,
    Price            DECIMAL(5, 2) NOT NULL,
    CardNumber       VARCHAR(255)  NOT NULL,
    PurchaseDate     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IsCancelled      BOOLEAN   DEFAULT FALSE,
    CreditIssued     BOOLEAN   DEFAULT FALSE,
    AmountIssued     INT       DEFAULT 0
);

CREATE TABLE ADMIN
(
    adminKey VARCHAR(255) NOT NULL PRIMARY KEY
);

INSERT INTO ADMIN (adminKey)
VALUES ('superstrongpassword');

INSERT INTO THEATERS (TName, Location)
VALUES ('Deimos', 'Hawkwood'),
       ('Phobos', 'Oakville'),
       ('Titan', 'Sunnyvale');
