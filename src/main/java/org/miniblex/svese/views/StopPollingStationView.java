package org.miniblex.svese.views;

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

/**
 * User interface to stop the voting session in a polling station
 */
@PermitAll
@Route(value = "/stopPollingStation", layout = MainView.class)
public class StopPollingStationView extends Div {
	Dialog dialog = new Dialog();
	private Button stopButton = new Button("Stop polling station", e -> dialog.open());

	public StopPollingStationView() {
		VerticalLayout dialogLayout = createStopPollingStationDialog();
		dialog.add(dialogLayout);
		add(dialog, stopButton);
		getStyle().set("position", "fixed").set("top", "0").set("right", "0").set("bottom", "0").set("left", "0").set("display", "flex").set("align-items", "center").set("justify-content", "center");
	}

	/**
	 * Creates a vertical layout that is used to create the stop dialog.
	 * 
	 * @return A {@link VerticalLayout} with all the dialog components for user
	 *         interaction.
	 */
	private VerticalLayout createStopPollingStationDialog() {
		H2 headline = new H2("Are you sure you want to stop the polling station?");
		headline.getStyle().set("margin-top", "0");

		Button confirmButton = new Button("Confirm", e -> {
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
