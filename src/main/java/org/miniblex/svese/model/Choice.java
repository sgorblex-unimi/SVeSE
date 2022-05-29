package org.miniblex.svese.model;

/**
 * Representation of a possible choice in an election.
 *
 * Immutable.
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
