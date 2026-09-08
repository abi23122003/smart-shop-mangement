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
    final selectedIndex = selected < 0 ? 0 : selected;
    final showQuickSale = location != '/sales' && location != '/sales/new';
    return Scaffold(
      appBar: AppBar(title: const Text('Sutharsan Store')),
      body: SafeArea(
        child: GestureDetector(
          behavior: HitTestBehavior.translucent,
          onTap: FocusManager.instance.primaryFocus?.unfocus,
          child: LayoutBuilder(
            builder: (context, constraints) => constraints.maxWidth >= 800
                ? Row(children: [
                    _rail(context, selectedIndex),
                    const VerticalDivider(width: 1),
                    Expanded(child: child),
                  ])
                : child,
          ),
        ),
      ),
      bottomNavigationBar: MediaQuery.sizeOf(context).width < 800 ? NavigationBar(
        selectedIndex: selectedIndex,
        onDestinationSelected: (index) => _navigate(context, index),
        destinations: [for (final item in destinations) NavigationDestination(icon: Icon(item.$3), label: item.$1)],
      ) : null,
      floatingActionButton: showQuickSale ? FloatingActionButton.extended(onPressed: () => context.go('/sales/new'), icon: const Icon(Icons.add), label: const Text('New sale')) : null,
    );
  }

  Widget _rail(BuildContext context, int selectedIndex) => NavigationRail(
        selectedIndex: selectedIndex,
        labelType: NavigationRailLabelType.all,
        onDestinationSelected: (index) => _navigate(context, index),
        destinations: [for (final item in destinations) NavigationRailDestination(icon: Icon(item.$3), label: Text(item.$1))],
      );

  void _navigate(BuildContext context, int index) {
    if (index == destinations.length - 1) {
      showModalBottomSheet<void>(context: context, builder: (_) => const MoreSheet());
    } else {
      context.go(destinations[index].$2);
    }
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
