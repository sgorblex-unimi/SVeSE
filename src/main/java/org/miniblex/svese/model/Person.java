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

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Representation of a real person, possibly an elector.
 *
 * Immutable.
 */
@Entity
public class Person {
	@Id
	private String ssn;

	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private String birthPlace;

	private boolean enabled;
	private String pwHash;

	/**
	 * Returns this {@link Person}'s age.
	 *
	 * @return the age.
	 */
	public int age() {
		return Period.between(birthDate, LocalDate.now()).getYears();
	}

	/**
	 * Returns the first name of this {@link Person}.
	 *
	 * @return first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the last name of this {@link Person}.
	 *
	 * @return last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the social security number of this {@link Person}.
	 *
	 * @return social security number.
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * Returns the birth date of this {@link Person}.
	 *
	 * @return birth date.
	 */
	public LocalDate getBirthDate() {
		return birthDate;
	}

	/**
	 * Returns the birth place of this {@link Person}.
	 *
	 * @return birth place.
	 */
	public String getBirthPlace() {
		return birthPlace;
	}

	/**
	 * Returns true if the SVeSE user linked to this Person is enabled, false
	 * otherwise.
	 *
	 * @return true if this Person is enabled, false otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Returns the password hash of the SVeSE user linked to this Person.
	 *
	 * @return the password hash for this person.
	 */
	public String getPwHash() {
		return pwHash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
		return result;
	}

	/**
	 * Two {@link Person}s are defined to be equals if they have the same ssn.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Person))
			return false;
		return this.ssn.equals(((Person) obj).ssn);
	}

	/**
	 * Constructs a new Person. Should only be called by Spring Data JPA for
	 * instantiating from the database.
	 */
	private Person() {
	}

	@Override
	public String toString() {
		return "Person[\"" + firstName + " " + lastName + "\", ssn=" + ssn + "]";
	}

}
