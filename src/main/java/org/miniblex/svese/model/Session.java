package org.miniblex.svese.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of the voting session and its properties. The system can
 * handle only one voting session at a time, therefore a (slightly modified)
 * singleton pattern is used.
 */
// TODO: have a method (possibly a JPA repository) to calculate turnout and keep
// track of people who have voted.
public class Session {
	private static Session instance = null; // singleton implementation pointer
	private static Person admin;

	private SessionParameters params; // all session parameters

	// session state
	private boolean isRunning = false;
	private final Map<Person, Boolean> approval; // stores guarantors and their approval of session parameters

	/**
	 * Creates a new voting session. Should only be called by
	 * {@code initializeSession}.
	 *
	 * @param params
	 *                the session's parameters.
	 */
	private Session(SessionParameters params, List<Person> guarantors) {
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
	 * @throws SessionRunningException
	 *                 if you try to initialize the session while it's running.
	 */
	public static void initializeSession(SessionParameters params, List<Person> guarantors) throws SessionRunningException {
		if (instance != null && instance.isRunning())
			throw new SessionRunningException();
		instance = new Session(params, guarantors);
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
		if (p.equals(admin))
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
	 */
	public static void setAdmin(Person administrator) {
		if (getSession() != null)
			throw new IllegalStateException("cannot change admin once the session is initialized");
		admin = administrator;
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
		return "Session[isRunning=" + isRunning + ", approval=" + approvalToString() + ",\nparams=" + params + "]";
	}

}