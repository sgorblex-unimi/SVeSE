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
 * Implementation of {@link Vote} for a referendum. In a referendum, only yes or
 * no is chosen, and the relative score is incremented.
 */
public class ReferendumVote extends CategoricalVote {

	/**
	 * Constructs a new ReferendumVote with the given {@link Choice}.
	 *
	 * @param chosen
	 *                the choice (assumed to be yes or no).
	 */
	public ReferendumVote(Choice chosen) {
		super(chosen);
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.REFERENDUM;
	}

}
