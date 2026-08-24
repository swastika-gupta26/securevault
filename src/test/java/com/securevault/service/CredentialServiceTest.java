package com.securevault.service;
import static org.mockito.ArgumentMatchers.any;
import com.securevault.dto.CredentialRequest;
import com.securevault.entity.Category;
import com.securevault.entity.Credential;
import com.securevault.entity.User;
import com.securevault.repository.CategoryRepository;
import com.securevault.repository.CredentialRepository;
import com.securevault.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private CredentialService credentialService;

    @Test
    void createCredential_shouldCreateCredentialSuccessfully() throws Exception {

        // Arrange
        CredentialRequest request = new CredentialRequest();
        request.setTitle("Gmail");
        request.setUsername("swastika@gmail.com");
        request.setPassword("myPassword");
        request.setNotes("Personal Gmail");
        request.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        Credential savedCredential = new Credential();
        savedCredential.setId(1L);
        savedCredential.setTitle("Gmail");
        savedCredential.setUsername("swastika@gmail.com");
        savedCredential.setEncryptedPassword("encryptedPassword");
        savedCredential.setNotes("Personal Gmail");
        savedCredential.setCategory(category);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(encryptionService.encrypt("myPassword"))
                .thenReturn("encryptedPassword");

        when(credentialRepository.save(any(Credential.class)))
                .thenReturn(savedCredential);

        // Act
        Credential result =
                credentialService.createCredential(request, "swastika@gmail.com");

        // Assert
        assertNotNull(result);
        assertEquals("Gmail", result.getTitle());
        assertEquals("swastika@gmail.com", result.getUsername());
        assertEquals("encryptedPassword", result.getEncryptedPassword());
        assertEquals("Personal Gmail", result.getNotes());
        assertEquals(category, result.getCategory());

        verify(categoryRepository).findById(1L);
        verify(encryptionService).encrypt("myPassword");
        verify(credentialRepository).save(any(Credential.class));
    }
    @Test
    void getCredentialById_shouldReturnDecryptedCredential() throws Exception {

        // Arrange
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Gmail");
        credential.setUsername("swastika@gmail.com");
        credential.setEncryptedPassword("encryptedPassword");

        when(credentialRepository.findById(1L))
                .thenReturn(Optional.of(credential));

        when(encryptionService.decrypt("encryptedPassword"))
                .thenReturn("myPassword");

        // Act
        Credential result = credentialService.getCredentialById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gmail", result.getTitle());
        assertEquals("swastika@gmail.com", result.getUsername());
        assertEquals("myPassword", result.getEncryptedPassword());

        verify(credentialRepository).findById(1L);
        verify(encryptionService).decrypt("encryptedPassword");
    }
    @Test
    void updateCredential_shouldUpdateCredentialSuccessfully() {

        // Arrange
        CredentialRequest request = new CredentialRequest();
        request.setTitle("Updated Gmail");
        request.setUsername("newemail@gmail.com");
        request.setNotes("Updated notes");
        request.setCategoryId(2L);

        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Gmail");
        credential.setUsername("oldemail@gmail.com");

        Category category = new Category();
        category.setId(2L);

        User user = new User();
        user.setEmail("swastika@gmail.com");

        when(credentialRepository.findById(1L))
                .thenReturn(Optional.of(credential));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(category));

        when(userRepository.findByEmail("swastika@gmail.com"))
                .thenReturn(Optional.of(user));

        when(credentialRepository.save(any(Credential.class)))
                .thenReturn(credential);

        // Act
        Credential result = credentialService.updateCredential(
                1L,
                request,
                "swastika@gmail.com"
        );

        // Assert
        assertNotNull(result);
        assertEquals("Updated Gmail", result.getTitle());
        assertEquals("newemail@gmail.com", result.getUsername());
        assertEquals("Updated notes", result.getNotes());
        assertEquals(category, result.getCategory());

        verify(credentialRepository).findById(1L);
        verify(categoryRepository).findById(2L);
        verify(userRepository).findByEmail("swastika@gmail.com");
        verify(credentialRepository).save(credential);
        verify(auditLogService).log("UPDATE_CREDENTIAL", user);
    }
    @Test
    void deleteById_shouldDeleteCredentialSuccessfully() {

        // Arrange
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Gmail");

        User user = new User();
        user.setEmail("swastika@gmail.com");

        when(credentialRepository.findById(1L))
                .thenReturn(Optional.of(credential));

        when(userRepository.findByEmail("swastika@gmail.com"))
                .thenReturn(Optional.of(user));

        // Act
        credentialService.deleteById(1L, "swastika@gmail.com");

        // Assert
        verify(credentialRepository).findById(1L);
        verify(userRepository).findByEmail("swastika@gmail.com");
        verify(credentialRepository).delete(credential);
        verify(auditLogService).log("DELETE_CREDENTIAL", user);
    }
    @Test
    void getCredentialByCategory_shouldReturnCredentials() {

        Credential credential1 = new Credential();
        credential1.setId(1L);
        credential1.setTitle("Gmail");

        Credential credential2 = new Credential();
        credential2.setId(2L);
        credential2.setTitle("GitHub");

        List<Credential> credentials = List.of(credential1, credential2);

        when(credentialRepository.findByCategoryId(1L))
                .thenReturn(credentials);

        List<Credential> result =
                credentialService.getCredentialByCategory(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Gmail", result.get(0).getTitle());
        assertEquals("GitHub", result.get(1).getTitle());

        verify(credentialRepository).findByCategoryId(1L);
    }
    @Test
    void searchCredentials_shouldReturnMatchingCredentials() {

        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Gmail");

        Page<Credential> page =
                new PageImpl<>(List.of(credential));

        Pageable pageable = PageRequest.of(0, 10);

        when(credentialRepository
                .findByTitleContainingIgnoreCase("Gmail", pageable))
                .thenReturn(page);

        Page<Credential> result =
                credentialService.searchCredentials("Gmail", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Gmail", result.getContent().get(0).getTitle());

        verify(credentialRepository)
                .findByTitleContainingIgnoreCase("Gmail", pageable);
    }
    @Test
    void getAllCredentials_shouldReturnPaginatedCredentials() {

        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Gmail");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Credential> page =
                new PageImpl<>(List.of(credential), pageable, 1);

        when(credentialRepository.findAll(pageable))
                .thenReturn(page);

        Page<Credential> result =
                credentialService.getAllCredentials(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Gmail", result.getContent().get(0).getTitle());

        verify(credentialRepository).findAll(pageable);
    }
    @Test
    void getCredentialById_shouldThrowExceptionWhenCredentialNotFound() {

        when(credentialRepository.findById(999L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> credentialService.getCredentialById(999L)
        );

        assertEquals("Credential not found", exception.getMessage());

        verify(credentialRepository).findById(999L);
    }




    @Test
    void createCredential_shouldThrowExceptionWhenCategoryNotFound() {

        CredentialRequest request = new CredentialRequest();
        request.setTitle("Gmail");
        request.setUsername("swastika@gmail.com");
        request.setPassword("password123");
        request.setNotes("Personal");
        request.setCategoryId(999L);

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> credentialService.createCredential(
                        request,
                        "swastika@gmail.com"
                )
        );

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository).findById(999L);
    }



    @Test
    void updateCredential_shouldThrowExceptionWhenUserNotFound() {

        CredentialRequest request = new CredentialRequest();
        request.setTitle("Updated Gmail");
        request.setUsername("newemail@gmail.com");
        request.setNotes("Updated notes");
        request.setCategoryId(1L);

        Credential credential = new Credential();
        credential.setId(1L);

        Category category = new Category();
        category.setId(1L);

        when(credentialRepository.findById(1L))
                .thenReturn(Optional.of(credential));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> credentialService.updateCredential(
                        1L,
                        request,
                        "unknown@gmail.com"
                )
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail("unknown@gmail.com");


        verify(credentialRepository, never()).save(any(Credential.class));
        verify(auditLogService, never())
                .log(anyString(), any(User.class));
    }
}