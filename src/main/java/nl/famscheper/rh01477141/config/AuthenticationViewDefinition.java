package nl.famscheper.rh01477141.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Definition of navigation actions for the authentication folder.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Named
@ApplicationScoped
public class AuthenticationViewDefinition {

    public Class<? extends AuthenticationViewConfig> login() {
        return AuthenticationViewConfig.Login.class;
    }

    public Class<? extends AuthenticationViewConfig> authorizationError() {
        return AuthenticationViewConfig.AuthorizationFailure.class;
    }

}
