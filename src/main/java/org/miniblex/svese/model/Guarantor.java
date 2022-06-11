package org.miniblex.svese.model;

/**
 * Representation of one of the guarantors of a {@link Session}.
 */
// TODO: this class will be actually implemented once the (roled) login method
// is fully operational.
public class Guarantor {
	public final String name;

	public Guarantor(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Guarantor[" + name + "]";
	}

}
