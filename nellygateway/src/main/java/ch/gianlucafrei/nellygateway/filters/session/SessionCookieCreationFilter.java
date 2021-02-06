package ch.gianlucafrei.nellygateway.filters.session;

import ch.gianlucafrei.nellygateway.GlobalClockSource;
import ch.gianlucafrei.nellygateway.config.configuration.NellyConfig;
import ch.gianlucafrei.nellygateway.cookies.LoginCookie;
import ch.gianlucafrei.nellygateway.services.crypto.CookieEncryptor;
import ch.gianlucafrei.nellygateway.services.login.drivers.UserModel;
import ch.gianlucafrei.nellygateway.session.Session;
import ch.gianlucafrei.nellygateway.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SessionCookieCreationFilter implements NellySessionFilter {

    private final CookieEncryptor cookieEncryptor;
    private final NellyConfig config;
    private final GlobalClockSource globalClockSource;

    @Override
    public void renewSession(Map<String, Object> filterContext, HttpServletResponse response) {

        Session session = (Session) filterContext.get("old-session");

        filterContext.put("providerKey", session.getProvider());
        filterContext.put("userModel", session.getUserModel());

        createSession(filterContext, response); // Create a new session cookie which overwrites the old one
    }

    @Override
    public int filterPriority() {
        return 2;
    }

    @Override
    public void createSession(Map<String, Object> filterContext, HttpServletResponse response) {

        String providerKey = (String) filterContext.get("providerKey");
        UserModel model = (UserModel) filterContext.get("userModel");

        int currentTimeSeconds = (int) (globalClockSource.getGlobalClock().millis() / 1000);
        int sessionDuration = config.getSessionBehaviour().getSessionDuration();
        int sessionExp = currentTimeSeconds + sessionDuration;
        String csrfToken = null;

        // Bind csrf token to encrypted login cookie
        if (filterContext.containsKey("csrfToken")) {
            csrfToken = (String) filterContext.get("csrfToken");
        }

        LoginCookie loginCookie = new LoginCookie(sessionExp, providerKey, model, csrfToken);

        String encryptedLoginCookie = cookieEncryptor.encryptObject(loginCookie);

        Cookie cookie = new Cookie(LoginCookie.NAME, encryptedLoginCookie);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(sessionDuration);
        cookie.setSecure(config.isHttpsHost());
        CookieUtils.addSameSiteCookie(cookie, LoginCookie.SAMESITE, response);
    }

    @Override
    public void destroySession(Map<String, Object> filterContext, HttpServletResponse response) {

        // Override session cookie with new cookie that has max-age = 0
        Cookie cookie = new Cookie(LoginCookie.NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(config.isHttpsHost());
        CookieUtils.addSameSiteCookie(cookie, LoginCookie.SAMESITE, response);
    }
}
