package org.miniblex.svese.views.vote;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.Session;
import org.miniblex.svese.model.VotingPaper;
import org.miniblex.svese.security.SecurityService;
import org.miniblex.svese.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Voting interface
 */
@PermitAll
@Route(value = "/vote", layout = MainView.class)
public class VoteView extends VerticalLayout {
	Dialog confirmDialog;
	Dialog voteDialog;
	VotingLayout vl;
	Label error = new Label("");
	List<Button> votingButtons;
	private final Person voter;

	public VoteView(@Autowired SecurityService sec) {
		getStyle().set("position", "fixed").set("top", "0").set("right", "0").set("bottom", "0").set("left", "0")
				.set("display", "flex").set("align-items", "center").set("justify-content", "center");

		HorizontalLayout buttonLayout = new HorizontalLayout();
		votingButtons = new ArrayList<Button>();
		voter = sec.getAuthenticatedPerson();
		boolean morePapers;

		try {
			Session s = Session.getSession();
			if (Session.isRunning()) {
				morePapers = false;
				for (VotingPaper vp : s.getPapers()) {
					Button b = new Button(vp.getTitle(), e -> {
						voteDialog = new Dialog();
						switch (vp.getMethod()) {
							case ORDINAL:
								vl = new OrdinalLayout(voter, voteDialog, vp, votingButtons, null);
								vl.open();
								break;
							case REFERENDUM:
								// Referendum is just a Categorical Voting with 2 choices
								vl = new CategoricalLayout(voter, voteDialog, vp, votingButtons, null, true, true);
								vl.open();
								break;
							case CATEGORICAL:
								vl = new CategoricalLayout(voter, voteDialog, vp, votingButtons, null, false, true);
								vl.open();
								break;
							case PREFERENCED:
								vl = new CategoricalLayout(voter, voteDialog, vp, votingButtons, null, false, false);
								vl.open();
								break;
						}
					});
					if (vp.hasVoted(sec.getAuthenticatedPerson())) {
						b.setEnabled(false);
					} else {
						b.setEnabled(true);
						morePapers = true;
					}
					votingButtons.add(b);
				}
				if (morePapers) {
					for (Button b : votingButtons) {
						buttonLayout.add(b);
					}
					error.setText("");
					add(buttonLayout);
				} else {
					error.setText("Alreay voted in all the avaiable voting paper");
				}
			} else {
				error.setText("The session has not been started yet");
			}
		} catch (NullPointerException e) {
			error.setText("There is no session available");
			for (Button b : votingButtons) {
				b.setEnabled(false);
				buttonLayout.add(b);
			}
		} finally {
			add(error);
		}
	}
}
