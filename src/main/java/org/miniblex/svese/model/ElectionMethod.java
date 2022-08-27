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
 * Representation of the election method used in a {@link VotingPaper} and its
 * {@link Vote}s.
 */
public enum ElectionMethod {
	/**
	 * The elector chooses a single option.
	 */
	CATEGORICAL,

	/**
	 * The elector chooses an order of preference among the options.
	 */
	ORDINAL,

	/**
	 * The elector categorically chooses an option, which may then open a choice
	 * within that option. For example, for choosing names in a group. The second
	 * choice may follow any method.
	 */
	PREFERENCED,

	/**
	 * The elector categorically chooses between two options, tipically yes and no.
	 */
	REFERENDUM
}
