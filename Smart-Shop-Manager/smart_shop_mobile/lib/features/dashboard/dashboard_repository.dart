import '../../core/api/api_client.dart';
import 'dashboard_model.dart';

class DashboardRepository {
  DashboardRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<DashboardModel> getDashboard() async {
    final response = await _apiClient.get<Map<String, dynamic>>('/dashboard');
    return DashboardModel.fromJson(response.data ?? const {});
  }
}
