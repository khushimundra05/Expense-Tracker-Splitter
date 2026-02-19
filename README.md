# Expense Tracker & Splitter

A full-stack expense splitting web application built with Spring Boot and React.

Users can create groups, add expenses, compute balances, and automatically calculate the minimum number of transactions required to settle debts.

---

## Features

- Create groups and add members
- Add expenses with payer selection
- Automatic equal split calculation
- Net balance computation
- Greedy algorithm to minimize settlement transactions
- REST API backend (Spring Boot + JPA)
- Modern React frontend (Vite + Tailwind CSS)

---

## Settlement Logic

1. Compute net balance per user
2. Separate creditors and debtors
3. Greedy matching to minimize number of payments
4. Repeat until all balances are zero

---

## Tech Stack

Backend: Java, Spring Boot, JPA, Maven  
Frontend: React, Vite, Tailwind CSS

---

## Run Locally

### Backend

```bash
cd expense-tracker-backend
mvn spring-boot:run
```

### Frontend

```bash
cd expense-tracker-frontend
npm install
npm run dev
```

---

## Author

Khushi Mundra
