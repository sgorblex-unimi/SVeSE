package org.miniblex.svese.views.sessionManagement;

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
@RolesAllowed("ADMIN")
@Route(value = "/management", layout = MainView.class)
public class SessionManagement extends VerticalLayout {
	Dialog readyDialog = new Dialog();
	Dialog closeDialog = new Dialog();
	private Button readyButton = new Button("Set Ready", e -> readyDialog.open());
	private Button closeButton = new Button("Force close", e -> closeDialog.open());
	private Session s;
	private final SecurityService sec;

	public SessionManagement(@Autowired SecurityService sec) {
		this.sec = sec;
		this.setAlignItems(Alignment.CENTER);
		HorizontalLayout hl = new HorizontalLayout();
		VerticalLayout readyDialogLayout = createReadyDialog();
		readyDialog.add(readyDialogLayout);
		VerticalLayout closeDialogLayout = createCloseDialog();
		closeDialog.add(closeDialogLayout);
		Label text = new Label();
		try {
			s = Session.getSession();
			this.add(createSessionLayout());
			if (!s.isReady()) {
				readyButton.setEnabled(true);
			} else {
				readyButton.setEnabled(false);
				readyButton.setText("Already set ready");
			}
			if (Session.isRunning()) {
				closeButton.setEnabled(true);
			} else {
				closeButton.setEnabled(false);
				closeButton.setText("Not running");
			}
		} catch (NullPointerException e) {
			text.setText("There is no session to be managed");
			readyButton.setEnabled(false);
			closeButton.setEnabled(false);
		} finally {
			hl.add(readyButton, closeButton);
			hl.setPadding(false);
			add(readyDialog, closeDialog, text, hl);
		}

		getStyle().set("position", "fixed").set("top", "0").set("right", "0").set("bottom", "0").set("left", "0")
				.set("display", "flex").set("align-items", "center").set("justify-content", "center");
	}

	private VerticalLayout createReadyDialog() {
		H2 headline = new H2("Are you sure you want to set the session ready?");
		headline.getStyle().set("margin-top", "0");

		Button confirmButton = new Button("Confirm", e -> {
			s.setReady();
			readyDialog.close();
			readyButton.setEnabled(false);
			readyButton.setText("Already set ready");
		});
		Button cancelButton = new Button("Cancel", e -> readyDialog.close());
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

	private VerticalLayout createCloseDialog() {
		H2 headline = new H2("Are you sure you want to force close the session?");
		headline.getStyle().set("margin-top", "0");

		Button confirmButton = new Button("Confirm", e -> {
			s.forceClose();
			closeDialog.close();
			closeButton.setEnabled(false);
			closeButton.setText("Not running");
		});
		Button cancelButton = new Button("Cancel", e -> closeDialog.close());
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
