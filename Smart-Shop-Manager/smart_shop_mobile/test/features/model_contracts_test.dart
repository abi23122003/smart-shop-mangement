import 'package:flutter_test/flutter_test.dart';
import 'package:smart_shop_mobile/features/customers/customer_model.dart';
import 'package:smart_shop_mobile/features/dashboard/dashboard_model.dart';
import 'package:smart_shop_mobile/features/products/product_model.dart';

void main() {
  test('maps dashboard metrics from the backend response', () {
    final dashboard = DashboardModel.fromJson({
      'totalProducts': 12,
      'totalCategories': 4,
      'totalCustomers': 8,
      'totalSuppliers': 3,
      'todaySales': 1250.50,
      'todayPurchases': 500,
      'lowStockProducts': 2,
    });

    expect(dashboard.totalProducts, 12);
    expect(dashboard.todaySales, 1250.50);
    expect(dashboard.lowStockProducts, 2);
  });

  test('maps product fields used by search, stock, and sales', () {
    final product = ProductModel.fromJson({
      'id': 7,
      'productName': 'Lux Soap',
      'barcode': '8901234567890',
      'quantity': 11,
      'purchasePrice': 30,
      'sellingPrice': 45.5,
      'minimumStock': 5,
    });

    expect(product.id, 7);
    expect(product.barcode, '8901234567890');
    expect(product.sellingPrice, 45.5);
    expect(product.quantity, 11);
  });

  test('serializes customer create payload using backend field names', () {
    final customer = CustomerModel(name: 'Ravi Kumar', phone: '9876543210', creditLimit: 850, creditEnabled: true);
    final payload = customer.toJson();

    expect(payload['customerName'], 'Ravi Kumar');
    expect(payload['phone'], '9876543210');
    expect(payload['creditLimit'], 850);
    expect(payload['creditEnabled'], isTrue);
  });
}
