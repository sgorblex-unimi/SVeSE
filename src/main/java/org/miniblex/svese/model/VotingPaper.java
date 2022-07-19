package org.miniblex.svese.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
 * TODO: result caching system (persistent?)
 */
public class VotingPaper implements Iterable<Choice> {
	private final String title;
	private final Map<Choice, VotingPaper> choices; // the keys are the available choices, the values are the optional suboptions
							// papers
	private final List<Vote> votes = new LinkedList<>();
	private final ElectionMethod method;
	private final VoteDecider decider;
	private final Set<Person> hasVoted = new HashSet<>();

	/**
	 * Constructs a new {@link VotingPaper} with the given parameters.
	 *
	 * @param title
	 *                the title of this VotingPaper.
	 * @param choices
	 *                choices for the election represented by this paper. In case of
	 *                choices with suboptions (PREFERENCED method), these must be
	 *                mapped to a {@link VotingPaper} containing said suboptions
	 *                through this map, else the choice must be mapped to
	 *                {@code null}.
	 * @param method
	 *                the method used by the election of this paper.
	 * @param decider
	 *                decides if a person can vote for this VotingPaper. Set it to
	 *                {@code null} if anyone can vote.
	 * @throws IllegalArgumentException
	 *                 if trying to add subpapers in a non PREFERENCED paper.
	 */
	public VotingPaper(String title, Map<Choice, VotingPaper> choices, ElectionMethod method, VoteDecider decider) {
		if (method != ElectionMethod.PREFERENCED)
			for (VotingPaper sub : choices.values())
				if (sub != null)
					throw new IllegalArgumentException("cannot add subchoices for an election method different from " + ElectionMethod.PREFERENCED);
		this.title = Objects.requireNonNull(title);
		this.choices = copyChoiceMap(Objects.requireNonNull(choices));
		this.method = Objects.requireNonNull(method);
		this.decider = decider;
	}

	/**
	 * Returns the title of this {@link VotingPaper}.
	 *
	 * @return the title.
	 */
	public String getTitle() {
		return title;
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
		return decider == null ? p != null : decider.canVote(p);
	}

