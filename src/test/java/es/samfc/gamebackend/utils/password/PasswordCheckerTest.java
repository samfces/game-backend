package es.samfc.gamebackend.utils.password;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordCheckerTest {

    @Test
    @DisplayName("Checking class constructor")
    void checkClassConstructor(){
        assertThrows(IllegalStateException.class, PasswordChecker::new);
    }

    @Test
    @DisplayName("Checking null password strength")
    void checkNullPasswordStrength() {
        assertEquals(0, PasswordChecker.getPasswordStrength(null));
    }

    @Test
    @DisplayName("Checking too short password")
    void checkTooShortPassword() {
        assertEquals(0, PasswordChecker.getPasswordStrength("a"));
    }

    @Test
    @DisplayName("Checking only lcase password strength")
    void checkOnlyLowercasePassword() {
        assertEquals(1, PasswordChecker.getPasswordStrength("password"));
    }

    @Test
    @DisplayName("Checking lcase+ucase password strength")
    void checkLowercaseUppercasePassword() {
        assertEquals(2, PasswordChecker.getPasswordStrength("Password"));
    }

    @Test
    @DisplayName("Checking lcase+ucase+digit password strength")
    void checkLcaseUcaseDigitPassword() {
        assertEquals(3, PasswordChecker.getPasswordStrength("Password3"));
    }

    @Test
    @DisplayName("Checking perfect password strength")
    void checkPerfectPassword(){
        assertEquals(4, PasswordChecker.getPasswordStrength("Password3!"));
    }
}