package com.example.ToDo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordUtils {
    // Gera um hash SHA-256 com salt, no formato HEX(salt) + ":" + HEX(hash)
    public static String hash(String password) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(salt) + ":" + bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }

    // Verifica se a senha em claro bate com o hash armazenado
    public static boolean verify(String password, String stored) {
        try {
            int sep = stored.indexOf(':');
            if (sep < 0) return false;

            byte[] salt = hexToBytes(stored.substring(0, sep));
            byte[] hashStored = hexToBytes(stored.substring(sep + 1));

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashAttempt = md.digest(password.getBytes(StandardCharsets.UTF_8));

            return MessageDigest.isEqual(hashStored, hashAttempt);
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
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}
