package ch.gianlucafrei.nellygateway.session;

import ch.gianlucafrei.nellygateway.cookies.LoginCookie;
import ch.gianlucafrei.nellygateway.filters.spring.ExtractAuthenticationFilter;
import ch.gianlucafrei.nellygateway.services.login.drivers.UserModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Session {

    private final long sessionExpSeconds;
    private final long remainingTimeSeconds;
    private final String provider;
    private final UserModel userModel;
    private final LoginCookie loginCookie;

    public static Optional<Session> fromSessionCookie(LoginCookie cookie, Clock clock) {

        if (cookie == null)
            return Optional.empty();

        long remainingTimeSeconds = cookie.getSessionExpSeconds() - (clock.millis() / 1000);
        if (remainingTimeSeconds < 0) {
            log.info("received expired session cookie");
            return Optional.empty();
        }


        Session session = new Session(
                cookie.getSessionExpSeconds(),
                remainingTimeSeconds,
                cookie.getProviderKey(),
                cookie.getUserModel(),
                cookie
        );

        return Optional.of(session);
    }
}
