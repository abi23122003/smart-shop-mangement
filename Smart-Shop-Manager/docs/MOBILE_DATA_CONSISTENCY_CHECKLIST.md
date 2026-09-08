# Mobile Data Consistency Checklist

Phase 22 review date: 2026-09-08

The Spring Boot backend remains authoritative for mutations. Flutter only collects input, submits requests, and reloads server data.

## Verified workflows

- A sale is submitted through `/sales`; backend stock reduction and credit updates are not reimplemented locally.
- A purchase is submitted through `/purchases`; backend inventory increase and total calculation remain authoritative.
- Customer due display is loaded from `/credits`, not calculated from a second local balance.
- Dashboard metrics are loaded from `/dashboard` after navigation or refresh.
- Reports are loaded from `/reports/*` and are not derived from cached mobile lists.
- Product and customer search results are used only for selection; they are not treated as permanent business state.
- Sale cart selections preserve their product snapshots when the user changes the search query, preventing item loss before submission.

## Required integration checks

1. Complete a purchase and verify product quantity increases in Products and Dashboard.
2. Complete a cash sale and verify product quantity decreases in Products and Reports.
3. Complete a credit sale and verify customer outstanding balance increases in Customers.
4. Record a customer payment and verify the outstanding balance decreases.
5. Delete a sale in the web app and verify restored stock and credit reversal are reflected after mobile refresh.
6. Compare report totals with the web application against the same database.

These checks require the running backend and a non-production test database. The current automated tests cover model contracts and local state behavior; Android device execution remains pending Android SDK setup.
