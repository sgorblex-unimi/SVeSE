package org.miniblex.svese.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Implementation of a voting paper, which stores possible choices and, when
 * close, result of the single election.
 *
 * Some of the {@link Choice}s of the VotingPaper may generate a secondary
 * selection among suboptions. These suboptions reside in another VotingPaper
 * which is mapped to the generating choice via the {@link Map} given in the
 * constructor. The users of this class may then retrieve the
 * {@link VotingPaper} linked to a choice via the method {@code getSubPaper}
 * when the elector chooses the generating choice. Choices that do not generate
 * a subpaper are mapped to null in both the constructor and as the returned
 * value of getSubPaper.
 *
 * Immutable when closed. Specifically, the state of mutability of an open
 * {@code VotingPaper} resides in its votes, as the {@link Choice}s,
 * {@link ElectionMethod} and {@link VoteDecider} cannot mute. The method
 * {@code copy} returns a copy of this VotingPaper with the mutable state
 * removed.
 *
 * TODO: result caching system
 */
public class VotingPaper implements Iterable<Choice> {
	private final Map<Choice, VotingPaper> choices; // the keys are the available choices, the values are the optional suboptions
							// papers
	private final List<Vote> votes = new LinkedList<>();
	private boolean isRunning = true;
	private final ElectionMethod method;
	private final VoteDecider decider;

	/**
	 * Constructs a new {@link VotingPaper} with the given parameters.
	 * 
	 * @param choices
	 *                choices for the election represented by this paper. In case of
	 *                choices with suboptions, these must be mapped to a
	 *                {@link VotingPaper} containing said suboptions through this
	 *                map, else the choice must be mapped to {@code null}.
	 * @param method
	 *                the method used by the election of this paper.
	 * @param decider
	 *                decides if a person can vote for this VotingPaper.
	 */
	public VotingPaper(Map<Choice, VotingPaper> choices, ElectionMethod method, VoteDecider decider) {
		this.choices = copyChoiceMap(choices);
		this.method = method;
		this.decider = decider;
	}

	/**
	 * Returns {@code true} if the election represented by this {@link VotingPaper}
	 * is running, {@code false} otherwise.
	 * 
	 * @return {@code true} if the election is running, {@code false} otherwise.
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Returns {@code true} if the given {@link Person} can vote in this
	 * {@link VotingPaper}, {@code false} otherwise.
	 * 
	 * @param p
	 *                the person which may be able to vote.
	 * @return {@code true} if the person can vote, {@code false} otherwise.
	 */
	public boolean canVote(Person p) {
		return decider.canVote(p);
	}

	/**
	 * Closes the election of this {@link VotingPaper}.
	 *
	 * After calling this method it won't any more be possible to add votes and it
	 * will be possible to calculate the results.
	 */
	public void close() {
		isRunning = false;
	}

	/**
	 * Returns the election method for this {@link VotingPaper}.
	 * 
	 * @return the election method.
	 */
	public ElectionMethod getMethod() {
		return method;
	}

	/**
	 * Returns the {@link VotingPaper} for the suboptions of the given
	 * {@link Choice}, or null if the choice doesn't include any. This is the
	 * correct way to retrieve the {@code VotingPaper}s for the suboptions of the
	 * single {@code Choice}s in order to then modify them, as they are not copies.
	 *
	 * @param c
	 *                the choice to query the subpaper of.
	 * @return the {@link VotingPaper} for the suboptions of the given choice.
	 */
	public VotingPaper getSubPaper(Choice c) {
		return choices.get(c);
	}

	/**
	 * Adds the given {@link Vote} to the election of this {@link VotingPaper}.
	 * 
	 * @param v
	 *                the vote.
	 */
	public void addVote(Vote v) {
		if (!isRunning())
			throw new IllegalStateException("cannot add a vote to a closed election");
		if (v.getMethod() != this.getMethod())
			throw new IllegalArgumentException("vote not compatible with the election method of this paper");
		votes.add(v);
	}

	/**
	 * Returns the {@link Results} for the election represented by this VotingPaper.
	 *
	 * @return the results.
	 */
	public Results getResults() {
		SortedSet<Results.Result> res = new TreeSet<>(); // TODO: TreeSet comparator for Result
		for (Choice c : choices.keySet()) {
			long score = 0;
			for (Vote v : votes) {
				score += v.getValue(c);
			}
			res.add(new Results.Result(c, score));
		}
		long totalVotes = votes.size();
		double turnout = 42; // TODO: method to establish how many were eligible for voting
		return new Results(res, totalVotes, turnout);
	}

	/**
	 * Represents this paper's election results.
	 *
	 * Immutable.
	 */
	public class Results {
		private final SortedSet<Result> allResults;
		private final long totalVotes;
		private final double turnout; // turnout in [0,1]

		private Results(SortedSet<Result> allResults, long totalVotes, double turnout) {
			this.allResults = allResults;
			this.totalVotes = totalVotes;
			this.turnout = turnout;
		}

		/**
		 * Returns the turnout of the election represented by the containing
		 * {@link VotingPaper} as a double between 0 and 1.
		 * 
		 * @return election turnout.
		 */
		public double getTurnout() {
			return turnout;
		}

		/**
		 * Returns the total number of votes for the election represented by the
		 * containing {@link VotingPaper}.
		 * 
		 * @return number of votes for the election.
		 */
		public long getTotalVotes() {
			return totalVotes;
		}

		/**
		 * Returns the score of the given {@link Choice} for the election represented by
		 * the containing {@link VotingPaper}.
		 * 
		 * @param c
		 *                the choice.
		 * @return the score for the choice.
		 */
		public long getScore(Choice c) {
			throw new NotImplementedException();
		}

		/**
		 * Returns the relative score of the given {@link Choice} as a double between 0
		 * and 1.
		 * 
		 * @param c
		 *                the choice.
		 * @return the relative score for the {@link Choice}.
		 */
		public double getRelativeScore(Choice c) {
			throw new NotImplementedException();
		}

		/**
		 * Represents the resulting vote for a single choice of a paper.
		 *
		 * Immutable.
		 */
		private static class Result {
			private final Choice c;
			private final long score;

			private Result(Choice c, long score) {
				this.c = c;
				this.score = score;
			}
		}

	}

	/**
	 * Returns a copy of this {@link VotingPaper}, as never utilized in a voting
	 * session.
	 * 
	 * @return a clone of this {@link VotingPaper}, omitting the status of the
	 *         original (votes, running state).
	 */
	public VotingPaper copy() {
		return new VotingPaper(copyChoiceMap(choices), method, decider);
	}

	/**
	 * Returns a {@link Set} containing the {@link Choice}s for this
	 * {@link VotingPaper}.
	 *
	 * @return set of choices.
	 */
	public Set<Choice> getChoices() {
		return choices.keySet();
	}

	@Override
	public Iterator<Choice> iterator() {
		return getChoices().iterator();
	}

	private static Map<Choice, VotingPaper> copyChoiceMap(Map<Choice, VotingPaper> choices) {
		Map<Choice, VotingPaper> res = new HashMap<>(choices.size());
		for (Map.Entry<Choice, VotingPaper> e : choices.entrySet())
			res.put(e.getKey(), e.getValue() == null ? null : e.getValue().copy());
		return res;
	}

}
