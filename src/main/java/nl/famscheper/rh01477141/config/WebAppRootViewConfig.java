package nl.famscheper.rh01477141.config;

import nl.famscheper.rh01477141.ErrorPageController;
import nl.famscheper.rh01477141.HomePageController;
import org.apache.deltaspike.core.api.config.view.DefaultErrorView;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.controller.ViewControllerRef;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

/**
 * Static view configuration for the root folder.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Folder(name = "/")
@View(navigation = View.NavigationMode.REDIRECT)
public interface WebAppRootViewConfig extends ViewConfig {

    @ViewControllerRef(ErrorPageController.class)
    @View(name = "error-message", navigation = View.NavigationMode.FORWARD, viewParams = View.ViewParameterMode.INCLUDE) class CustomErrorPage
            extends DefaultErrorView {
        // define our default error page (/error-message.xhtml)
    }

    @ViewControllerRef(HomePageController.class) class Home implements WebAppRootViewConfig {
        // Define controller for home.xhtml
    }

}
