package org.miniblex.svese.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA {@link org.springframework.data.repository.Repository} for
 * the {@link Person} class. This is used to retrieve people's data from the
 * persistent storage.
 */
public interface PersonRepository extends JpaRepository<Person, String> {
}
