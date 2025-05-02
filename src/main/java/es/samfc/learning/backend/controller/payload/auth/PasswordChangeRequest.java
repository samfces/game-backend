package es.samfc.learning.backend.controller.payload.auth;

public class PasswordChangeRequest {

    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
