package org.miniblex.svese.views.newSession;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.PersonRepository;
import org.miniblex.svese.model.Session;
import org.miniblex.svese.model.Session.SessionParameters;
import org.miniblex.svese.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Admin UI to create new voting session
 */
@RolesAllowed("ADMIN")
@Route(value = "/newsession", layout = MainView.class)
public class NewSessionView extends VerticalLayout {
	private Dialog dialog = new Dialog();
	private Html error = new Html("<b style='color:red'> Given parameters are null or not valid. Retry</b>");
	private Button confirmButton;

	private PersonRepository pr;

	private DateTimePicker startPicker;
	private DateTimePicker endPicker;
	private NewVotingPaper newVotingPaper;
	private GuarantorsGrid guarantorsGrid;

	public NewSessionView(@Autowired PersonRepository pr) {
		this.pr = pr;
		if (!Session.isRunning()) {
			VerticalLayout dialogLayout = createDialog();
			dialog.add(dialogLayout);
			this.setAlignItems(Alignment.CENTER);
			newVotingPaper = new NewVotingPaper();
			this.add(newVotingPaper, createForm(), dialog);
		} else {
			this.add(new Label("Session is currently running"));
		}
		getStyle().set("position", "fixed").set("top", "1").set("right", "0").set("bottom", "1").set("left", "0")
				.set("display", "flex").set("align-items", "center").set("justify-content", "center");
	}

	/**
	 * Creates a vertical layout that is used to create the confirm dialog.
	 * 
	 * @param securityService
	 *                        security components used to logout and delete session
	 *                        information.
	 * @return A {@link VerticalLayout} with all the components in the layout.
	 */
	private VerticalLayout createDialog() {
		H2 headline = new H2("Are you sure?");
		headline.getStyle().set("margin-top", "0");

		Button confirmButton = new Button("Create new session", e -> {
			try {
				createNewSession();
				dialog.close();
			} catch (NullPointerException | IllegalArgumentException ex) {
				error.setVisible(true);
				dialog.close();
			}
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
	 * {@link org.miniblex.svese.model.Session}.
	 * 
	 * @return An {@link HorizontalLayout} containing the form just created
	 */
	private HorizontalLayout createForm() {
		HorizontalLayout form = new HorizontalLayout();
		VerticalLayout basicComponents = new VerticalLayout();
		error.setVisible(false);

		confirmButton = new Button("Create new session", e -> {
			dialog.open();
		});

		startPicker = new DateTimePicker();
		startPicker.setLabel("Start of the voting session");

		endPicker = new DateTimePicker();
		endPicker.setLabel("End of the voting session");

		basicComponents.add(new Label("Session parameters"), startPicker, endPicker, error, confirmButton);
		guarantorsGrid = new GuarantorsGrid(pr);

		VerticalLayout guarantorsLayout = new VerticalLayout(new Label("Guarantors selection"),
				guarantorsGrid);
		guarantorsLayout.setPadding(false);
		form.add(guarantorsLayout, basicComponents);
		form.setSizeFull();
		return form;
	}

	private void createNewSession() {
		SessionParameters sp = new SessionParameters();
		sp.setStart(startPicker.getValue());
		sp.setEnd(endPicker.getValue());
		sp.addPaper(newVotingPaper.getVotingPaper());

		List<Person> g = new ArrayList<>();
		for (Person p : guarantorsGrid.getSelected()) {
			g.add(p);
		}
		Session.initializeSession(sp, g);
	}

}
