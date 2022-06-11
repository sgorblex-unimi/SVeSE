package org.miniblex.svese.model;

/**
 * Implementation of {@link Vote} for an election with preferences. In
 * categorical elections with preferences, after the selection of a choice
 * (usually a political group), the elector makes a second vote over a list
 * dependent on said choice (usually a person for the selected group). A
 * PreferencedVote contains only the first choice, while the vote on the second
 * list is part of the subpaper.
 */
public class PreferencedVote extends CategoricalVote {

	/**
	 * Constructs a new PreferencedVote with the given {@link Choice}.
	 * 
	 * @param chosen
	 *                the choice.
	 */
	public PreferencedVote(Choice chosen) {
		super(chosen);
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.PREFERENCED;
	}

}
