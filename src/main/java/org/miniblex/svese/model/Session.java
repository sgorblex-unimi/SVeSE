package org.miniblex.svese.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of the voting session and its properties. The system can
 * handle only one voting session at a time, therefore a (slightly modified)
 * singleton pattern is used.
 */
public class Session {
	private static Session instance = null; // singleton implementation pointer

	private SessionParameters params; // all session parameters

	// session state
	private boolean isRunning = false;
	private final Map<Guarantor, Boolean> approval = new HashMap<>(); // stores guarantors and their approval of session parameters
	// TODO: timer field which starts at session start

	/**
	 * Creates a new voting session. Should only be called by
	 * {@code initializeSession}.
	 *
	 * @param params
	 *                the session's parameters.
	 */
	private Session(SessionParameters params) {
		this.params = params.copy();
		// TODO: complete implementation (add negative approval for all guarantors in
		// the list)
	}

	/**
	 * Returns the only existing Session instance.
	 *
	 * @return The current initialized session.
	 * @throws SessionNotCreatedException
	 *                 when the session has not yet been created.
	 */
	public static Session getSession() throws SessionNotCreatedException {
		if (instance == null) {
			throw new SessionNotCreatedException();
		}
		return instance;
	}

	/**
	 * Creates and initializes a new voting session. Overwrites any existing
	 * session.
	 *
	 * @param params
	 *                the session's parameters. Modification safe.
	 * @throws SessionRunningException
	 *                 if you try to initialize the session while it's running.
	 */
	public static void initializeSession(SessionParameters params) throws SessionRunningException {
		if (instance != null && instance.isRunning())
			throw new SessionRunningException();
		instance = new Session(params);
	}

	/**
	 * Returns a copy of the current Session parameters.
	 * 
	 * @return current session parameters. Modification safe.
	 */
	public SessionParameters getCurrentParameters() {
		return params.copy();
	}

	/**
	 * Modifies this {@link Session}'s parameters according to the given ones,
	 * voiding any guarantors' approval.
	 * 
	 * @param params
	 *                the new session parameters. Likely, a modified version of the
	 *                one returned by {@code getCurrentParameters}. Modification
	 *                safe.
	 * @throws SessionRunningException
	 *                 If it is made an attempt to modify the session while it's
	 *                 running.
	 */
	public void editParameters(SessionParameters params) throws SessionRunningException {
		if (this.isRunning())
			throw new SessionRunningException();
		this.params = params.copy();
	}

	/**
	 * Returns {@code true} if the voting session is running, {@code false}
	 * otherwise.
	 *
	 * @return {@code true} if the session is running, {@code false} otherwise.
	 */
	private boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * Returns {@code true} if all guarantors in the session approved new session
	 * parameters, {@code false} otherwise
	 *
	 * @return {@code true} if all guarantors approved new parameters, {@code false}
	 *         otherwise
	 */
	public boolean checkApproval() {
		for (Map.Entry<Guarantor, Boolean> a : approval.entrySet()) {
			if (a.getValue() == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Represents a voting {@link Session}'s parameters.
	 *
	 * The modification of mutable parameters affects the {@link SessionParameters}
	 * until it is assigned to a {@link Session}, which uses an immutable copy of
	 * it.
	 */
	public class SessionParameters implements Cloneable {
		private LocalDateTime start; // Start time of the voting session
		private LocalDateTime end; // End time of the voting session
		private final List<VotingPaper> papers; // Voting papers
		// TODO: other parameters

		// TODO: appropriate constructor
		public SessionParameters() {
			papers = new ArrayList<>();
		}

		/**
		 * Used by clone
		 */
		private SessionParameters(LocalDateTime start, LocalDateTime end, List<VotingPaper> papers) {
			this.start = start;
			this.end = end;
			this.papers = papers;
		}

		// TODO: getters, setters

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
		 * Returns the list of {@link VotingPaper} of the {@link Session} described by
		 * this {@link SessionParameters}.
		 * 
		 * @return list of papers of the session. May be modified to add or remove
		 *         {@link VotingPaper}s.
		 */
		public List<VotingPaper> getPaperList() {
			return papers;
		}
	}
}
