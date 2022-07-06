package org.miniblex.svese.views.vote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.Session;
import org.miniblex.svese.model.Vote;
import org.miniblex.svese.model.VotingPaper;

public abstract class VotingLayout extends VerticalLayout {
	Dialog voteDialog;
	Dialog confirmDialog;
	final VotingPaper vp;
	final List<Button> votingButtons;
	Map<VotingPaper, Vote> votes;
	Person voter;

	VotingLayout(Person voter, Dialog voteDialog, VotingPaper vp, List<Button> votingButtons,
			Map<VotingPaper, Vote> votes) {
		this.voter = voter;
		this.voteDialog = voteDialog;
		this.vp = vp;
		this.votingButtons = votingButtons;
		if (votes == null) {
			this.votes = new HashMap<>();
		} else {
			this.votes = votes;
		}
	}

	public void open() {
		voteDialog.open();
	}

	/**
	 * Creates a vertical layout that is used to create the dialog to confirm vote.
	 * 
	 * @return A {@link VerticalLayout} with all the dialog components for user
	 *         interaction.
	 */
	public VerticalLayout createConfirmDialog() {
		VerticalLayout confirmLayout = new VerticalLayout();

		H2 headline = new H2("Are you sure?");
		confirmLayout.add(headline);
		confirmLayout.add(new Label("You voted for:"));

		for (Map.Entry<VotingPaper, Vote> v : votes.entrySet()) {
			confirmLayout.add(new Label("- " + v.getKey().getTitle()));
		}
		headline.getStyle().set("margin-top", "0");
		Button confirmButton = new Button("Confirm");

		Button cancelButton = new Button("Cancel", e -> confirmDialog.close());
		confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
		confirmButton.getStyle().set("margin-inline-end", "auto");
		initializeConfirmButton(confirmButton);
		HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
		buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		buttonLayout.getStyle().set("flex-wrap", "wrap");
		buttonLayout.setWidthFull();

		confirmLayout.add(buttonLayout);
		confirmLayout.setPadding(false);
		confirmLayout.setSpacing(false);

		return confirmLayout;
	}

	public void initializeConfirmButton(Button confirmButton) {
		confirmButton.setText("Confirm");
		confirmButton.addClickListener(e -> {
			for (Map.Entry<VotingPaper, Vote> v : votes.entrySet()) {
				v.getKey().addVote(v.getValue(), voter);
				for (Button b : votingButtons) {
					if (b.getText().equals(v.getKey().getTitle())) {
						b.setEnabled(false);
					}
				}
			}
			confirmDialog.close();
		});
	}

}
