package ch.gianlucafrei.nellygateway.config.configuration;

import ch.gianlucafrei.nellygateway.config.ErrorValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionBehaviour implements ErrorValidation {

    private int sessionDuration;
    private int renewWhenLessThan;
    private String redirectLoginSuccess;
    private String redirectLoginFailure;
    private String redirectLogout;

    @Override
    public List<String> getErrors(ApplicationContext context) {
        var errors = new ArrayList<String>();

        if (sessionDuration < 60)
            errors.add("session duration is to short < 60s");

        if (redirectLoginSuccess == null)
            errors.add("redirectLoginSuccess not defined");

        if (redirectLoginFailure == null)
            errors.add("redirectLoginFailure not defined");

        if (redirectLogout == null)
            errors.add("redirectLogout not defined");

        if (!errors.isEmpty())
            return errors;

        if (renewWhenLessThan >= sessionDuration)
            errors.add("renewWhenLessThan cannot be >= than sessionDuration");

        return errors;
    }
}
