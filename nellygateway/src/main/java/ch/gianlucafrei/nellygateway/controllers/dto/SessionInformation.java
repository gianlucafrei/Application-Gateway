package ch.gianlucafrei.nellygateway.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionInformation {

    public static final String SESSION_STATE_AUTHENTICATED = "authenticated";
    public static final String SESSION_STATE_ANONYMOUS = "anonymous";

    private String state;
    private int expiresIn;

    public SessionInformation(String state) {
        this.state = state;
    }
}
