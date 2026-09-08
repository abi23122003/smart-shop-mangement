import 'dart:async';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/widgets/app_state_widgets.dart';
import 'customer_model.dart';
import 'customer_repository.dart';
import 'customers_controller.dart';

class CustomersPage extends StatefulWidget {
  const CustomersPage({super.key});

  @override
  State<CustomersPage> createState() => _CustomersPageState();
}

class _CustomersPageState extends State<CustomersPage> {
  late final CustomersController _controller;
  final _searchController = TextEditingController();
  Timer? _searchTimer;

  @override
  void initState() {
    super.initState();
    _controller = CustomersController(CustomerRepository(context.read<ApiClient>()));
    _controller.load();
  }

  @override
  void dispose() {
    _searchTimer?.cancel();
    _searchController.dispose();
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => ChangeNotifierProvider.value(
        value: _controller,
        child: Consumer<CustomersController>(
          builder: (context, controller, _) => Column(children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 12),
              child: TextField(
                controller: _searchController,
                onChanged: (value) {
                  _searchTimer?.cancel();
                  _searchTimer = Timer(const Duration(milliseconds: 350), () => controller.load(value));
                },
                decoration: const InputDecoration(labelText: 'Search customers', prefixIcon: Icon(Icons.search)),
              ),
            ),
            Expanded(child: _body(controller)),
          ]),
        ),
      );

  Widget _body(CustomersController controller) {
    if (controller.isLoading && controller.customers.isEmpty) return const AppLoadingState(message: 'Loading customers...');
    if (controller.error != null && controller.customers.isEmpty) return AppErrorState(message: controller.error!, onRetry: controller.load);
    if (controller.customers.isEmpty) return const AppEmptyState(icon: Icons.people_outline, title: 'No customers found', message: 'Add your first customer to track sales and credit.');
    return RefreshIndicator(
      onRefresh: controller.load,
      child: ListView.separated(
        padding: const EdgeInsets.fromLTRB(20, 8, 20, 100),
        itemCount: controller.customers.length,
        separatorBuilder: (_, _) => const SizedBox(height: 10),
        itemBuilder: (_, index) {
          final customer = controller.customers[index];
          final credit = customer.id == null ? null : controller.credits[customer.id];
          return Card(
            child: ListTile(
              leading: const CircleAvatar(child: Icon(Icons.person_outline)),
              title: Text(customer.name),
              subtitle: Text('${customer.phone}\nOutstanding: INR ${(credit?.balance ?? 0).toStringAsFixed(2)}'),
              isThreeLine: true,
                trailing: credit != null && credit.balance > 0
                  ? IconButton(icon: const Icon(Icons.payments_outlined), tooltip: 'Record payment', onPressed: () => _payment(customer))
                  : null,
            ),
          );
        },
      ),
    );
  }

  Future<void> _payment(CustomerModel customer) async {
    final amountController = TextEditingController();
    final amount = await showDialog<double>(
      context: context,
      builder: (_) => AlertDialog(
        title: Text('Payment for ${customer.name}'),
        content: TextField(controller: amountController, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'Amount', prefixText: 'INR ')),
        actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')), FilledButton(onPressed: () => Navigator.pop(context, double.tryParse(amountController.text)), child: const Text('Record'))],
      ),
    );
    amountController.dispose();
    if (amount != null && amount > 0) await _controller.recordPayment(customer, amount);
  }
}
