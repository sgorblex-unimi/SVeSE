package org.miniblex.svese.views.vote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import org.miniblex.svese.model.Choice;
import org.miniblex.svese.model.OrdinalVote;
import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.Vote;
import org.miniblex.svese.model.VotingPaper;

public class OrdinalLayout extends VotingLayout {

	OrdinalLayout(Person voter, Dialog voteDialog, VotingPaper vp, List<Button> votingButtons,
			Map<VotingPaper, Vote> votes) {
		super(voter, voteDialog, vp, votingButtons, votes);
		initialiazeOrdinalLayout();
	}

	private void initialiazeOrdinalLayout() {
		VerticalLayout voteLayout = new VerticalLayout();
		List<Select<Choice>> sels = new ArrayList<>();
		Set<Choice> availableChoice = vp.getChoices();
		int pos = 1;

		for (Choice c : vp.getChoices()) {
			Select<Choice> s = new Select<>();
			s.setLabel("Posizione " + pos++ + " :");
			s.setItemLabelGenerator(Choice::getName);
			s.setItems(availableChoice);
			sels.add(s);
		}

		for (Select<Choice> sel : sels) {
			voteLayout.add(sel);
		}

		Button confirmButton;
		confirmButton = new Button("Vote", e -> {
			List<Choice> order = new ArrayList<>();
			for (Select<Choice> sel : sels)
				order.add(sel.getValue());
			OrdinalVote v = new OrdinalVote(order);
			votes.put(vp, v);
			voteDialog.close();
			confirmDialog = new Dialog();
			confirmDialog.add(createConfirmDialog());
			confirmDialog.open();
		});

		voteLayout.add(confirmButton);
		voteDialog.add(voteLayout);
	}
}
