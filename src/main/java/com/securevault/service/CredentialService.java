package com.securevault.service;

import com.securevault.dto.CredentialRequest;
import com.securevault.entity.Category;
import com.securevault.entity.Credential;
import com.securevault.entity.User;
import com.securevault.repository.CategoryRepository;
import com.securevault.repository.CredentialRepository;
import com.securevault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CredentialService {
    @Autowired
    private JwtService jwtservice;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserRepository userRepository;

    public Credential createCredential (CredentialRequest request, String email)throws Exception{
        Credential credential = new Credential();
        credential.setTitle(request.getTitle());
        credential.setUsername(request.getUsername());
        credential.setNotes(request.getNotes());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        credential.setCategory(category);
        String encryptedPassword= encryptionService.encrypt(request.getPassword());
        credential.setEncryptedPassword(encryptedPassword);

        return credentialRepository.save(credential);
    }

    public List<Credential> getAllCredential(){
        return credentialRepository.findAll();
    }
    public Page<Credential> getAllCredentials(Pageable pageable){
        return credentialRepository.findAll(pageable);
    }

    public Credential getCredentialById(Long id) throws Exception{
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found"));
        String decryptedPassword= encryptionService.decrypt(credential.getEncryptedPassword());
        credential.setEncryptedPassword(decryptedPassword);
        return credential;
    }

    public Credential updateCredential(Long id, CredentialRequest request,  String email){

        Credential credential = credentialRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Credential not found!"));
        credential.setUsername(request.getUsername());
        credential.setNotes(request.getNotes());
        credential.setTitle(request.getTitle());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        credential.setCategory(category);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Credential updatedCredential = credentialRepository.save(credential);

        auditLogService.log("UPDATE_CREDENTIAL", user);

        return updatedCredential;

    }
    public void deleteById(Long id, String email){
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        credentialRepository.delete(credential);

        auditLogService.log("DELETE_CREDENTIAL", user);
    }

    public List<Credential> getCredentialByCategory(Long categoryId){
        return credentialRepository.findByCategoryId(categoryId);
    }
    public Page<Credential> searchCredentials(String title, Pageable pageable){
        return credentialRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

}
