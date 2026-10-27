USE sponsorship_hub;

SELECT Name,
    CASE WHEN Tracking_Status <> 'Available' AND Locked_Until < NOW() THEN 'Available' ELSE Tracking_Status END AS Light,
    GREATEST(CEIL(TIMESTAMPDIFF(MINUTE, NOW(), Locked_Until) / 1440), 0) AS Days_Left
FROM companies
ORDER BY Light, Name;

UPDATE companies
SET Tracking_Status = 'Available', Locked_Until = NULL, Locked_By = NULL
WHERE Tracking_Status <> 'Available' AND Locked_Until < NOW();

SELECT e.Name, COUNT(*) AS Deals, SUM(h.Amount) AS Total
FROM sponsorship_history h
JOIN events e ON e.Event_ID = h.Event_ID
WHERE h.Status = 'Approved'
GROUP BY e.Event_ID, e.Name
ORDER BY Total DESC;
