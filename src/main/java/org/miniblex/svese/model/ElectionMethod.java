package org.miniblex.svese.model;

/**
 * Representation of the election method used in a {@link VotingPaper} and its
 * {@link Vote}s.
 */
public enum ElectionMethod {
	/**
	 * The elector chooses a single option.
	 */
	CATEGORICAL,

	/**
	 * The elector chooses an order of preference among the options.
	 */
	ORDINAL,

	/**
	 * The elector categorically chooses an option, which may then open a choice
	 * within that option. For example, for choosing names in a group. The second
	 * choice may follow any method.
	 */
	PREFERENCED,

	/**
	 * The elector categorically chooses between two options, tipically yes and no.
	 */
	REFERENDUM
}
