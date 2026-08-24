package com.securevault.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.securevault.controller.CredentialController;
import com.securevault.dto.CredentialRequest;
import com.securevault.entity.Credential;
import com.securevault.service.CredentialService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(MockitoExtension.class)
class CredentialControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CredentialService credentialService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CredentialController credentialController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(credentialController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void createCredential_shouldReturnCreatedCredential() throws Exception {

        // Arrange
        CredentialRequest request = new CredentialRequest();
        request.setTitle("Gmail");
        request.setUsername("swastika@gmail.com");
        request.setPassword("myPassword");
        request.setNotes("Personal Gmail");
        request.setCategoryId(1L);

        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Gmail");
        credential.setUsername("swastika@gmail.com");

        when(authentication.getName())
                .thenReturn("swastika@gmail.com");

        when(credentialService.createCredential(
                any(CredentialRequest.class),
                any(String.class)
        )).thenReturn(credential);

        // Act + Assert
        mockMvc.perform(
                        post("/api/credentials")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Gmail"))
                .andExpect(jsonPath("$.username").value("swastika@gmail.com"));
    }
    @Test
    void getCredentialById_shouldReturnCredential() throws Exception {

        // Arrange
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Gmail");
        credential.setUsername("swastika@gmail.com");

        when(credentialService.getCredentialById(1L))
                .thenReturn(credential);

        // Act + Assert
        mockMvc.perform(
                        get("/api/credentials/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Gmail"))
                .andExpect(jsonPath("$.username").value("swastika@gmail.com"));
    }
    @Test
    void updateCredential_shouldReturnUpdatedCredential() throws Exception {

        // Arrange
        CredentialRequest request = new CredentialRequest();
        request.setTitle("Updated Gmail");
        request.setUsername("newemail@gmail.com");
        request.setPassword("newPassword");
        request.setNotes("Updated notes");
        request.setCategoryId(1L);

        Credential credential = new Credential();
        credential.setId(1L);
        credential.setTitle("Updated Gmail");
        credential.setUsername("newemail@gmail.com");
        credential.setNotes("Updated notes");

        when(authentication.getName())
                .thenReturn("swastika@gmail.com");

        when(credentialService.updateCredential(
                any(Long.class),
                any(CredentialRequest.class),
                any(String.class)
        )).thenReturn(credential);

        // Act + Assert
        mockMvc.perform(
                        put("/api/credentials/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Gmail"))
                .andExpect(jsonPath("$.username").value("newemail@gmail.com"))
                .andExpect(jsonPath("$.notes").value("Updated notes"));
    }
    @Test
    void deleteCredential_shouldReturnOk() throws Exception {

        // Arrange
        when(authentication.getName())
                .thenReturn("swastika@gmail.com");

        // Act + Assert
        mockMvc.perform(
                        delete("/api/credentials/1")
                                .principal(authentication)
                )
                .andExpect(status().isOk());

        verify(credentialService)
                .deleteById(1L, "swastika@gmail.com");
    }
}
