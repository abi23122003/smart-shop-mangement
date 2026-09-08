import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/errors/app_exception.dart';
import '../../core/widgets/app_state_widgets.dart';
import 'supplier_model.dart';
import 'supplier_repository.dart';

class SuppliersPage extends StatefulWidget {
  const SuppliersPage({super.key});

  @override
  State<SuppliersPage> createState() => _SuppliersPageState();
}

class _SuppliersPageState extends State<SuppliersPage> {
  late final SupplierRepository _repository;
  List<SupplierModel> _suppliers = const [];
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _repository = SupplierRepository(context.read<ApiClient>());
    _load();
  }

  Future<void> _load() async {
    try {
      final suppliers = await _repository.list();
      if (mounted) setState(() { _suppliers = suppliers; _error = null; });
    } on AppException catch (exception) {
      if (mounted) setState(() => _error = exception.message);
    } catch (_) {
      if (mounted) setState(() => _error = 'Suppliers could not be loaded.');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        floatingActionButton: FloatingActionButton(onPressed: _add, tooltip: 'Add supplier', child: const Icon(Icons.add)),
        body: _loading ? const AppLoadingState(message: 'Loading suppliers...') : _error != null && _suppliers.isEmpty ? AppErrorState(message: _error!, onRetry: _load) : _suppliers.isEmpty ? const AppEmptyState(icon: Icons.local_shipping_outlined, title: 'No suppliers found', message: 'Add a supplier before recording purchases.') : RefreshIndicator(onRefresh: _load, child: ListView.separated(padding: const EdgeInsets.fromLTRB(20, 20, 20, 100), itemCount: _suppliers.length, separatorBuilder: (_, _) => const SizedBox(height: 10), itemBuilder: (_, index) { final supplier = _suppliers[index]; return Card(child: ListTile(leading: const CircleAvatar(child: Icon(Icons.local_shipping_outlined)), title: Text(supplier.name), subtitle: Text('${supplier.code}\n${supplier.phone}'), isThreeLine: true)); })),
      );

  Future<void> _add() async {
    final code = TextEditingController();
    final name = TextEditingController();
    final phone = TextEditingController();
    final result = await showDialog<SupplierModel>(context: context, builder: (_) => AlertDialog(title: const Text('Add supplier'), content: Column(mainAxisSize: MainAxisSize.min, children: [TextField(controller: code, decoration: const InputDecoration(labelText: 'Supplier code')), TextField(controller: name, decoration: const InputDecoration(labelText: 'Supplier name')), TextField(controller: phone, keyboardType: TextInputType.phone, decoration: const InputDecoration(labelText: 'Phone'))]), actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')), FilledButton(onPressed: () => Navigator.pop(context, SupplierModel(code: code.text.trim(), name: name.text.trim(), phone: phone.text.trim())), child: const Text('Save'))]));
    code.dispose(); name.dispose(); phone.dispose();
    if (result == null || result.code.isEmpty || result.name.isEmpty || result.phone.isEmpty) return;
    try { await _repository.create(result); await _load(); } on AppException catch (exception) { if (mounted) setState(() => _error = exception.message); }
  }
}
