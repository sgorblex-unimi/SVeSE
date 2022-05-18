package org.miniblex.svese.model;

/**
 * Implementation of {@link Vote} for an election with preferences. In
 * categorical elections with preferences, after the selection of a choice
 * (usually a political group), the elector makes a second vote over a list
 * dependent on said choice (usually a person for the selected group). A
 * PreferencedVote contains the choice and embeds the vote on the second list
 * (also referred to as subvote).
 *
 * TODO [?]: possibly add a method to retrieve the choice
 */
public class PreferencedVote extends CategoricalVote {
	private final Vote sub;

	/**
	 * Constructs a new PreferencedVote with the given choice and the given subvote.
	 * 
	 * @param chosen
	 * @param sub
	 */
	public PreferencedVote(Choice chosen, Vote sub) {
		super(chosen);
		this.sub = sub;
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.PREFERENCED;
	}

	/**
	 * Returns the subvote of this PreferencedVote. It is responsability of the
	 * caller to know to what {@link CompositeChoice} and related
	 * {@link VotingPaper} the subvote is referring to.
	 * 
	 * @return the subvote.
	 */
	public Vote getSub() {
		return sub;
	}

}
