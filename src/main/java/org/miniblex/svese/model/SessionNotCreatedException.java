package org.miniblex.svese.model;

/**
 * Thrown when an operation requires {@link Session} which has not been created
 * yet.
 */
public class SessionNotCreatedException extends Exception {
	/**
	 * Constructs a SessionNotCreatedException with no detail message.
	 */
	public SessionNotCreatedException() {
		super();
	}

	/**
	 * Constructs a new SessionNotCreatedException with the given detail message.
	 * 
	 * @param msg
	 *                the detail message.
	 */
	public SessionNotCreatedException(String msg) {
		super(msg);
	}
}
