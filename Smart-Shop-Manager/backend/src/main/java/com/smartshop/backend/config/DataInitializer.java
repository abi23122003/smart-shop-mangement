package com.smartshop.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.smartshop.backend.entity.User;
import com.smartshop.backend.entity.Category;
import com.smartshop.backend.repository.CategoryRepository;
import com.smartshop.backend.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CategoryRepository categoryRepository) {

        return args -> {
            User admin = userRepository.findByUsername("admin").orElse(null);

            if (admin == null) {
                admin = new User();
                admin.setUsername("admin");
            }

            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);

            String[] defaultCategories = { "Grocery", "Personal Care", "Home Care", "Beverages", "Snacks", "Dairy & Bakery", "Fruits & Vegetables", "Household", "Baby Care", "Stationery", "Other" };
            for (String name : defaultCategories) {
                if (categoryRepository.findByNameIgnoreCase(name).isEmpty()) {
                    Category category = new Category();
                    category.setName(name);
                    category.setDescription("Default " + name + " category");
                    category.setActive(true);
                    categoryRepository.save(category);
                }
            }

            System.out.println("Admin user ensured successfully. Password matches default: "
                    + passwordEncoder.matches("admin123", admin.getPassword()));
        };
    }
}
