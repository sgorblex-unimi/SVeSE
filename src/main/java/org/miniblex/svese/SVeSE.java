package org.miniblex.svese;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.PersonRepository;
import org.miniblex.svese.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Main class of the SVeSE system and application.
 */
@SpringBootApplication
public class SVeSE {
	/** System name. */
	public static String SYS_NAME = "SVeSE";
	/** System description. */
	public static String SYS_DESCRIPTION = "Sistema di Voto e Scrutinio Elettronico";

	private static Path adminFilePath = Path.of("admin.txt");

	private static SVeSE context;

	@Autowired
	private PersonRepository personRepo;

	@EventListener(ApplicationReadyEvent.class)
	private void postInit() {
		context = this;
		try {
			String adminSsn = Files.readString(adminFilePath).trim();
			Optional<Person> admin = personRepo.findById(adminSsn);
			if (admin.isEmpty()) {
				System.err.println("WARNING: the admin specified in " + adminFilePath + " is not a valid person!");
			} else {
				System.err.println("admin ssn is " + adminSsn);
				Session.setAdmin(admin.get());
			}
		} catch (IOException e) {
			System.err.println("WARNING: proceeding without setting admin");
		}
	}

	/**
	 * Starts the application.
	 *
	 * @param args
	 *                ignored.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SVeSE.class, args);
	}

	/**
	 * Returns a valid {@link PersonRepository} if the application has been
	 * successfully initialized.
	 *
	 * @return a {@link PersonRepository}.
	 */
	public static PersonRepository getPersonRepo() {
		return context == null ? null : context.personRepo;
	}

	/**
	 * Constructs a new SVeSE. Should only be called by Spring.
	 */
	SVeSE() {
	}

}
