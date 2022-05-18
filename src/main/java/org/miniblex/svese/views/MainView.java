package org.miniblex.svese.views;

import java.util.ArrayList;
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

/**
 * Menu on the top of the screen, with links to the other views
 */
@PermitAll
public class MainView extends AppLayout {
	private final Tabs menu;

	public MainView() {
		menu = createMenuTabs();
		this.addToNavbar(true, menu);
	}

	private static Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
		tabs.add(getAvailableTabs());
		return tabs;
	}

	private static Tab[] getAvailableTabs() {
		final List<Tab> tabs = new ArrayList<>(4);
		tabs.add(createTab(VaadinIcon.LIST_SELECT, "Vote", VoteView.class));
		tabs.add(createTab(VaadinIcon.STOP_COG, "Stop Polling Station", StopPollingStationView.class));
		tabs.add(createTab(VaadinIcon.SIGN_OUT, "Logout", LogoutView.class));
		return tabs.toArray(new Tab[tabs.size()]);
	}

	private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
		return createTab(populateLink(new RouterLink(null, viewClass), icon, title));
	}

	private static Tab createTab(Component content) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(content);
		return tab;
	}

	private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
		a.add(icon.create());
		a.add(title);
		return a;
	}
}
