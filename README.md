# Corporate Outreach & Sponsorship Management Hub

Student societies keep emailing the same companies for sponsorships, and the companies get tired of it. This app lets a committee check if a company is free, hold it for a while, and log how the pitch went, so two societies don't chase the same company at once.

Only the database is done so far.

## Database

MySQL 8.0, database `Sponsorship_Hub`, five tables: `users`, `companies`, `events`, `event_committee` and `sponsorship_history`.

```
mysql -u root -p < db/init.sql
```

That creates the tables and loads the sample data. `db/verify.sql` then lists every company with its traffic light, frees any expired locks, and totals the approved money per event.

Status colours: green is `Available`, yellow is `In Progress` (a society is talking to them), red is `Locked` (cooldown after a sponsorship).

I added a few things the handover didn't have: a phone number, who holds the lock and per-company lock lengths on `companies`, a `Notes` column on `sponsorship_history`, and a unique email on `users`.

## Sample logins

Every seeded user has the password `Hub@2026`.

- Admin: nimali.perera@eng.uni.example
- Student: sachini.r@eng.uni.example
