package com.securevault.controller;

import com.securevault.dto.CredentialRequest;
import com.securevault.entity.Credential;
import com.securevault.repository.CredentialRepository;
import com.securevault.service.CredentialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Credential createCredential(@Valid @RequestBody CredentialRequest request){
       return credentialService.createCredential(request);
    }

    @GetMapping
    public List<Credential> getAllCredential(){
        return credentialService.getAllCredential();
    }

    @GetMapping("/{id}")
    public Optional<Credential> getCredentialById(@PathVariable Long id){
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
}
