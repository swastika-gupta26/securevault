package com.securevault.service;

import com.securevault.dto.CredentialRequest;
import com.securevault.entity.Credential;
import com.securevault.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CredentialService {
    @Autowired
    private JwtService jwtservice;

    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private EncryptionService encryptionService;

    public Credential createCredential (CredentialRequest request)throws Exception{
        Credential credential = new Credential();
        credential.setTitle(request.getTitle());
        credential.setUsername(request.getUsername());
        credential.setNotes(request.getNotes());
        String encryptedPassword= encryptionService.encrypt(request.getPassword());
        credential.setEncryptedPassword(encryptedPassword);

        return credentialRepository.save(credential);
    }

    public List<Credential> getAllCredential(){
        return credentialRepository.findAll();
    }

    public Credential getCredentialById(Long id) throws Exception{
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found"));
        String decryptedPassword= encryptionService.decrypt(credential.getEncryptedPassword());
        credential.setEncryptedPassword(decryptedPassword);
        return credential;
    }

    public Credential updateCredential(Long id, CredentialRequest request){
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Credential not found!"));
        credential.setUsername(request.getUsername());
        credential.setNotes(request.getNotes());
        credential.setTitle(request.getTitle());

       return credentialRepository.save(credential);

    }
    public void deleteById(Long id){
        credentialRepository.deleteById(id);
    }
}
