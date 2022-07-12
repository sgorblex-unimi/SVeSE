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
	private static int DEFAULT_AGE_THRESHOLD = 18;

	private final int ageThreshold;

	/**
	 * Constructs a new {@link AdultDecider} with 18 as age threshold.
	 */
	public AdultDecider() {
		this.ageThreshold = DEFAULT_AGE_THRESHOLD;
	}

	/**
	 * Constructs a new {@link AdultDecider} with the given age threshold.
	 * 
	 * @param threshold
	 *                the age threshold. Non negative.
	 * @throws IllegalArgumentException
	 *                 if the given age is negative.
	 */
	public AdultDecider(int threshold) {
		if (threshold < 0)
			throw new IllegalArgumentException("cannot use a negative age threshold");
		this.ageThreshold = threshold;
	}

	@Override
	public boolean canVote(Person p) {
		if (p == null)
			return false;
		return p.age() >= ageThreshold;
	}

	@Override
	public String toString() {
		return "AdultDecider[ageThreshold=" + ageThreshold + "]";
	}

}
