package org.miniblex.svese.model;

/**
 * The interface for a Vote. A vote has no reference to an election, so it is
 * left to the user to make the votes relevant to the election choices.
 *
 * Immutable.
 */
public interface Vote {
	/**
	 * Returns the score value in this {@link Vote} for the given {@link Choice}.
	 * See the specification of the single implementations for how the value is
	 * calculated.
	 *
	 * @param c
	 *                a possible choice.
	 * @return the value in this vote for the choice.
	 */
	int getValue(Choice c);

	/**
	 * Returns the {@link ElectionMethod} which this {@link Vote} is compatible to.
	 *
	 * @return the election method of this vote.
	 */
	ElectionMethod getMethod();
}
