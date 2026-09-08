import 'package:flutter/foundation.dart';

import '../../core/errors/app_exception.dart';
import 'dashboard_model.dart';
import 'dashboard_repository.dart';

class DashboardController extends ChangeNotifier {
  DashboardController(this._repository);

  final DashboardRepository _repository;
  DashboardModel? data;
  bool isLoading = false;
  String? error;

  Future<void> load() async {
    isLoading = true;
    error = null;
    notifyListeners();
    try {
      data = await _repository.getDashboard();
    } on AppException catch (exception) {
      error = exception.message;
    } catch (_) {
      error = 'Dashboard data could not be loaded.';
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }
}
