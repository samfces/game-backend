package es.samfc.gamebackend.events.auth;

import es.samfc.gamebackend.controller.payload.MessageResponse;
import es.samfc.gamebackend.controller.payload.auth.LoginRequest;
import es.samfc.gamebackend.events.RestEvent;

public class AuthLoginEvent extends RestEvent<LoginRequest, MessageResponse> {

    public AuthLoginEvent(MessageResponse responseData, LoginRequest requestData) {
        super("auth.login", responseData, requestData);
    }

}
