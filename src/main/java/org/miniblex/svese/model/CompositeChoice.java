package org.miniblex.svese.model;

/**
 * Represents a {@link VotingPaper} choice which has multiple suboptions. E.g.
 * people for a group).
 *
 * If the user of a {@link VotingPaper} wants to access the complete paper, it
 * may check if it is composite.
 *
 * Immutable with the exception of the {@link VotingPaper} returned by
 * getSubopts.
 */
public class CompositeChoice extends Choice {
	private final VotingPaper subopts;

	/**
	 * Constructs a new composite choice which selection enables the options within
	 * the given {@link VotingPaper}.
	 * 
	 * @param name
	 *                the name of the macro choice (e.g. group name).
	 * @param suboptions
	 *                the {@link VotingPaper} enabled by the selection of this
	 *                choice.
	 */
	public CompositeChoice(String name, VotingPaper suboptions) {
		super(name);
		this.subopts = suboptions;
	}

	@Override
	public boolean isComposite() {
		return true;
	}

	/**
	 * Returns the {@link VotingPaper} composed by the suboptions of this
	 * {@link Choice}.
	 * 
	 * @return the {@link VotingPaper} with the suboptions.
	 */
	public VotingPaper getSubopts() {
		return subopts;
	}

}
