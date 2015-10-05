package nl.famscheper.rh01477141.config;

import nl.famscheper.rh01477141.authentication.LoginController;
import nl.famscheper.rh01477141.authorization.AuthorizationFailureController;
import org.apache.deltaspike.core.api.config.view.controller.ViewControllerRef;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * Static view configuration for the authentication folder.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Folder(name = "./authentication")
public interface AuthenticationViewConfig extends WebAppRootViewConfig {

    @ViewControllerRef(LoginController.class) @View(viewParams = View.ViewParameterMode.INCLUDE) class Login implements AuthenticationViewConfig {
        // Define controller for login.xhtml
    }

    @ViewControllerRef(AuthorizationFailureController.class) @View(navigation = View.NavigationMode.FORWARD) class AuthorizationFailure
            implements AuthenticationViewConfig {
        // Define controller for authorizationFailure.xhtml
    }
}
