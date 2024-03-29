package drathus.game.com;

import org.lwjgl.input.Keyboard;

public class Bullet extends Entity {
	private Game game;
	private boolean hit;
	private int sequence;

	private static String[] costumeRefs = { "Bullet_1.png", "Bullet_2.png", "Bullet_3.png", "Bullet_4.png", "Bullet_5.png", "Bullet_6.png" };
	private static Sprite[] costumes = new Sprite[6];

	/** Bounds for the bullet, if goes off screen, destroy */
	private int leftBorder, rightBorder, topBorder, bottomBorder;

	private int angleDegrees;
	private int speed = 350;

	public Bullet(Game game, Sprite sprite, float x, float y, int dir) {
		super(sprite, Math.round(x), Math.round(y));
		dx = (dir == Keyboard.KEY_A) ? -speed : (dir == Keyboard.KEY_D) ? speed : 0;
		dy = (dir == Keyboard.KEY_W) ? -speed : (dir == Keyboard.KEY_S) ? speed : 0;
		angleDegrees = (dir == Keyboard.KEY_A || dir == Keyboard.KEY_D) ? 0 : 90;
		leftBorder = topBorder = 0;
		rightBorder = Game.width - width;
		bottomBorder = Game.height - height;
		this.game = game;
		for (int i = 0; i < 6; i++) {
			costumes[i] = game.getSprite(costumeRefs[i]);
		}
	}

	public void reinitialize(int x, int y) { //reuse this entity
		this.x = x;
		this.y = y;
		hit = false;
	}

	@Override
	public void move(long delta) {
		if (hit) {
			endSequence();
			return;
		}
		super.move(delta);
		if (x < leftBorder || x > rightBorder || y < topBorder || y > bottomBorder) {
			game.removeEntity(this);
		}
	}

	/**
	 * The end animation of sprites when the bullet is destroyed
	 */
	public void endSequence() {
		if (sequence == 11) {
			game.removeEntity(this);
			return;
		}

		/** Update animation every two frames */
		if (sequence % 2 == 0) {
			sprite = costumes[sequence / 2];
		}
		sequence++;
	}

	@Override
	public void draw() {
		super.draw(angleDegrees);
	}

	@Override
	public void collidedWith(Entity other) {
		if (hit) return;
		if (other instanceof Enemy) {
			hit = true;
			game.removeEntity(other);
		} else if (other instanceof SolidEntity) {
			hit = true;
		}
	}
}
