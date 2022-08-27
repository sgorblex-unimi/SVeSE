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

import java.util.Objects;

/**
 * Implementation of {@link Vote} for a categorical election. In a categorical
 * election, only one choice is chosen, which means (only) its score is
 * incremented.
 *
 * TODO[?]:possibly add a method to retrieve the choice
 */
public class CategoricalVote implements Vote {
	private final Choice chosen;

	/**
	 * Constructs a new CategoricalVote with the given choice.
	 *
	 * @param chosen
	 *                the selected choice.
	 */
	public CategoricalVote(Choice chosen) {
		this.chosen = Objects.requireNonNull(chosen);
	}

	/**
	 * Returns 1 for the selected {@link Choice}, else 0.
	 */
	@Override
	public int getValue(Choice c) {
		return this.chosen.equals(c) ? 1 : 0;
	}

	@Override
	public ElectionMethod getMethod() {
		return ElectionMethod.CATEGORICAL;
	}

}
