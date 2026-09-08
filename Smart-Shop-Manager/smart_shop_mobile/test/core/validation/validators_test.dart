import 'package:flutter_test/flutter_test.dart';
import 'package:smart_shop_mobile/core/validation/validators.dart';

void main() {
  test('validates required values and phone numbers', () {
    expect(Validators.required(''), isNotNull);
    expect(Validators.required('Sutharsan'), isNull);
    expect(Validators.phone('123'), isNotNull);
    expect(Validators.phone('9876543210'), isNull);
  });

  test('validates numeric values', () {
    expect(Validators.positiveNumber('0'), isNotNull);
    expect(Validators.positiveNumber('0', allowZero: true), isNull);
    expect(Validators.positiveNumber('45.50'), isNull);
  });
}
