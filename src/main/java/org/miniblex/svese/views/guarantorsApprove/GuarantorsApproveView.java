package org.miniblex.svese.views.guarantorsApprove;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.model.Choice;
import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.Session;
import org.miniblex.svese.model.VotingPaper;
import org.miniblex.svese.model.Session.SessionParameters;
import org.miniblex.svese.security.SecurityService;
import org.miniblex.svese.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User interface to stop the voting session in a polling station
 */
@RolesAllowed("GUARANTOR")
@Route(value = "/approve", layout = MainView.class)
public class GuarantorsApproveView extends VerticalLayout {
	Dialog dialog = new Dialog();
	private Button approveButton = new Button("Approve", e -> dialog.open());
	private Session s;
	private final SecurityService sec;

	public GuarantorsApproveView(@Autowired SecurityService sec) {
		this.sec = sec;
		this.setAlignItems(Alignment.CENTER);
		VerticalLayout dialogLayout = createConfirmDialog();
		Label text = new Label();
		dialog.add(dialogLayout);
		try {
			s = Session.getSession();
			if (!s.isReady()) {
				text.setText("Current session is not ready to be approved");
				approveButton.setEnabled(false);
			} else if (s.hasApproved(sec.getAuthenticatedPerson())) {
				approveButton.setEnabled(false);
				approveButton.setText("Already approved");
			} else {
				this.add(createSessionLayout());
				approveButton.setEnabled(true);
			}
		} catch (

		NullPointerException e) {
			text.setText("There is no session currently waiting to be approved");
			approveButton.setEnabled(false);
		} finally {
			add(dialog, text, approveButton);
		}

		getStyle().set("position", "fixed").set("top", "0").set("right", "0").set("bottom", "0").set("left", "0")
				.set("display", "flex").set("align-items", "center").set("justify-content", "center");
	}

	/**
	 * Creates a vertical layout that is used to create the stop dialog.
	 * 
	 * @return A {@link VerticalLayout} with all the dialog components for user
	 *         interaction.
	 */
	private VerticalLayout createConfirmDialog() {
		H2 headline = new H2("Are you sure you want to approve session parameters?");
		headline.getStyle().set("margin-top", "0");

		Button confirmButton = new Button("Confirm", e -> {
			s.approve(sec.getAuthenticatedPerson());
			dialog.close();
			approveButton.setEnabled(false);
			approveButton.setText("Already approved");
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

	private VerticalLayout createSessionLayout() {
		VerticalLayout vl = new VerticalLayout();
		Session s = Session.getSession();
		SessionParameters sp = s.getCurrentParameters();
		vl.add(new Label("La sessione contiene i seguenti garanti:"));
		for (Person p : s.getGuarantors()) {
			vl.add(new Label(p.getFirstName() + " " + p.getLastName() + " [" + p.getSsn() + "] \n"));
		}
		vl.add(new Label("Inizio: " + sp.getStart().toString().replace("T", " ").split("\\.")[0]));
		vl.add(new Label("Fine: " + sp.getEnd().toString().replace("T", " ").split("\\.")[0]));

		List<VotingPaper> subvps = new ArrayList<>();
		for (VotingPaper vp : s.getPapers()) {
			String vpString = "Voting Paper: \"" + vp.getTitle() + "\": [";
			for (Choice c : vp.getChoices()) {
				vpString += c.getName();
				VotingPaper sub = vp.getSubPaper(c);
				if (sub != null) {
					vpString += " --> " + sub.getTitle() + ", ";
					subvps.add(sub);
				} else {
					vpString += ", ";
				}
			}
			vpString += "]";
			vl.add(new Label(vpString));
		}
		for (VotingPaper vp : subvps) {
			String vpString = "Voting Paper: \"" + vp.getTitle() + "\": [";
			for (Choice c : vp.getChoices()) {
				vpString += c.getName();
				vpString += ", ";
			}
			vpString += "]";
			vl.add(new Label(vpString));
		}

		vl.setAlignItems(Alignment.CENTER);
		return vl;
	}
}
