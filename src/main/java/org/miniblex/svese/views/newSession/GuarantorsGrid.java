package org.miniblex.svese.views.newSession;

import java.util.Set;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.PersonRepository;

public class GuarantorsGrid extends HorizontalLayout {
	Grid<Person> g = new Grid<>(Person.class, false);
	private PersonRepository pr;

	public GuarantorsGrid(PersonRepository pr) {
		this.pr = pr;
		initializeGrid();
		setSizeFull();
		add(g);
	}

	private void initializeGrid() {
		g.addColumn(Person::getFirstName).setHeader("First name").setSortable(true);
		g.addColumn(Person::getLastName).setHeader("Last name").setSortable(true);
		g.addColumn(Person::getBirthPlace).setHeader("Birth place").setSortable(true);
		g.addColumn(Person::getBirthDate).setHeader("Birth date").setSortable(true);
		g.addColumn((Person p) -> new String(p.getSsn())).setHeader("SSN").setSortable(true);
		g.setSelectionMode(Grid.SelectionMode.MULTI);

		g.setItems(pr.findAll());

		add(g);
	}

	protected Set<Person> getSelected() {
		return g.getSelectedItems();
	}
}
