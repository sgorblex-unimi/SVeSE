package org.miniblex.svese.views.newSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import org.miniblex.svese.model.AdultDecider;
import org.miniblex.svese.model.Choice;
import org.miniblex.svese.model.ElectionMethod;
import org.miniblex.svese.model.VotingPaper;

/**
 * View used to create a new voting paper.
 */
public class NewVotingPaper extends VerticalLayout {
	private Dialog d;
	private List<Choice> parentViewChoices;
	private Map<Choice, VotingPaper> parentChoices;
	private Select<ElectionMethod> parentSelect;
	private TextField parentTitle;

	public NewVotingPaper() {
		d = new Dialog();
		this.parentViewChoices = new ArrayList<>();
		this.parentChoices = new HashMap<>();
		this.parentSelect = new Select<ElectionMethod>();
		this.parentTitle = new TextField();

		// Horizontal layout with choice management component
		HorizontalLayout h = new HorizontalLayout();
		h.setSizeFull();
		Grid<Choice> g = getGrid(parentViewChoices);

		h.add(getForm(parentTitle, parentViewChoices, parentChoices, g, parentSelect, false), g);
		this.setPadding(false);
		add(h, d);
	}

	/**
	 * Generate a grid to show viewChoices and to delete {@link Choice}.
	 * 
	 * @param viewChoices {@link Choice} {@link List} to be shown.
	 * @return Returns the new grid.
	 */
	private Grid<Choice> getGrid(List<Choice> viewChoices) {
		Grid<Choice> g = new Grid<>(Choice.class, false);
		GridContextMenu<Choice> menu = g.addContextMenu();
		g.addColumn("name").setHeader("Name");
		menu.setOpenOnClick(true);
		menu.addItem("Delete", event -> {
			if (event.getItem().isPresent()) {
				viewChoices.remove(event.getItem().get());
				refreshGrid(g, viewChoices);
			}
		});
		return g;
	}

	/**
	 * Generate the form with the components needed to create a new
	 * {@link VotingPaper}.
	 * 
	 * @param title            {@link TextField} with the title of the new
	 *                         {@link VotingPaper}
	 *
	 * @param viewChoices      {@link List} with all the choices to be shown in the
	 *                         grid.
	 * @param choices          {@link Map} with choices and related voting paper,
	 *                         used to create a new session.
	 * @param g                {@link Grid} where input choices are shown.
	 * @param select           {@link Select} to decide with {@link ElectionMethod}
	 *                         to use.
	 * @param disableComposite If {@code True} disable the composite checkbox.
	 *
	 * @return A {@link VerticalLayout} containing the form.
	 */
	private VerticalLayout getForm(TextField title, List<Choice> viewChoices, Map<Choice, VotingPaper> choices,
			Grid<Choice> g, Select<ElectionMethod> select, boolean disableComposite) {
		TextField name = new TextField("Name", "Insert name...");
		Checkbox composite = new Checkbox("Suboptions enabled");
		composite.setEnabled(!disableComposite);
		Button add = new Button("Add");

		title.setLabel("Title");
		title.setPlaceholder("Insert title...");
		title.setRequired(true);
		title.setRequiredIndicatorVisible(true);

		select.setLabel("Election method");
		select.setItems(ElectionMethod.values());
		select.setItemLabelGenerator((var s) -> s.toString().substring(0, 1) + s.toString().substring(1).toLowerCase());
		select.setPlaceholder("Method");
		select.setRequiredIndicatorVisible(true);
		select.setErrorMessage("This field is required");

		HorizontalLayout form = new HorizontalLayout();

		add.addClickShortcut(Key.ENTER);
		form.setAlignItems(Alignment.BASELINE);
		form.add(name, composite, add);
		form.setPadding(false);

		add.addClickListener(e -> {
			if (composite.getValue()) {
				subVotingPaper(choices, viewChoices, g, name.getValue());
			} else {
				viewChoices.add(new Choice(name.getValue()));
				choices.put(new Choice(name.getValue()), null);
			}
			name.setValue("");
			refreshGrid(g, viewChoices);
		});

		VerticalLayout vert = new VerticalLayout(new Label("Choices input"), title, form, select);
		vert.setWidth("50%");
		return vert;
	}

	/**
	 * Refresh the {@link Grid} with the {@link Choice}s in the parameter.
	 * 
	 * @param g           {@link Grid} to be refreshed.
	 * @param viewChoices New data to refresh the {@link Grid}.
	 */
	private void refreshGrid(Grid<Choice> g, List<Choice> viewChoices) {
		g.setItems(viewChoices);
	}

	/**
	 * Create a dialog to let the user create a subvoting paper for the
	 * {@link Choice} specified by {@code name}.
	 * 
	 * @param choices     {@link Choice}s list where current {@link Choice} is going
	 *                    to be added.
	 * @param viewChoices {@link Choice}s list to be shown in the first
	 *                    {@link Grid}.
	 * @param parentGrid  {@link Grid} where viewChoice are shown. This is a pointer
	 *                    to the "first level" voting paper grid.
	 * @param name        Name of the current composite {@link Choice}.
	 */
	private void subVotingPaper(Map<Choice, VotingPaper> choices, List<Choice> viewChoices,
			Grid<Choice> parentGrid, String name) {
		d = new Dialog();
		d.setSizeFull();
		List<Choice> subViewChoices = new ArrayList<>();
		Map<Choice, VotingPaper> subChoices = new HashMap<>();
		Select<ElectionMethod> select = new Select<ElectionMethod>();
		TextField title = new TextField();
		System.out.println(subViewChoices);

		VerticalLayout vdialog = new VerticalLayout();
		vdialog.setSizeFull();

		HorizontalLayout h = new HorizontalLayout();
		h.setSizeFull();
		Grid<Choice> g = getGrid(subViewChoices);
		h.add(g, getForm(title, subViewChoices, subChoices, g, select, true));

		vdialog.add(h, new Button("Confirm", e -> {
			d.close();
			VotingPaper vot = new VotingPaper(title.getValue(), choices, select.getValue(), new AdultDecider());
			choices.put(new Choice(name), vot);
			viewChoices.add(new Choice(name));
			refreshGrid(parentGrid, viewChoices);
		}));
		d.add(vdialog);
		d.open();
	}

	/**
	 * Returns the parent voting paper created by the user.
	 * 
	 * @return the parent voting paper.
	 */
	protected VotingPaper getVotingPaper() {
		return new VotingPaper(parentTitle.getValue(), parentChoices, parentSelect.getValue(), new AdultDecider());
	}
}
