package ch.gianlucafrei.nellygateway.services.login.drivers.github;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GitHubUserEmail {

    private String email;
    private boolean primary;
    private boolean verified;

}
