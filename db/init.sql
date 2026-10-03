CREATE DATABASE IF NOT EXISTS Sponsorship_Hub;
USE Sponsorship_Hub;

CREATE TABLE users (
    User_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255),
    University_Reg_No VARCHAR(50),
    Email VARCHAR(255),
    Password VARCHAR(255),
    Role VARCHAR(50)
);

CREATE TABLE companies (
    Company_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255),
    Contact_Person VARCHAR(255),
    Position VARCHAR(100),
    Field VARCHAR(100),
    Email VARCHAR(255),
    Tracking_Status ENUM('Available', 'In Progress', 'Locked') DEFAULT 'Available',
    Locked_Until DATETIME
);

CREATE TABLE events (
    Event_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255),
    Organized_Society VARCHAR(255),
    Event_Month VARCHAR(50),
    Event_Date DATE
);

CREATE TABLE event_committee (
    Committee_ID INT AUTO_INCREMENT PRIMARY KEY,
    User_ID INT,
    Event_ID INT,
    Position VARCHAR(100),
    Assigned_Date DATE,
    Is Active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (User_ID) REFERENCES users(User_ID),
    FOREIGN KEY (Event_ID) REFERENCES events(Event_ID)
);

CREATE TABLE sponsorship_history (
    Sponsorship_ID INT AUTO_INCREMENT PRIMARY KEY,
    Company_ID INT,
    Event_ID INT,
    Handled_By INT,
    Amount DECIMAL(10, 2),
    Status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    Requested_Date DATE,
    FOREIGN KEY (Company_ID) REFERENCES companies(Company_ID),
    FOREIGN KEY (Event_ID) REFERENCES events(Event_ID),
    FOREIGN KEY (Handled_By) REFERENCES users(User_ID)
);
