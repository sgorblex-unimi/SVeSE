package org.miniblex.svese.views.vote;

import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

import org.miniblex.svese.model.CategoricalVote;
import org.miniblex.svese.model.Choice;
import org.miniblex.svese.model.ElectionMethod;
import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.PreferencedVote;
import org.miniblex.svese.model.ReferendumVote;
import org.miniblex.svese.model.Vote;
import org.miniblex.svese.model.VotingPaper;

public class CategoricalLayout extends VotingLayout {
	CategoricalLayout(Person voter, Dialog voteDialog, VotingPaper vp, List<Button> votingButtons,
			Map<VotingPaper, Vote> votes,
			boolean isReferendum, boolean isLeaf) {
		super(voter, voteDialog, vp, votingButtons, votes);
		initialiazeCategoricalLayout(isReferendum, isLeaf);
	}

	private void initialiazeCategoricalLayout(boolean isReferendum, boolean isLeaf) {
		VerticalLayout voteLayout = new VerticalLayout();
		voteLayout.setPadding(false);

		RadioButtonGroup<Choice> sel = initializeVotingRadioButton();
		sel.setItems(vp.getChoices());

		Button confirmButton;
		if (isLeaf) {
			confirmButton = new Button("Vote", e -> {
				if (isReferendum) {
					votes.put(vp, new ReferendumVote(sel.getValue()));
				} else {
					votes.put(vp, new CategoricalVote(sel.getValue()));
				}
				voteDialog.close();
				confirmDialog = new Dialog();
				confirmDialog.add(createConfirmDialog());
				confirmDialog.open();
			});
		} else {
			confirmButton = new Button("Next", e -> {
				votes.put(vp, new PreferencedVote(sel.getValue()));
				Choice c = sel.getValue();
				VotingPaper newVp = vp.getSubPaper(c);
				VotingLayout vl;
				if (newVp.getMethod() == ElectionMethod.CATEGORICAL) {
					vl = new CategoricalLayout(voter, new Dialog(), newVp, votingButtons, votes, isReferendum, true);
					vl.open();
				} else if (newVp.getMethod() == ElectionMethod.ORDINAL) {
					vl = new OrdinalLayout(voter, new Dialog(), vp, votingButtons, votes);
					vl.open();
				}
				voteDialog.close();
			});
		}

		voteLayout.add(sel, confirmButton);
		voteDialog.add(voteLayout);
	}

	private RadioButtonGroup<Choice> initializeVotingRadioButton() {
		RadioButtonGroup<Choice> rg = new RadioButtonGroup<>();

		rg.setLabel("Select your choice");
		rg.setItemLabelGenerator(Choice::getName);
		rg.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		return rg;
	}
}
