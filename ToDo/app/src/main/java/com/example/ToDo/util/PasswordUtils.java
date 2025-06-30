package com.example.ToDo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordUtils {

    private static final int HASH_ITERATIONS = 1000;
    private static final String ALGO_PREFIX = "sha256$";

    // Gera um hash com salt e múltiplas rodadas, formato: sha256$HEX(salt):HEX(hash)
    public static String hash(String password) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Aplica múltiplas rodadas de hash
            for (int i = 0; i < HASH_ITERATIONS; i++) {
                md.reset();
                digest = md.digest(digest);
            }

            return ALGO_PREFIX + bytesToHex(salt) + ":" + bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }

    // Verifica se a senha em claro corresponde ao hash salvo
    public static boolean verify(String password, String stored) {
        try {
            if (!stored.startsWith(ALGO_PREFIX)) return false;

            String payload = stored.substring(ALGO_PREFIX.length());
            int sep = payload.indexOf(':');
            if (sep < 0) return false;

            byte[] salt = hexToBytes(payload.substring(0, sep));
            byte[] hashStored = hexToBytes(payload.substring(sep + 1));

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < HASH_ITERATIONS; i++) {
                md.reset();
                digest = md.digest(digest);
            }

            return MessageDigest.isEqual(hashStored, digest);
        } catch (Exception e) {
            return false;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) (
                    (Character.digit(hex.charAt(i), 16) << 4)
                            + Character.digit(hex.charAt(i + 1), 16)
            );
        }
        return data;
    }
}