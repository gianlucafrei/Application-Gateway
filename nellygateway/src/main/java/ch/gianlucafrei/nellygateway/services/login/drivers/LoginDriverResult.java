package ch.gianlucafrei.nellygateway.services.login.drivers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Getter @Setter
@AllArgsConstructor
public class LoginDriverResult {

    private URI authURI;
    private String state;

}
