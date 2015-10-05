package nl.famscheper.rh01477141.exception.handler;

import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.exception.control.BeforeHandles;
import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.security.api.authorization.AccessDeniedException;

import javax.faces.context.FacesContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DeltaSpike Exception handler.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@ExceptionHandler
public class AccessDeniedExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(AccessDeniedExceptionHandler.class.getName());

    public void handleAccessDenied(@BeforeHandles ExceptionEvent<AccessDeniedException> evt) {
        ViewConfigResolver viewConfigResolver = BeanProvider.getContextualReference(ViewConfigResolver.class);
        Class<?> deniedSecurePage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();

        LOGGER.log(Level.FINEST, "Ignoring AccessDeniedException occurred for page " + deniedSecurePage, evt.getException());

        evt.handled();
    }

}
