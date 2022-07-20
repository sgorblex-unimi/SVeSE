package org.miniblex.svese.views.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import org.miniblex.svese.views.newSession.NewSessionView;
import org.miniblex.svese.model.Role;
import org.miniblex.svese.model.Session;
import org.miniblex.svese.security.SecurityService;
import org.miniblex.svese.views.logout.LogoutView;
import org.miniblex.svese.views.guarantorsApprove.GuarantorsApproveView;
import org.miniblex.svese.views.vote.VoteView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Menu on the top of the screen, with links to the other views
 */
@PermitAll
public class MainView extends AppLayout {
	private final Tabs menu;
	private final SecurityService s;

	public MainView(@Autowired SecurityService s) {
		this.s = s;
		menu = createMenuTabs();
		this.addToNavbar(true, menu);
	}

	private Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
		tabs.add(getAvailableTabs());
		return tabs;
	}

	private Tab[] getAvailableTabs() {
		final List<Tab> tabs = new ArrayList<>(4);
		Collection<Role> roles = Session.getRoles(s.getAuthenticatedPerson());
		tabs.add(createTab(VaadinIcon.LIST_SELECT, "Vote", VoteView.class));
		if (roles.contains(Role.ADMIN)) {
			tabs.add(createTab(VaadinIcon.COGS, "New session", NewSessionView.class));
		}
		if (roles.contains(Role.GUARANTOR)) {
			tabs.add(createTab(VaadinIcon.CHECK, "Approve", GuarantorsApproveView.class));
		}
		tabs.add(createTab(VaadinIcon.SIGN_OUT, "Logout", LogoutView.class));
		return tabs.toArray(new Tab[tabs.size()]);
	}

	private Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
		return createTab(populateLink(new RouterLink(null, viewClass), icon, title));
	}

	private Tab createTab(Component content) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(content);
		return tab;
	}

	private <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
		a.add(icon.create());
		a.add(title);
		return a;
	}
}
