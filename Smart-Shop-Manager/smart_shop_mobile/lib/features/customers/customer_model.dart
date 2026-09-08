class CustomerModel {
  const CustomerModel({
    this.id,
    required this.name,
    required this.phone,
    this.email,
    this.address,
    this.creditLimit = 0,
    this.creditEnabled = false,
  });

  final int? id;
  final String name;
  final String phone;
  final String? email;
  final String? address;
  final double creditLimit;
  final bool creditEnabled;

  factory CustomerModel.fromJson(Map<String, dynamic> json) => CustomerModel(
        id: (json['id'] as num?)?.toInt(),
        name: '${json['customerName'] ?? ''}',
        phone: '${json['phone'] ?? ''}',
        email: json['email'] as String?,
        address: json['address'] as String?,
        creditLimit: _number(json['creditLimit']),
        creditEnabled: json['creditEnabled'] is bool && json['creditEnabled'] as bool,
      );

  Map<String, dynamic> toJson() => {
        'customerName': name,
        'phone': phone,
        'email': email,
        'address': address,
        'creditLimit': creditLimit,
        'creditEnabled': creditEnabled,
        'active': true,
      };

  static double _number(Object? value) => value is num ? value.toDouble() : 0;
}
