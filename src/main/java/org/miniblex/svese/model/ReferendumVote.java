package org.miniblex.svese.model;

/**
 * Implementation of {@link Vote} for a referendum. In a referendum, only yes or
 * no is chosen, and the relative score is incremented.
 */
public class ReferendumVote extends CategoricalVote {

	/**
	 * Constructs a new ReferendumVote with the given {@link Choice}.
	 * 
	 * @param chosen
	 *                the choice (assumed to be yes or no).
	 */
	public ReferendumVote(Choice chosen) {
		super(chosen);
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.REFERENDUM;
	}

}
