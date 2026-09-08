abstract final class Validators {
  static String? required(String? value, {String field = 'This field'}) {
    return value == null || value.trim().isEmpty ? '$field is required.' : null;
  }

  static String? phone(String? value) {
    return value == null || !RegExp(r'^\d{10}$').hasMatch(value.trim()) ? 'Enter exactly 10 digits.' : null;
  }

  static String? positiveNumber(String? value, {bool allowZero = false}) {
    final number = double.tryParse(value?.trim() ?? '');
    if (number == null || (allowZero ? number < 0 : number <= 0)) return 'Enter a valid number.';
    return null;
  }

  static String? email(String? value) {
    if (value == null || value.trim().isEmpty) return null;
    return RegExp(r'^[^@\s]+@[^@\s]+\.[^@\s]+$').hasMatch(value.trim()) ? null : 'Enter a valid email address.';
  }
}
