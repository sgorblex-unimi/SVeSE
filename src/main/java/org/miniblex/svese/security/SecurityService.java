package org.miniblex.svese.security;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Provides utilities relative to security and web session.
 */
@Component
public class SecurityService {
	private static final String LOGOUT_SUCCESS_URL = "/login";

	/**
	 * Returns Spring Security authenticated user.
	 */
	private UserDetails getAuthenticatedUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return (UserDetails) principal;
		}
		// anonymous or no authentication.
		return null;
	}

	@Autowired
	private PersonRepository repo;

	/**
	 * Returns the current authenticated {@link Person}.
	 *
	 * @return the logged {@link Person}.
	 */
	public Person getAuthenticatedPerson() {
		UserDetails user = getAuthenticatedUser();
		if (user == null)
			return null;
		Optional<Person> person = repo.findById(user.getUsername());
		if (person.isEmpty())
			throw new AssertionError("the logged user is not a valid person!?");
		return person.get();
	}

	/**
	 * Logs the current user out.
	 */
	public void logout() {
		UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
	}

	/**
	 * Constructs a new SecurityService. Should only be called by
	 * Spring's @Autowired.
	 */
	private SecurityService() {
	}

}
