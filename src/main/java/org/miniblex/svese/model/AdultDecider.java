package org.miniblex.svese.model;

/**
 * Implementation of {@link VoteDecider} which lets only adults vote. An adult
 * is defined by a {@link Person} with an age of at least a certain threshold.
 * This threshold is 18 if using the empty constructor, or the given number if
 * using the parameterized constructor.
 *
 * Immutable.
 */
public class AdultDecider implements VoteDecider {
	private final int ageThreshold;

	/**
	 * Constructs a new {@link AdultDecider} with 18 as age threshold.
	 */
	public AdultDecider() {
		this.ageThreshold = 18;
	}

	/**
	 * Constructs a new {@link AdultDecider} with the given age threshold.
	 * 
	 * @param threshold
	 *                the age threshold.
	 */
	public AdultDecider(int threshold) {
		this.ageThreshold = threshold;
	}

	@Override
	public boolean canVote(Person p) {
		return p.age() >= ageThreshold;
	}

}
