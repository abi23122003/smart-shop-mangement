import 'dart:convert';

import 'package:flutter/foundation.dart';

import '../errors/app_exception.dart';
import 'auth_repository.dart';

class AuthController extends ChangeNotifier {
  AuthController(this._repository);

  final AuthRepository _repository;
  String? _token;
  bool _isInitializing = true;
  bool _isLoading = false;
  String? _error;

  bool get isInitializing => _isInitializing;
  bool get isLoading => _isLoading;
  bool get isAuthenticated => _token != null;
  String? get error => _error;

  Future<void> restoreSession() async {
    _token = await _repository.restoreToken();
    if (_token != null && _isExpired(_token!)) {
      await _repository.logout();
      _token = null;
    }
    _isInitializing = false;
    notifyListeners();
  }

  Future<bool> login(String username, String password) async {
    _isLoading = true;
    _error = null;
    notifyListeners();
    try {
      await _repository.login(username.trim(), password);
      _token = await _repository.restoreToken();
      return true;
    } on AppException catch (exception) {
      _error = exception.message;
      return false;
    } catch (_) {
      _error = 'Sign-in failed. Please check your credentials and try again.';
      return false;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> logout() async {
    await _repository.logout();
    _token = null;
    _error = null;
    notifyListeners();
  }

  void clearError() {
    if (_error == null) return;
    _error = null;
    notifyListeners();
  }

  static bool _isExpired(String token) {
    try {
      final sections = token.split('.');
      if (sections.length != 3) return true;
      final normalized = base64Url.normalize(sections[1]);
      final payload = jsonDecode(utf8.decode(base64Url.decode(normalized)));
      final expiration = payload['exp'];
      return expiration is! num ||
          DateTime.fromMillisecondsSinceEpoch((expiration * 1000).toInt()).isBefore(DateTime.now());
    } catch (_) {
      return true;
    }
  }
}
