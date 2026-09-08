import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:smart_shop_mobile/core/auth/auth_controller.dart';
import 'package:smart_shop_mobile/core/auth/auth_repository.dart';
import 'package:smart_shop_mobile/features/login/login_page.dart';

void main() {
  testWidgets('renders the login screen', (tester) async {
    final controller = AuthController(_FakeAuthRepository());
    await tester.pumpWidget(
      ChangeNotifierProvider.value(
        value: controller,
        child: const MaterialApp(home: LoginPage()),
      ),
    );

    expect(find.text('Sutharsan Store'), findsOneWidget);
    expect(find.text('Sign in to manage your shop.'), findsOneWidget);
    expect(find.text('Sign in'), findsOneWidget);
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
