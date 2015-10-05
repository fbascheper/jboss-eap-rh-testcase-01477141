/*
 * Copyright 2009-2015 PIANOo; TenderNed programma.
 */
package nl.famscheper.rh01477141.context.stereotype;

import nl.famscheper.rh01477141.authorization.IdentityBasedAccessDecisionVoter;
import nl.famscheper.rh01477141.config.AuthenticationViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewMetaData;
import org.apache.deltaspike.security.api.authorization.Secured;

import javax.enterprise.inject.Stereotype;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A stereotype used to annotate a {@code ViewConfig} indicating that this page or folder is only accessible to authenticated user with the specified role(s).
 *
 * @author Erik-Berndt Scheper
 * @since 16-07-2015
 */
@Documented
@Stereotype
@Retention(RUNTIME)
@Secured(value = IdentityBasedAccessDecisionVoter.class, errorView = AuthenticationViewConfig.AuthorizationFailure.class)
@ViewMetaData
public @interface SecuredByRole {

    /**
     * Required {@link Role}s a user must belong to, in order to view the secured view.
     *
     * @return {@link Role}s a user must belong to, in order to view the secured view
     */
    Role[] roles() default {};

    /**
     * An enumeration of all identified roles within the TN web application.
     */
    enum Role {
        USER,
        ADMINISTRATOR;
    }

}
