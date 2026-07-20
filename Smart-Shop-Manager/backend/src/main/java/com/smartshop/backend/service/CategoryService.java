package com.smartshop.backend.service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smartshop.backend.dto.CategoryDTO;
import com.smartshop.backend.dto.CategoryStatisticsDTO;
import com.smartshop.backend.entity.Category;
import com.smartshop.backend.mapper.CategoryMapper;
import com.smartshop.backend.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        Category category = CategoryMapper.toEntity(categoryDTO);

        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toDTO(savedCategory);
    }
    public List<CategoryDTO> getAllCategories() {

    List<Category> categories = categoryRepository.findAll();

    return categories.stream()
            .map(CategoryMapper::toDTO)
            .toList();
}
public Optional<CategoryDTO> getCategoryById(Long id) {

    Optional<Category> category = categoryRepository.findById(id);

    return category.map(CategoryMapper::toDTO);
}
public CategoryDTO updateCategory(Long id, CategoryDTO updatedCategory) {

    Optional<Category> existingCategory = categoryRepository.findById(id);

    if (existingCategory.isPresent()) {

        Category category = existingCategory.get();

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        category.setActive(updatedCategory.getActive());

        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toDTO(savedCategory);
    }

    return null;
}
public Page<CategoryDTO> getCategoriesByPage(int page, int size) {

    Page<Category> categories =
            categoryRepository.findAll(PageRequest.of(page, size));

    return categories.map(CategoryMapper::toDTO);
}
public List<CategoryDTO> getCategoriesSorted(String field) {

    List<Category> categories =
            categoryRepository.findAll(Sort.by(field));

    return categories.stream()
            .map(CategoryMapper::toDTO)
            .toList();
}
public Page<CategoryDTO> filterCategories(
        String keyword,
        int page,
        int size,
        String sortField) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortField));

    Page<Category> categories =
            categoryRepository.findByNameContainingIgnoreCase(
                    keyword,
                    pageable);

    return categories.map(CategoryMapper::toDTO);
}
public CategoryStatisticsDTO getCategoryStatistics() {

    long totalCategories = categoryRepository.count();

    long activeCategories = categoryRepository.countActiveCategories();

    long inactiveCategories = categoryRepository.countInactiveCategories();

    return new CategoryStatisticsDTO(
            totalCategories,
            activeCategories,
            inactiveCategories
    );
}
public void deleteCategory(Long id) {

    categoryRepository.deleteById(id);
}
public List<CategoryDTO> searchCategories(String keyword) {

    List<Category> categories =
            categoryRepository.findByNameContainingIgnoreCase(keyword);

    return categories.stream()
            .map(CategoryMapper::toDTO)
            .toList();
}
}
