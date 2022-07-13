package org.miniblex.svese;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.miniblex.svese.model.AdultDecider;
import org.miniblex.svese.model.CategoricalVote;
import org.miniblex.svese.model.Choice;
import org.miniblex.svese.model.ElectionMethod;
import org.miniblex.svese.model.OrdinalVote;
import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.PersonRepository;
import org.miniblex.svese.model.PreferencedVote;
import org.miniblex.svese.model.ReferendumVote;
import org.miniblex.svese.model.Session;
import org.miniblex.svese.model.Vote;
import org.miniblex.svese.model.VotingPaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestModel {
	private static List<String> people = List.of("DAD52", "ALL15", "ATU12", "JVN55");
	private static String admStr = people.get(0);
	private static List<String> guarStr = List.of(people.get(1), people.get(2));

	@Autowired
	private PersonRepository personRepo;

	private Person findPerson(String ssn) {
		Optional<Person> g = personRepo.findById(ssn);
		if (!g.isEmpty())
			return g.get();
		else
			throw new IllegalArgumentException("couldn't find person with SSN " + ssn);
	}

	private List<Person> getEveryone() {
		List<Person> res = new ArrayList<>();
		for (String g : people) {
			res.add(findPerson(g));
		}
		return res;
	}

	private Person getAdmin() {
		return findPerson(admStr);
	}

	private List<Person> getGuarantors() {
		List<Person> res = new ArrayList<>();
		for (String g : guarStr) {
			res.add(findPerson(g));
		}
		return res;
	}

	private Choice randomChoice(VotingPaper paper) {
		return Utils.randomElement(paper.getChoices());
	}

	private List<Choice> randomChoiceOrder(VotingPaper paper) {
		Set<Choice> s = new HashSet<>(paper.getChoices());
		int nChoices = s.size();
		List<Choice> res = new ArrayList<>(nChoices);
		for (int i = 0; i < nChoices; i++) {
			Choice c = Utils.randomElement(s);
			s.remove(c);
			res.add(c);
		}
		return res;
	}

	private void voteRandom(VotingPaper paper, Person p) {
		Vote v;
		switch (paper.getMethod()) {
		case PREFERENCED:
			Choice c = randomChoice(paper);
			v = new PreferencedVote(c);
			VotingPaper sub = paper.getSubPaper(c);
			if (sub != null)
				voteRandom(sub, p);
			break;
		case REFERENDUM:
			v = new ReferendumVote(randomChoice(paper));
			break;
		case ORDINAL:
			v = new OrdinalVote(randomChoiceOrder(paper));
			break;
		case CATEGORICAL:
			v = new CategoricalVote(randomChoice(paper));
			break;
		default:
			throw new AssertionError();
		}
		paper.addVote(v, p);
	}

	private void voteInExampleSession() {
		Session s = Objects.requireNonNull(Session.getSession());
		for (Person p : getEveryone()) {
			for (VotingPaper paper : s.getPapers()) {
				voteRandom(paper, p);
			}
		}
	}

	private void approveSession() {
		for (Person g : getGuarantors()) {
			Session.getSession().approve(g);
		}
	}

	@Test
	void thereIsNoSession() {
		assertNull(Session.getSession());
	}

	@Nested
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class TestInitializedSession {

		@BeforeAll
		private void setupExampleSession() {
			Session.setAdmin(getAdmin());
			Session.SessionParameters params = new Session.SessionParameters();
			params.setStart(LocalDateTime.now());
			params.setEnd(LocalDateTime.now().plusDays(3));
			Map<Choice, VotingPaper> choices1 = new HashMap<>();
			choices1.put(new Choice("Sub 1.1"), null);
			choices1.put(new Choice("Sub 1.2"), null);
			choices1.put(new Choice("Sub 1.3"), null);
			VotingPaper paper1 = new VotingPaper("Choice 1: Ordinal subchoices", choices1, ElectionMethod.ORDINAL, new AdultDecider());
			Map<Choice, VotingPaper> choices2 = new HashMap<>();
			choices2.put(new Choice("Sub 2.1"), null);
			choices2.put(new Choice("Sub 2.2"), null);
			VotingPaper paper2 = new VotingPaper("Choice 2: Categorical subchoices", choices2, ElectionMethod.CATEGORICAL, new AdultDecider());
			Map<Choice, VotingPaper> choices = new HashMap<>();
			choices.put(new Choice("Choice 1"), paper1);
			choices.put(new Choice("Choice 2"), paper2);
			choices.put(new Choice("Choice 3"), null);
			VotingPaper paper = new VotingPaper("Preferenced Example", choices, ElectionMethod.PREFERENCED, new AdultDecider());
			params.addPaper(paper);
			Session.initializeSession(params, getGuarantors());
		}

		@Test
		void sessionExists() {
			Session s = Session.getSession();
			assertNotNull(s);
		}

		@Test
		void sessionIsNotRunning() {
			assertFalse(Session.isRunning());
		}

		@Test
		void sessionReadyness() {
			Session s = Session.getSession();
			assertFalse(s.isReady());
			s.setReady();
			assertTrue(s.isReady());
		}

		@Test
		void cannotVoteIfSessionNotRunning() {
			Exception e = assertThrows(IllegalStateException.class, () -> {
				voteInExampleSession(); // voting without session started
			});
			assertEquals(e.getMessage(), "cannot add a vote to a closed election");
		}

		@Nested
		@TestInstance(TestInstance.Lifecycle.PER_CLASS)
		class TestRunningSession {

			@BeforeAll
			private void startSession() {
				Session s = Session.getSession();
				s.setReady();
				approveSession();
			}

			@Test
			void sessionApproved() {
				assertTrue(Session.getSession().checkApproval());
			}

			@Test
			void sessionRunning() {
				assertTrue(Session.isRunning());
			}

			@Test
			void exampleVotes() {
				voteInExampleSession();
			}

			private List<VotingPaper.Results> getAllResults() {
				List<VotingPaper.Results> res = new LinkedList<>();
				for (VotingPaper p : Session.getSession().getPapers())
					res.addAll(getPaperResults(p));
				return res;
			}

			private List<VotingPaper.Results> getPaperResults(VotingPaper p) {
				List<VotingPaper.Results> res = new LinkedList<>();
				res.add(p.getResults());
				if (p.getMethod() == ElectionMethod.PREFERENCED) {
					for (Choice c : p.getChoices()) {
						VotingPaper sub = p.getSubPaper(c);
						if (sub != null)
							res.addAll(getPaperResults(sub));
					}
				}
				return res;
			}

			@Test
			void cannotGetResults() {
				Exception e = assertThrows(IllegalStateException.class, () -> {
					getAllResults(); // getting results without session closed
				});
				assertEquals(e.getMessage(), "cannot get results when the session is running");
			}

			@Nested
			@TestInstance(TestInstance.Lifecycle.PER_CLASS)
			class TestEndedSession {

				@BeforeAll
				private void closeSession() {
					Session.getSession().forceClose();
				}

				@Test
				void isClosed() {
					assertFalse(Session.isRunning());
				}

				@Test
				void getResults() {
					getAllResults();
				}

			}

		}

	}

}
