abstract final class AppConstants {
  static const defaultApiBaseUrl = 'http://localhost:8081/api';
  static const apiBaseUrl = String.fromEnvironment(
    'API_BASE_URL',
    defaultValue: defaultApiBaseUrl,
  );
  static const requestTimeout = Duration(seconds: 20);
  static const tokenStorageKey = 'smart_shop_access_token';
}
