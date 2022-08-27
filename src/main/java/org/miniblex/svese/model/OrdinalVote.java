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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link Vote} for an election with ordinal vote. In an
 * election with ordinal vote, the elector chooses an order of preference among
 * the possible choices.
 *
 * This implementation assigns zero score points for the last choice, one for
 * the second to the last, et cetera up to the most valuable to the elector, to
 * which size-1 points is assigned.
 */
public class OrdinalVote implements Vote {
	private List<Choice> order;

	/**
	 * Constructs a new OrdinalVote from the given ordered list of {@link Choice}s.
	 *
	 * @param order
	 *                ordered list of choices for this vote, from most to least
	 *                valuable.
	 * @throws NullPointerException
	 *                 if either {@code order} is {@code null} or any of its
	 *                 elements is.
	 * @throws IllegalArgumentException
	 *                 if a Choice occurs twice in {@code order}.
	 */
	public OrdinalVote(List<Choice> order) {
		Set<Choice> checkDups = new HashSet<>();
		for (Choice c : Objects.requireNonNull(order))
			if (!checkDups.add(Objects.requireNonNull(c)))
				throw new IllegalArgumentException("duplicate Choice in list");
		this.order = order;
	}

	/**
	 * Returns 0 for the last {@link Choice} of the list, 1 for the second to the
	 * last, etc., up to the size of the list minus 1
	 */
	@Override
	public int getValue(Choice c) {
		int idx = order.indexOf(c);
		if (idx < 0)
			return 0;
		return order.size() - idx - 1;
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.ORDINAL;
	}

}
