CREATE DATABASE IF NOT EXISTS Sponsorship_Hub;
USE Sponsorship_Hub;

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

INSERT INTO users (Name, University_Reg_No, Email, Password, Role) VALUES
('Dr. Nimali Perera', 'STAFF-001', 'nimali.perera@eng.uni.example', '$2a$10$9zki5BtmHxCgsC3H8Tt6SOvpDPEPOzpqd9rpI.vug5LecKrYZ68Lm', 'Admin'),
('Sachini Rathnayake', 'EE/22/014', 'sachini.r@eng.uni.example', '$2a$10$eXZGraHLjMCLil3wKLUybuFVvsGVGl9wZM4PXkk6B28E7ZZCzn/2G', 'Student'),
('Dilshan Fernando', 'EE/22/057', 'dilshan.f@eng.uni.example', '$2a$10$nJ/LbS9C1ABiw2Qx/wZaJeN6paWStRw8lwjbzl4JqrW3L3f1p4F.G', 'Student'),
('Tharushi Jayasinghe', 'EE/23/021', 'tharushi.j@eng.uni.example', '$2a$10$zDuhl1eH2FfgSjvVm5aOgeA3VNaCK1H5ptF2dLmpcHScpBXAoSeqO', 'Student'),
('Nuwan Silva', 'EE/23/066', 'nuwan.s@eng.uni.example', '$2a$10$yKbVXNGNC2Aa2Mby6CNg..YOzJaK0PA0bfoDneoaYPT2fBUw1kt4W', 'Student'),
('Kavindu Madushanka', 'EE/21/038', 'kavindu.m@eng.uni.example', '$2a$10$5ebbTZyL/L6Rh9nBMngwB.CE/qhB8ZvH3Xb.TcEu0wN7TrSqwE8De', 'Student'),
('Ishara Gunawardena', 'EE/22/102', 'ishara.g@eng.uni.example', '$2a$10$hELaxlaj8EgJjLVra6KNceeV8UhVBuIdtKyCNuBsCTgfdID6qkmUO', 'Student');

INSERT INTO companies (Name, Contact_Person, Position, Field, Email, Contact_Number, Tracking_Status, Locked_Until, Locked_By) VALUES
('WSO2', 'Ruwan Abeysekera', 'Community Programs Lead', 'Software', 'ruwan.a@wso2.example', '+94 11 555 0101', 'Available', NULL, NULL),
('Virtusa', 'Amaya Wickramasinghe', 'University Relations Manager', 'Software', 'amaya.w@virtusa.example', '+94 11 555 0102', 'In Progress', NOW() + INTERVAL 3 DAY, 3),
('IFS', 'Dinesh Karunaratne', 'Talent Partnerships Manager', 'Software', 'dinesh.k@ifs.example', '+94 11 555 0103', 'Locked', NOW() + INTERVAL 123 DAY, 2),
('99x', 'Chamari Hettiarachchi', 'People Operations Lead', 'Software', 'chamari.h@99x.example', '+94 11 555 0104', 'Available', NULL, NULL),
('hSenid Mobile', 'Lasantha Dissanayake', 'Head of Engineering', 'Telecom Software', 'lasantha.d@hsenid.example', '+94 11 555 0105', 'Available', NULL, NULL),
('Dialog Axiata', 'Poorna Ranasinghe', 'Corporate Communications Manager', 'Telecom', 'poorna.r@dialog.example', '+94 11 555 0106', 'Locked', NOW() + INTERVAL 143 DAY, 5),
('Sri Lanka Telecom', 'Janaka Weerasekara', 'Manager, Brand and Sponsorships', 'Telecom', 'janaka.w@slt.example', '+94 11 555 0107', 'In Progress', NOW() + INTERVAL 5 DAY, 4),
('MAS Holdings', 'Shanika Amarasinghe', 'Community Relations Executive', 'Apparel', 'shanika.a@mas.example', '+94 11 555 0108', 'Available', NULL, NULL),
('Hemas Holdings', 'Roshan De Mel', 'Group CSR Coordinator', 'Conglomerate', 'roshan.d@hemas.example', '+94 11 555 0109', 'In Progress', NOW() - INTERVAL 2 DAY, 2),
('John Keells Holdings', 'Anushka Peiris', 'Manager, Corporate Affairs', 'Conglomerate', 'anushka.p@keells.example', '+94 11 555 0110', 'Available', NULL, NULL),
('LSEG Technology', 'Mihiri Samarakoon', 'Early Careers Partner', 'Fintech', 'mihiri.s@lseg.example', '+94 11 555 0111', 'Available', NULL, NULL),
('Hayleys', 'Thilina Gamage', 'Sustainability and Outreach Lead', 'Manufacturing', 'thilina.g@hayleys.example', '+94 11 555 0112', 'Locked', NOW() + INTERVAL 163 DAY, 7),
('Ceylon Electricity Board', 'Eng. Prasad Liyanage', 'Chief Engineer, Training', 'Energy', 'prasad.l@ceb.example', '+94 11 555 0113', 'Available', NULL, NULL),
('DIMO', 'Harsha Jayawardena', 'Marketing Manager', 'Automotive', 'harsha.j@dimo.example', '+94 11 555 0114', 'Available', NULL, NULL);

