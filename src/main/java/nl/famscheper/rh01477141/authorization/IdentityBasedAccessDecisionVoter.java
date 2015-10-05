package nl.famscheper.rh01477141.authorization;

import nl.famscheper.rh01477141.authentication.Identity;
import nl.famscheper.rh01477141.config.WebAppRootViewConfig;
import nl.famscheper.rh01477141.context.stereotype.SecuredByRole;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.jsf.impl.util.ClientWindowHelper;
import org.apache.deltaspike.security.api.authorization.AbstractAccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.*;

/**
 * An {@link org.apache.deltaspike.security.api.authorization.AccessDecisionVoter}-implementation based on the {@link Identity}.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@SessionScoped
public class IdentityBasedAccessDecisionVoter extends AbstractAccessDecisionVoter {

    public static final List<String> IGNORED_REQUEST_PARAMETER_LIST = Arrays
            .asList(ClientWindowHelper.RequestParameters.GET_WINDOW_ID, ClientWindowHelper.RequestParameters.JSF_POST_WINDOW_ID,
                    ClientWindowHelper.RequestParameters.POST_WINDOW_ID, ClientWindowHelper.RequestParameters.REQUEST_TOKEN);

    @Inject
    Identity identity;

    private Class<? extends ViewConfig> deniedSecurePage = null;
    private Map<String, String> deniedSecurePageRequestParameterMap = null;

    @Override
    protected void checkPermission(AccessDecisionVoterContext accessDecisionVoterContext, Set<SecurityViolation> violations) {

        ViewConfigResolver viewConfigResolver = BeanProvider.getContextualReference(ViewConfigResolver.class);
        String requestedViewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        Class<? extends ViewConfig> requestedPage = viewConfigResolver.getViewConfigDescriptor(requestedViewId).getConfigClass();

        SecuredByRole securityAnnotation = accessDecisionVoterContext.getMetaDataFor(SecuredByRole.class.getName(), SecuredByRole.class);

        if (!identity.isLoggedIn()) {
            // add the security violation.
            violations.add(newSecurityViolation("Not logged in"));

        } else if (!isAuthorized(identity, securityAnnotation)) {
            // add the security violation.
            violations.add(newSecurityViolation("Not authorized"));

        }

        if (!violations.isEmpty()) {
            // and remember the requested page that was denied access
            deniedSecurePage = requestedPage;
            deniedSecurePageRequestParameterMap = preservedRequestParameters();
        }
    }

    /**
     * Return the secured page that was requested by the user, who was denied access because of authorization / authentication failure.
     *
     * @return denied page
     */
    @SuppressWarnings("unchecked")
    public Class<? extends WebAppRootViewConfig> getDeniedSecurePage() {
        return (Class<? extends WebAppRootViewConfig>) deniedSecurePage;
    }

    /**
     * Return the request parameters belonging to the denied secure page.
     *
     * @return the request parameters belonging to the denied secure page
     */
    public Map<String, String> getDeniedSecurePageRequestParameterMap() {
        return Collections.unmodifiableMap(deniedSecurePageRequestParameterMap);
    }

    /**
     * Create a request parameter map from the current request, so they can be restored after a succesful login.
     *
     * @return the map of request parameters
     */
    private Map<String, String> preservedRequestParameters() {
        Map<String, String> result = new HashMap<>();

        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        requestParameterMap.entrySet().stream().filter(entry -> !IGNORED_REQUEST_PARAMETER_LIST.contains(entry.getKey())).forEach(entry -> {
            result.put(entry.getKey(), entry.getValue());
        });

        return result;
    }

    /**
     * Verify that the given identity has the required roles fot this security annotation.
     *
     * @param identity           the identity to test
     * @param securityAnnotation the specified security annotation
     * @return {@code true} if authorization is allowed
     */
    private boolean isAuthorized(Identity identity, SecuredByRole securityAnnotation) {
        return hasRequiredRolesFor(securityAnnotation).stream().anyMatch(role -> identity.getRoles().contains(role));
    }

    /**
     * Build the set of required roles for the given security annotation.
     *
     * @param securityAnnotation the annotation
     * @return the set of required roles
     */
    public Set<SecuredByRole.Role> hasRequiredRolesFor(SecuredByRole securityAnnotation) {
        Set<SecuredByRole.Role> result;

        SecuredByRole.Role[] roles = securityAnnotation.roles();
        if (roles.length == 0) {
            result = EnumSet.noneOf(SecuredByRole.Role.class);
        } else {
            result = EnumSet.of(roles[0], roles);
        }

        return result;
    }
}
