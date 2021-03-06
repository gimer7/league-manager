package it.unipd.dei.db.kayak.league_manager;

import java.io.File;
import java.util.Collection;

import it.unipd.dei.db.kayak.league_manager.data.Tournament;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

public class TournamentTable extends Table {
	private StringPropertyFilter nameFilter;
	private StringPropertyFilter emailFilter;

	private TournamentTable() {
	}

	public TournamentTable(Collection<Tournament> tournaments) {
		super("");

		Container tournamentContainer = new IndexedContainer();

		tournamentContainer.addContainerProperty("", Button.class, null);
		tournamentContainer.addContainerProperty("Name", String.class, null);
		tournamentContainer.addContainerProperty("Year", Integer.class, null);
		tournamentContainer.addContainerProperty("Sex", String.class, null);
		tournamentContainer
				.addContainerProperty("Max Age", Integer.class, null);
		tournamentContainer.addContainerProperty("Organizer Email",
				String.class, null);

		this.setContainerDataSource(tournamentContainer);

		for (Tournament t : tournaments) {
			this.addTournament(t);
		}

		Filterable filterable = (Filterable) tournamentContainer;
		nameFilter = new StringPropertyFilter("", "Name");
		filterable.addContainerFilter(nameFilter);
		emailFilter = new StringPropertyFilter("", "Organizer Email");
		filterable.addContainerFilter(emailFilter);

		this.setColumnExpandRatio("", 0);
		this.setColumnWidth("", 45);
		this.setColumnExpandRatio("Name", 1);
		this.setColumnExpandRatio("Year", 1);
		this.setColumnExpandRatio("Sex", 1);
		this.setColumnExpandRatio("Max Age", 1);
		this.setColumnExpandRatio("Organizer Email", 1);
	}

	public void filterTournamentNames(String text) {
		if (!text.toLowerCase().equals(nameFilter.getFilter().toLowerCase())) {
			Filterable container = (Filterable) this.getContainerDataSource();
			container.removeContainerFilter(nameFilter);
			nameFilter.setFilter(text);
			container.addContainerFilter(nameFilter);
		}
	}

	public void addTournament(Tournament tournament) {
		final String tournamentName = tournament.getName();
		final int tournamentYear = tournament.getYear();

		Button btn = new Button("", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Home home = ((MyVaadinUI) UI.getCurrent()).getHome();
				home.showTournamentCalendarView(tournamentName, tournamentYear);
			}
		});

		String basepath = VaadinService.getCurrent().getBaseDirectory()
				.getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath
				+ "/WEB-INF/images/magnifier.png"));
		btn.setIcon(resource);

		this.addItem(
				new Object[] { btn, tournament.getName(), tournament.getYear(),
						tournament.getSex() ? "M" : "F",
						tournament.getMaxAge(), tournament.getOrganizerEmail(), },
				tournament.getName() + tournament.getYear());
	}

	public void removeTournament(String tournamentName, int tournamentYear) {
		this.removeItem(tournamentName + tournamentYear);
	}

	public boolean hasTournament(String tournamentName, int tournamentYear) {
		return this.containsId(tournamentName + tournamentYear);
	}
}
