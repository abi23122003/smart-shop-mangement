class DashboardModel {
  const DashboardModel({
    required this.totalProducts,
    required this.totalCategories,
    required this.totalCustomers,
    required this.totalSuppliers,
    required this.todaySales,
    required this.todayPurchases,
    required this.lowStockProducts,
  });

  final int totalProducts;
  final int totalCategories;
  final int totalCustomers;
  final int totalSuppliers;
  final double todaySales;
  final double todayPurchases;
  final int lowStockProducts;

  factory DashboardModel.fromJson(Map<String, dynamic> json) => DashboardModel(
        totalProducts: _int(json['totalProducts']),
        totalCategories: _int(json['totalCategories']),
        totalCustomers: _int(json['totalCustomers']),
        totalSuppliers: _int(json['totalSuppliers']),
        todaySales: _double(json['todaySales']),
        todayPurchases: _double(json['todayPurchases']),
        lowStockProducts: _int(json['lowStockProducts']),
      );

  static int _int(Object? value) => value is num ? value.toInt() : 0;
  static double _double(Object? value) => value is num ? value.toDouble() : 0;
}
