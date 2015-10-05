package nl.famscheper.rh01477141.authentication;

import nl.famscheper.rh01477141.config.WebAppRootViewConfig;

/**
 * Definition of the login events.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
public class LoginEvent {

    /**
     * Event definition for successful login.
     */
    public static class LoginSuccessful {
        private final String username;
        private final Class<? extends WebAppRootViewConfig> defaultDestination;

        public LoginSuccessful(String username, Class<? extends WebAppRootViewConfig> defaultDestination) {
            this.username = username;
            this.defaultDestination = defaultDestination;
        }

        public String getUsername() {
            return username;
        }

        public Class<? extends WebAppRootViewConfig> getDefaultDestination() {
            return defaultDestination;
        }
    }

    /**
     * Event definition for failed login.
     */
    public static class LoginFailed {
        private final String username;

        public LoginFailed(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }
}
