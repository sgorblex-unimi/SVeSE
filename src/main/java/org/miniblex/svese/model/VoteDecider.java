package org.miniblex.svese.model;

/**
 * Every {@link VotingPaper} contains a {@link VoteDecider}, which lets or
 * denies the right to vote to a user through the method canVote.
 *
 * Immutable.
 */
public interface VoteDecider {
	/**
	 * Decides if the given {@link Person} can vote in the {@link VotingPaper} this
	 * {@link VoteDecider} belongs to.
	 * 
	 * @param p
	 *                the person which may be able to vote.
	 * @return {@code true} if the person can vote, {@code false} otherwise
	 */
	boolean canVote(Person p);
}
