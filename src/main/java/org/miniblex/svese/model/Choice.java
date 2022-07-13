package org.miniblex.svese.model;

import java.util.Objects;

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
	 * @throws NullPointerException
	 *                 if name is {@code null}.
	 */
	public Choice(String name) {
		this.name = Objects.requireNonNull(name);
	}

	/**
	 * Returns the name of this {@link Choice}.
	 *
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Two {@link Choice}s are defined to be equals if they have the same name.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Choice))
			return false;
		return this.name.equals(((Choice) obj).name);
	}

	@Override
	public String toString() {
		return "Choice[name=\"" + name + "\"]";
	}

}
