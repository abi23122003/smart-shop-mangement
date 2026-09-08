import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

import '../core/api/api_client.dart';
import '../core/auth/auth_controller.dart';
import '../core/auth/auth_repository.dart';
import '../core/storage/token_storage.dart';
import '../core/widgets/app_state_widgets.dart';
import 'routes.dart';
import 'theme.dart';

class SmartShopApp extends StatefulWidget {
  const SmartShopApp({super.key});

  @override
  State<SmartShopApp> createState() => _SmartShopAppState();
}

class _SmartShopAppState extends State<SmartShopApp> {
  late final ApiClient _apiClient;
  late final AuthController _authController;
  late final GoRouter _router;

  @override
  void initState() {
    super.initState();
    final apiClient = ApiClient();
    final authController = AuthController(ApiAuthRepository(apiClient, TokenStorage()));
    apiClient.setUnauthorizedHandler(authController.logout);
    _apiClient = apiClient;
    _authController = authController;
    _router = createRouter(_authController);
    _authController.restoreSession();
  }

  @override
  void dispose() {
    _authController.dispose();
    _router.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        Provider<ApiClient>.value(value: _apiClient),
        ChangeNotifierProvider<AuthController>.value(value: _authController),
      ],
      child: MaterialApp.router(
      title: 'Sutharsan Store',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.light,
      darkTheme: AppTheme.dark,
      themeMode: ThemeMode.system,
      routerConfig: _router,
      ),
    );
  }
}

class FoundationHomePage extends StatelessWidget {
  const FoundationHomePage({super.key});

  @override
  Widget build(BuildContext context) {
    final colors = Theme.of(context).colorScheme;
    return Scaffold(
      appBar: AppBar(
        title: const Text('Sutharsan Store'),
        actions: [
          IconButton(
            tooltip: 'Theme settings',
            onPressed: () {},
            icon: const Icon(Icons.brightness_6_outlined),
          ),
        ],
      ),
      body: SafeArea(
        child: LayoutBuilder(
          builder: (context, constraints) {
            final horizontalPadding = constraints.maxWidth >= 600 ? 32.0 : 20.0;
            return ListView(
              padding: EdgeInsets.fromLTRB(horizontalPadding, 28, horizontalPadding, 32),
              children: [
                Text('Shop overview', style: Theme.of(context).textTheme.headlineMedium),
                const SizedBox(height: 8),
                Text(
                  'Your daily operations will appear here after sign-in.',
                  style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                        color: colors.onSurfaceVariant,
                      ),
                ),
                const SizedBox(height: 24),
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(20),
                    child: Row(
                      children: [
                        CircleAvatar(
                          radius: 26,
                          backgroundColor: colors.primaryContainer,
                          child: Icon(Icons.storefront_outlined, color: colors.onPrimaryContainer),
                        ),
                        const SizedBox(width: 16),
                        const Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text('Sutharsan Store'),
                              SizedBox(height: 4),
                              Text('Inventory, sales, customers, and reports'),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 20),
                const AppEmptyState(
                  icon: Icons.dashboard_customize_outlined,
                  title: 'Ready for setup',
                  message: 'Connect your store account to load live business data.',
                ),
              ],
            );
          },
        ),
      ),
    );
  }
}
