CREATE DATABASE IF NOT EXISTS sponsorship_hub;
USE sponsorship_hub;

CREATE TABLE users (
    User_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    University_Reg_No VARCHAR(50) NOT NULL UNIQUE,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(50) NOT NULL CHECK (Role COLLATE utf8mb4_bin IN ('Student', 'Admin'))
);

CREATE TABLE companies (
    Company_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Contact_Person VARCHAR(255),
    Position VARCHAR(100),
    Field VARCHAR(100),
    Email VARCHAR(255),
    Contact_Number VARCHAR(20),
    Tracking_Status ENUM('Available', 'In Progress', 'Locked') NOT NULL DEFAULT 'Available',
    Locked_Until DATETIME,
    Locked_By INT,
    Lock_Days INT,
    Cooldown_Days INT,
    FOREIGN KEY (Locked_By) REFERENCES users(User_ID),
    INDEX idx_status_until (Tracking_Status, Locked_Until)
);

CREATE TABLE events (
    Event_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Organized_Society VARCHAR(255),
    Event_Month VARCHAR(50),
    Event_Date DATE
);

CREATE TABLE event_committee (
    Committee_ID INT AUTO_INCREMENT PRIMARY KEY,
    User_ID INT NOT NULL,
    Event_ID INT NOT NULL,
    Position VARCHAR(100),
    Assigned_Date DATE,
    Is_Active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (User_ID, Event_ID),
    FOREIGN KEY (User_ID) REFERENCES users(User_ID),
    FOREIGN KEY (Event_ID) REFERENCES events(Event_ID)
);

CREATE TABLE sponsorship_history (
    Sponsorship_ID INT AUTO_INCREMENT PRIMARY KEY,
    Company_ID INT NOT NULL,
    Event_ID INT NOT NULL,
    Handled_By INT NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL CHECK (Amount > 0),
    Status ENUM('Pending', 'Approved', 'Rejected') NOT NULL DEFAULT 'Pending',
    Requested_Date DATE NOT NULL,
    Notes TEXT,
    FOREIGN KEY (Company_ID) REFERENCES companies(Company_ID),
    FOREIGN KEY (Event_ID) REFERENCES events(Event_ID),
    FOREIGN KEY (Handled_By) REFERENCES users(User_ID)
);

INSERT INTO companies (Name, Contact_Person, Position, Field, Email, Contact_Number) VALUES
('WSO2', 'Ruwan Abeysekera', 'Community Programs Lead', 'Software', 'ruwan.a@wso2.example', '+94 11 555 0101'),
('Virtusa', 'Amaya Wickramasinghe', 'University Relations Manager', 'Software', 'amaya.w@virtusa.example', '+94 11 555 0102'),
('IFS', 'Dinesh Karunaratne', 'Talent Partnerships Manager', 'Software', 'dinesh.k@ifs.example', '+94 11 555 0103'),
('99x', 'Chamari Hettiarachchi', 'People Operations Lead', 'Software', 'chamari.h@99x.example', '+94 11 555 0104'),
('hSenid Mobile', 'Lasantha Dissanayake', 'Head of Engineering', 'Telecom Software', 'lasantha.d@hsenid.example', '+94 11 555 0105'),
('Dialog Axiata', 'Poorna Ranasinghe', 'Corporate Communications Manager', 'Telecom', 'poorna.r@dialog.example', '+94 11 555 0106'),
('Sri Lanka Telecom', 'Janaka Weerasekara', 'Manager, Brand and Sponsorships', 'Telecom', 'janaka.w@slt.example', '+94 11 555 0107'),
('MAS Holdings', 'Shanika Amarasinghe', 'Community Relations Executive', 'Apparel', 'shanika.a@mas.example', '+94 11 555 0108'),
('Hemas Holdings', 'Roshan De Mel', 'Group CSR Coordinator', 'Conglomerate', 'roshan.d@hemas.example', '+94 11 555 0109'),
('John Keells Holdings', 'Anushka Peiris', 'Manager, Corporate Affairs', 'Conglomerate', 'anushka.p@keells.example', '+94 11 555 0110'),
('LSEG Technology', 'Mihiri Samarakoon', 'Early Careers Partner', 'Fintech', 'mihiri.s@lseg.example', '+94 11 555 0111'),
('Hayleys', 'Thilina Gamage', 'Sustainability and Outreach Lead', 'Manufacturing', 'thilina.g@hayleys.example', '+94 11 555 0112'),
('Ceylon Electricity Board', 'Eng. Prasad Liyanage', 'Chief Engineer, Training', 'Energy', 'prasad.l@ceb.example', '+94 11 555 0113'),
('DIMO', 'Harsha Jayawardena', 'Marketing Manager', 'Automotive', 'harsha.j@dimo.example', '+94 11 555 0114');

INSERT INTO events (Name, Organized_Society, Event_Month, Event_Date) VALUES
('CareerXpo 2025', 'CareerXpo Organizing Committee', 'November', '2025-11-14'),
('XBotix 2025', 'XBotix Organizing Committee', 'August', '2025-08-23'),
('CareerXpo 2026', 'CareerXpo Organizing Committee', 'November', '2026-11-20'),
('XBotix 2026', 'XBotix Organizing Committee', 'December', '2026-12-05'),
('IEEE Student Branch Congress 2026', 'IEEE Student Branch', 'January', '2027-01-16');
