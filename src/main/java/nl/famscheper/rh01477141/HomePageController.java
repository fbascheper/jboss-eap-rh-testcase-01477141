package nl.famscheper.rh01477141;

import nl.famscheper.rh01477141.config.ExampleViewDefinition;
import org.apache.deltaspike.core.api.config.view.controller.PreRenderView;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Controller for the Home page (/home.xhtml).
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Model
public class HomePageController {

    @Inject
    private ViewNavigationHandler viewNavigationHandler;

    @Inject
    private ExampleViewDefinition exampleViewDefinition;

    @PreRenderView
    protected void onPreRenderView() throws IOException {
        this.viewNavigationHandler.navigateTo(exampleViewDefinition.demoPage());

    }
}
