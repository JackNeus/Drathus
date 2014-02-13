package drathus.game.com;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class GameApplet extends Applet {
	/**
	 * Gets rid of stupid yellow warning
	 */
	private static final long serialVersionUID = 1L;
	Canvas display_parent;
	Thread gameThread;
	Game game;

	//Will only work if Canvas is created
	//Starts LWJGL Display and game loop in additional thread
	public void startLWJGL() {
		gameThread = new Thread() {
			@Override
			public void run() {
				try {
					Display.setParent(display_parent);
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				game = new Game(false);
				game.execute();
			}
		};
		gameThread.start();
	}

	/*Tell game loop to stop running*/
	private void stopLWJGL() {
		Game.gameRunning = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void destroy() {
		remove(display_parent);
		super.destroy();
		System.out.println("Clear up");
	}

	@Override
	public void init() {
		setLayout(new BorderLayout());
		try {
			display_parent = new Canvas() {
				/**
				 * Gets rid of stupid warning
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void addNotify() {
					super.addNotify();
					startLWJGL();
				}

				@Override
				public void removeNotify() {
					stopLWJGL();
					super.removeNotify();
				}
			};
			display_parent.setSize(getWidth(), getHeight());
			add(display_parent);
			display_parent.setFocusable(true);
			display_parent.requestFocus();
			display_parent.setIgnoreRepaint(true);
			setVisible(true);
		} catch (Exception e) {
			System.err.println(e);
			throw new RuntimeException("Unable to create display");
		}
	}
}
