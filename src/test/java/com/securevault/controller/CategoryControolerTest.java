package com.securevault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securevault.dto.CategoryRequest;
import com.securevault.entity.Category;
import com.securevault.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .build();

        objectMapper = new ObjectMapper();
    }


    @Test
    void createCategory_shouldReturnCreatedCategory() throws Exception {

        CategoryRequest request = new CategoryRequest();


        request.setName("Social");

        Category category = new Category();
        category.setId(1L);
        category.setName("Social");

        when(authentication.getName())
                .thenReturn("swastika@gmail.com");

        when(categoryService.createCategory(
                any(CategoryRequest.class),
                any(String.class)
        )).thenReturn(category);

        mockMvc.perform(
                        post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Social"));
    }


    @Test
    void getAllCategories_shouldReturnCategories() throws Exception {

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Social");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Work");

        when(categoryService.getAllCategories())
                .thenReturn(List.of(category1, category2));

        mockMvc.perform(
                        get("/api/categories")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Social"))
                .andExpect(jsonPath("$[1].name").value("Work"));
    }


    @Test
    void getCategoryById_shouldReturnCategory() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("Social");

        when(categoryService.getCategoryById(1L))
                .thenReturn(Optional.of(category));

        mockMvc.perform(
                        get("/api/categories/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Social"));
    }


    @Test
    void deleteCategory_shouldReturnOk() throws Exception {

        mockMvc.perform(
                        delete("/api/categories/1")
                )
                .andExpect(status().isOk());

        verify(categoryService)
                .deleteCategory(1L);
    }
}