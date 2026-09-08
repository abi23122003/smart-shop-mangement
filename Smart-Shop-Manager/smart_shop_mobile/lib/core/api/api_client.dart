import 'dart:async';

import 'package:dio/dio.dart';

import '../constants/app_constants.dart';
import '../errors/app_exception.dart';
import '../storage/token_storage.dart';

typedef UnauthorizedHandler = FutureOr<void> Function();

class ApiClient {
  ApiClient({
    Dio? dio,
    TokenStorage? tokenStorage,
    this.onUnauthorized,
  })  : _tokenStorage = tokenStorage ?? TokenStorage(),
        _dio = dio ?? Dio() {
    _dio.options = _dio.options.copyWith(
      baseUrl: AppConstants.apiBaseUrl,
      connectTimeout: AppConstants.requestTimeout,
      sendTimeout: AppConstants.requestTimeout,
      receiveTimeout: AppConstants.requestTimeout,
      headers: {'Content-Type': 'application/json'},
    );
    _dio.interceptors.add(_AuthInterceptor(_tokenStorage, onUnauthorized));
  }

  final Dio _dio;
  final TokenStorage _tokenStorage;
  UnauthorizedHandler? onUnauthorized;

  void setUnauthorizedHandler(UnauthorizedHandler handler) {
    onUnauthorized = handler;
  }

  Future<Response<T>> get<T>(
    String path, {
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) => _request(() => _dio.get<T>(path, queryParameters: queryParameters, options: options));

  Future<Response<T>> post<T>(
    String path, {
    Object? data,
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) => _request(() => _dio.post<T>(path, data: data, queryParameters: queryParameters, options: options));

  Future<Response<T>> put<T>(
    String path, {
    Object? data,
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) => _request(() => _dio.put<T>(path, data: data, queryParameters: queryParameters, options: options));

  Future<Response<T>> patch<T>(
    String path, {
    Object? data,
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) => _request(() => _dio.patch<T>(path, data: data, queryParameters: queryParameters, options: options));

  Future<Response<T>> delete<T>(
    String path, {
    Object? data,
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) => _request(() => _dio.delete<T>(path, data: data, queryParameters: queryParameters, options: options));

  Future<Response<T>> _request<T>(Future<Response<T>> Function() request) async {
    try {
      return await request();
    } on DioException catch (error) {
      throw AppException.fromDio(error);
    }
  }
}

class _AuthInterceptor extends Interceptor {
  _AuthInterceptor(this._tokenStorage, this._onUnauthorized);

  final TokenStorage _tokenStorage;
  final UnauthorizedHandler? _onUnauthorized;

  @override
  Future<void> onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    final token = await _tokenStorage.read();
    if (token != null && token.isNotEmpty) {
      options.headers['Authorization'] = 'Bearer $token';
    }
    handler.next(options);
  }

  @override
  Future<void> onError(DioException error, ErrorInterceptorHandler handler) async {
    if (error.response?.statusCode == 401) {
      await _tokenStorage.clear();
      await _onUnauthorized?.call();
    }
    handler.next(error);
  }
}
