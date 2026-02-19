<<<<<<< HEAD
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
=======
# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) (or [oxc](https://oxc.rs) when used in [rolldown-vite](https://vite.dev/guide/rolldown)) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.
>>>>>>> b10887ff55024b65c8153fdd4b3715f2755bddc2
