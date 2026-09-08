import 'package:flutter/foundation.dart';

import '../../core/errors/app_exception.dart';
import 'product_model.dart';
import 'product_repository.dart';

class ProductsController extends ChangeNotifier {
  ProductsController(this._repository);

  final ProductRepository _repository;
  List<ProductModel> products = const [];
  bool isLoading = false;
  String? error;
  String query = '';

  Future<void> load([String? value]) async {
    query = value ?? query;
    isLoading = true;
    error = null;
    notifyListeners();
    try {
      products = query.trim().isEmpty ? await _repository.browse() : await _repository.search(query.trim());
    } on AppException catch (exception) {
      error = exception.message;
    } catch (_) {
      error = 'Products could not be loaded.';
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }
}
