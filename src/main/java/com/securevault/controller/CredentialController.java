package com.securevault.controller;

import com.securevault.dto.CredentialRequest;
import com.securevault.entity.Credential;
import com.securevault.repository.CredentialRepository;
import com.securevault.service.CredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Tag(
        name = "Credentials",
        description = "APIs for managing user credentials"
)
@RequestMapping("/api/credentials")
@RestController
@SecurityRequirement(name = "bearerAuth")
public class CredentialController {
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private CredentialRepository credentialRepository;
    @Operation(
            summary = "Create a credential",
            description = "Creates and securely stores a new credential"
    )
    @PostMapping
    public Credential createCredential(@Valid @RequestBody CredentialRequest request, Authentication authentication)throws Exception{
        String email= authentication.getName();
       return credentialService.createCredential(request, email);
    }
    @GetMapping("/category/{categoryId}")
    public List<Credential> getCredentialByCategory(@PathVariable Long categoryId){
        return credentialService.getCredentialByCategory(categoryId);
    }
    @Operation(
            summary = "Get all credentials",
            description = "Returns all credentials of the authenticated user"
    )
    @GetMapping
    public List<Credential> getAllCredential(){
        return credentialService.getAllCredential();
    }

    @GetMapping("/search")
    public Page<Credential> searchCredential(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return credentialService.searchCredentials(title, pageable);
    }
    @Operation(
            summary = "Get credential by ID",
            description = "Fetches a credential using its ID"
    )
    @GetMapping("/{id}")
    public Credential getCredentialById(@PathVariable Long id) throws Exception{
        return credentialService.getCredentialById(id);
    }
    @Operation(
            summary = "Update credential",
            description = "Updates an existing credential"
    )
    @PutMapping("/{id}")
    public Credential updateCredential(@PathVariable Long id,
                                       @Valid @RequestBody CredentialRequest request, Authentication authentication){
        String email = authentication.getName();
       return credentialService.updateCredential(id, request, email);
    }
    @Operation(
            summary = "Delete credential",
            description = "Deletes an existing credential"
    )
    @DeleteMapping ("/{id}")
    public void deleteById(@PathVariable Long id,  Authentication authentication){
        String email = authentication.getName();
        credentialService.deleteById(id, email);
    }

    @GetMapping("/page")
    public Page<Credential> getAllCredentials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return credentialService.getAllCredentials(pageable);
    }

}
