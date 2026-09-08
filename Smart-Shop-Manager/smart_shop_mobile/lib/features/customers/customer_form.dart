import 'package:flutter/material.dart';

import 'customer_model.dart';
import '../../core/validation/validators.dart';

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
          TextFormField(controller: name, decoration: const InputDecoration(labelText: 'Name'), validator: (value) => Validators.required(value, field: 'Name')),
          TextFormField(controller: phone, keyboardType: TextInputType.phone, decoration: const InputDecoration(labelText: 'Phone'), validator: Validators.phone),
          TextFormField(controller: creditLimit, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'Credit limit'), validator: (value) => Validators.positiveNumber(value, allowZero: true)),
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
