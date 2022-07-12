package org.miniblex.svese.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.miniblex.svese.model.Person;
import org.miniblex.svese.model.PersonRepository;
import org.miniblex.svese.model.Role;
import org.miniblex.svese.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implementation of {@link UserDetailsService}: provides information about a
 * user which is to be logged in.
 */
// @Service("userDetailsService")
// @Transactional
class SVeSEUserDetailService implements UserDetailsService {

	@Autowired
	private PersonRepository personRepo;

	@Override
	public UserDetails loadUserByUsername(String SSN) throws UsernameNotFoundException {

		Optional<Person> userMaybe = personRepo.findById(SSN);
		if (userMaybe.isEmpty()) {
			return new User(" ", " ", true, true, true, true, getGrantedAuthorities(new ArrayList<>()));
		}
		Person user = userMaybe.get();
		return new User(user.getSsn(), user.getPwHash(), user.isEnabled(), true, true, true, getGrantedAuthorities(Session.getRoles(user)));
	}

	/**
	 * Converts the model {@link Role}s into Spring Security
	 * {@link GrantedAuthority}s.
	 */
	private List<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role r : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + r)); // Vaadin looks for "ROLE_<rolename>"
		}
		return authorities;
	}
}
