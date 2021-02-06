package ch.gianlucafrei.nellygateway.cookies;

import ch.gianlucafrei.nellygateway.services.login.drivers.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginCookie {

    public static final String NAME = "session";
    public static final String SAMESITE = "lax";

    private int sessionExpSeconds;
    private String providerKey;
    private UserModel userModel;
    private String csrfToken;

}
