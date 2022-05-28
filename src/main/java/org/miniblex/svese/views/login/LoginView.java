package org.miniblex.svese.views.login;

import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.SVeSE;

/**
 * Form to authenticate and gain access to resources
 */
@Route("/login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	LoginOverlay lo = new LoginOverlay();

	LoginView() {
		lo.getElement().getThemeList().add("dark");
		lo.setTitle(SVeSE.SYS_NAME);
		lo.setDescription(SVeSE.SYS_DESCRIPTION);
		lo.setForgotPasswordButtonVisible(false);
		lo.setOpened(true);
		lo.setAction("login");
		add(lo);
	}

	/**
	 * Checks if user failed login and shows an error message.
	 */
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
			// authentication error
			lo.setError(true);
		}
	}
}
