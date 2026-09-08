class CategoryModel {
  const CategoryModel({required this.id, required this.name, this.description, this.active = true});

  final int id;
  final String name;
  final String? description;
  final bool active;

  factory CategoryModel.fromJson(Map<String, dynamic> json) => CategoryModel(
        id: (json['id'] as num?)?.toInt() ?? 0,
        name: '${json['name'] ?? ''}',
        description: json['description'] as String?,
        active: json['active'] is bool ? json['active'] as bool : true,
      );
}
