class SupplierModel {
  const SupplierModel({this.id, required this.code, required this.name, required this.phone, this.email, this.address});

  final int? id;
  final String code;
  final String name;
  final String phone;
  final String? email;
  final String? address;

  factory SupplierModel.fromJson(Map<String, dynamic> json) => SupplierModel(
        id: (json['id'] as num?)?.toInt(),
        code: '${json['supplierCode'] ?? ''}',
        name: '${json['supplierName'] ?? ''}',
        phone: '${json['phone'] ?? ''}',
        email: json['email'] as String?,
        address: json['address'] as String?,
      );

  Map<String, dynamic> toJson() => {'supplierCode': code, 'supplierName': name, 'phone': phone, 'email': email, 'address': address, 'active': true};
}
