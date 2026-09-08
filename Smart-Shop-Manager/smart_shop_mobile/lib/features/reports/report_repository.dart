import '../../core/api/api_client.dart';

class ReportRepository {
  ReportRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<List<Map<String, dynamic>>> rows(String type) async {
    final response = await _apiClient.get<List<dynamic>>('/reports/$type');
    return (response.data ?? const []).whereType<Map<String, dynamic>>().toList();
  }

  Future<Map<String, dynamic>> profit() async {
    final response = await _apiClient.get<Map<String, dynamic>>('/reports/profit');
    return response.data ?? const {};
  }
}
