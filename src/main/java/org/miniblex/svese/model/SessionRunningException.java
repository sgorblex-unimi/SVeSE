package org.miniblex.svese.model;

/**
 * Thrown when there was an attempt to modify the Session while running.
 */
public class SessionRunningException extends RuntimeException {
	/**
	 * Constructs a SessionRunningException with no detail message.
	 */
	public SessionRunningException() {
		super();
	}

	/**
	 * Constructs a new SessionRunningException with the given detail message.
	 * 
	 * @param msg
	 *                the detail message.
	 */
	public SessionRunningException(String msg) {
		super(msg);
	}
}
