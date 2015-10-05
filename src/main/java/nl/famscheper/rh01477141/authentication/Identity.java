package nl.famscheper.rh01477141.authentication;

import nl.famscheper.rh01477141.context.stereotype.SecuredByRole;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Session scoped identity.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Named
@SessionScoped
public class Identity implements Serializable {

    private static final long serialVersionUID = -9154737979944336061L;

    private boolean loggedIn;
    private String username;
    private String password;
    private Set<SecuredByRole.Role> roles;

    @PostConstruct
    public void onPostConstruct() {
        this.loggedIn = false;
        this.username = "";
        this.password = "";
        roles = EnumSet.noneOf(SecuredByRole.Role.class);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addRole(SecuredByRole.Role role) {
        this.roles.add(role);
    }

    public Set<SecuredByRole.Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
