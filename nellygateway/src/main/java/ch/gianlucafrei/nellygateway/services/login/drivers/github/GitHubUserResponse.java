package ch.gianlucafrei.nellygateway.services.login.drivers.github;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GitHubUserResponse {

    private String login;
    private String id;
    private String avatar_url;

}
