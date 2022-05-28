package org.miniblex.svese.security;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

import org.miniblex.svese.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Application security configuration. Configures both Vaadin and Spring
 * Security.
 */
@EnableWebSecurity
@Configuration
class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.logout().logoutSuccessUrl("/");
		setLoginView(http, LoginView.class);
		http.formLogin().defaultSuccessUrl("/vote", true);
	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**");
		super.configure(web);
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return new SVeSEUserDetailService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
