import '../../core/api/api_client.dart';
import '../products/product_model.dart';
import '../products/product_repository.dart';
import '../suppliers/supplier_model.dart';
import '../suppliers/supplier_repository.dart';

class PurchaseRepository {
  PurchaseRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<List<SupplierModel>> suppliers() => SupplierRepository(_apiClient).list();
  Future<List<ProductModel>> products(String keyword) => ProductRepository(_apiClient).search(keyword);

  Future<Map<String, dynamic>> create({required int supplierId, required String code, required int quantity, required double price, required int productId}) async {
    final response = await _apiClient.post<Map<String, dynamic>>('/purchases', data: {
      'purchaseCode': code,
      'purchaseDate': DateTime.now().toIso8601String().split('T').first,
      'supplierId': supplierId,
      'purchaseItems': [{'productId': productId, 'quantity': quantity, 'purchasePrice': price, 'totalPrice': quantity * price}],
    });
    return response.data ?? const {};
  }
}
