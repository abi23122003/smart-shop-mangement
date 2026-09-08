import 'package:dio/dio.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:smart_shop_mobile/core/errors/app_exception.dart';

void main() {
  test('maps an unauthorized response to an unauthorized exception', () {
    final exception = AppException.fromDio(
      DioException(
        requestOptions: RequestOptions(path: '/dashboard'),
        response: Response<dynamic>(
          requestOptions: RequestOptions(path: '/dashboard'),
          statusCode: 401,
        ),
      ),
    );

    expect(exception, isA<UnauthorizedException>());
    expect(exception.statusCode, 401);
  });

  test('maps connection failures to a retryable network exception', () {
    final exception = AppException.fromDio(
      DioException(
        requestOptions: RequestOptions(path: '/dashboard'),
        type: DioExceptionType.connectionError,
      ),
    );

    expect(exception, isA<NetworkException>());
    expect(exception.message, contains('Unable to connect'));
  });
}
