package nl.famscheper.rh01477141.exception.handler;

import nl.famscheper.rh01477141.config.WebAppRootViewConfig;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;
import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DeltaSpike Exception handler.
 *
 * @author Erik-Berndt Scheper
 * @since 27-07-2015
 */
@ExceptionHandler
public class RuntimeExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(RuntimeExceptionHandler.class.getName());

    public void handleRuntimeException(@Handles ExceptionEvent<RuntimeException> evt) {
        LOGGER.log(Level.SEVERE, "Handling uncaught exception (and returning 500 error page):", evt.getException());

        FacesContext facesContext = FacesContext.getCurrentInstance();
        removeUnhandledFacesExceptionQueuedEvents(facesContext);

        if (facesContext.getExternalContext().isResponseCommitted()) {
            LOGGER.log(Level.SEVERE, "Cannot render 500.xhtml because response has been committed");
            evt.abort();

        } else {
            ViewNavigationHandler viewNavigationHandler = BeanProvider.getContextualReference(ViewNavigationHandler.class);
            viewNavigationHandler.navigateTo(WebAppRootViewConfig.CustomErrorPage.class);

            evt.handled();
        }
    }

    private void removeUnhandledFacesExceptionQueuedEvents(FacesContext context) {
        Iterable<ExceptionQueuedEvent> exceptionQueuedEvents = context.getExceptionHandler().getUnhandledExceptionQueuedEvents();

        if (exceptionQueuedEvents == null || !exceptionQueuedEvents.iterator().hasNext()) {
            return;
        }

        Iterator<ExceptionQueuedEvent> iterator = exceptionQueuedEvents.iterator();
        ExceptionQueuedEvent exceptionQueuedEvent = iterator.next();
        LOGGER.log(Level.SEVERE, "Ignoring exceptionQueuedEvent:", exceptionQueuedEvent.getContext().getException());
        iterator.remove();

        while (iterator.hasNext()) {
            iterator.remove();
        }
    }

}
