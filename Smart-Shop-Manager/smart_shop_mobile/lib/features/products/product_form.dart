import 'package:flutter/material.dart';

import '../../core/validation/validators.dart';
import 'category_model.dart';
import 'product_model.dart';

Future<ProductModel?> showProductForm(
  BuildContext context, {
  ProductModel? product,
  required List<CategoryModel> categories,
}) => showModalBottomSheet<ProductModel>(
  context: context,
  isScrollControlled: true,
  builder: (_) => _ProductForm(product: product, categories: categories),
);

class _ProductForm extends StatefulWidget {
  const _ProductForm({this.product, required this.categories});
  final ProductModel? product;
  final List<CategoryModel> categories;
  @override
  State<_ProductForm> createState() => _ProductFormState();
}

class _ProductFormState extends State<_ProductForm> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _name = TextEditingController(
    text: widget.product?.name ?? '',
  );
  late final TextEditingController _barcode = TextEditingController(
    text: widget.product?.barcode ?? '',
  );
  late final TextEditingController _subcategory = TextEditingController(
    text: widget.product?.subcategory ?? '',
  );
  late final TextEditingController _brand = TextEditingController(
    text: widget.product?.brand ?? '',
  );
  late final TextEditingController _variant = TextEditingController(
    text: widget.product?.variant ?? '',
  );
  late final TextEditingController _quantity = TextEditingController(
    text: '${widget.product?.quantity ?? 0}',
  );
  late final TextEditingController _minimum = TextEditingController(
    text: '${widget.product?.minimumStock ?? 0}',
  );
  late final TextEditingController _purchase = TextEditingController(
    text: '${widget.product?.purchasePrice ?? 0}',
  );
  late final TextEditingController _selling = TextEditingController(
    text: '${widget.product?.sellingPrice ?? 0}',
  );
  int? _categoryId;
  String _unit = 'Piece';
  bool _expiryApplicable = false;
  DateTime? _expiryDate;

  @override
  void initState() {
    super.initState();
    _categoryId = widget.product?.categoryId;
    _unit = widget.product?.unit?.isNotEmpty == true
        ? widget.product!.unit!
        : 'Piece';
    _expiryApplicable = widget.product?.expiryApplicable ?? false;
    _expiryDate = widget.product?.expiryDate;
  }

  @override
  void dispose() {
    for (final controller in [
      _name,
      _barcode,
      _subcategory,
      _brand,
      _variant,
      _quantity,
      _minimum,
      _purchase,
      _selling,
    ]) {
      controller.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final padding = MediaQuery.viewInsetsOf(context).bottom;
    return SafeArea(
      child: Padding(
        padding: EdgeInsets.fromLTRB(20, 16, 20, padding + 20),
        child: Form(
          key: _formKey,
          child: ListView(
            shrinkWrap: true,
            children: [
              Text(
                widget.product == null ? 'Add product' : 'Edit product',
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _name,
                decoration: const InputDecoration(labelText: 'Product name'),
                validator: (value) =>
                    Validators.required(value, field: 'Product name'),
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<int>(
                initialValue: _categoryId,
                decoration: const InputDecoration(labelText: 'Category'),
                items: widget.categories
                    .where((item) => item.active || item.id == _categoryId)
                    .map(
                      (item) => DropdownMenuItem(
                        value: item.id,
                        child: Text(item.name),
                      ),
                    )
                    .toList(),
                onChanged: (value) => setState(() => _categoryId = value),
                validator: (value) =>
                    value == null ? 'Select a category.' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _subcategory,
                decoration: const InputDecoration(labelText: 'Subcategory'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _brand,
                decoration: const InputDecoration(labelText: 'Brand'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _variant,
                decoration: const InputDecoration(
                  labelText: 'Size / variant',
                  hintText: 'Example: 500 g or 1 L',
                ),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _barcode,
                decoration: const InputDecoration(
                  labelText: 'Barcode',
                  helperText: 'Optional. The backend requires each provided barcode to be unique.',
                ),
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: _unit,
                decoration: const InputDecoration(labelText: 'Unit'),
                items:
                    const [
                          'Piece',
                          'Kg',
                          'Gram',
                          'Litre',
                          'ml',
                          'Bag',
                          'Box',
                          'Pack',
                          'Bottle',
                        ]
                        .map(
                          (item) =>
                              DropdownMenuItem(value: item, child: Text(item)),
                        )
                        .toList(),
                onChanged: (value) => setState(() => _unit = value ?? 'Piece'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _quantity,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'Initial quantity',
                ),
                validator: _nonNegative('Initial quantity'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _minimum,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(labelText: 'Minimum stock'),
                validator: _nonNegative('Minimum stock'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _purchase,
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                decoration: const InputDecoration(
                  labelText: 'Purchase / dealer price',
                  prefixText: 'INR ',
                ),
                validator: _nonNegative('Purchase price'),
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _selling,
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                decoration: const InputDecoration(
                  labelText: 'Selling price',
                  prefixText: 'INR ',
                ),
                validator: (value) {
                  final error = _nonNegative('Selling price')(value);
                  if (error != null) {
                    return error;
                  }
                  final purchase = double.tryParse(_purchase.text) ?? 0;
                  if ((double.tryParse(value ?? '') ?? 0) < purchase) {
                    return 'Selling price cannot be lower than purchase price.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 12),
              SwitchListTile.adaptive(
                contentPadding: EdgeInsets.zero,
                title: const Text('Expiry applicable'),
                value: _expiryApplicable,
                onChanged: (value) => setState(() {
                  _expiryApplicable = value;
                  if (!value) _expiryDate = null;
                }),
              ),
              if (_expiryApplicable)
                ListTile(
                  contentPadding: EdgeInsets.zero,
                  title: const Text('Expiry date'),
                  subtitle: Text(
                    _expiryDate == null
                        ? 'Select expiry date'
                        : '${_expiryDate!.day.toString().padLeft(2, '0')}/${_expiryDate!.month.toString().padLeft(2, '0')}/${_expiryDate!.year}',
                  ),
                  trailing: const Icon(Icons.calendar_today_outlined),
                  onTap: _selectExpiry,
                ),
              if (_expiryApplicable && _expiryDate == null)
                const Padding(
                  padding: EdgeInsets.only(bottom: 8),
                  child: Text(
                    'Select an expiry date.',
                    style: TextStyle(color: Colors.red),
                  ),
                ),
              const SizedBox(height: 16),
              FilledButton(
                onPressed: _save,
                child: Text(
                  widget.product == null ? 'Add product' : 'Save changes',
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  String? Function(String?) _nonNegative(String field) => (value) {
    final number = double.tryParse(value ?? '');
    if (number == null || number < 0) return '$field must be zero or more.';
    return null;
  };
  Future<void> _selectExpiry() async {
    final picked = await showDatePicker(
      context: context,
      initialDate: _expiryDate ?? DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime(2100),
    );
    if (picked != null) {
      setState(() => _expiryDate = picked);
    }
  }

  void _save() {
    if (!_formKey.currentState!.validate() ||
        (_expiryApplicable && _expiryDate == null)) {
      return;
    }
    Navigator.pop(
      context,
      ProductModel(
        id: widget.product?.id,
        productCode: widget.product?.productCode,
        name: _name.text,
        barcode: _barcode.text,
        brand: _brand.text,
        subcategory: _subcategory.text,
        categoryId: _categoryId,
        variant: _variant.text,
        unit: _unit,
        quantity: int.tryParse(_quantity.text) ?? 0,
        purchasePrice: double.tryParse(_purchase.text),
        sellingPrice: double.tryParse(_selling.text),
        minimumStock: int.tryParse(_minimum.text) ?? 0,
        expiryApplicable: _expiryApplicable,
        expiryDate: _expiryDate,
        active: widget.product?.active ?? true,
      ),
    );
  }
}
