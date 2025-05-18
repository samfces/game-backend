package es.samfc.gamebackend.utils.password;

/**
 * Clase para validar la seguridad de una contraseña.
 */
public class PasswordChecker {

    PasswordChecker() {
        throw new IllegalStateException("Illegal constructor");
    }

    /**
     * Método para obtener la seguridad de una contraseña.
     * @param password Contraseña.
     * @return int Seguridad de la contraseña.
     */
    public static int getPasswordStrength(String password) {
        if (password == null) return 0;
        int strength = 0;

        //Comprobamos si la contraseña es de al menos 8 caracteres
        if (password.length() < 8) {
            return strength;
        }

        //Comprobamos si:
        //- La contraseña contiene al menos una letra en minúscula
        //- La contraseña contiene al menos una letra en mayúscula
        //- La contraseña contiene al menos un número
        //- La contraseña contiene al menos un carácter especial
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!hasLowerCase) hasLowerCase = Character.isLowerCase(c);
            if (!hasUpperCase) hasUpperCase = Character.isUpperCase(c);
            if (!hasDigit) hasDigit = Character.isDigit(c);
            if (!hasSpecial) hasSpecial = !Character.isLetter(c) && !Character.isDigit(c);
        }

        strength += hasLowerCase ? 1 : 0;
        strength += hasUpperCase ? 1 : 0;
        strength += hasDigit ? 1 : 0;
        strength += hasSpecial ? 1 : 0;

        return strength;
    }

}
