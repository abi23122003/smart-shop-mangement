import '../../core/api/api_client.dart';
import 'supplier_model.dart';

class SupplierRepository {
  SupplierRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<List<SupplierModel>> list() async {
    final response = await _apiClient.get<List<dynamic>>('/suppliers');
    return (response.data ?? const []).whereType<Map<String, dynamic>>().map(SupplierModel.fromJson).toList();
  }

  Future<SupplierModel> create(SupplierModel supplier) async {
    final response = await _apiClient.post<Map<String, dynamic>>('/suppliers', data: supplier.toJson());
    return SupplierModel.fromJson(response.data ?? const {});
  }
}
