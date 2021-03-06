package it.unipd.dei.db.kayak.league_manager;

import it.unipd.dei.db.kayak.league_manager.data.LMUser;
import it.unipd.dei.db.kayak.league_manager.database.Helper;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LoginElement {
	// private fields
	VerticalLayout mainLayout;

	public LoginElement() {
		this.setUpContent();
	}

	private void setUpContent() {
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(new MarginInfo(false, true, false, true));

		setUnlogged();
	}

	public void setLoggedIn(String loggedInEmail) {
		mainLayout.removeAllComponents();

		mainLayout.addComponent(new Label("Logged in as"));
		mainLayout.addComponent(new Label(loggedInEmail));

		mainLayout.addComponent(new Button("Logout", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Home home = ((MyVaadinUI) UI.getCurrent()).getHome();
				home.setUnlogged();
			}
		}));
	}

	public void setUnlogged() {
		mainLayout.removeAllComponents();

		mainLayout.addComponent(new Label("User email"));
		final TextField emailArea = new TextField();
		emailArea.setWidth("100%");
		mainLayout.addComponent(emailArea);

		mainLayout.addComponent(new Label("Password"));
		final PasswordField passwordArea = new PasswordField();
		passwordArea.setWidth("100%");
		mainLayout.addComponent(passwordArea);

		mainLayout.addComponent(new Button("Login", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String email = emailArea.getValue();
				LMUser user = null;
//				for (LMUser u : FakeDataWarehouse.getUsers()) {
//					System.out.println("confronting " + email + " equals "
//							+ u.getEmail() + " : " + u.getEmail().equals(email));
//					if (u.getEmail().equals(email)) {
//						user = u;
//						break;
//					}
//				}

				if (user == null) {
					// TODO: show notification about wrong login email
					Notification.show("Login Error", "Invalid email",
							Notification.Type.WARNING_MESSAGE);
					System.out.println("invalid email: " + email);
					return;
				}

				byte[] digest = Helper.getHashedString(passwordArea
						.getValue());
				boolean check = true;
				for (int i = 0; i < digest.length; ++i) {
					check &= (digest[i] == user.getPassword()[i]);
				}

				if (!check) {
					// TODO: show notification about wrong login password
					Notification.show("Login Error", "Invalid password",
							Notification.Type.WARNING_MESSAGE);
					System.out.println("invalid password: "
							+ passwordArea.getCaption());
					return;
				}

				Home home = ((MyVaadinUI) UI.getCurrent()).getHome();
				home.setLoggedIn(email);
			}
		}));
	}

	public VerticalLayout getContent() {
		return mainLayout;
	}
}
