package ch.gianlucafrei.nellygateway.cookies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginStateCookie {

    public static final String NAME = "state";

    private String provider;
    private String state;
    private String returnUrl;

}
