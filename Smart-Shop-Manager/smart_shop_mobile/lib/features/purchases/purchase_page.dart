import 'dart:async';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/errors/app_exception.dart';
import '../../core/widgets/app_state_widgets.dart';
import '../products/product_model.dart';
import '../suppliers/supplier_model.dart';
import 'purchase_repository.dart';

class PurchasePage extends StatefulWidget {
  const PurchasePage({super.key});

  @override
  State<PurchasePage> createState() => _PurchasePageState();
}

class _PurchasePageState extends State<PurchasePage> {
  late final PurchaseRepository _repository;
  List<SupplierModel> _suppliers = const [];
  List<ProductModel> _products = const [];
  SupplierModel? _supplier;
  ProductModel? _product;
  final _code = TextEditingController();
  final _search = TextEditingController();
  final _quantity = TextEditingController(text: '1');
  final _price = TextEditingController(text: '0');
  Timer? _timer;
  bool _loading = true;
  bool _saving = false;
  String? _error;

  @override
  void initState() { super.initState(); _repository = PurchaseRepository(context.read<ApiClient>()); _loadSuppliers(); }
  @override
  void dispose() { _timer?.cancel(); _code.dispose(); _search.dispose(); _quantity.dispose(); _price.dispose(); super.dispose(); }

  Future<void> _loadSuppliers() async { try { _suppliers = await _repository.suppliers(); } on AppException catch (exception) { _error = exception.message; } catch (_) { _error = 'Suppliers could not be loaded.'; } finally { if (mounted) setState(() => _loading = false); } }
  void _findProduct(String value) { _timer?.cancel(); _timer = Timer(const Duration(milliseconds: 350), () async { if (value.trim().isEmpty) return; try { final products = await _repository.products(value); if (mounted) setState(() => _products = products); } on AppException catch (exception) { if (mounted) setState(() => _error = exception.message); } }); }

  @override
  Widget build(BuildContext context) {
    if (_loading) return const AppLoadingState(message: 'Loading purchase form...');
    return ListView(padding: const EdgeInsets.fromLTRB(20, 20, 20, 100), children: [
      Text('New purchase', style: Theme.of(context).textTheme.headlineMedium), const SizedBox(height: 16),
      if (_error != null) Text(_error!, style: TextStyle(color: Theme.of(context).colorScheme.error)),
      TextField(controller: _code, decoration: const InputDecoration(labelText: 'Purchase code')), const SizedBox(height: 12),
      DropdownButtonFormField<SupplierModel>(initialValue: _supplier, decoration: const InputDecoration(labelText: 'Supplier'), items: _suppliers.map((supplier) => DropdownMenuItem(value: supplier, child: Text(supplier.name))).toList(), onChanged: (value) => setState(() => _supplier = value)), const SizedBox(height: 12),
      TextField(controller: _search, onChanged: _findProduct, decoration: const InputDecoration(labelText: 'Search product')),
      if (_products.isNotEmpty) ...[
        const SizedBox(height: 8),
        for (final product in _products)
          ListTile(
            title: Text(product.name),
            trailing: IconButton(
              onPressed: () => setState(() {
                _product = product;
                _price.text = '${product.purchasePrice ?? 0}';
              }),
              icon: const Icon(Icons.add),
            ),
          ),
      ],
      if (_product != null) Text('Selected: ${_product!.name}', style: Theme.of(context).textTheme.titleMedium), const SizedBox(height: 12),
      Row(children: [Expanded(child: TextField(controller: _quantity, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'Quantity'))), const SizedBox(width: 12), Expanded(child: TextField(controller: _price, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'Purchase price')))]), const SizedBox(height: 24),
      FilledButton.icon(onPressed: _saving ? null : _save, icon: const Icon(Icons.inventory_2_outlined), label: Text(_saving ? 'Saving...' : 'Complete purchase')),
    ]);
  }

  Future<void> _save() async { final quantity = int.tryParse(_quantity.text); final price = double.tryParse(_price.text); if (_supplier?.id == null || _product?.id == null || _code.text.trim().isEmpty || quantity == null || quantity <= 0 || price == null || price < 0) { setState(() => _error = 'Enter a code, supplier, product, quantity, and valid price.'); return; } setState(() { _saving = true; _error = null; }); try { final result = await _repository.create(supplierId: _supplier!.id!, code: _code.text.trim(), quantity: quantity, price: price, productId: _product!.id!); if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Purchase ${result['purchaseCode'] ?? ''} completed. Inventory increased.'))); } on AppException catch (exception) { if (mounted) setState(() => _error = exception.message); } finally { if (mounted) setState(() => _saving = false); } }
}
