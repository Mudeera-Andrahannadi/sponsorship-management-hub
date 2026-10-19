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
DB_PASS=your_mysql_password JWT_SECRET=any_string_of_32_or_more_characters ./mvnw spring-boot:run
```

`DB_PASS` and `JWT_SECRET` are required. `DB_USER` (default `root`), `DB_HOST` (default `localhost`), `DB_PORT` (default `3306`) and `CORS_ORIGIN` (default `http://localhost:5173`) are optional.

Anyone can register, but `/api/auth/register` only ever creates a Student. Admins are seeded or added by another admin. Login returns a JWT that lasts 8 hours.

`./mvnw test` starts a throwaway MySQL container and loads `db/init.sql` into it, so Docker has to be running.

## How locking works

A student on an active committee claims a company, and it turns yellow for 7 days. Logging a pitch for it restarts the 7 days. If nothing happens, the company goes back to green by itself (a job runs every minute, and every list call checks too). When the admin approves a sponsorship the company turns red for 183 days. Rejecting it frees the company.

A student can hold at most 3 companies at once (`hub.max-holds`). The admin can't approve a pitch while a different society holds the company at that moment; unlock it first.

Both lengths are defaults. The admin can change them for a single company, and can lock or unlock a company by hand.

Two students claiming the same company at the same moment is safe. The claim is one conditional `UPDATE`, so only one of them gets it and the other gets a 409 that says who has it.

## API

Everything except register and login needs `Authorization: Bearer <token>`.

| Route | Who | What it does |
|---|---|---|
| `POST /api/auth/register`, `/login` | anyone | sign up as a student, get a token |
| `GET /api/companies?field=&status=` | any user | directory with live status and days left |
| `POST /api/companies/{id}/lock`, `/release` | student | claim or give back a company |
| `POST /api/companies`, `PUT`, `DELETE /api/companies/{id}` | admin | manage the directory |
| `GET /api/events`, `POST /api/events` | any user, admin | list events, create one |
| `POST /api/committees/assign`, `GET /api/committees`, `PATCH /api/committees/{id}` | admin | put students on committees, switch them off |
| `GET /api/committees/mine` | any user | events the student is active on |
| `POST /api/sponsorships` | student | log a pitch for a company you hold |
| `PATCH /api/sponsorships/{id}/status` | student, admin | edit your pending pitch; only the admin can approve or reject |
| `GET /api/history`, `/company/{id}`, `/event/{id}` | any user | sponsorship history |
| `PUT /api/admin/companies/{id}/lock-days`, `POST .../lock`, `.../unlock` | admin | per-company lengths, manual lock and unlock |
| `GET /api/admin/analytics` | admin | counts per colour, money per event, top sponsors, locks ending in 48 hours |
| `GET /api/admin/users`, `POST /api/admin/users` | admin | list users, add an admin or student |

`./mvnw test` covers all of this against a real MySQL container.

## Sample logins

Every seeded user has the password `Hub@2026`.

- Admin: nimali.perera@eng.uni.example
- Student: sachini.r@eng.uni.example
