package ch.gianlucafrei.nellygateway.cookies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CsrfCookie {

    public static final String NAME = "csrf";

    private String csrfToken;

}
