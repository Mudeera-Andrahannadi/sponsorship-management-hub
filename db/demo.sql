USE sponsorship_hub;

INSERT INTO users (Name, University_Reg_No, Email, Password, Role) VALUES
('Dr. Nimali Perera', 'STAFF-001', 'nimali.perera@eng.uni.example', '$2a$10$9zki5BtmHxCgsC3H8Tt6SOvpDPEPOzpqd9rpI.vug5LecKrYZ68Lm', 'Admin'),
('Sachini Rathnayake', 'EE/22/014', 'sachini.r@eng.uni.example', '$2a$10$eXZGraHLjMCLil3wKLUybuFVvsGVGl9wZM4PXkk6B28E7ZZCzn/2G', 'Student'),
('Dilshan Fernando', 'EE/22/057', 'dilshan.f@eng.uni.example', '$2a$10$nJ/LbS9C1ABiw2Qx/wZaJeN6paWStRw8lwjbzl4JqrW3L3f1p4F.G', 'Student'),
('Tharushi Jayasinghe', 'EE/23/021', 'tharushi.j@eng.uni.example', '$2a$10$zDuhl1eH2FfgSjvVm5aOgeA3VNaCK1H5ptF2dLmpcHScpBXAoSeqO', 'Student'),
('Nuwan Silva', 'EE/23/066', 'nuwan.s@eng.uni.example', '$2a$10$yKbVXNGNC2Aa2Mby6CNg..YOzJaK0PA0bfoDneoaYPT2fBUw1kt4W', 'Student'),
('Kavindu Madushanka', 'EE/21/038', 'kavindu.m@eng.uni.example', '$2a$10$5ebbTZyL/L6Rh9nBMngwB.CE/qhB8ZvH3Xb.TcEu0wN7TrSqwE8De', 'Student'),
('Ishara Gunawardena', 'EE/22/102', 'ishara.g@eng.uni.example', '$2a$10$hELaxlaj8EgJjLVra6KNceeV8UhVBuIdtKyCNuBsCTgfdID6qkmUO', 'Student');

UPDATE companies SET Tracking_Status = 'In Progress', Locked_Until = NOW() + INTERVAL 3 DAY, Locked_By = 3 WHERE Name = 'Virtusa';
UPDATE companies SET Tracking_Status = 'Locked', Locked_Until = NOW() + INTERVAL 123 DAY, Locked_By = 2 WHERE Name = 'IFS';
UPDATE companies SET Tracking_Status = 'Locked', Locked_Until = NOW() + INTERVAL 143 DAY, Locked_By = 5 WHERE Name = 'Dialog Axiata';
UPDATE companies SET Tracking_Status = 'In Progress', Locked_Until = NOW() + INTERVAL 5 DAY, Locked_By = 4 WHERE Name = 'Sri Lanka Telecom';
UPDATE companies SET Tracking_Status = 'In Progress', Locked_Until = NOW() - INTERVAL 2 DAY, Locked_By = 2 WHERE Name = 'Hemas Holdings';
UPDATE companies SET Tracking_Status = 'Locked', Locked_Until = NOW() + INTERVAL 163 DAY, Locked_By = 7 WHERE Name = 'Hayleys';

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
