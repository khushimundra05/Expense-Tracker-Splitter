# Expense Tracker — Frontend

React + Vite + Tailwind CSS frontend for the expense tracker backend.

## Setup

1. Install dependencies: `npm install`
2. Create a `.env` file (or use defaults):
   ```
   VITE_API_URL=http://localhost:8080
   ```
3. Start the backend (from `expense-tracker-backend`): run the Spring Boot application on port 8080.
4. Start the frontend: `npm run dev` — app runs at http://localhost:5173

## Features

- **Dashboard** — List all groups, create a new group.
- **Group detail** — View summary (total spent), balances (who owes / is owed), settlements (who pays whom), list of expenses, add expense form, add member to group.
- **Users** — List all users, create a new user (ID + name). Users can then be added to groups from the group page.

## Stack

- React 19, React Router, Vite 7
- Tailwind CSS v4
- Fetch API for backend calls (see `src/api/client.js`)
