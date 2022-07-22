package org.miniblex.svese.views.consultResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.miniblex.svese.model.Choice;
import org.miniblex.svese.model.Session;
import org.miniblex.svese.model.VotingPaper;
import org.miniblex.svese.model.VotingPaper.Results;
import org.miniblex.svese.model.VotingPaper.Results.Result;
import org.miniblex.svese.views.main.MainView;

/**
 * User interface to stop the voting session in a polling station
 */
@RolesAllowed({ "GUARANTOR", "ADMIN" })
@Route(value = "/consult", layout = MainView.class)
public class ConsultResultView extends VerticalLayout {
	private Session s;

	public ConsultResultView() {
		Label error = new Label();
		try {
			s = Session.getSession();
			if (Session.isRunning()) {
				error.setText("Current session is still running");
			} else {
				add(createResultLayout(s.getPapers()));
			}
		} catch (NullPointerException e) {
			error.setText("No session is currently available");
		} finally {
			add(error);
		}

		getStyle().set("position", "fixed").set("top", "0").set("right", "0").set("bottom", "0").set("left", "0")
				.set("display", "flex").set("align-items", "center").set("justify-content", "center");
		this.setPadding(false);
	}

	private VerticalLayout createResultLayout(List<VotingPaper> vps) {
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout buttons = new HorizontalLayout();
		for (VotingPaper vp : vps) {
			if (vp == null) {
				continue;
			}
			Button b = new Button("Risultati: " + vp.getTitle(), e -> {
				Dialog d = null;
				switch (vp.getMethod()) {
					case CATEGORICAL:
						d = createCategoricalResultDialog(vp);
						break;
					case REFERENDUM:
						d = createCategoricalResultDialog(vp);
						break;
					case ORDINAL:
						d = createOrdinalResultDialog(vp);
						break;
					case PREFERENCED:
						d = createPreferencedResultDialog(vp);
						break;
				}
				vl.add(d);
				d.open();
			});
			buttons.add(b);
		}
		vl.add(buttons);
		vl.setAlignItems(Alignment.CENTER);
		return vl;
	}

	private Dialog createOrdinalResultDialog(VotingPaper vp) {
		Dialog d = new Dialog();
		VerticalLayout vl = new VerticalLayout();
		vl.setPadding(false);
		Results results = vp.getResults();
		String result = "<dl>" + vp.getTitle();
		for (Result r : results) {
			result += "<dt>" + r.getChoice().getName() + " - Punteggio: " + r.getScore() + "("
					+ round(r.getRelativeScore() * 100, 2) + "%) </dt>";
		}
		result += "In totale sono stati registrati " + results.getTotalVotes() + " voti, con un affluenza del "
				+ round(results.getTurnout() * 100, 2) + "%.<br>";
		result += "Il vincitore è " + results.iterator().next().getChoice().getName();
		result += "</dl>";

		Html resultHtml = new Html(result);
		vl.add(resultHtml);
		vl.add(new Button("Close", e -> d.close()));
		vl.setAlignItems(Alignment.CENTER);
		d.add(vl);
		return d;
	}

	private Dialog createCategoricalResultDialog(VotingPaper vp) {
		Dialog d = new Dialog();
		VerticalLayout vl = new VerticalLayout();
		vl.setPadding(false);
		vl.add(createCategoricalTable(vp));
		vl.add(new Button("Close", e -> d.close()));
		vl.setAlignItems(Alignment.CENTER);
		d.add(vl);
		return d;
	}

	private Dialog createPreferencedResultDialog(VotingPaper vp) {
		Dialog d = new Dialog();
		VerticalLayout vl = new VerticalLayout();
		List<VotingPaper> subVotingPapers = new LinkedList<>();
		for (Choice c : vp.getChoices()) {
			subVotingPapers.add(vp.getSubPaper(c));
		}
		vl.setAlignItems(Alignment.CENTER);
		vl.add(createCategoricalTable(vp), createResultLayout(subVotingPapers));
		d.add(vl);
		return d;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public Html createCategoricalTable(VotingPaper vp) {
		Results results = vp.getResults();
		String result = "<table>";
		result += "<caption>" + vp.getTitle() + "</caption>";
		result += "<th>Nome</th><th>Voti</th><th>Percentuale</th>";
		for (Result r : results) {
			result += "<tr>";
			result += "<td style=\"text-align: center; vertical-align: middle;\">" + r.getChoice().getName() + "</td>";
			result += "<td style=\"text-align: center; vertical-align: middle;\">" + r.getScore() + "</td>";
			result += "<td style=\"text-align: center; vertical-align: middle;\">" + round(r.getRelativeScore() * 100, 2)
					+ "% </td>";
			result += "</tr>";
		}
		result += "<tr><td style=\"text-align: center; vertical-align: middle;\"> Voti totali e <br> affluenza</td>";
		result += "<td style=\"text-align: center; vertical-align: middle;\">" + results.getTotalVotes() + "</td>";
		result += "<td style=\"text-align: center; vertical-align: middle;\">" + round(results.getTurnout() * 100, 2)
				+ "%</td></tr>";
		result += "Il vincitore è " + results.iterator().next().getChoice().getName();
		result += "</table>";
		return new Html(result);
	}
}
