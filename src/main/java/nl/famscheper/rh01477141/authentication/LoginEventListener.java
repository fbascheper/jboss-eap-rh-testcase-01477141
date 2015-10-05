package nl.famscheper.rh01477141.authentication;

import nl.famscheper.rh01477141.authorization.IdentityBasedAccessDecisionVoter;
import nl.famscheper.rh01477141.config.AuthenticationViewDefinition;
import nl.famscheper.rh01477141.config.WebAppRootViewConfig;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameterContext;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Event listener for the login events.
 *
 * @author Erik-Berndt Scheper
 * @since 16-07-2015
 */
@RequestScoped
public class LoginEventListener {

    private static final Logger LOGGER = Logger.getLogger(LoginEventListener.class.getName());

    @Inject
    Identity identity;

    @Inject
    private ViewNavigationHandler viewNavigationHandler;

    @Inject
    NavigationParameterContext navigationParameterContext;

    @Inject
    private IdentityBasedAccessDecisionVoter loggedInAccessDecisionVoter;

    @Inject
    AuthenticationViewDefinition authenticationViewDefinition;

    public void handleLoginSuccessful(@Observes LoginEvent.LoginSuccessful event) {
        Class<? extends WebAppRootViewConfig> destination = loggedInAccessDecisionVoter.getDeniedSecurePage();

        if (destination != null) {
            Map<String, String> requestParameterMap = loggedInAccessDecisionVoter.getDeniedSecurePageRequestParameterMap();
            for (Map.Entry<String, String> requestParameterEntry : requestParameterMap.entrySet()) {
                navigationParameterContext.addPageParameter(requestParameterEntry.getKey(), requestParameterEntry.getValue());
            }

        } else {
            destination = event.getDefaultDestination();
        }

        Objects.requireNonNull(destination);

        LOGGER.log(Level.WARNING, "Handling successful login for username " + event.getUsername());
        this.viewNavigationHandler.navigateTo(destination);
    }

    public void handleLoginFailed(@Observes LoginEvent.LoginFailed event) {
        LOGGER.log(Level.WARNING, "Handling failed login for username " + event.getUsername());

        this.viewNavigationHandler.navigateTo(authenticationViewDefinition.login());
    }
}
