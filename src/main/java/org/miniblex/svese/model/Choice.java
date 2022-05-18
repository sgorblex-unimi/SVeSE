package org.miniblex.svese.model;

/**
 * Representation of a possible choice in an election.
 *
 * Immutable, with the exception of the {@link VotingPaper} made by the
 * suboptions in case of a composite choice (see isComposite).
 */
public class Choice {
	private final String name;
	// TODO: add optional picture, constructor with name+pic, getter for pic.

	/**
	 * Constructs a new Choice with the given name.
	 * 
	 * @param name
	 *                the name.
	 */
	public Choice(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of this {@link Choice}.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns {@code true} if this {@link Choice} is composite, {@code false}
	 * otherwise. If a choice is composite, you can cast it to
	 * {@link CompositeChoice} and use the method getSubopts to get the
	 * {@link VotingPaper} with this {@link Choice}'s suboptions.
	 * 
	 * @return {@code true} if this {@link Choice} is composite, {@code false}
	 *         otherwise.
	 */
	public boolean isComposite() {
		return false;
	}

	/**
	 * Two {@link Choice}s are defined to be equals if they have the same name.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Choice))
			return false;
		Choice other = (Choice) obj;
		return this.name.equals(other.name);
	}

}
