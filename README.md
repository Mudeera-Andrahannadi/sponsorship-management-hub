# Corporate Outreach & Sponsorship Management Hub

Student societies keep emailing the same companies for sponsorships, and the companies get tired of it. This app lets a committee check if a company is free, hold it for a while, and log how the pitch went, so two societies don't chase the same company at once.

The database and the Spring Boot project setup are done so far.

## Database

MySQL 8.0, database `Sponsorship_Hub`, five tables: `users`, `companies`, `events`, `event_committee` and `sponsorship_history`.

```
mysql -u root -p < db/init.sql
```

That creates the tables and loads the sample data. `db/verify.sql` then lists every company with its traffic light, frees any expired locks, and totals the approved money per event.

Status colours: green is `Available`, yellow is `In Progress` (a society is talking to them), red is `Locked` (cooldown after a sponsorship).

I added a few things the handover didn't have: a phone number, who holds the lock and per-company lock lengths on `companies`, a `Notes` column on `sponsorship_history`, and a unique email on `users`.

## Backend

Spring Boot 3.3.4 on Java 21, built with the Maven wrapper in `backend/`. It connects to `sponsorship_hub` and uses `ddl-auto=validate`, so it never touches the tables.

```
cd backend
DB_PASS=your_mysql_password ./mvnw spring-boot:run
```

`DB_PASS` is required. `DB_USER` (default `root`), `DB_HOST` (default `localhost`) and `DB_PORT` (default `3306`) are optional.

`./mvnw test` starts a throwaway MySQL container and loads `db/init.sql` into it, so Docker has to be running.

## Sample logins

Every seeded user has the password `Hub@2026`.

- Admin: nimali.perera@eng.uni.example
- Student: sachini.r@eng.uni.example
