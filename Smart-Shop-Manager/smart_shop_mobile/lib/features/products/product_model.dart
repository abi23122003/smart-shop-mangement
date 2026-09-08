class ProductModel {
  const ProductModel({
    this.id,
    this.productCode,
    required this.name,
    this.barcode,
    this.brand,
    this.subcategory,
    this.categoryId,
    this.variant,
    this.unit,
    this.quantity = 0,
    this.purchasePrice,
    this.sellingPrice,
    this.minimumStock = 0,
    this.expiryDate,
    this.expiryApplicable = false,
    this.active = true,
  });

  final int? id;
  final String? productCode;
  final String name;
  final String? barcode;
  final String? brand;
  final String? subcategory;
  final int? categoryId;
  final String? variant;
  final String? unit;
  final int quantity;
  final double? purchasePrice;
  final double? sellingPrice;
  final int minimumStock;
  final DateTime? expiryDate;
  final bool expiryApplicable;
  final bool active;

  factory ProductModel.fromJson(Map<String, dynamic> json) => ProductModel(
    id: _intOrNull(json['id']),
    productCode: json['productCode'] as String?,
    name: '${json['productName'] ?? ''}',
    barcode: json['barcode'] as String?,
    brand: json['brand'] as String?,
    subcategory: json['subcategory'] as String?,
    categoryId: _intOrNull(json['categoryId']),
    variant: json['variant'] as String?,
    unit: json['unit'] as String?,
    quantity: _int(json['quantity']),
    purchasePrice: _doubleOrNull(json['purchasePrice']),
    sellingPrice: _doubleOrNull(json['sellingPrice']),
    minimumStock: _int(json['minimumStock']),
    expiryDate: _dateOrNull(json['expiryDate']),
    expiryApplicable: json['expiryApplicable'] is bool
        ? json['expiryApplicable'] as bool
        : false,
    active: json['active'] is bool ? json['active'] as bool : true,
  );

  static int _int(Object? value) => value is num ? value.toInt() : 0;
  static int? _intOrNull(Object? value) => value is num ? value.toInt() : null;
  static double? _doubleOrNull(Object? value) =>
      value is num ? value.toDouble() : null;
  static DateTime? _dateOrNull(Object? value) =>
      value is String ? DateTime.tryParse(value) : null;

  Map<String, dynamic> toRequestJson() => {
    if (productCode?.trim().isNotEmpty ?? false)
      'productCode': productCode!.trim(),
    'productName': name.trim(),
    'barcode': barcode?.trim() ?? '',
    'brand': brand?.trim() ?? '',
    'subcategory': subcategory?.trim() ?? '',
    'categoryId': categoryId,
    'variant': variant?.trim() ?? '',
    'unit': unit?.trim() ?? '',
    'quantity': quantity,
    'purchasePrice': purchasePrice ?? 0,
    'sellingPrice': sellingPrice ?? 0,
    'minimumStock': minimumStock,
    'expiryApplicable': expiryApplicable,
    'expiryDate': expiryApplicable && expiryDate != null
        ? '${expiryDate!.year.toString().padLeft(4, '0')}-${expiryDate!.month.toString().padLeft(2, '0')}-${expiryDate!.day.toString().padLeft(2, '0')}'
        : null,
    'active': active,
  };
}
