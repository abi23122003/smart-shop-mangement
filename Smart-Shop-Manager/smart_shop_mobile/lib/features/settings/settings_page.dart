import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

import '../../core/auth/auth_controller.dart';

class SettingsPage extends StatelessWidget {
  const SettingsPage({super.key});

  @override
  Widget build(BuildContext context) => ListView(
        padding: const EdgeInsets.fromLTRB(20, 20, 20, 100),
        children: [
          Text('Settings', style: Theme.of(context).textTheme.headlineMedium),
          const SizedBox(height: 16),
          const Card(child: ListTile(leading: Icon(Icons.store_outlined), title: Text('Shop information'), subtitle: Text('Sutharsan Store'))),
          const SizedBox(height: 8),
          const Card(child: ListTile(leading: Icon(Icons.receipt_long_outlined), title: Text('Receipt settings'), subtitle: Text('Receipt generation is handled by the backend.'))),
          const SizedBox(height: 8),
          Card(child: ListTile(leading: const Icon(Icons.logout), title: const Text('Logout'), onTap: () async { await context.read<AuthController>().logout(); if (context.mounted) context.go('/login'); })),
        ],
      );
}
