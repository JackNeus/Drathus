package drathus.game.com;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Game {
	/** Window variables */
	private String WINDOW_TITLE = "Drathus - The Game";
	public static int width = 800;
	public static int height = 600;
	private boolean fullscreen;

	/** FPS/Time Variables */
	long lastFrame;
	long lastFPS;
	private int fps;
	private long lastFpsTime;
	private long lastLoopTime = getTime();
	private static long timerTicksPerSecond = Sys.getTimerResolution(); //For some reason find this up here instead of in each call to getTime();
	private long shotPeriod = (long) (0.5 * 1000); //rest period between shots in milliseconds
	private long lastShot = 0L; //the time that last shot was fired

	/** Application/Applet state variables */
	public static boolean gameRunning = true;
	public static boolean isApplication;

	/** Game variables */
	private Player player;
	private SolidEntity obstacle;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> removeList = new ArrayList<Entity>();

	/** Map/Background variables */
	private Map map;
	private TextureLoader textureLoader;

	public Game(boolean fullscreen) {
		this.fullscreen = fullscreen;
		initialize();
	}

	public long getTime() {
		return (Sys.getTime() * 1000) / timerTicksPerSecond;
	}

	public static void sleep(long duration) { //sleep for a certain amount of milliseconds
		try {
			Thread.sleep((duration * timerTicksPerSecond) / 1000);
		} catch (InterruptedException inte) {
		}
	}

	public void initialize() {
		try {
			setDisplayMode();
			Display.setTitle(WINDOW_TITLE);
			Display.setFullscreen(fullscreen);
			Display.create();

			if (isApplication) { //This will trap the mouse inside the window and it will also make it invisible
				//Mouse.setGrabbed(true);
			}

			glEnable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);

			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();

			glOrtho(0, width, height, 0, -1, 1);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glViewport(0, 0, width, height); //Sets the area we're viewing

			textureLoader = new TextureLoader();
		} catch (LWJGLException le) {
			System.out.println("Game exiting - exception in initilization");
			le.printStackTrace();
			Game.gameRunning = false;
		}
		startGame();
	}

	private boolean setDisplayMode() {
		try {
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, 60, 60);//Gets avaliable modes
			org.lwjgl.util.Display.setDisplayMode(dm, new String[] { //Sets a mode - you just give it the list of avaliable modes
					"width=" + width, "height=" + height, "freq=" + 60, "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel() });
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to enter fullscreen, continuing in windowed mode");
		}
		return false;
	}

	private void startGame() {
		entities.clear();
		initEntities();
		initMap();
	}

	private void initEntities() {
		for (int i = 0; i < (width / 100) + 1; i++) {
			for (int j = 0; j < (height / 100) + 1; j++) {
				Stage tile = new Stage(this, "grass.png", i * 100, j * 100);
				//tile.collidedWith(tile);
				entities.add(tile);
			}
		}
		player = new Player(this, "man1_fr1.gif", 400, 300);
		player.speed = 200;
		entities.add(player);
		player.collidedWith(player);
		obstacle = new SolidEntity(getSprite("obstacle.png"), 100, 200);
		entities.add(obstacle);
	}

	private void initMap() {
		map = new Map("TestMap.tmx");
	}
	
	private void gameLoop() {
		while (Game.gameRunning) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

			frameRendering();
			Display.update();
		}
		Display.destroy();
	}

	public void frameRendering() {
		Display.sync(60); //Lock at <= 60fps

		//Figures out how long it has been since last update, used to calculate displacement of entities
		long delta = getTime() - lastLoopTime;
		lastLoopTime = getTime();
		lastFpsTime += delta;
		fps++;

		//update FPS counter if a second has passed
		if (lastFpsTime >= 1000) {
			Display.setTitle(WINDOW_TITLE + " (FPS: " + fps + ")");
			fps = 0;
			lastFpsTime = 0;
		}

		for (Entity entity : entities) {
			entity.move(delta);
		}

		for (Entity entity : entities) {
			entity.draw();
		}

		for (int i = 0; i < entities.size(); i++) {
			for (int j = i + 1; j < entities.size(); j++) {
				Entity me = entities.get(i), you = entities.get(j);
				if (me.collidesWith(you)) {
					me.collidedWith(you);
					you.collidedWith(me);
				}
			}
		}

		entities.removeAll(removeList);
		removeList.clear();

		player.dx = player.dy = 0;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) player.dx = -player.speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) player.dx = player.speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) player.dy = player.speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) player.dy = -player.speed;

		if (Keyboard.isKeyDown(Keyboard.KEY_A) && System.currentTimeMillis() - lastShot >= shotPeriod) {
			lastShot = System.currentTimeMillis();
			entities.add(new Bullet(this, getSprite("Bullet.png"), player.x + player.width, player.y + player.height / 2, Keyboard.KEY_A));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) && System.currentTimeMillis() - lastShot >= shotPeriod) {
			lastShot = System.currentTimeMillis();
			entities.add(new Bullet(this, getSprite("Bullet.png"), player.x + player.width, player.y + player.height / 2, Keyboard.KEY_S));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D) && System.currentTimeMillis() - lastShot >= shotPeriod) {
			lastShot = System.currentTimeMillis();
			entities.add(new Bullet(this, getSprite("Bullet.png"), player.x + player.width, player.y + player.height / 2, Keyboard.KEY_D));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_W) && System.currentTimeMillis() - lastShot >= shotPeriod) {
			lastShot = System.currentTimeMillis();
			entities.add(new Bullet(this, getSprite("Bullet.png"), player.x + player.width, player.y + player.height / 2, Keyboard.KEY_W));
		}

		if ((Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) && isApplication) {
			Game.gameRunning = false;
		}
	}

	public static void main(String argv[]) {
		isApplication = true;
		System.out.println("Use -fullscreen for fullscreen mode");
		new Game((argv.length > 0 && "-fullscreen".equalsIgnoreCase(argv[0]))).execute();
	}

	public void execute() {
		gameLoop();
	}

	public Sprite getSprite(String ref) {
		return new Sprite(textureLoader, ref);
	}

	public Player getPlayer() {
		return player;
	}

	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	public void notifyDeath() {
		System.out.println("YOU LOOSE!");
		gameRunning = false;
	}
}
