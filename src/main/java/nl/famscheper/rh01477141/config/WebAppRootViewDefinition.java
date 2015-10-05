package nl.famscheper.rh01477141.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Definition of navigation actions for the root folder.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Named
@ApplicationScoped
public class WebAppRootViewDefinition {

    public Class<? extends WebAppRootViewConfig> homePage() {
        return WebAppRootViewConfig.Home.class;
    }

}
