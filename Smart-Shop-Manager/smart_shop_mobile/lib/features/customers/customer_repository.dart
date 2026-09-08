import '../../core/api/api_client.dart';
import 'credit_model.dart';
import 'customer_model.dart';

class CustomerRepository {
  CustomerRepository(this._apiClient);

  final ApiClient _apiClient;

  Future<List<CustomerModel>> list({String keyword = ''}) async {
    final path = keyword.trim().isEmpty ? '/customers' : '/customers/search';
    final response = await _apiClient.get<List<dynamic>>(path, queryParameters: keyword.trim().isEmpty ? null : {'keyword': keyword.trim()});
    return (response.data ?? const []).whereType<Map<String, dynamic>>().map(CustomerModel.fromJson).toList();
  }

  Future<CustomerModel> create(CustomerModel customer) async {
    final response = await _apiClient.post<Map<String, dynamic>>('/customers', data: customer.toJson());
    return CustomerModel.fromJson(response.data ?? const {});
  }

  Future<List<CreditModel>> credits() async {
    final response = await _apiClient.get<List<dynamic>>('/credits');
    return (response.data ?? const []).whereType<Map<String, dynamic>>().map(CreditModel.fromJson).toList();
  }

  Future<CreditModel> recordPayment(int customerId, double amount, String remarks) async {
    final response = await _apiClient.post<Map<String, dynamic>>('/credits/payment', data: {
      'customerId': customerId,
      'amount': amount,
      'remarks': remarks,
    });
    return CreditModel.fromJson(response.data ?? const {});
  }
}
