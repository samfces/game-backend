package es.samfc.learning.backend.utils.password;

public class PasswordChecker {

    private PasswordChecker() {
        throw new IllegalStateException("Illegal constructor");
    }

    public static int getPasswordStrength(String password) {
        int strength = 0;

        if (password.length() < 8) {
            return strength;
        }

        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isLetter(c)) {
                hasSpecial = true;
            }
        }

        strength += hasLowerCase ? 1 : 0;
        strength += hasUpperCase ? 1 : 0;
        strength += hasDigit ? 1 : 0;
        strength += hasSpecial ? 1 : 0;

        return strength;
    }

}
