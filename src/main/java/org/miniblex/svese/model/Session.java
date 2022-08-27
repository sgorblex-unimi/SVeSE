// Copyright (C) 2021 Alessandro "Sgorblex" Clerici Lorenzini and Edoardo "Miniman" Della Rossa.
//
// This file is part of SVeSE.
//
// SVeSE is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// SVeSE is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with SVeSE.  If not, see <https://www.gnu.org/licenses/>.

package org.miniblex.svese.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.miniblex.svese.SVeSE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of the voting session and its properties. The system can
 * handle only one voting session at a time, therefore a (slightly modified)
 * singleton pattern is used.
 */
public class Session {
	private static Session instance = null; // singleton implementation pointer
	private static Person admin;

	private SessionParameters params; // all session parameters

	// session state
	private boolean isReady = false; // true when the administrator finished setting up
	private final Map<Person, Boolean> approval; // stores guarantors and their approval of session parameters

	private static final Logger logger = LoggerFactory.getLogger(Session.class);

	/**
	 * Creates a new voting session. Should only be called by
	 * {@code initializeSession}.
	 */
	private Session(SessionParameters params, List<Person> guarantors) {
		Objects.requireNonNull(params);
		Objects.requireNonNull(guarantors);
		if (guarantors.size() < 1 || guarantors.contains(admin))
			throw new IllegalArgumentException("invalid guarantor list");
		params.checkLegality();
		this.params = params.copy();
		this.approval = new HashMap<>(guarantors.size());
		for (Person g : guarantors)
			approval.put(g, false);
	}

	/**
	 * Returns the only existing Session instance.
	 *
	 * @return The current session, or null if the session has not been initialized
	 *         yet.
	 */
	public static Session getSession() {
		return instance;
	}

	/**
	 * Creates and initializes a new voting session. Overwrites any existing
	 * session.
	 *
	 * @param params
	 *                the session's parameters. Modification safe.
	 * @param guarantors
	 *                the {@link List} of session guarantors. The list must contain
	 *                at least one {@link Person} and must not contain the admin.
	 * @throws IllegalStateException
	 *                 if you try to initialize the session while it's running.
	 * @throws NullPointerException
	 *                 if {@code guarantors} or {@code params} or any of its
	 *                 components are {@code null}.
	 * @throws IllegalArgumentException
	 *                 if parameters or the guarantor list are invalid.
	 */
	public static void initializeSession(SessionParameters params, List<Person> guarantors) {
		if (isRunning())
			throw new IllegalStateException("cannot initialize a running session");
		instance = new Session(params, guarantors);
		logger.info("Initialized " + instance);
	}

	/**
	 * Returns a copy of the current Session parameters. The scope of this method is
	 * during initialization or to retrieve the session's static parameters (e.g.
	 * start and end time). To get a running session's voting papers in order to
	 * vote, use {@code getPapers}.
	 *
	 * @return current session parameters. Modification safe.
	 */
	public SessionParameters getCurrentParameters() {
		return params.copy();
	}

	/**
	 * Returns a {@link List} with the {@link VotingPaper}s of this running or
	 * concluded Session. The user may modify the single papers, but not the list.
	 * This is the correct method to retrieve {@link VotingPaper}s for voting and
	 * retrieving results. If you just need a copy of the paper list for reference
	 * (initializing phase), please use {@code getCurrentParameters.getPaperList}.
	 *
	 * @return current session papers.
	 */
	public List<VotingPaper> getPapers() {
		return List.copyOf(params.getPaperList());
	}

	/**
	 * Returns the list of guarantors currently added for the session.
	 *
	 * @return current session guarantors.
	 */
	public List<Person> getGuarantors() {
		return List.copyOf(approval.keySet());
	}

	/**
	 * Called by the admin when the session is ready. From this call on, the
	 * guarantors may approve the session by calling {@code approve}.
	 */
	public void setReady() {
		isReady = true;
		logger.info("Session set to ready");
	}

