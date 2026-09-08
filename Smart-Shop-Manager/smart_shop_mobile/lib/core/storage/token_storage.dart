import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import '../constants/app_constants.dart';

class TokenStorage {
  TokenStorage({FlutterSecureStorage? storage})
      : _storage = storage ?? const FlutterSecureStorage();

  final FlutterSecureStorage _storage;

  Future<String?> read() => _storage.read(key: AppConstants.tokenStorageKey);

  Future<void> write(String token) => _storage.write(
        key: AppConstants.tokenStorageKey,
        value: token,
      );

  Future<void> clear() => _storage.delete(key: AppConstants.tokenStorageKey);
}
