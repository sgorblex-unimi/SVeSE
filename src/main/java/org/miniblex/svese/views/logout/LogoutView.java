package org.miniblex.svese.views.logout;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.security.SecurityService;
import org.miniblex.svese.views.main.MainView;

/**
 * Logout user interface
 */
@PermitAll
@Route(value = "/logout", layout = MainView.class)
// TODO: possibly substitute with logout button
public class LogoutView extends Div {
	Dialog dialog = new Dialog();
	private Button stopButton = new Button("Logout", e -> dialog.open());

	public LogoutView(SecurityService securityService) {
		VerticalLayout dialogLayout = createLogoutDialog(securityService);
		dialog.add(dialogLayout);
		add(dialog, stopButton);
		getStyle().set("position", "fixed").set("top", "0").set("right", "0").set("bottom", "0").set("left", "0")
				.set("display", "flex").set("align-items", "center").set("justify-content", "center");
	}

	/**
	 * Creates a vertical layout that is used to create the logout dialog.
	 * 
	 * @param securityService
	 *          security components used to logout and delete session information.
	 * @return A {@link VerticalLayout} with all the components in the layout.
	 */
	private VerticalLayout createLogoutDialog(SecurityService securityService) {
		H2 headline = new H2("Are you sure?");
		headline.getStyle().set("margin-top", "0");

		Button confirmButton = new Button("Confirm", e -> {
			securityService.logout();
			dialog.close();
		});
		Button cancelButton = new Button("Cancel", e -> dialog.close());
		confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
		confirmButton.getStyle().set("margin-inline-end", "auto");
		HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
		buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		buttonLayout.getStyle().set("flex-wrap", "wrap");
		buttonLayout.setWidthFull();

		VerticalLayout dialogLayout = new VerticalLayout(headline, buttonLayout);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(false);

		return dialogLayout;
	}
}
