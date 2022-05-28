package org.miniblex.svese.views.vote;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.security.SecurityService;
import org.miniblex.svese.views.main.MainView;

/**
 * Voting interface
 */
@PermitAll
@Route(value = "/vote", layout = MainView.class)
public class VoteView extends VerticalLayout {
	Dialog dialog = new Dialog();
	private Button confirmButton = new Button("Confirm", e -> dialog.open());

	public VoteView(SecurityService securityService) {

		Select<String> dropdown = new Select<>();
		dropdown.setLabel("Select your choice");
		dropdown.setItems("Example 1", "Example 2", "Example 3");
		dropdown.setValue("Example 1");

		VerticalLayout dialogLayout = confirmVoteDialog();
		dialog.add(dialogLayout);
		add(dialog, dropdown, confirmButton);
		getStyle().set("position", "fixed").set("top", "0").set("right", "0").set("bottom", "0").set("left", "0")
				.set("display", "flex").set("align-items", "center").set("justify-content", "center");
	}

	/**
	 * Creates a vertical layout that is used to create the dialog to confirm vote.
	 * 
	 * @return A {@link VerticalLayout} with all the dialog components for user
	 *         interaction.
	 */
	private VerticalLayout confirmVoteDialog() {
		H2 headline = new H2("Are you sure?");
		headline.getStyle().set("margin-top", "0");

		Button confirmButton = new Button("Confirm", e -> {
			// TODO Store preferences
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
