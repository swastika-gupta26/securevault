package com.securevault.service;

import com.securevault.dto.CategoryRequest;
import com.securevault.dto.CredentialResponse;
import com.securevault.entity.Category;
import com.securevault.entity.Credential;
import com.securevault.entity.User;
import com.securevault.repository.CategoryRepository;
import com.securevault.repository.CredentialRepository;
import com.securevault.repository.UserRepository;
import com.zaxxer.hikari.util.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialRepository credentialRepository;

    public Category createCategory(CategoryRequest request, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUser(user);


        if(request.getParentCategoryId()!= null){
            Category parentCategory = categoryRepository
                    .findById(request.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        return categoryRepository.save(category);
    }
    public List<Category> getAllCategories(){

        return categoryRepository.findAll();
    }
    public Optional<Category> getCategoryById(Long id){

        return categoryRepository.findById(id);
    }
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }

}
