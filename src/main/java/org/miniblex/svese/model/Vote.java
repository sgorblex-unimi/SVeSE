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
 * The interface for a Vote. A vote has no reference to an election, so it is
 * left to the user to make the votes relevant to the election choices.
 *
 * Immutable.
 */
public interface Vote {
	/**
	 * Returns the score value in this {@link Vote} for the given {@link Choice}.
	 * See the specification of the single implementations for how the value is
	 * calculated.
	 *
	 * @param c
	 *                a possible choice.
	 * @return the value in this vote for the choice.
	 */
	int getValue(Choice c);

	/**
	 * Returns the {@link ElectionMethod} which this {@link Vote} is compatible to.
	 *
	 * @return the election method of this vote.
	 */
	ElectionMethod getMethod();
}
