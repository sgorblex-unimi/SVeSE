package org.miniblex.svese.views;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.security.SecurityService;

/**
 * Admin UI to create new voting session
 */
@PermitAll
@Route(value = "/newsession", layout = MainView.class)
public class NewSessionView extends Div {
	Dialog dialog = new Dialog();
	private Button confirmButton;

	private DateTimePicker startPicker;
	private DateTimePicker endPicker;

	public NewSessionView(SecurityService securityService) {
		VerticalLayout dialogLayout = createLogoutDialog(securityService);
		dialog.add(dialogLayout);
		add(createForm(), dialog, confirmButton);
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

		Button confirmButton = new Button("Create new session", e -> {
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

	/**
	 * Creates the form with input fields for every
	 * {@link org.miniblex.svese.model.Session} {@link PropertyId}
	 * 
	 * @return
	 */
	private HorizontalLayout createForm() {
		HorizontalLayout form = new HorizontalLayout();
		VerticalLayout picker = new VerticalLayout();

		confirmButton = new Button("Confirm", e -> dialog.open());

		startPicker = new DateTimePicker();
		startPicker.setLabel("Start of the voting session");
		endPicker = new DateTimePicker();
		endPicker.setLabel("End of the voting session");

		picker.add(startPicker, endPicker);
		form.add(picker, confirmButton);
		return form;

	}
}
