package org.miniblex.svese.model;

/**
 * Roles a Person can have in the SVeSE system.
 */
public enum Role {
	/**
	 * The voting session administrator. Builds the voting papers, sets the session
	 * parameters, starts the session.
	 */
	ADMIN,

	/**
	 * Guarantors of the voting session. The administrator needs their approvation
	 * in order to start the session.
	 */
	GUARANTOR
}
