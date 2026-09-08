import 'package:flutter_test/flutter_test.dart';
import 'package:smart_shop_mobile/core/auth/auth_controller.dart';
import 'package:smart_shop_mobile/core/auth/auth_repository.dart';

void main() {
  test('starts without an authenticated session', () {
    final controller = AuthController(_FakeAuthRepository());

    expect(controller.isAuthenticated, isFalse);
    expect(controller.isInitializing, isTrue);
  });
}

class _FakeAuthRepository implements AuthRepository {
  @override
  Future<void> login(String username, String password) async {}

  @override
  Future<String?> restoreToken() async => null;

  @override
  Future<void> logout() async {}
}
