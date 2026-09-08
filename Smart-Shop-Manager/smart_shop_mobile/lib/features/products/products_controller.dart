import 'package:flutter/foundation.dart';

import '../../core/errors/app_exception.dart';
import 'product_model.dart';
import 'product_repository.dart';
import 'category_model.dart';

class ProductsController extends ChangeNotifier {
  ProductsController(this._repository);

  final ProductRepository _repository;
  List<ProductModel> products = const [];
  List<CategoryModel> categories = const [];
  List<ProductModel> taxonomyProducts = const [];
  bool isLoading = false;
  String? error;
  String query = '';
  int? categoryId;
  String subcategory = '';
  String brand = '';
  String stockStatus = 'all';
  int page = 0;
  int totalPages = 1;
  int totalElements = 0;

  Future<void> initialize() async {
    try {
      categories = await _repository.categories();
      taxonomyProducts = await _repository.search('');
    } on AppException catch (exception) {
      error = exception.message;
    } catch (_) {
      error = 'Product filters could not be loaded.';
    }
    await load();
  }

  Future<void> load([String? value]) async {
    query = value ?? query;
    isLoading = true;
    error = null;
    notifyListeners();
    try {
      final result = await _repository.browse(
        keyword: query.trim(),
        categoryId: categoryId,
        subcategory: subcategory,
        brand: brand,
        stockStatus: stockStatus,
        page: page,
      );
      products = result.products;
      page = result.page;
      totalPages = result.totalPages < 1 ? 1 : result.totalPages;
      totalElements = result.totalElements;
    } on AppException catch (exception) {
      error = exception.message;
    } catch (_) {
      error = 'Products could not be loaded.';
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  Future<void> applyFilters({
    String? queryValue,
    int? newCategoryId,
    bool changeCategory = false,
    String? newSubcategory,
    String? newBrand,
    String? newStockStatus,
    int? newPage,
  }) {
    query = queryValue ?? query;
    if (changeCategory) categoryId = newCategoryId;
    subcategory = newSubcategory ?? subcategory;
    brand = newBrand ?? brand;
    stockStatus = newStockStatus ?? stockStatus;
    page = newPage ?? 0;
    return load();
  }

  Future<void> clearFilters() {
    query = '';
    categoryId = null;
    subcategory = '';
    brand = '';
    stockStatus = 'all';
    page = 0;
    return load();
  }

  Future<void> refreshTaxonomy() async {
    taxonomyProducts = await _repository.search('');
    await load();
  }
}
