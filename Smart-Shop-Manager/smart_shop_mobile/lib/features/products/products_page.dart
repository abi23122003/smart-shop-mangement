import 'dart:async';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../core/api/api_client.dart';
import '../../core/errors/app_exception.dart';
import '../../core/widgets/app_state_widgets.dart';
import 'product_form.dart';
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
  final _search = TextEditingController();
  Timer? _timer;

  @override
  void initState() {
    super.initState();
    _controller = ProductsController(
      ProductRepository(context.read<ApiClient>()),
    )..initialize();
  }

  @override
  void dispose() {
    _timer?.cancel();
    _search.dispose();
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => ChangeNotifierProvider.value(
    value: _controller,
    child: Consumer<ProductsController>(
      builder: (_, controller, _) => Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 20, 20, 8),
            child: Row(
              children: [
                Expanded(
                  child: Text(
                    'Products',
                    style: Theme.of(context).textTheme.headlineMedium,
                  ),
                ),
                IconButton.filled(
                  tooltip: 'Add product',
                  onPressed: controller.categories.isEmpty ? null : _addProduct,
                  icon: const Icon(Icons.add),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: TextField(
              controller: _search,
              onChanged: _debouncedSearch,
              decoration: InputDecoration(
                labelText: 'Search product, brand, or barcode',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: _search.text.isEmpty
                    ? null
                    : IconButton(
                        onPressed: _clearAll,
                        icon: const Icon(Icons.clear),
                      ),
              ),
            ),
          ),
          _filters(controller),
          Expanded(child: _body(controller)),
        ],
      ),
    ),
  );

  Widget _filters(ProductsController controller) {
    final categoryName = controller.categories
        .where((item) => item.id == controller.categoryId)
        .map((item) => item.name)
        .firstOrNull;
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 12, 20, 8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          OutlinedButton.icon(
            onPressed: () => _showFilters(controller),
            icon: const Icon(Icons.tune),
            label: Text(_filterLabel(controller, categoryName)),
          ),
          if (controller.categoryId != null ||
              controller.subcategory.isNotEmpty ||
              controller.brand.isNotEmpty ||
              controller.stockStatus != 'all')
            Align(
              alignment: Alignment.centerRight,
              child: TextButton(
                onPressed: _clearAll,
                child: const Text('Clear filters'),
              ),
            ),
        ],
      ),
    );
  }

  String _filterLabel(ProductsController controller, String? categoryName) {
    final count = [
      categoryName,
      controller.subcategory.isEmpty ? null : controller.subcategory,
      controller.brand.isEmpty ? null : controller.brand,
      controller.stockStatus == 'all' ? null : controller.stockStatus,
    ].whereType<String>().length;
    return count == 0
        ? 'Filter products'
        : '$count filter${count == 1 ? '' : 's'} applied';
  }

  List<String> _subcategoriesFor(
    ProductsController controller,
    int? categoryId,
  ) =>
      (controller.taxonomyProducts
          .where((item) => categoryId == null || item.categoryId == categoryId)
          .map((item) => item.subcategory)
          .whereType<String>()
          .where((item) => item.isNotEmpty)
          .toSet()
          .toList()
        ..sort());

  List<String> _brandsFor(
    ProductsController controller,
    int? categoryId,
    String subcategory,
  ) =>
      (controller.taxonomyProducts
          .where(
            (item) =>
                (categoryId == null || item.categoryId == categoryId) &&
                (subcategory.isEmpty || item.subcategory == subcategory),
          )
          .map((item) => item.brand)
          .whereType<String>()
          .where((item) => item.isNotEmpty)
          .toSet()
          .toList()
        ..sort());

  Future<void> _showFilters(ProductsController controller) async {
    var category = controller.categoryId;
    var subcategory = controller.subcategory;
    var brand = controller.brand;
    var stock = controller.stockStatus;
    final applied = await showModalBottomSheet<bool>(
      context: context,
      isScrollControlled: true,
      builder: (sheetContext) => StatefulBuilder(
        builder: (_, setSheetState) => SafeArea(
          child: Padding(
            padding: EdgeInsets.fromLTRB(
              20,
              16,
              20,
              MediaQuery.viewInsetsOf(sheetContext).bottom + 20,
            ),
            child: ListView(
              shrinkWrap: true,
              children: [
                Text(
                  'Filter products',
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
                const SizedBox(height: 16),
                DropdownButtonFormField<int?>(
                  initialValue: category,
                  decoration: const InputDecoration(labelText: 'Category'),
                  items: [
                    const DropdownMenuItem<int?>(
                      value: null,
                      child: Text('All categories'),
                    ),
                    ...controller.categories
                        .where((item) => item.active || item.id == category)
                        .map(
                          (item) => DropdownMenuItem<int?>(
                            value: item.id,
                            child: Text(item.name),
                          ),
                        ),
                  ],
                  onChanged: (value) => setSheetState(() {
                    category = value;
                    subcategory = '';
                    brand = '';
                  }),
                ),
                const SizedBox(height: 12),
                DropdownButtonFormField<String>(
                  initialValue: subcategory,
                  decoration: const InputDecoration(labelText: 'Subcategory'),
                  items: [
                    const DropdownMenuItem(
                      value: '',
                      child: Text('All subcategories'),
                    ),
                    ..._subcategoriesFor(controller, category).map(
                      (item) =>
                          DropdownMenuItem(value: item, child: Text(item)),
                    ),
                  ],
                  onChanged: (value) => setSheetState(() {
                    subcategory = value ?? '';
                    brand = '';
                  }),
                ),
                const SizedBox(height: 12),
                DropdownButtonFormField<String>(
                  initialValue: brand,
                  decoration: const InputDecoration(labelText: 'Brand'),
                  items: [
                    const DropdownMenuItem(
                      value: '',
                      child: Text('All brands'),
                    ),
                    ..._brandsFor(controller, category, subcategory).map(
                      (item) =>
                          DropdownMenuItem(value: item, child: Text(item)),
                    ),
                  ],
                  onChanged: (value) =>
                      setSheetState(() => brand = value ?? ''),
                ),
                const SizedBox(height: 12),
                DropdownButtonFormField<String>(
                  initialValue: stock,
                  decoration: const InputDecoration(labelText: 'Stock status'),
                  items: const [
                    DropdownMenuItem(value: 'all', child: Text('All stock')),
                    DropdownMenuItem(value: 'in', child: Text('In stock')),
                    DropdownMenuItem(value: 'low', child: Text('Low stock')),
                    DropdownMenuItem(value: 'out', child: Text('Out of stock')),
                  ],
                  onChanged: (value) =>
                      setSheetState(() => stock = value ?? 'all'),
                ),
                const SizedBox(height: 20),
                FilledButton(
                  onPressed: () => Navigator.pop(sheetContext, true),
                  child: const Text('Apply filters'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
    if (applied == true) {
      await controller.applyFilters(
        newCategoryId: category,
        changeCategory: true,
        newSubcategory: subcategory,
        newBrand: brand,
        newStockStatus: stock,
      );
    }
  }

  Widget _body(ProductsController controller) {
    if (controller.isLoading && controller.products.isEmpty) {
      return const AppLoadingState(message: 'Loading products...');
    }
    if (controller.error != null && controller.products.isEmpty) {
      return AppErrorState(
        message: controller.error!,
        onRetry: controller.initialize,
      );
    }
    if (controller.products.isEmpty) {
      return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const AppEmptyState(
            icon: Icons.inventory_2_outlined,
            title: 'No products found',
            message: 'Try clearing filters or add a new product.',
          ),
          TextButton(onPressed: _clearAll, child: const Text('Clear filters')),
        ],
      );
    }
    return RefreshIndicator(
      onRefresh: controller.load,
      child: ListView.separated(
        padding: const EdgeInsets.fromLTRB(20, 8, 20, 100),
        itemCount: controller.products.length + 1,
        separatorBuilder: (_, _) => const SizedBox(height: 10),
        itemBuilder: (_, index) {
          if (index == controller.products.length) {
            return _pagination(controller);
          }
          return _card(controller.products[index]);
        },
      ),
    );
  }

  Widget _pagination(ProductsController controller) => Padding(
    padding: const EdgeInsets.symmetric(vertical: 8),
    child: Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          '${controller.totalElements} product${controller.totalElements == 1 ? '' : 's'}',
        ),
        Row(
          children: [
            IconButton(
              tooltip: 'Previous page',
              onPressed: controller.page > 0
                  ? () => controller.applyFilters(newPage: controller.page - 1)
                  : null,
              icon: const Icon(Icons.chevron_left),
            ),
            Text('${controller.page + 1} / ${controller.totalPages}'),
            IconButton(
              tooltip: 'Next page',
              onPressed: controller.page + 1 < controller.totalPages
                  ? () => controller.applyFilters(newPage: controller.page + 1)
                  : null,
              icon: const Icon(Icons.chevron_right),
            ),
          ],
        ),
      ],
    ),
  );

  Widget _card(ProductModel product) {
    final stock = product.quantity == 0
        ? 'Out of stock'
        : product.quantity <= product.minimumStock
        ? 'Low stock'
        : 'In stock';
    final color = product.quantity == 0
        ? Colors.red
        : product.quantity <= product.minimumStock
        ? Colors.orange
        : Colors.green;
    final expiry = product.expiryApplicable
        ? product.expiryDate == null
              ? 'Expiry date not set'
              : 'Expires ${product.expiryDate!.day.toString().padLeft(2, '0')}/${product.expiryDate!.month.toString().padLeft(2, '0')}/${product.expiryDate!.year}'
        : null;
    return Card(
      child: InkWell(
        borderRadius: BorderRadius.circular(12),
        onTap: () => _details(product),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: Text(
                      product.name,
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                  ),
                  PopupMenuButton<String>(
                    onSelected: (value) => _productAction(value, product),
                    itemBuilder: (_) => const [
                      PopupMenuItem(
                        value: 'details',
                        child: Text('View details'),
                      ),
                      PopupMenuItem(value: 'edit', child: Text('Edit')),
                      PopupMenuItem(value: 'restock', child: Text('Restock')),
                      PopupMenuItem(
                        value: 'deactivate',
                        child: Text('Deactivate'),
                      ),
                    ],
                  ),
                ],
              ),
              if ([
                product.brand,
                product.subcategory,
                product.variant,
              ].whereType<String>().where((item) => item.isNotEmpty).isNotEmpty)
                Padding(
                  padding: const EdgeInsets.only(top: 4),
                  child: Text(
                    [product.brand, product.subcategory, product.variant]
                        .whereType<String>()
                        .where((item) => item.isNotEmpty)
                        .join(' • '),
                  ),
                ),
              const SizedBox(height: 10),
              Wrap(
                spacing: 8,
                runSpacing: 6,
                children: [
                  Chip(
                    label: Text(
                      '$stock: ${product.quantity}/${product.minimumStock}',
                    ),
                    visualDensity: VisualDensity.compact,
                    side: BorderSide(color: color),
                  ),
                  if (product.barcode?.isNotEmpty == true)
                    Chip(
                      label: Text('Barcode: ${product.barcode}'),
                      visualDensity: VisualDensity.compact,
                    ),
                  if (expiry != null)
                    Chip(
                      avatar: const Icon(Icons.event_outlined, size: 16),
                      label: Text(expiry),
                      visualDensity: VisualDensity.compact,
                    ),
                ],
              ),
              const SizedBox(height: 10),
              Text(
                'Dealer: INR ${(product.purchasePrice ?? 0).toStringAsFixed(2)}   Selling: INR ${(product.sellingPrice ?? 0).toStringAsFixed(2)}',
                style: Theme.of(context).textTheme.bodySmall,
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _debouncedSearch(String value) {
    setState(() {});
    _timer?.cancel();
    _timer = Timer(
      const Duration(milliseconds: 350),
      () => _controller.applyFilters(queryValue: value),
    );
  }

  Future<void> _clearAll() async {
    _search.clear();
    setState(() {});
    await _controller.clearFilters();
  }

  Future<void> _addProduct() async {
    final product = await showProductForm(
      context,
      categories: _controller.categories,
    );
    if (product == null) return;
    await _save(product, isEdit: false);
  }

  Future<void> _edit(ProductModel product) async {
    final full = product.id == null ? product : await _loadDetail(product);
    if (!mounted) return;
    final changed = await showProductForm(
      context,
      product: full,
      categories: _controller.categories,
    );
    if (changed != null) await _save(changed, isEdit: true);
  }

  Future<ProductModel> _loadDetail(ProductModel product) async {
    try {
      return await ProductRepository(context.read<ApiClient>())
          .get(product.id!);
    } catch (_) {
      return product;
    }
  }

  Future<void> _save(ProductModel product, {required bool isEdit}) async {
    try {
      final repository = ProductRepository(context.read<ApiClient>());
      if (isEdit) {
        await repository.update(product);
      } else {
        await repository.create(product);
      }
      await _controller.refreshTaxonomy();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(isEdit ? 'Product updated.' : 'Product added.'),
          ),
        );
      }
    } on AppException catch (error) {
      _showError(error.message);
    } catch (_) {
      _showError('Product could not be saved.');
    }
  }

  Future<void> _deactivate(ProductModel product) async {
    if (product.id == null) {
      return;
    }
    final confirm = await showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Deactivate product?'),
        content: Text(
          '${product.name} will be hidden from active product lists.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text('Deactivate'),
          ),
        ],
      ),
    );
    if (confirm != true || !mounted) {
      return;
    }
    try {
      await ProductRepository(context.read<ApiClient>())
          .deactivate(product.id!);
      await _controller.refreshTaxonomy();
      if (mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(const SnackBar(content: Text('Product deactivated.')));
      }
    } on AppException catch (error) {
      _showError(error.message);
    }
  }

  Future<void> _restock(ProductModel product) async {
    if (product.barcode?.trim().isEmpty ?? true) {
      _showError(
        'This product has no barcode. Restock requires the existing barcode API.',
      );
      return;
    }
    final input = TextEditingController();
    final quantity = await showDialog<int>(
      context: context,
      builder: (_) => AlertDialog(
        title: Text('Restock ${product.name}'),
        content: TextField(
          controller: input,
          autofocus: true,
          keyboardType: TextInputType.number,
          decoration: const InputDecoration(labelText: 'Quantity'),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () => Navigator.pop(context, int.tryParse(input.text)),
            child: const Text('Restock'),
          ),
        ],
      ),
    );
    input.dispose();
    if (!mounted) {
      return;
    }
    if (quantity == null || quantity <= 0) {
      if (quantity != null) {
        _showError('Restock quantity must be greater than zero.');
      }
      return;
    }
    try {
      await ProductRepository(context.read<ApiClient>())
          .restock(barcode: product.barcode!, quantity: quantity);
      await _controller.refreshTaxonomy();
      if (mounted) {
        ScaffoldMessenger.of(context)
            .showSnackBar(const SnackBar(content: Text('Stock updated.')));
      }
    } on AppException catch (error) {
      _showError(error.message);
    }
  }

  void _details(ProductModel product) {
    showModalBottomSheet(
      context: context,
      builder: (_) => SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: ListView(
            shrinkWrap: true,
            children: [
              Text(
                product.name,
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 12),
              ..._detailRows(product),
            ],
          ),
        ),
      ),
    );
  }

  List<Widget> _detailRows(ProductModel p) {
    final category =
        _controller.categories
            .where((item) => item.id == p.categoryId)
            .map((item) => item.name)
            .firstOrNull ??
        'Not set';
    final expiry = p.expiryDate == null
        ? 'Not set'
        : '${p.expiryDate!.day.toString().padLeft(2, '0')}/${p.expiryDate!.month.toString().padLeft(2, '0')}/${p.expiryDate!.year}';
    final values = <String, String>{
      'Category': category,
      'Subcategory': p.subcategory?.isNotEmpty == true
          ? p.subcategory!
          : 'Not set',
      'Brand': p.brand?.isNotEmpty == true ? p.brand! : 'Not set',
      'Variant': p.variant?.isNotEmpty == true ? p.variant! : 'Not set',
      'Barcode': p.barcode?.isNotEmpty == true ? p.barcode! : 'Not set',
      'Unit': p.unit ?? 'Not set',
      'Current stock': '${p.quantity}',
      'Minimum stock': '${p.minimumStock}',
      'Purchase price': 'INR ${(p.purchasePrice ?? 0).toStringAsFixed(2)}',
      'Selling price': 'INR ${(p.sellingPrice ?? 0).toStringAsFixed(2)}',
      'Expiry': p.expiryApplicable ? expiry : 'Not applicable',
      'Status': p.active ? 'Active' : 'Inactive',
    };
    return values.entries
        .map(
          (entry) => ListTile(
            contentPadding: EdgeInsets.zero,
            title: Text(entry.key),
            trailing: Text(entry.value, textAlign: TextAlign.right),
          ),
        )
        .toList();
  }

  void _productAction(String action, ProductModel product) {
    switch (action) {
      case 'details':
        _details(product);
        break;
      case 'edit':
        _edit(product);
        break;
      case 'restock':
        _restock(product);
        break;
      case 'deactivate':
        _deactivate(product);
        break;
    }
  }

  void _showError(String message) {
    if (mounted) {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text(message)));
    }
  }
}
