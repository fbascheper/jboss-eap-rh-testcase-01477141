package nl.famscheper.rh01477141.authorization;

import nl.famscheper.rh01477141.authentication.Identity;
import nl.famscheper.rh01477141.config.AuthenticationViewDefinition;
import org.apache.deltaspike.core.api.config.view.controller.PreRenderView;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Controller for the authorization failures, e.g. when a user is not logged in, or when a user is logged in, but requests access to unauthorized pages.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Model
public class AuthorizationFailureController {

    @Inject
    Identity identity;

    @Inject
    private ViewNavigationHandler viewNavigationHandler;

    @Inject
    private AuthenticationViewDefinition authenticationViewDefinition;

    @PreRenderView
    protected void onPreRenderView() throws IOException {
        if (!identity.isLoggedIn()) {
            this.viewNavigationHandler.navigateTo(authenticationViewDefinition.login());
        }

    }
}
