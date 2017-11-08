package com.ru.tgra.lab1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ru.tgra.game.RaceGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Sweet-ish roligt roadtrip"; // or whatever you like
		config.width = 1024;  //experiment with
		config.height = 512;  //the window size
		config.x = 80;
		config.y = 80;
		//config.fullscreen = true;
		

		new LwjglApplication(new RaceGame(), config);
	}
}
