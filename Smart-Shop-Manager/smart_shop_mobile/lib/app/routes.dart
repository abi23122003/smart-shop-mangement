import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../core/auth/auth_controller.dart';
import '../features/login/login_page.dart';
import '../features/dashboard/dashboard_page.dart';
import '../features/products/products_page.dart';
import '../features/customers/customers_page.dart';
import '../features/sales/sale_page.dart';
import '../features/suppliers/suppliers_page.dart';
import '../features/purchases/purchase_page.dart';
import '../features/reports/reports_page.dart';
import '../features/settings/settings_page.dart';
import 'mobile_shell.dart';

GoRouter createRouter(AuthController auth) {
  return GoRouter(
    initialLocation: '/loading',
    refreshListenable: auth,
    redirect: (context, state) {
      if (auth.isInitializing) return '/loading';
      final isLogin = state.matchedLocation == '/login';
      if (!auth.isAuthenticated) return isLogin ? null : '/login';
      return isLogin || state.matchedLocation == '/loading' ? '/dashboard' : null;
    },
    routes: [
      GoRoute(path: '/loading', builder: (_, _) => const _LoadingPage()),
      GoRoute(path: '/login', builder: (_, _) => const LoginPage()),
      ShellRoute(
        builder: (context, state, child) => MobileShell(child: child),
        routes: [
          GoRoute(path: '/dashboard', builder: (_, _) => const DashboardPage()),
          GoRoute(path: '/products', builder: (_, _) => const ProductsPage()),
          GoRoute(path: '/customers', builder: (_, _) => const CustomersPage()),
          GoRoute(path: '/sales', builder: (_, _) => const SalePage()),
          GoRoute(path: '/sales/new', builder: (_, _) => const SalePage()),
          GoRoute(path: '/purchases', builder: (_, _) => const PurchasePage()),
          GoRoute(path: '/suppliers', builder: (_, _) => const SuppliersPage()),
          GoRoute(path: '/reports', builder: (_, _) => const ReportsPage()),
          GoRoute(path: '/settings', builder: (_, _) => const SettingsPage()),
        ],
      ),
    ],
  );
}

class _LoadingPage extends StatelessWidget {
  const _LoadingPage();

  @override
  Widget build(BuildContext context) => const Scaffold(body: Center(child: CircularProgressIndicator()));
}
