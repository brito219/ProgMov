package com.example.ToDo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    // expressão regular para validação de e-mail
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // expressão regular para validação de senha:
    // - mínimo de 8 caracteres
    // - pelo menos uma letra maiúscula (?=.*[A-Z])
    // - pelo menos uma letra minúscula (?=.*[a-z])
    // - pelo menos um dígito (?=.*\\d)
    // - pelo menos um caractere especial (?=.*[!@#$%^&*()_+\-=\[\]{};\\',.<>/?])
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};\\',.<>/?]).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    /**
     * valida se o formato do e-mail é válido.
     * @param email O e-mail a ser validado.
     * @return true se o e-mail for válido, false caso contrário.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * valida se a senha atende aos requisitos de complexidade.
     * @param password A senha a ser validada.
     * @return true se a senha for válida, false caso contrário.
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }
}
