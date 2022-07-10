package org.miniblex.svese.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA {@link Repository} for the {@link Person} class. This is use
 * to retrieve people's data from the persistent storage.
 */
public interface PersonRepository extends JpaRepository<Person, String> {
}
