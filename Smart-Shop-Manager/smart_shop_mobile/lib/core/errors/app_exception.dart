import 'package:dio/dio.dart';

sealed class AppException implements Exception {
  const AppException(this.message, {this.statusCode});

  final String message;
  final int? statusCode;

  @override
  String toString() => message;

  factory AppException.fromDio(DioException error) {
    final statusCode = error.response?.statusCode;
    final serverMessage = _messageFromResponse(error.response?.data);
    if (statusCode == 401) {
      return const UnauthorizedException();
    }
    if (statusCode != null && statusCode >= 400) {
      return ServerException(
        serverMessage ?? 'The server rejected the request.',
        statusCode: statusCode,
      );
    }
    if (error.type == DioExceptionType.connectionTimeout ||
        error.type == DioExceptionType.sendTimeout ||
        error.type == DioExceptionType.receiveTimeout) {
      return const NetworkException('The request timed out. Please try again.');
    }
    if (error.type == DioExceptionType.connectionError) {
      return const NetworkException('Unable to connect to the server.');
    }
    return NetworkException(serverMessage ?? 'A network error occurred.');
  }

  static String? _messageFromResponse(Object? data) {
    if (data is Map) {
      final message = data['message'] ?? data['error'];
      if (message is String && message.trim().isNotEmpty) return message;
      final errors = data['errors'];
      if (errors is Map) {
        final messages = errors.entries
            .map((entry) => '${entry.key}: ${entry.value}')
            .join('\n');
        if (messages.isNotEmpty) return messages;
      }
      if (errors is List) {
        final messages = errors.whereType<String>().join('\n');
        if (messages.isNotEmpty) return messages;
      }
    }
    if (data is String && data.trim().isNotEmpty) return data;
    return null;
  }
}

final class UnauthorizedException extends AppException {
  const UnauthorizedException() : super('Your session has expired. Please sign in again.', statusCode: 401);
}

final class ServerException extends AppException {
  const ServerException(super.message, {super.statusCode});
}

final class NetworkException extends AppException {
  const NetworkException(super.message);
}
