import 'dart:async';

import 'package:flutter/material.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/widgets/app_state_widgets.dart';
import 'product_model.dart';
import 'product_repository.dart';
import 'products_controller.dart';

class ProductsPage extends StatefulWidget {
  const ProductsPage({super.key});

  @override
  State<ProductsPage> createState() => _ProductsPageState();
}

class _ProductsPageState extends State<ProductsPage> {
  late final ProductsController _controller;
  final _searchController = TextEditingController();
  Timer? _searchTimer;

  @override
  void initState() {
    super.initState();
    _controller = ProductsController(ProductRepository(context.read<ApiClient>()));
    _controller.load();
  }

  @override
  void dispose() {
    _searchTimer?.cancel();
    _searchController.dispose();
    _controller.dispose();
    super.dispose();
  }

  void _search(String value) {
    _searchTimer?.cancel();
    _searchTimer = Timer(const Duration(milliseconds: 350), () => _controller.load(value));
  }

  Future<void> _scan() async {
    final code = await showModalBottomSheet<String>(
      context: context,
      isScrollControlled: true,
      builder: (_) => const _BarcodeScannerSheet(),
    );
    if (code != null && mounted) {
      _searchController.text = code;
      _controller.load(code);
    }
  }

  @override
  Widget build(BuildContext context) => ChangeNotifierProvider.value(
        value: _controller,
        child: Consumer<ProductsController>(
          builder: (context, controller, _) => Column(
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 20, 20, 12),
                child: Row(children: [
                  Expanded(
                    child: TextField(
                      controller: _searchController,
                      onChanged: _search,
                      decoration: const InputDecoration(labelText: 'Search products or barcode', prefixIcon: Icon(Icons.search)),
                    ),
                  ),
                  const SizedBox(width: 8),
                  IconButton.filledTonal(onPressed: _scan, tooltip: 'Scan barcode', icon: const Icon(Icons.qr_code_scanner)),
                ]),
              ),
              Expanded(child: _body(controller)),
            ],
          ),
        ),
      );

  Widget _body(ProductsController controller) {
    if (controller.isLoading && controller.products.isEmpty) return const AppLoadingState(message: 'Loading products...');
    if (controller.error != null && controller.products.isEmpty) return AppErrorState(message: controller.error!, onRetry: controller.load);
    if (controller.products.isEmpty) return const AppEmptyState(icon: Icons.inventory_2_outlined, title: 'No products found', message: 'Add products in the web app or clear the search.');
    return RefreshIndicator(
      onRefresh: controller.load,
      child: ListView.separated(
        padding: const EdgeInsets.fromLTRB(20, 8, 20, 100),
        itemCount: controller.products.length,
        separatorBuilder: (_, _) => const SizedBox(height: 10),
        itemBuilder: (_, index) => _ProductCard(product: controller.products[index]),
      ),
    );
  }
}

class _ProductCard extends StatelessWidget {
  const _ProductCard({required this.product});

  final ProductModel product;

  @override
  Widget build(BuildContext context) {
    final lowStock = product.quantity <= product.minimumStock;
    return Card(
      child: ListTile(
        leading: CircleAvatar(child: Icon(lowStock ? Icons.warning_amber : Icons.inventory_2_outlined)),
        title: Text(product.name),
        subtitle: Text('${product.brand ?? 'No brand'}${product.variant == null ? '' : ' • ${product.variant}'}\nStock: ${product.quantity} ${product.unit ?? 'units'}'),
        isThreeLine: true,
        trailing: Text(product.sellingPrice == null ? '-' : 'INR ${product.sellingPrice!.toStringAsFixed(2)}'),
      ),
    );
  }
}

class _BarcodeScannerSheet extends StatelessWidget {
  const _BarcodeScannerSheet();

  @override
  Widget build(BuildContext context) => SizedBox(
        height: MediaQuery.sizeOf(context).height * .65,
        child: MobileScanner(
          onDetect: (capture) {
            final value = capture.barcodes.firstOrNull?.rawValue;
            if (value != null && value.isNotEmpty) Navigator.pop(context, value);
          },
        ),
      );
}
