package nl.avans.steam.interfaces;

import nl.avans.steam.model.Game;

public interface ProfileActivityInterface {
	public void updateUserStatus(int onlineStatus, String currentGame);
	public void updateGames(Game[] games);
}
