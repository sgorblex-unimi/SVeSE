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
 * Every {@link VotingPaper} contains a {@link VoteDecider}, which lets or
 * denies the right to vote to a user through the method canVote.
 *
 * Immutable.
 */
public interface VoteDecider {
	/**
	 * Decides if the given {@link Person} can vote in the {@link VotingPaper} this
	 * {@link VoteDecider} belongs to. Returns false if the given Person is
	 * {@code null}.
	 *
	 * @param p
	 *                the person which may be able to vote.
	 * @return {@code true} if the person can vote, {@code false} otherwise.
	 */
	boolean canVote(Person p);
}
