package nl.famscheper.rh01477141.config;

import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameter;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameterContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Definition of navigation actions for the example folder.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Named
@ApplicationScoped
public class ExampleViewDefinition {

    @Inject
    private NavigationParameterContext navigationParameterContext;

    public Class<? extends ExampleViewConfig> demoPage() {
        return ExampleViewConfig.DemoPage.class;
    }

    public Class<? extends ExampleViewConfig> securedAdminPage() {
        return ExampleViewConfig.SecuredAdminPage.class;
    }

    public Class<? extends ExampleViewConfig> securedUserPage() {
        return ExampleViewConfig.SecuredUserPage.class;
    }

    @NavigationParameter(key = "param4", value = "Parameter from the Controller class")
    public Class<ExampleViewConfig.NavigationParameterPage> navigationParameterPage() {
        this.navigationParameterContext.addPageParameter("param3",
                "I also come from a navigation parameter using Dynamic Configuration via NavigationParameterContext");
        return ExampleViewConfig.NavigationParameterPage.class;
    }

}
