# Expense Tracker & Splitter — Resume Project Roadmap

This document maps your current implementation to concepts, and outlines how to level up the project for interviews: re-learn Spring Boot + React, build an impressive frontend, add auth/DB, and add **standout features** that make the project unique.

---

## Part 1: Re-learn Spring Boot + React (mapped to your code)

### Spring Boot concepts you’ve already used

| Concept | Where in your project |
|--------|------------------------|
| **REST controllers** | `GroupController`, `ExpenseController`, `BalanceController` — `@RestController`, `@GetMapping`, `@PostMapping`, path variables |
| **Dependency injection** | Constructor injection in all controllers and services |
| **JPA / Hibernate** | `Expense`, `Group`, `User`, `GroupMember`, `ExpenseShare` — `@Entity`, `@OneToMany`, `@ManyToOne`, repositories |
| **Spring Data JPA** | `GroupRepository`, `ExpenseRepository`, `UserRepository`, etc. — `JpaRepository` |
| **Validation** | `@Valid` on `CreateExpenseRequest` in `ExpenseController` |
| **Exception handling** | `GlobalExceptionHandler`, `@ControllerAdvice`, `ResourceNotFoundException` |
| **DTOs** | `GroupSummaryResponseDto`, `SettlementDto`, `BalanceResponseDto`, `CreateExpenseRequest` — separation of API shape from entities |
| **Layered architecture** | Controller → Service → Repository → Model |

**Worth re-reading / practicing:**  
REST best practices (status codes, idempotency), JPA cascades and `FetchType`, transaction boundaries (`@Transactional` where needed), and how `application.properties` configures H2 and JPA.

### React concepts you’ve already used

| Concept | Where in your project |
|--------|------------------------|
| **Components** | Single `App` component |
| **State** | `useState` for `groups`, `selectedGroupId`, `summary`, `loading` |
| **Effects** | `useEffect` for fetch-on-mount (groups) and fetch-when-selection-changes (summary) |
| **Conditional rendering** | `if (loading)`, `{summary && (...)}` |
| **Lists and keys** | `groups.map(…)` with `key={group.id}` |

**Next to solidify:**  
Custom hooks (e.g. `useGroups`, `useGroupSummary`), React Router, centralized API client (fetch/axios), error and loading UX, and optionally a small state layer (Context or similar) when you add auth.

---

## Part 2: Impressive frontend (interview-ready)

Goal: **Polished, modern UI** that shows you care about UX and front-end skills.

### Tech and structure

- **React Router** — `/`, `/groups/:id`, `/groups/:id/expenses`, `/login` (when you add auth).
- **API layer** — One module (e.g. `src/api/client.js` or `src/services/expenseApi.js`) with functions like `getGroups()`, `getGroupSummary(id)`, `addExpense(...)`, `createGroup(name)`. Use environment variable for base URL.
- **Styling** — Choose one and stick to it:
  - **Tailwind CSS** — Fast to build, very presentable, great for interviews.
  - Or **CSS Modules** / **styled-components** if you want to show CSS architecture.
- **UI polish**:
  - Consistent **typography** (e.g. Google Fonts: DM Sans, Outfit, or similar — avoid default Inter).
  - **Loading states** — Skeletons or spinners for groups and summary.
  - **Empty states** — “No groups yet — create one” with clear CTA.
  - **Error states** — Toast or inline message with retry.
  - **Responsive** — At least one breakpoint (e.g. sidebar collapses on mobile).

### Pages/screens to build

1. **Dashboard / Home**  
   List of groups (cards or list), “Create group” button, maybe total balance across groups later.

2. **Group detail**  
   Group name, total spent, **balances table** (who owes / is owed), **settlements** (“Alice pays Bob ₹500”), and list of **recent expenses** with option to add expense.

3. **Add expense flow**  
   Form: description, amount, paid by (dropdown of group members), split type (equal / custom shares if you add it). Success feedback then refresh summary.

4. **Create group**  
   Name + add members (e.g. by existing user IDs or, after auth, by email/username).

### “Wow” touches for interviews

- **Micro-interactions** — Button hover/active, card hover, smooth transitions when switching groups.
- **Settlements as “action cards”** — e.g. “Bob pays Alice ₹200” with a small “Settle” or “Mark as paid” (can be UI-only at first).
- **Charts** — e.g. pie chart “Share of total spend by member” or bar chart “Spending over time” (requires an endpoint that returns per-member or time-series data).
- **Dark/light theme** — CSS variables + one toggle; shows awareness of accessibility and theming.

---

## Part 3: Spring Boot features to add (auth + DB)

### Database

- You already have **H2 + JPA** and `ddl-auto=update`. For resume/production story:
  - Keep H2 for **local dev** and optionally **tests**.
  - Add **PostgreSQL** (or MySQL) profile for “production-like” run:
    - `application-prod.properties` (or `application-postgres.properties`) with `spring.datasource.url=...`, credentials, and `spring.jpa.hibernate.ddl-auto=validate` (or use Flyway/Liquibase for migrations).
  - This shows you understand **multiple environments** and **migrations**.

### Authentication and authorization

- **Spring Security + JWT** is the standard resume combo:
  - **Register** — `POST /auth/register` (create user with hashed password).
  - **Login** — `POST /auth/login` (validate credentials, return JWT).
  - **Protected endpoints** — All group/expense/balance APIs require `Authorization: Bearer <token>`.
  - **User identity** — Replace “user by id” with “current user” from JWT; link `User` to an `email` + `password` (or use a separate `Account` entity if you prefer).