INSERT INTO events (Name, Organized_Society, Event_Month, Event_Date) VALUES
('CareerXpo 2025', 'CareerXpo Organizing Committee', 'November', '2025-11-14'),
('XBotix 2025', 'XBotix Organizing Committee', 'August', '2025-08-23'),
('CareerXpo 2026', 'CareerXpo Organizing Committee', 'November', '2026-11-20'),
('XBotix 2026', 'XBotix Organizing Committee', 'December', '2026-12-05'),
('IEEE Student Branch Congress 2026', 'IEEE Student Branch', 'January', '2027-01-16');

INSERT INTO event_committee (User_ID, Event_ID, Position, Assigned_Date, Is_Active) VALUES
(2, 3, 'Coordinator', '2026-08-15', TRUE),
(3, 3, 'Treasurer', '2026-08-15', TRUE),
(4, 4, 'Coordinator', '2026-09-01', TRUE),
(5, 4, 'Sponsorship Lead', '2026-09-01', TRUE),
(7, 5, 'Coordinator', '2026-09-20', TRUE),
(2, 1, 'Treasurer', '2025-07-01', FALSE),
(6, 1, 'Coordinator', '2025-07-01', FALSE),
(6, 2, 'Sponsorship Lead', '2025-05-10', FALSE);

INSERT INTO sponsorship_history (Company_ID, Event_ID, Handled_By, Amount, Status, Requested_Date, Notes) VALUES
(3, 1, 6, 250000.00, 'Approved', '2025-08-04', 'Gold sponsor. Asked for a stall and a slot in the opening speech.'),
(2, 1, 6, 200000.00, 'Approved', '2025-09-12', 'Paid half upfront, rest after the event.'),
(8, 1, 2, 180000.00, 'Rejected', '2025-09-20', 'Budget already used up for the year. Try again after March.'),
(1, 2, 6, 120000.00, 'Approved', '2025-06-02', 'Sponsored the prize pool. Wanted their logo on the arena banner.'),
(10, 2, 6, 90000.00, 'Approved', '2025-06-18', 'Paid in kind with robot kits and some cash.'),
(3, 3, 2, 300000.00, 'Approved', CURDATE() - INTERVAL 60 DAY, 'Bigger than last year. Contact prefers email over calls.'),
(6, 4, 5, 150000.00, 'Approved', CURDATE() - INTERVAL 40 DAY, 'Signed after two meetings. Invoice goes to their CSR desk.'),
(12, 5, 7, 75000.00, 'Approved', CURDATE() - INTERVAL 20 DAY, 'Quick yes. Needs a short report after the event.'),
(5, 4, 5, 60000.00, 'Rejected', CURDATE() - INTERVAL 30 DAY, 'Said no this round, open to a talk at the venue.'),
(2, 3, 3, 150000.00, 'Pending', CURDATE() - INTERVAL 4 DAY, 'Proposal sent, waiting on their reply.'),
(7, 4, 4, 100000.00, 'Pending', CURDATE() - INTERVAL 2 DAY, 'Intro call done, they want a budget breakdown.'),
(9, 3, 2, 80000.00, 'Pending', CURDATE() - INTERVAL 9 DAY, 'No reply since the first email.');
