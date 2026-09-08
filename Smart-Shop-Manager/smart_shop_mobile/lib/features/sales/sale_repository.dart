import '../../core/api/api_client.dart';
import 'package:dio/dio.dart';
import '../customers/customer_model.dart';
import '../customers/customer_repository.dart';
import '../products/product_model.dart';
import '../products/product_repository.dart';

class SaleRepository {
  SaleRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<List<CustomerModel>> customers() => CustomerRepository(_apiClient).list();

  Future<List<ProductModel>> products(String keyword) => ProductRepository(_apiClient).search(keyword);

  Future<Map<String, dynamic>> create({required int customerId, required String paymentMethod, required List<Map<String, dynamic>> items}) async {
    final response = await _apiClient.post<Map<String, dynamic>>('/sales', data: {
      'saleDate': DateTime.now().toIso8601String().split('T').first,
      'customerId': customerId,
      'paymentMethod': paymentMethod,
      'saleItems': items,
    });
    return response.data ?? const {};
  }

  Future<List<int>> invoice(int saleId) async {
    final response = await _apiClient.get<List<int>>(
      '/sales/$saleId/invoice',
      options: Options(responseType: ResponseType.bytes),
    );
    return response.data ?? const [];
  }
}
