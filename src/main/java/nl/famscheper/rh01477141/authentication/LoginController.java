package nl.famscheper.rh01477141.authentication;

import nl.famscheper.rh01477141.config.AuthenticationViewDefinition;
import nl.famscheper.rh01477141.config.ExampleViewDefinition;
import nl.famscheper.rh01477141.config.WebAppRootViewConfig;
import nl.famscheper.rh01477141.config.WebAppRootViewDefinition;
import nl.famscheper.rh01477141.context.stereotype.SecuredByRole;
import org.apache.deltaspike.core.api.config.view.controller.PreRenderView;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.apache.deltaspike.core.spi.scope.window.WindowContext;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * View controller for login / logout.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Model
public class LoginController {

    @Inject
    Identity identity;

    @Inject
    AuthenticationViewDefinition authenticationViewDefinition;

    @Inject
    WindowContext windowContext;

    @Inject
    Event<LoginEvent.LoginSuccessful> loginSuccessfulEvent;

    @Inject
    Event<LoginEvent.LoginFailed> loginFailedEvent;

    @Inject
    ViewNavigationHandler viewNavigationHandler;

    @Inject
    WebAppRootViewDefinition webAppViewDefinition;

    @Inject
    ExampleViewDefinition exampleViewDefinition;

    /**
     * Method to initialize the managed bean.
     */
    @PreRenderView
    public void initialize() {

    }

    public Class<? extends WebAppRootViewConfig> logout() throws IOException {
        Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            HttpSession httpSession = (HttpSession) session;
            httpSession.invalidate();
        }

        // close the DeltaSpike window context
        this.windowContext.closeWindow(this.windowContext.getCurrentWindowId());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return webAppViewDefinition.homePage();
    }

    /**
     * Login with username and password.
     */
    public void loginWithUsernamePwd() {

        // login succeeds if the username starts with "a"...
        String username = identity.getUsername();
        boolean loginSuccessful = username != null && username.startsWith("a");

        if (loginSuccessful) {
            identity.setLoggedIn(true);
            identity.setPassword("*****");
            identity.addRole(SecuredByRole.Role.USER);

            if (username.startsWith("admin")) {
                identity.addRole(SecuredByRole.Role.ADMINISTRATOR);
            }

            Class<? extends WebAppRootViewConfig> defaultDestination = exampleViewDefinition.securedUserPage();
            loginSuccessfulEvent.fire(new LoginEvent.LoginSuccessful(username, defaultDestination));

        } else {
            String messageText = "Your username was invalid (hint: must start with letter 'a' and 'admin' for ADMIN role)";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, messageText, messageText));
            loginFailedEvent.fire(new LoginEvent.LoginFailed(username));
        }
    }

}
