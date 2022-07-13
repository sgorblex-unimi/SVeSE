package org.miniblex.svese.model;

import java.util.Objects;

/**
 * Implementation of {@link Vote} for a categorical election. In a categorical
 * election, only one choice is chosen, which means (only) its score is
 * incremented.
 *
 * TODO[?]:possibly add a method to retrieve the choice
 */
public class CategoricalVote implements Vote {
	private final Choice chosen;

	/**
	 * Constructs a new CategoricalVote with the given choice.
	 *
	 * @param chosen
	 *                the selected choice.
	 */
	public CategoricalVote(Choice chosen) {
		this.chosen = Objects.requireNonNull(chosen);
	}

	/**
	 * Returns 1 for the selected {@link Choice}, else 0.
	 */
	@Override
	public int getValue(Choice c) {
		return this.chosen.equals(c) ? 1 : 0;
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.CATEGORICAL;
	}

}
