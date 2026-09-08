import '../../core/api/api_client.dart';
import '../../core/constants/app_constants.dart';
import 'category_model.dart';
import 'product_model.dart';

class ProductPageResult {
  const ProductPageResult({
    required this.products,
    required this.page,
    required this.totalPages,
    required this.totalElements,
  });
  final List<ProductModel> products;
  final int page;
  final int totalPages;
  final int totalElements;
}

class ProductRepository {
  ProductRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<ProductPageResult> browse({
    String keyword = '',
    int? categoryId,
    String subcategory = '',
    String brand = '',
    String stockStatus = 'all',
    int page = 0,
  }) async {
    final response = await _apiClient.get<Map<String, dynamic>>(
      '/products/browse',
      queryParameters: {
        'keyword': keyword,
        ...?categoryId == null ? null : {'categoryId': categoryId},
        'subcategory': subcategory,
        'brand': brand,
        'stockStatus': stockStatus,
        'page': page,
        'size': AppConstants.defaultPageSize,
        'sort': 'productName',
        'direction': 'asc',
      },
    );
    final content = response.data?['content'];
    if (content is! List) {
      return const ProductPageResult(
        products: [],
        page: 0,
        totalPages: 1,
        totalElements: 0,
      );
    }
    return ProductPageResult(
      products: content
          .whereType<Map<String, dynamic>>()
          .map(ProductModel.fromJson)
          .toList(),
      page: (response.data?['number'] as num?)?.toInt() ?? page,
      totalPages: (response.data?['totalPages'] as num?)?.toInt() ?? 1,
      totalElements:
          (response.data?['totalElements'] as num?)?.toInt() ?? content.length,
    );
  }

  Future<List<ProductModel>> search(String keyword) async {
    final response = await _apiClient.get<List<dynamic>>(
      '/products/search',
      queryParameters: {'keyword': keyword},
    );
    return (response.data ?? const [])
        .whereType<Map<String, dynamic>>()
        .map(ProductModel.fromJson)
        .toList();
  }

  Future<List<CategoryModel>> categories() async {
    final response = await _apiClient.get<List<dynamic>>('/categories');
    return (response.data ?? const [])
        .whereType<Map<String, dynamic>>()
        .map(CategoryModel.fromJson)
        .toList();
  }

  Future<ProductModel> get(int id) async {
    final response = await _apiClient.get<Map<String, dynamic>>(
      '/products/$id',
    );
    return ProductModel.fromJson(response.data ?? const {});
  }

  Future<ProductModel> create(ProductModel product) async {
    final response = await _apiClient.post<Map<String, dynamic>>(
      '/products',
      data: product.toRequestJson(),
    );
    return ProductModel.fromJson(response.data ?? const {});
  }

  Future<ProductModel> update(ProductModel product) async {
    final response = await _apiClient.put<Map<String, dynamic>>(
      '/products/${product.id}',
      data: product.toRequestJson(),
    );
    return ProductModel.fromJson(response.data ?? const {});
  }

  Future<void> deactivate(int id) => _apiClient.delete<String>('/products/$id');

  Future<ProductModel> restock({
    required String barcode,
    required int quantity,
  }) async {
    final response = await _apiClient.post<Map<String, dynamic>>(
      '/products/restock',
      data: {'barcode': barcode, 'quantity': quantity},
    );
    return ProductModel.fromJson(response.data ?? const {});
  }
}