- **Optional:**  
  Role-based access (e.g. “only group members can add expenses”) and “invite by email” (send link with token or group code).

### Other backend improvements

- **Global CORS** — Configure in `WebMvcConfigurer` or `Security` so you don’t repeat `@CrossOrigin` on every controller.
- **Re-enable settlements** — In `GroupSummaryService`, call `settlementService.calculateSettlements(balances)` and pass the result into `GroupSummaryResponseDto` so the frontend can show “who pays whom.”
- **Pagination** — For “list expenses in group” or “list groups,” add `Pageable` and return `Page<...>` for a cleaner API.
- **Validation** — Add `@NotBlank`, `@Positive` etc. on DTOs and return `400` with clear messages from `GlobalExceptionHandler`.

---

## Part 4: Standout features (make it unique and useful)

These shift the project from “generic expense splitter” to something you can talk about in interviews.

### 1. **Trip / event mode** (recommended)

- **Idea:** Groups are tied to a **trip or event** (e.g. “Goa Trip”, “Office Offsite”) with optional **date range** and **currency**.
- **Value:** “We use it for trips and events” is a clear use case; interviewers understand it.
- **Implementation:** Add `Trip` (or `Event`) entity: name, start/end date, currency, optional image/link. Group belongs to one Trip. UI: “Create trip” → “Create group for this trip” → add expenses. Dashboard can show “Upcoming / past trips.”

### 2. **Minimum-transaction settlements** (you already have it)

- **Idea:** Your `SettlementService` already minimizes the number of payments. **Highlight it** in the UI and in the README: “Settles group debt in the minimum number of transactions.”
- **Value:** Shows algorithms and problem-solving; easy to explain in an interview.
- **Implementation:** Expose settlements clearly on the group summary page; add a short “How it works” or tooltip. Optionally add “Mark as paid” to record that a settlement was done (requires a new entity and endpoint).

### 3. **Invite link / join by code**

- **Idea:** Create a group → backend generates a **join code** or **invite link** (e.g. `/join/ABC123`). Anyone with the link can join the group (after login or as guest, depending on your auth).
- **Value:** Removes “add user by ID”; feels like a real product.
- **Implementation:** Add `Group.inviteCode` (unique), `GET /groups/join/:code` and `POST /groups/join/:code` (join current user to group). Frontend: “Share this link” and “Join with code” flow.

### 4. **Categories and simple analytics**

- **Idea:** Each expense has a **category** (Food, Transport, Stay, etc.). Dashboard shows “Spending by category” (pie/bar chart) and “Spending over time” (optional).
- **Value:** Positions the app as “tracking + splitting,” not just splitting.
- **Implementation:** Add `Expense.category` (enum or string); new endpoint e.g. `GET /groups/:id/analytics?by=category` returning aggregates. Frontend charts (e.g. Chart.js or Recharts).

### 5. **Recurring expenses** (optional)

- **Idea:** “Rent”, “Internet” every month — same amount, same split. Mark expense as recurring; system can suggest “Add this month’s rent?”
- **Value:** Useful for roommates and long-running groups.
- **Implementation:** `Expense.recurringRule` (e.g. monthly), cron or scheduled job to create suggested expenses, or simple “Duplicate last month” button.

### 6. **Export / share summary**

- **Idea:** “Download summary as PDF” or “Email summary to group” for a given group (and optionally trip).
- **Value:** Nice closing gesture for a trip; good for interviews (PDF generation or email integration).
- **Implementation:** Backend: endpoint that generates PDF (e.g. iText, OpenPDF) or sends email (Spring Mail); or frontend-only PDF (e.g. jsPDF) from existing summary data.

---

## Suggested order of work

1. **Quick wins**  
   - Re-enable settlements in `GroupSummaryService`.  
   - Add CORS globally and ensure all controllers work from the frontend.  
   - Frontend: use summary’s `settlements` and show “Who pays whom.”

2. **Frontend overhaul**  
   - Router, API layer, Tailwind (or chosen styling), dashboard + group detail + add expense.  
   - Loading, empty, error states and one “wow” (e.g. chart or theme toggle).

3. **Auth**  
   - Spring Security + JWT, register/login, protect APIs, simple login/signup on frontend.

4. **DB story**  
   - PostgreSQL profile and (if time) Flyway or Liquibase.

5. **Standout features**  
   - Pick 1–2: e.g. **Trip/event mode** + **invite link**, or **categories + analytics**, and implement end-to-end.

6. **README and polish**  
   - One-page README: what it does, tech stack, how to run, one screenshot or GIF, and 2–3 bullet points on “what makes this project interesting” (e.g. minimum-transaction settlements, trip mode, JWT auth).

---

## Summary

- **Re-learn** by walking through your existing controllers, services, entities, and React components; then add one small concept at a time (e.g. pagination, custom hook, JWT).
- **Frontend:** Router, API layer, Tailwind, full flows (groups → summary → add expense), loading/error/empty states, and one or two standout UI elements.
- **Backend:** Auth (Spring Security + JWT), optional PostgreSQL profile, global CORS, re-enable settlements, validation and error messages.
- **Standout:** Trip/event mode, invite link, categories + analytics, and clearly explaining “minimum-transaction settlements” will make this project memorable and interview-ready.

If you tell me your priority (e.g. “frontend first” or “auth first”), I can break the next step into concrete tasks and code changes in your repo.
