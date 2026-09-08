# Mobile Test Matrix

Phase 21 baseline: 2026-09-08

## Automated checks

- Flutter static analysis: `flutter analyze`
- Flutter unit/widget tests: `flutter test`
- Backend regression tests: `backend\mvnw.cmd test`
- Dashboard, product, and customer JSON contract parsing
- Shared validation rules
- Network/401 error mapping
- Login screen rendering

## Manual or integration checks still requiring a running API

| Area | Scenarios |
|---|---|
| Authentication | valid login, wrong password, expired JWT, logout, 401 redirect |
| Products | browse, search, barcode lookup, stock display, low-stock state |
| Customers | create, search, credit balance, payment recording |
| Sales | cash, UPI, card, credit, multiple items, stock reduction, invoice endpoint |
| Purchases | supplier selection, purchase submission, stock increase |
| Reports | each supported report endpoint and profit summary |
| UX | phone layout, keyboard behavior, wide layout, camera permissions |
| Release | API URL injection, HTTPS endpoint, signed APK installation |

The current environment has no Android SDK/emulator, so Android camera, device networking, and APK checks remain release-gated. The API-backed scenarios should be run after Android Studio/SDK setup and against a non-production test database.
