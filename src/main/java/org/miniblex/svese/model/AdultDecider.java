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
