import 'package:flutter/foundation.dart';

import '../../core/errors/app_exception.dart';
import 'credit_model.dart';
import 'customer_model.dart';
import 'customer_repository.dart';

class CustomersController extends ChangeNotifier {
  CustomersController(this._repository);

  final CustomerRepository _repository;
  List<CustomerModel> customers = const [];
  Map<int, CreditModel> credits = const {};
  bool isLoading = false;
  String? error;

  Future<void> load([String keyword = '']) async {
    isLoading = true;
    error = null;
    notifyListeners();
    try {
      final results = await Future.wait([_repository.list(keyword: keyword), _repository.credits()]);
      customers = results[0] as List<CustomerModel>;
      credits = {for (final credit in results[1] as List<CreditModel>) credit.customerId: credit};
    } on AppException catch (exception) {
      error = exception.message;
    } catch (_) {
      error = 'Customers could not be loaded.';
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  Future<bool> create(CustomerModel customer) async {
    try {
      await _repository.create(customer);
      await load();
      return true;
    } on AppException catch (exception) {
      error = exception.message;
      notifyListeners();
      return false;
    }
  }

  Future<bool> recordPayment(CustomerModel customer, double amount) async {
    try {
      final credit = await _repository.recordPayment(customer.id!, amount, 'Payment recorded from mobile app');
      credits = {...credits, customer.id!: credit};
      notifyListeners();
      return true;
    } on AppException catch (exception) {
      error = exception.message;
      notifyListeners();
      return false;
    }
  }
}
