/*
 * Copyright 2009-2015 PIANOo; TenderNed programma.
 */
package nl.famscheper.rh01477141;

import org.apache.deltaspike.core.api.config.view.controller.PreRenderView;
import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the error page page.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Model
public class ErrorPageController {

    private static final Logger LOGGER = Logger.getLogger(ErrorPageController.class.getName());

    @Inject
    private ViewNavigationHandler viewNavigationHandler;

    private boolean displayFallbackMessage = true;

    @PreRenderView
    protected void onPreRenderView() throws IOException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();

        for (Map.Entry<String, Object> flashEntry : flash.entrySet()) {
            HandledException handledException = HandledException.of(flashEntry.getKey());

            if (handledException != null) {
                if (flashEntry.getValue() instanceof Exception) {
                    LOGGER.log(Level.INFO, "Handling exception:", (Exception) flashEntry.getValue());
                } else {
                    LOGGER.log(Level.SEVERE, "Handling exception " + flashEntry.getKey() + ", value = " + flashEntry.getValue());
                }
                String messageText = handledException.getMessageKey();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, messageText));
            }
        }

        displayFallbackMessage = facesContext.getMessageList().isEmpty();
    }

    public boolean isDisplayFallbackMessage() {
        return displayFallbackMessage;
    }

    /**
     * Enumeration of all exceptions handled by the {@link ErrorPageController}.
     */
    public enum HandledException {
        VIEW_EXPIRED(ViewExpiredException.class, "JSF View was expired : (" + ViewExpiredException.class.getName() + ")");

        private final String exceptionClassName;
        private final String messageKey;

        private static final Map<String, HandledException> handledExceptions = new HashMap<>();

        static {
            for (HandledException value : HandledException.values()) {
                handledExceptions.put(value.exceptionClassName, value);
            }
        }

        HandledException(Class<? extends Exception> exceptionClass, String messageKey) {
            this.exceptionClassName = exceptionClass.getName();
            this.messageKey = messageKey;
        }

        public String getMessageKey() {
            return messageKey;
        }

        /**
         * Look up the instance belonging to the specified exception class name.
         *
         * @param exceptionClassName name of the exception class to lookup
         * @return the instance found, possibly {@code null}
         */
        public static HandledException of(String exceptionClassName) {
            Objects.requireNonNull(exceptionClassName);

            return handledExceptions.get(exceptionClassName);
        }

        /**
         * Look up the instance belonging to the specified exception class.
         *
         * @param exceptionClass the exception class to lookup
         * @return the instance found, possibly {@code null}
         */
        public static HandledException of(Class<? extends Exception> exceptionClass) {
            Objects.requireNonNull(exceptionClass);

            return HandledException.of(exceptionClass.getName());
        }

    }
}
