package com.securevault.service;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptionServiceTest {

    @Test
    void encryptionAndDecryptionShouldWork() throws Exception {


        byte[] key = new byte[32];
        String base64Key = Base64.getEncoder().encodeToString(key);

        EncryptionService encryptionService =
                new EncryptionService(base64Key);

        String original = "mySecret123";

        String encrypted = encryptionService.encrypt(original);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(original, decrypted);
    }
}