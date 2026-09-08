# Mobile Runtime Policy

Phase 19 decision: the first mobile release is online-only.

## Runtime flow

```text
Flutter app -> Spring Boot REST API -> MySQL
```

Flutter does not connect directly to MySQL and does not maintain a second copy of business state. Sales, purchases, stock changes, credit balances, and report calculations remain backend-owned.

## Offline scope

Offline synchronization is intentionally deferred. The current app does not:

- queue sales or purchases offline;
- cache mutable inventory or credit balances as authoritative data;
- replay transactions after reconnecting;
- resolve duplicate transactions or sync conflicts.

When the API is unavailable, screens show an explicit retryable network error. This prevents stale stock or customer due data from being presented as current.

## Development URLs

The API base URL is configured with the Dart compile-time value `API_BASE_URL`:

```powershell
flutter run --dart-define=API_BASE_URL=http://10.0.2.2:8081/api
```

Use `10.0.2.2` for an Android emulator reaching the host machine. For a physical phone, use the development computer's LAN IP, for example:

```powershell
flutter run --dart-define=API_BASE_URL=http://192.168.1.20:8081/api
```

The default `http://localhost:8081/api` is suitable only for desktop/web development on the same machine. Production builds must use a reachable HTTPS API URL.

## Reconsidering offline support

Offline support should only be added after online workflows are stable and the shop has a clear requirement. It would require durable pending-transaction storage, idempotency keys, synchronization rules, conflict handling, and tests for duplicate prevention.

See [MOBILE_SECURITY_REVIEW.md](MOBILE_SECURITY_REVIEW.md) for the Phase 20 release security gate.