	/**
	 * Returns {@code true} if the given {@link Person} has voted in this
	 * {@link VotingPaper}, {@code false} otherwise.
	 *
	 * @param p
	 *                the person which may have voted.
	 * @return {@code true} if the person has voted, {@code false} otherwise.
	 */
	public boolean hasVoted(Person p) {
		return hasVoted.contains(p);
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
	 * Adds the given {@link Vote} by the given {@link Person} to the election of
	 * this {@link VotingPaper}. This is the only time a Person and their vote are
	 * associated.
	 *
	 * @param v
	 *                the vote. Its method must be the same of this VotingPaper.
	 * @param p
	 *                the person.
	 * @throws IllegalStateException
	 *                 if the election is closed.
	 * @throws IllegalArgumentException
	 *                 if the method of the vote is not the same of this
	 *                 VotingPaper, if the given person cannot vote for this
	 *                 VotingPaper or if they have already voted.
	 */
	public void addVote(Vote v, Person p) {
		if (!Session.isRunning())
			throw new IllegalStateException("cannot add a vote to a closed election");
		Objects.requireNonNull(v);
		if (v.getMethod() != this.getMethod())
			throw new IllegalArgumentException("vote not compatible with the election method of this paper");
		if (!decider.canVote(p))
			throw new IllegalArgumentException("the given person cannot vote for this paper");
		if (hasVoted(p))
			throw new IllegalArgumentException("the given person has already voted for this paper");
		votes.add(v);
		hasVoted.add(p);
	}

	/**
	 * Returns the {@link Results} for the election represented by this VotingPaper.
	 *
	 * @return the results.
	 * @throws IllegalStateException
	 *                 if the election of this paper is still running.
	 */
	public Results getResults() {
		if (Session.isRunning())
			throw new IllegalStateException("election is still running");
		return new Results();
	}

	/**
	 * Represents this paper's election results.
	 *
	 * Immutable.
	 *
	 * Results' iterator iterates in descending order of score.
	 */
	public class Results implements Iterable<Results.Result> {
		private final List<Result> allResults;
		private final long totalVotes;
		private final double turnout; // turnout in [0,1]

		private Results() {
			List<Result> res = new ArrayList<>();
			for (Choice c : getChoices()) {
				long score = 0;
				for (Vote v : votes) {
					score += v.getValue(c);
				}
				res.add(new Result(c, score));
			}
			res.sort(new Comparator<>() {
				@Override
				public int compare(Result res1, Result res2) {
					return Math.toIntExact(res1.score - res2.score);
				}
			});
			this.totalVotes = hasVoted.size();
			Session s = Session.getSession();
			if (s == null)
				throw new IllegalStateException("cannot get the results: there is no session");
			double eligible = s.howManyEligible(decider);
			this.turnout = totalVotes / eligible;
			this.allResults = res;
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
		 * @return the score for the {@link Choice}.
		 * @throws IllegalArgumentException
		 *                 if the given choice was not part of the paper.
		 */
		public long getScore(Choice c) {
			Objects.requireNonNull(c);
			for (Result r : allResults) {
				if (r.getChoice().equals(c))
					return r.getScore();
			}
			throw new IllegalArgumentException("the given choice was not present in this paper");
		}

		/**
		 * Returns the relative score of the given {@link Choice} as a double between 0
		 * and 1.
		 *
		 * @param c
		 *                the choice.
		 * @return the relative score for the {@link Choice}.
		 * @throws IllegalArgumentException
		 *                 if the given choice was not part of the paper.
		 */
		public double getRelativeScore(Choice c) {
			Objects.requireNonNull(c);
			for (Result r : allResults) {
				if (r.getChoice().equals(c))
					return r.getRelativeScore();
			}
			throw new IllegalArgumentException("the given choice was not present in this paper");
		}

		@Override
		public Iterator<Result> iterator() {
			return allResults.iterator();
		}

		/**
		 * Represents the resulting vote for a single choice of a paper.
		 *
		 * Immutable.
		 */
		public class Result {
			private final Choice c;
			private final long score;

			private Result(Choice c, long score) {
				this.c = Objects.requireNonNull(c);
				this.score = score;
			}

			/**
			 * Return this Result's {@link Choice}.
			 *
			 * @return the choice.
			 */
			public Choice getChoice() {
				return c;
			}

			/**
			 * Return this Result's score.
			 *
			 * @return the score.
			 */
			public long getScore() {
				return score;
			}

			/**
			 * Return this Result's relative score as a double between 0 and 1.
			 *
			 *
			 * @return the relative score.
			 */
			public double getRelativeScore() {
				int numChoices = getChoices().size();
				int scorePerVote = getMethod() == ElectionMethod.ORDINAL ? (numChoices - 1) * numChoices / 2 : 1;
				return (double) score / (totalVotes * scorePerVote);
			}

			@Override
			public String toString() {
				String prettyRelative = Double.toString(getRelativeScore() * 100);
				prettyRelative = prettyRelative.length() < 4 ? prettyRelative : prettyRelative.substring(0, 4);
				return "[\"" + getChoice().getName() + "\": " + getScore() + " (" + prettyRelative + "%)]";
			}

		}

		@Override
		public String toString() {
			String res = "Results[paper=\"" + getTitle() + "\", totalVotes=" + getTotalVotes() + ", turnout=" + getTurnout() + ", results=\n";
			for (Result r : this) {
				res += r + "\n";
			}
			return res.trim() + "]";
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
		return new VotingPaper(title, copyChoiceMap(choices), method, decider);
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

	/**
	 * Creates a copy of the given Choice map. Also checks no Choices are
	 * {@code null}.
	 */
	private static Map<Choice, VotingPaper> copyChoiceMap(Map<Choice, VotingPaper> choices) {
		Map<Choice, VotingPaper> res = new HashMap<>(choices.size());
		for (Map.Entry<Choice, VotingPaper> e : choices.entrySet())
			res.put(Objects.requireNonNull(e.getKey()), e.getValue() == null ? null : e.getValue().copy());
		return res;
	}

	private String choicesToString() {
		String res = "[\n";
		for (Map.Entry<Choice, VotingPaper> e : choices.entrySet()) {
			res += e.getKey();
			VotingPaper sub = e.getValue();
			if (sub != null)
				res += " --> " + sub;
			res += "\n";
		}
		return res.trim() + "]";
	}

	@Override
	public String toString() {
		return "VotingPaper[\"" + title + "\", method=" + method + ", decider=" + decider + ", voteNumber=" + hasVoted.size() + ", choices=" + choicesToString() + "]";
	}

}
