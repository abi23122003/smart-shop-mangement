import '../../core/api/api_client.dart';
import '../../core/constants/app_constants.dart';
import 'supplier_model.dart';

class SupplierRepository {
  SupplierRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<List<SupplierModel>> list() async {
    final response = await _apiClient.get<Map<String, dynamic>>('/suppliers/page', queryParameters: {'page': 0, 'size': AppConstants.defaultPageSize});
    final content = response.data?['content'];
    return content is List ? content.whereType<Map<String, dynamic>>().map(SupplierModel.fromJson).toList() : const [];
  }

  Future<SupplierModel> create(SupplierModel supplier) async {
    final response = await _apiClient.post<Map<String, dynamic>>('/suppliers', data: supplier.toJson());
    return SupplierModel.fromJson(response.data ?? const {});
  }
}
