class ProductModel {
  const ProductModel({
    this.id,
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
    this.active = true,
  });

  final int? id;
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
  final bool active;

  factory ProductModel.fromJson(Map<String, dynamic> json) => ProductModel(
        id: _intOrNull(json['id']),
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
        active: json['active'] is bool ? json['active'] as bool : true,
      );

  static int _int(Object? value) => value is num ? value.toInt() : 0;
  static int? _intOrNull(Object? value) => value is num ? value.toInt() : null;
  static double? _doubleOrNull(Object? value) => value is num ? value.toDouble() : null;
}
