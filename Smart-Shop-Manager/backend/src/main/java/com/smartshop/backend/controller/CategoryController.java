package com.smartshop.backend.controller;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import com.smartshop.backend.dto.CategoryDTO;
import com.smartshop.backend.dto.CategoryStatisticsDTO;
import com.smartshop.backend.service.CategoryService;
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
@PostMapping
public CategoryDTO saveCategory(@Valid @RequestBody CategoryDTO categoryDTO) {

    return categoryService.saveCategory(categoryDTO);
}
    @GetMapping
public List<CategoryDTO> getAllCategories() {

    return categoryService.getAllCategories();
}
@GetMapping("/search")
public List<CategoryDTO> searchCategories(
        @RequestParam String keyword) {

    return categoryService.searchCategories(keyword);
}
@GetMapping("/page")
public Page<CategoryDTO> getCategoriesByPage(

        @RequestParam int page,

        @RequestParam int size) {

    return categoryService.getCategoriesByPage(page, size);
}
@GetMapping("/sort")
public List<CategoryDTO> getCategoriesSorted(
        @RequestParam String field) {

    return categoryService.getCategoriesSorted(field);
}
@GetMapping("/filter")
public Page<CategoryDTO> filterCategories(

        @RequestParam String keyword,

        @RequestParam int page,

        @RequestParam int size,

        @RequestParam String sortField) {

    return categoryService.filterCategories(
            keyword,
            page,
            size,
            sortField);
}

@GetMapping("/statistics")
public CategoryStatisticsDTO getCategoryStatistics() {

    return categoryService.getCategoryStatistics();
}
@GetMapping("/{id}")
public Optional<CategoryDTO> getCategoryById(@PathVariable Long id) {

    return categoryService.getCategoryById(id);
}
 @PutMapping("/{id}")
public CategoryDTO updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryDTO categoryDTO) {

    return categoryService.updateCategory(id, categoryDTO);
}
@DeleteMapping("/{id}")
public String deleteCategory(@PathVariable Long id) {

    categoryService.deleteCategory(id);

    return "Category deleted successfully!";
}
}