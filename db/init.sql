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
    Is_Active BOOLEAN DEFAULT TRUE,
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

INSERT INTO users (Name, University_Reg_No, Email, Password, Role) VALUES
('Dr. Nimali Perera', 'STAFF-001', 'nimali.perera@eng.uni.example', '$2a$10$9zki5BtmHxCgsC3H8Tt6SOvpDPEPOzpqd9rpI.vug5LecKrYZ68Lm', 'Admin'),
('Sachini Rathnayake', 'EE/22/014', 'sachini.r@eng.uni.example', '$2a$10$eXZGraHLjMCLil3wKLUybuFVvsGVGl9wZM4PXkk6B28E7ZZCzn/2G', 'Student'),
('Dilshan Fernando', 'EE/22/057', 'dilshan.f@eng.uni.example', '$2a$10$nJ/LbS9C1ABiw2Qx/wZaJeN6paWStRw8lwjbzl4JqrW3L3f1p4F.G', 'Student'),
('Tharushi Jayasinghe', 'EE/23/021', 'tharushi.j@eng.uni.example', '$2a$10$zDuhl1eH2FfgSjvVm5aOgeA3VNaCK1H5ptF2dLmpcHScpBXAoSeqO', 'Student'),
('Nuwan Silva', 'EE/23/066', 'nuwan.s@eng.uni.example', '$2a$10$yKbVXNGNC2Aa2Mby6CNg..YOzJaK0PA0bfoDneoaYPT2fBUw1kt4W', 'Student'),
('Kavindu Madushanka', 'EE/21/038', 'kavindu.m@eng.uni.example', '$2a$10$5ebbTZyL/L6Rh9nBMngwB.CE/qhB8ZvH3Xb.TcEu0wN7TrSqwE8De', 'Student'),
('Ishara Gunawardena', 'EE/22/102', 'ishara.g@eng.uni.example', '$2a$10$hELaxlaj8EgJjLVra6KNceeV8UhVBuIdtKyCNuBsCTgfdID6qkmUO', 'Student');

INSERT INTO companies (Name, Contact_Person, Position, Field, Email, Tracking_Status, Locked_Until) VALUES
('WSO2', 'Ruwan Abeysekera', 'Community Programs Lead', 'Software', 'ruwan.a@wso2.example', 'Available', NULL),
('Virtusa', 'Amaya Wickramasinghe', 'University Relations Manager', 'Software', 'amaya.w@virtusa.example', 'In Progress', NOW() + INTERVAL 3 DAY),
('IFS', 'Dinesh Karunaratne', 'Talent Partnerships Manager', 'Software', 'dinesh.k@ifs.example', 'Locked', NOW() + INTERVAL 123 DAY),
('99x', 'Chamari Hettiarachchi', 'People Operations Lead', 'Software', 'chamari.h@99x.example', 'Available', NULL),
('hSenid Mobile', 'Lasantha Dissanayake', 'Head of Engineering', 'Telecom Software', 'lasantha.d@hsenid.example', 'Available', NULL),
('Dialog Axiata', 'Poorna Ranasinghe', 'Corporate Communications Manager', 'Telecom', 'poorna.r@dialog.example', 'Locked', NOW() + INTERVAL 143 DAY),
('Sri Lanka Telecom', 'Janaka Weerasekara', 'Manager, Brand and Sponsorships', 'Telecom', 'janaka.w@slt.example', 'In Progress', NOW() + INTERVAL 5 DAY),
('MAS Holdings', 'Shanika Amarasinghe', 'Community Relations Executive', 'Apparel', 'shanika.a@mas.example', 'Available', NULL),
('Hemas Holdings', 'Roshan De Mel', 'Group CSR Coordinator', 'Conglomerate', 'roshan.d@hemas.example', 'In Progress', NOW() - INTERVAL 2 DAY),
('John Keells Holdings', 'Anushka Peiris', 'Manager, Corporate Affairs', 'Conglomerate', 'anushka.p@keells.example', 'Available', NULL),
('LSEG Technology', 'Mihiri Samarakoon', 'Early Careers Partner', 'Fintech', 'mihiri.s@lseg.example', 'Available', NULL),
('Hayleys', 'Thilina Gamage', 'Sustainability and Outreach Lead', 'Manufacturing', 'thilina.g@hayleys.example', 'Locked', NOW() + INTERVAL 163 DAY),
('Ceylon Electricity Board', 'Eng. Prasad Liyanage', 'Chief Engineer, Training', 'Energy', 'prasad.l@ceb.example', 'Available', NULL),
('DIMO', 'Harsha Jayawardena', 'Marketing Manager', 'Automotive', 'harsha.j@dimo.example', 'Available', NULL);

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

INSERT INTO sponsorship_history (Company_ID, Event_ID, Handled_By, Amount, Status, Requested_Date) VALUES
(3, 1, 6, 250000.00, 'Approved', '2025-08-04'),
(2, 1, 6, 200000.00, 'Approved', '2025-09-12'),
(8, 1, 2, 180000.00, 'Rejected', '2025-09-20'),
(1, 2, 6, 120000.00, 'Approved', '2025-06-02'),
(10, 2, 6, 90000.00, 'Approved', '2025-06-18'),
(3, 3, 2, 300000.00, 'Approved', CURDATE() - INTERVAL 60 DAY),
(6, 4, 5, 150000.00, 'Approved', CURDATE() - INTERVAL 40 DAY),
(12, 5, 7, 75000.00, 'Approved', CURDATE() - INTERVAL 20 DAY),
(5, 4, 5, 60000.00, 'Rejected', CURDATE() - INTERVAL 30 DAY),
(2, 3, 3, 150000.00, 'Pending', CURDATE() - INTERVAL 4 DAY),
(7, 4, 4, 100000.00, 'Pending', CURDATE() - INTERVAL 2 DAY),
(9, 3, 2, 80000.00, 'Pending', CURDATE() - INTERVAL 9 DAY);
