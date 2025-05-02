package es.samfc.learning.backend.controller.payload.auth;

public class PasswordChangeRequest {

    private String oldPassword;
    private String newPassword;

    public PasswordChangeRequest() {
        //Empty constructor for Jackson
    }

    public PasswordChangeRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
