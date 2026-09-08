import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/widgets/app_state_widgets.dart';
import 'dashboard_controller.dart';
import 'dashboard_repository.dart';

class DashboardPage extends StatefulWidget {
  const DashboardPage({super.key});

  @override
  State<DashboardPage> createState() => _DashboardPageState();
}

class _DashboardPageState extends State<DashboardPage> {
  late final DashboardController _controller;

  @override
  void initState() {
    super.initState();
    _controller = DashboardController(DashboardRepository(context.read<ApiClient>()));
    _controller.load();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider.value(
      value: _controller,
      child: Consumer<DashboardController>(
        builder: (context, controller, _) {
          if (controller.isLoading && controller.data == null) return const AppLoadingState(message: 'Loading dashboard...');
          if (controller.error != null && controller.data == null) return AppErrorState(message: controller.error!, onRetry: controller.load);
          final data = controller.data;
          if (data == null) return const AppEmptyState(icon: Icons.dashboard_outlined, title: 'No dashboard data', message: 'There is no shop data to show yet.');
          return RefreshIndicator(
            onRefresh: controller.load,
            child: ListView(
              padding: const EdgeInsets.fromLTRB(20, 24, 20, 32),
              children: [
                Text('Dashboard', style: Theme.of(context).textTheme.headlineMedium),
                const SizedBox(height: 6),
                Text('A live view of Sutharsan Store.', style: Theme.of(context).textTheme.bodyLarge),
                const SizedBox(height: 24),
                GridView.count(
                  crossAxisCount: MediaQuery.sizeOf(context).width >= 600 ? 4 : 2,
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  crossAxisSpacing: 12,
                  mainAxisSpacing: 12,
                  childAspectRatio: 1.35,
                  children: [
                    _MetricCard(label: 'Products', value: '${data.totalProducts}', icon: Icons.inventory_2_outlined),
                    _MetricCard(label: 'Categories', value: '${data.totalCategories}', icon: Icons.category_outlined),
                    _MetricCard(label: 'Customers', value: '${data.totalCustomers}', icon: Icons.people_outline),
                    _MetricCard(label: "Today's sales", value: _money(data.todaySales), icon: Icons.trending_up),
                  ],
                ),
                const SizedBox(height: 20),
                Card(
                  child: ListTile(
                    leading: const Icon(Icons.warning_amber_outlined),
                    title: const Text('Low stock'),
                    subtitle: Text('${data.lowStockProducts} product${data.lowStockProducts == 1 ? '' : 's'} need attention.'),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }

  String _money(double value) => 'INR ${value.toStringAsFixed(2)}';
}

class _MetricCard extends StatelessWidget {
  const _MetricCard({required this.label, required this.value, required this.icon});

  final String label;
  final String value;
  final IconData icon;

  @override
  Widget build(BuildContext context) => Card(
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
            Icon(icon, color: Theme.of(context).colorScheme.primary),
            Text(value, style: Theme.of(context).textTheme.titleLarge),
            Text(label, style: Theme.of(context).textTheme.bodySmall),
          ]),
        ),
      );
}
