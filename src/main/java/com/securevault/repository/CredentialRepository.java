package com.securevault.repository;

import com.securevault.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findByCategoryId(Long categoryId);

}
