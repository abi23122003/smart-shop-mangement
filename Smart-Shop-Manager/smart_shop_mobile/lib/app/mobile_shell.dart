import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class MobileShell extends StatelessWidget {
  const MobileShell({super.key, required this.child});

  final Widget child;

  static const destinations = [
    ('Home', '/dashboard', Icons.home_outlined),
    ('Products', '/products', Icons.inventory_2_outlined),
    ('Customers', '/customers', Icons.people_outline),
    ('Sales', '/sales', Icons.point_of_sale_outlined),
    ('More', '/more', Icons.more_horiz),
  ];

  @override
  Widget build(BuildContext context) {
    final location = GoRouterState.of(context).matchedLocation;
    final selected = destinations.indexWhere((item) => location.startsWith(item.$2));
    return Scaffold(
      appBar: AppBar(title: const Text('Sutharsan Store')),
      body: child,
      bottomNavigationBar: NavigationBar(
        selectedIndex: selected < 0 ? 0 : selected,
        onDestinationSelected: (index) {
          if (index == destinations.length - 1) {
            showModalBottomSheet<void>(context: context, builder: (_) => const MoreSheet());
          } else {
            context.go(destinations[index].$2);
          }
        },
        destinations: [
          for (final item in destinations) NavigationDestination(icon: Icon(item.$3), label: item.$1),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => context.go('/sales/new'),
        icon: const Icon(Icons.add),
        label: const Text('New sale'),
      ),
    );
  }
}

class MoreSheet extends StatelessWidget {
  const MoreSheet({super.key});

  @override
  Widget build(BuildContext context) => SafeArea(
        child: Wrap(
          children: [
            for (final item in const [
              ('Purchases', '/purchases', Icons.shopping_cart_outlined),
              ('Suppliers', '/suppliers', Icons.local_shipping_outlined),
              ('Reports', '/reports', Icons.assessment_outlined),
              ('Settings', '/settings', Icons.settings_outlined),
            ])
              ListTile(
                leading: Icon(item.$3),
                title: Text(item.$1),
                onTap: () {
                  Navigator.pop(context);
                  context.go(item.$2);
                },
              ),
          ],
        ),
      );
}
