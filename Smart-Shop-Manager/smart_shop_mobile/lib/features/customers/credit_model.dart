class CreditModel {
  const CreditModel({required this.customerId, required this.balance, required this.status});

  final int customerId;
  final double balance;
  final String status;

  factory CreditModel.fromJson(Map<String, dynamic> json) => CreditModel(
        customerId: (json['customerId'] as num?)?.toInt() ?? 0,
        balance: json['balance'] is num ? (json['balance'] as num).toDouble() : 0,
        status: '${json['status'] ?? 'CLEARED'}',
      );
}
