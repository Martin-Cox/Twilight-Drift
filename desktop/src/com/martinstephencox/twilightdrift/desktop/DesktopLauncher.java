package com.martinstephencox.twilightdrift.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.martinstephencox.twilightdrift.TwilightDrift;
import com.martinstephencox.twilightdrift.main.Setup;
import com.martinstephencox.twilightdrift.screens.DebugScreen;
import com.martinstephencox.twilightdrift.screens.Window;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
        config.resizable = false;
		new LwjglApplication(new Window(), config);
	}
}
