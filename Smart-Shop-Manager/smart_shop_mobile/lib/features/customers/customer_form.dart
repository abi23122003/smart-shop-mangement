import 'package:flutter/material.dart';

import 'customer_model.dart';

Future<CustomerModel?> showCustomerForm(BuildContext context) async {
  final formKey = GlobalKey<FormState>();
  final name = TextEditingController();
  final phone = TextEditingController();
  final creditLimit = TextEditingController(text: '0');
  final result = await showDialog<CustomerModel>(
    context: context,
    builder: (_) => AlertDialog(
      title: const Text('Add customer'),
      content: Form(
        key: formKey,
        child: Column(mainAxisSize: MainAxisSize.min, children: [
          TextFormField(controller: name, decoration: const InputDecoration(labelText: 'Name'), validator: (value) => value == null || value.trim().isEmpty ? 'Required' : null),
          TextFormField(controller: phone, keyboardType: TextInputType.phone, decoration: const InputDecoration(labelText: 'Phone'), validator: (value) => value == null || !RegExp(r'^\d{10}$').hasMatch(value) ? 'Enter 10 digits' : null),
          TextFormField(controller: creditLimit, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'Credit limit'), validator: (value) => double.tryParse(value ?? '') == null ? 'Enter a number' : null),
        ]),
      ),
      actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')), FilledButton(onPressed: () { if (formKey.currentState!.validate()) Navigator.pop(context, CustomerModel(name: name.text.trim(), phone: phone.text.trim(), creditLimit: double.parse(creditLimit.text), creditEnabled: true)); }, child: const Text('Save'))],
    ),
  );
  name.dispose();
  phone.dispose();
  creditLimit.dispose();
  return result;
}