	/**
	 * Returns {@code true} if the {@link Session} is ready to be approved,
	 * {@code false} otherwise.
	 *
	 * @return ready state of the Session.
	 */
	public boolean isReady() {
		return isReady;
	}

	/**
	 * Returns {@code true} if the voting session is running, {@code false}
	 * otherwise.
	 *
	 * @return {@code true} if the session is running, {@code false} otherwise.
	 */
	public static boolean isRunning() {
		LocalDateTime now = LocalDateTime.now();
		return instance != null && instance.params.getStart().isBefore(now) && instance.params.getEnd().isAfter(now) && instance.checkApproval();
	}

	/**
	 * Manually closes the session. Resets the state of ready and approval.
	 */
	public void forceClose() {
		isReady = false;
		for (Person p : approval.keySet()) {
			approval.put(p, false);
		}
	}

	/**
	 * Returns {@code true} if the given guarantor has approved the initialized
	 * session, {@code false} otherwise.
	 *
	 * @param p
	 *                a guarantor of the session.
	 * @return {@code true} if the person has approved the session, {@code false}
	 *         otherwise.
	 * @throws IllegalArgumentException
	 *                 if p is not a guarantor of the session.
	 */
	public boolean hasApproved(Person p) {
		if (!approval.containsKey(p))
			throw new IllegalArgumentException("person " + p + " is not a guarantor of the session");
		return approval.get(p);
	}

	/**
	 * Sets the given guarantor to approve the session and its parameters.
	 *
	 * @param p
	 *                the guarantor.
	 * @throws IllegalStateException
	 *                 if the session is not ready to be approved.
	 * @throws IllegalArgumentException
	 *                 if the given {@link Person} is not a guarantor of the
	 *                 session.
	 */
	public void approve(Person p) {
		if (!isReady)
			throw new IllegalStateException("the session is not ready to be approved");
		if (!approval.containsKey(p))
			throw new IllegalArgumentException("person " + p + " is not a guarantor of the session");
		approval.put(p, true);
		logger.info("Session approved by " + p);
	}

