package org.miniblex.svese.model;

import java.time.LocalDate;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Representation of a real person, possibly an elector.
 *
 * Immutable.
 */
public class Person {
	private final String fname;
	private final String lname;
	private final char[] ssn; // TODO: substitute with a custom class (?)
	private final LocalDate birthDate;
	private final String birthPlace;

	/**
	 * Constructs a new {@link Person} with the given information.
	 * 
	 * @param fname
	 *                   first name.
	 * @param lname
	 *                   last name.
	 * @param ssn
	 *                   social security number.
	 * @param birthDate
	 *                   birth date.
	 * @param birthPlace
	 *                   birth place.
	 */
	public Person(String fname, String lname, char[] ssn, LocalDate birthDate, String birthPlace) {
		this.fname = fname;
		this.lname = lname;
		this.ssn = ssn;
		this.birthDate = birthDate;
		this.birthPlace = birthPlace;
	}

	/**
	 * Returns this {@link Person}'s age.
	 * 
	 * @return the age.
	 */
	public int age() {
		throw new NotImplementedException();
	}

	/**
	 * Returns the first name of this {@link Person}.
	 * 
	 * @return first name.
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * Returns the last name of this {@link Person}.
	 * 
	 * @return last name.
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * Returns the social security number of this {@link Person}.
	 * 
	 * @return social security number.
	 */
	public char[] getSsn() {
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

}
