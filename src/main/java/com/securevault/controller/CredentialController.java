package com.securevault.controller;

import com.securevault.dto.CredentialRequest;
import com.securevault.entity.Credential;
import com.securevault.repository.CredentialRepository;
import com.securevault.service.CredentialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/credentials")
@RestController
public class CredentialController {
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private CredentialRepository credentialRepository;

    @PostMapping
    public Credential createCredential(@Valid @RequestBody CredentialRequest request)throws Exception{
       return credentialService.createCredential(request);
    }
    @GetMapping("/category/{categoryId}")
    public List<Credential> getCredentialByCategory(@PathVariable Long categoryId){
        return credentialService.getCredentialByCategory(categoryId);
    }

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

    @GetMapping("/{id}")
    public Credential getCredentialById(@PathVariable Long id) throws Exception{
        return credentialService.getCredentialById(id);
    }
    @PutMapping("/{id}")
    public Credential updateCredential(@PathVariable Long id,
                                       @Valid @RequestBody CredentialRequest request){
       return credentialService.updateCredential(id, request);
    }
    @DeleteMapping ("/{id}")
    public void deleteById(@PathVariable Long id){
          credentialService.deleteById(id);
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