	/**
	 * Returns {@code true} if all guarantors in the session approved new session
	 * parameters, {@code false} otherwise
	 *
	 * @return {@code true} if all guarantors approved new parameters, {@code false}
	 *         otherwise
	 */
	public boolean checkApproval() {
		for (Map.Entry<Person, Boolean> a : approval.entrySet()) {
			if (a.getValue() == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a {@link Collection} containing the roles of the given {@link Person}
	 * for the current Session.
	 *
	 * @param p
	 *                the person to inspect.
	 * @return the person's roles.
	 */
	public static Collection<Role> getRoles(Person p) {
		Collection<Role> res = new ArrayList<>();
		if (admin.equals(p))
			res.add(Role.ADMIN);
		if (instance != null) {
			if (instance.approval.containsKey(p))
				res.add(Role.GUARANTOR);
		}
		return res;
	}

	/**
	 * Sets the session administrator to the given {@link Person}.
	 *
	 * @param administrator
	 *                the admin.
	 * @throws IllegalStateException
	 *                 if trying to change admin when the session is initialized.
	 */
	public static void setAdmin(Person administrator) {
		if (getSession() != null)
			throw new IllegalStateException("cannot change admin once the session is initialized");
		admin = Objects.requireNonNull(administrator);
		logger.info("Session administrator set to " + administrator);
	}

	/**
	 * Returns the number of registered {@link Person}s that can vote according to
	 * the given {@link VoteDecider}.
	 *
	 * @param decider
	 *                the {@link VoteDecider} to use.
	 * @return number of people that can vote.
	 */
	public long howManyEligible(VoteDecider decider) {
		PersonRepository personRepo = SVeSE.getPersonRepo();
		List<Person> l = personRepo.findAll();
		long count = 0;
		for (Person p : l)
			if (decider.canVote(p))
				count++;
		return count;
	}

	/**
	 * Represents a voting {@link Session}'s parameters.
	 *
	 * The modification of mutable parameters affects the {@link SessionParameters}
	 * until it is assigned to a {@link Session}, which uses an immutable copy of
	 * it.
	 */
	public static class SessionParameters {
		private LocalDateTime start; // Start time of the voting session
		private LocalDateTime end; // End time of the voting session
		private final List<VotingPaper> papers; // Voting papers

		/**
		 * Constructs a new, blank {@link SessionParameters}.
		 */
		public SessionParameters() {
			papers = new ArrayList<>();
		}

		/**
		 * Sets this {@link SessionParameters}' start time to {@code start}.
		 *
		 * @param start
		 *                the new start time.
		 */
		public void setStart(LocalDateTime start) {
			this.start = start;
		}

		/**
		 * Sets this {@link SessionParameters}' end time to {@code end}.
		 *
		 * @param end
		 *                the new end time.
		 */
		public void setEnd(LocalDateTime end) {
			this.end = end;
		}

		/**
		 * Returns this {@link SessionParameters}' start time.
		 *
		 * @return the start time.
		 */
		public LocalDateTime getStart() {
			return start;
		}

		/**
		 * Returns this {@link SessionParameters}' end time.
		 *
		 * @return the end time.
		 */
		public LocalDateTime getEnd() {
			return end;
		}

		/**
		 * Adds the given {@link VotingPaper} to this {@link SessionParameters}' papers.
		 *
		 * @param p
		 *                a {@link VotingPaper}.
		 */
		public void addPaper(VotingPaper p) {
			papers.add(p.copy());
		}

		/**
		 * Returns the list of {@link VotingPaper} of the {@link Session} described by
		 * this {@link SessionParameters}.
		 *
		 * @return list of papers of the session. May be modified to add or remove
		 *         {@link VotingPaper}s.
		 */
		public List<VotingPaper> getPaperList() {
			return papers;
		}

		/**
		 * Used by copy
		 */
		private SessionParameters(LocalDateTime start, LocalDateTime end, List<VotingPaper> papers) {
			this.start = start;
			this.end = end;
			this.papers = papers;
		}

		/**
		 * Returns a copy of this {@link SessionParameters}. Any parameter with state
		 * has its state voided (e.g. VotingPapers do not contain votes).
		 *
		 * @return copy of this SessionParameters.
		 */
		public SessionParameters copy() {
			List<VotingPaper> papers = new ArrayList<>(this.papers.size());
			for (VotingPaper p : this.papers) {
				papers.add(p.copy());
			}
			return new SessionParameters(start, end, papers);
		}

		/**
		 * Throws IllegalArgumentException or NullPointerException if the parameters are
		 * not legal at the current state.
		 */
		private void checkLegality() {
			Objects.requireNonNull(start);
			Objects.requireNonNull(end);
			Objects.requireNonNull(papers);
			for (VotingPaper vp : papers)
				Objects.requireNonNull(vp);
			if (end.isBefore(start))
				throw new IllegalArgumentException("end is after start");
		}

		private String papersToString() {
			String res = "";
			for (VotingPaper p : papers) {
				res += p + "\n";
			}
			return res.trim();
		}

		@Override
		public String toString() {
			String res = "[";
			res += "start=" + start + ", ";
			res += "end=" + end + ", ";
			res += "papers=\n" + papersToString();
			return res + "]";
		}

	}

	private String approvalToString() {
		String res = "[\n";
		for (Map.Entry<Person, Boolean> e : approval.entrySet())
			res += e.getKey() + ": " + e.getValue() + "\n";
		return res.trim() + "]";
	}

	@Override
	public String toString() {
		return "Session[admin=" + admin + ", isReady=" + isReady + ", approval=" + approvalToString() + ",\nparams=" + params + "]";
	}

}
