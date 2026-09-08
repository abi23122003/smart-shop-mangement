import 'dart:async';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/errors/app_exception.dart';
import '../../core/widgets/app_state_widgets.dart';
import '../customers/customer_model.dart';
import '../products/product_model.dart';
import 'sale_repository.dart';

class SalePage extends StatefulWidget {
  const SalePage({super.key});

  @override
  State<SalePage> createState() => _SalePageState();
}

class _SalePageState extends State<SalePage> {
  late final SaleRepository _repository;
  final _searchController = TextEditingController();
  Timer? _searchTimer;
  List<CustomerModel> _customers = const [];
  List<ProductModel> _products = const [];
  final Map<int, int> _cart = {};
  final Map<int, ProductModel> _cartProducts = {};
  int? _customerId;
  String _paymentMethod = 'Cash';
  bool _loading = true;
  bool _saving = false;
  String? _error;

  @override
  void initState() {
    super.initState();
    _repository = SaleRepository(context.read<ApiClient>());
    _loadCustomers();
  }

  @override
  void dispose() {
    _searchTimer?.cancel();
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _loadCustomers() async {
    try {
      _customers = await _repository.customers();
    } on AppException catch (exception) {
      _error = exception.message;
    } catch (_) {
      _error = 'Customers could not be loaded.';
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  Future<void> _search(String value) async {
    _searchTimer?.cancel();
    _searchTimer = Timer(const Duration(milliseconds: 350), () async {
      if (value.trim().isEmpty) {
        if (mounted) setState(() => _products = []);
        return;
      }
      try {
        final products = await _repository.products(value);
        if (mounted) setState(() => _products = products);
      } on AppException catch (exception) {
        if (mounted) setState(() => _error = exception.message);
      }
    });
  }

  Future<void> _completeSale() async {
    if (_customerId == null || _cart.isEmpty) {
      setState(() => _error = 'Choose a customer and add at least one product.');
      return;
    }
    setState(() { _saving = true; _error = null; });
    try {
      final items = _cart.entries.map((entry) {
        final product = _cartProducts[entry.key]!;
        return {'productId': product.id, 'quantity': entry.value, 'sellingPrice': product.sellingPrice ?? 0};
      }).toList();
      final sale = await _repository.create(customerId: _customerId!, paymentMethod: _paymentMethod, items: items);
      if (mounted) {
        setState(() { _cart.clear(); _cartProducts.clear(); _products = []; _searchController.clear(); });
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Sale ${sale['saleCode'] ?? ''} completed successfully.')));
      }
    } on AppException catch (exception) {
      setState(() => _error = exception.message);
    } catch (_) {
      setState(() => _error = 'Sale could not be completed.');
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) return const AppLoadingState(message: 'Loading sale form...');
    return ListView(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 100),
      children: [
        Text('New sale', style: Theme.of(context).textTheme.headlineMedium),
        const SizedBox(height: 16),
        if (_error != null) ...[AppErrorState(message: _error!), const SizedBox(height: 12)],
        DropdownButtonFormField<int>(
          initialValue: _customerId,
          decoration: const InputDecoration(labelText: 'Customer'),
          items: _customers.map((customer) => DropdownMenuItem(value: customer.id, child: Text(customer.name))).toList(),
          onChanged: (value) => setState(() => _customerId = value),
        ),
        const SizedBox(height: 16),
        TextField(controller: _searchController, onChanged: _search, decoration: const InputDecoration(labelText: 'Search or scan product', prefixIcon: Icon(Icons.search))),
        if (_products.isNotEmpty) ...[
          const SizedBox(height: 8),
          Card(
            child: Column(
              children: [
                for (final product in _products)
                  ListTile(
                    title: Text(product.name),
                    subtitle: Text('Stock ${product.quantity} - INR ${(product.sellingPrice ?? 0).toStringAsFixed(2)}'),
                    trailing: IconButton(
                      onPressed: product.quantity > 0 && product.id != null
                          ? () => setState(() {
                              _cart[product.id!] = (_cart[product.id!] ?? 0) + 1;
                              _cartProducts[product.id!] = product;
                            })
                          : null,
                      icon: const Icon(Icons.add),
                    ),
                  ),
              ],
            ),
          ),
        ],
        const SizedBox(height: 16),
        Text('Cart', style: Theme.of(context).textTheme.titleLarge),
        const SizedBox(height: 8),
        if (_cart.isEmpty) const Text('No products added yet.'),
        for (final entry in _cart.entries) _cartRow(entry),
        const SizedBox(height: 16),
        DropdownButtonFormField<String>(initialValue: _paymentMethod, decoration: const InputDecoration(labelText: 'Payment method'), items: const ['Cash', 'UPI', 'Card', 'Credit'].map((method) => DropdownMenuItem(value: method, child: Text(method))).toList(), onChanged: (value) => setState(() => _paymentMethod = value ?? 'Cash')),
        const SizedBox(height: 24),
        FilledButton.icon(onPressed: _saving ? null : _completeSale, icon: _saving ? const SizedBox.square(dimension: 18, child: CircularProgressIndicator(strokeWidth: 2)) : const Icon(Icons.check), label: Text(_saving ? 'Completing...' : 'Complete sale')),
      ],
    );
  }

  Widget _cartRow(MapEntry<int, int> entry) {
    final product = _cartProducts[entry.key];
    return ListTile(title: Text(product?.name ?? 'Product #${entry.key}'), subtitle: Text('Quantity: ${entry.value}'), trailing: IconButton(onPressed: () => setState(() { _cart.remove(entry.key); _cartProducts.remove(entry.key); }), icon: const Icon(Icons.delete_outline)));
  }
}
