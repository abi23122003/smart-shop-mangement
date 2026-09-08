# Mobile Security Review

Phase 20 review date: 2026-09-08

## Passed Controls

- Flutter does not connect directly to MySQL.
- JWTs are stored with `flutter_secure_storage`, not shared preferences or plaintext files.
- The API client adds the bearer token centrally and clears it after HTTP 401.
- Login credentials are entered by the user and are not hardcoded in Flutter.
- The API base URL is supplied with `--dart-define=API_BASE_URL` for device and production environments.
- Business rules remain in Spring Boot; Flutter does not calculate stock or customer due balances as authoritative state.
- Online-only behavior avoids replaying stale or duplicated transactions.

## Findings

### High: hardcoded backend secrets

The backend currently contains a hardcoded JWT signing secret in `JwtService.java` and a MySQL root password in `application.properties`. Neither value is present in the Flutter project, but both must be removed before deployment.

Required mitigation:

- inject the JWT secret from an environment variable or secret manager;
- inject database URL, username, and password from environment-specific configuration;
- rotate both existing values after externalization;
- never package either value in an APK or public repository.

### High: HTTP local development URL

The default mobile URL is `http://localhost:8081/api` for local development. This is acceptable only for desktop/web development. Android emulator and physical-device development must use a reachable host URL, and production must use HTTPS.

Required commands:

```powershell
flutter run --dart-define=API_BASE_URL=http://10.0.2.2:8081/api
flutter build apk --release --dart-define=API_BASE_URL=https://api.example.com/api
```

### Medium: backend CORS is localhost-only

The backend CORS configuration currently allows localhost and 127.0.0.1 patterns. A deployed mobile APK does not send browser CORS headers, but any web deployment or browser-based testing must use an explicit production origin list rather than a wildcard.

### Medium: token revocation is not server-side

The backend uses stateless 24-hour JWTs and has no logout/revocation endpoint. Mobile logout clears the local token. A compromised unexpired token remains valid until expiry; production hardening should consider short-lived access tokens and refresh-token rotation.

## Release Gate

The mobile release must not be marked production-ready until:

1. backend secrets are externalized and rotated;
2. the release API URL is HTTPS;
3. Android release signing is configured outside source control;
4. the APK contains no credentials, database URL, JWT secret, or test account;
5. authentication failure, logout, and expired-token behavior are tested against the deployed API.

The Phase 21 automated and manual test matrix is tracked in [MOBILE_TEST_MATRIX.md](MOBILE_TEST_MATRIX.md).
