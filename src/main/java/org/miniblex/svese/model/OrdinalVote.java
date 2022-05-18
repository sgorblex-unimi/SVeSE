package org.miniblex.svese.model;

import java.util.List;

/**
 * Implementation of {@link Vote} for an election with ordinal vote. In an
 * election with ordinal vote, the elector chooses an order of preference among
 * the possible choices.
 *
 * This implementation assigns zero score points for the last choice, one for
 * the second to the last, et cetera up to the most valuable to the elector, to
 * which size-1 points is assigned.
 */
public class OrdinalVote implements Vote {
	private List<Choice> order;

	/**
	 * Constructs a new OrdinalVote from the given ordered list of Choices.
	 * 
	 * @param order
	 *                ordered list of choices for this vote, from most to least
	 *                valuable.
	 */
	public OrdinalVote(List<Choice> order) {
		this.order = order;
	}

	/**
	 * Returns 0 for the last {@link Choice} of the list, 1 for the second to the
	 * last, etc., up to the size of the list minus 1
	 */
	@Override
	public int getValue(Choice c) {
		return order.size() - order.indexOf(c) - 1;
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.ORDINAL;
	}

}
