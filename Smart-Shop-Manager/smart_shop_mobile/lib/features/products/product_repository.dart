import '../../core/api/api_client.dart';
import '../../core/constants/app_constants.dart';
import 'product_model.dart';

class ProductRepository {
  ProductRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<List<ProductModel>> browse({String keyword = '', int? categoryId, String stockStatus = 'all'}) async {
    final response = await _apiClient.get<Map<String, dynamic>>('/products/browse', queryParameters: {
      'keyword': keyword,
      ...?categoryId == null ? null : {'categoryId': categoryId},
      'stockStatus': stockStatus,
      'page': 0,
      'size': AppConstants.defaultPageSize,
      'sort': 'productName',
      'direction': 'asc',
    });
    final content = response.data?['content'];
    if (content is! List) return const [];
    return content.whereType<Map<String, dynamic>>().map(ProductModel.fromJson).toList();
  }

  Future<List<ProductModel>> search(String keyword) async {
    final response = await _apiClient.get<List<dynamic>>('/products/search', queryParameters: {'keyword': keyword});
    return (response.data ?? const []).whereType<Map<String, dynamic>>().map(ProductModel.fromJson).toList();
  }
}
