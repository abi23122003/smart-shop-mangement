import '../api/api_client.dart';
import '../storage/token_storage.dart';

abstract interface class AuthRepository {
  Future<void> login(String username, String password);

  Future<String?> restoreToken();

  Future<void> logout();
}

class ApiAuthRepository implements AuthRepository {
  ApiAuthRepository(this._apiClient, this._tokenStorage);

  final ApiClient _apiClient;
  final TokenStorage _tokenStorage;

  @override
  Future<void> login(String username, String password) async {
    final response = await _apiClient.post<Map<String, dynamic>>(
      '/auth/login',
      data: {'username': username, 'password': password},
    );
    final token = response.data?['token'];
    if (token is! String || token.isEmpty) {
      throw StateError('The login response did not contain a token.');
    }
    await _tokenStorage.write(token);
  }

  @override
  Future<String?> restoreToken() => _tokenStorage.read();

  @override
  Future<void> logout() => _tokenStorage.clear();
}
