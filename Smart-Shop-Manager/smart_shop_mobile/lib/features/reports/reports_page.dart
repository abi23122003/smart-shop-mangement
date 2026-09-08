import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/errors/app_exception.dart';
import '../../core/widgets/app_state_widgets.dart';
import 'report_repository.dart';

class ReportsPage extends StatefulWidget {
  const ReportsPage({super.key});

  @override
  State<ReportsPage> createState() => _ReportsPageState();
}

class _ReportsPageState extends State<ReportsPage> {
  late final ReportRepository _repository;
  String _type = 'sales';
  List<Map<String, dynamic>> _rows = const [];
  Map<String, dynamic> _profit = const {};
  bool _loading = true;
  String? _error;

  @override
  void initState() { super.initState(); _repository = ReportRepository(context.read<ApiClient>()); _load(); }

  Future<void> _load() async { setState(() { _loading = true; _error = null; }); try { final result = await Future.wait([_repository.rows(_type), _repository.profit()]); if (mounted) setState(() { _rows = result[0] as List<Map<String, dynamic>>; _profit = result[1] as Map<String, dynamic>; }); } on AppException catch (exception) { if (mounted) setState(() => _error = exception.message); } catch (_) { if (mounted) setState(() => _error = 'Reports could not be loaded.'); } finally { if (mounted) setState(() => _loading = false); } }

  @override
  Widget build(BuildContext context) => ListView(padding: const EdgeInsets.fromLTRB(20, 20, 20, 100), children: [
        Text('Reports', style: Theme.of(context).textTheme.headlineMedium), const SizedBox(height: 16),
        DropdownButtonFormField<String>(initialValue: _type, decoration: const InputDecoration(labelText: 'Report'), items: const {'sales': 'Sales', 'purchases': 'Purchases', 'products': 'Products', 'stock': 'Inventory', 'customers': 'Customers', 'suppliers': 'Suppliers'}.entries.map((entry) => DropdownMenuItem(value: entry.key, child: Text(entry.value))).toList(), onChanged: (value) { if (value != null) { setState(() => _type = value); _load(); } }),
        const SizedBox(height: 16),
        Card(child: ListTile(leading: const Icon(Icons.trending_up), title: const Text('Total profit'), subtitle: Text('INR ${(_profit['totalProfit'] as num?)?.toStringAsFixed(2) ?? '0.00'}'))), const SizedBox(height: 16),
        if (_loading) const SizedBox(height: 240, child: AppLoadingState(message: 'Loading report...')) else if (_error != null) AppErrorState(message: _error!, onRetry: _load) else if (_rows.isEmpty) const AppEmptyState(icon: Icons.assessment_outlined, title: 'No report data', message: 'Transactions will appear here when available.') else ..._rows.map((row) => Card(child: ListTile(title: Text(_title(row)), subtitle: Text(_details(row))))),
      ]);

  String _title(Map<String, dynamic> row) => '${row['productName'] ?? row['customerName'] ?? row['supplierName'] ?? row['saleCode'] ?? row['purchaseCode'] ?? 'Record'}';
  String _details(Map<String, dynamic> row) => 'Total: INR ${row['totalAmount'] ?? row['sellingPrice'] ?? row['quantity'] ?? '-'}';
}
