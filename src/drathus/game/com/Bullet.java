package drathus.game.com;

import org.lwjgl.input.Keyboard;

public class Bullet extends Entity {
	private Game game;
	private boolean hit = false;

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
	}

	public void reinitialize(int x, int y) { //reuse this entity
		this.x = x;
		this.y = y;
		hit = false;
	}

	@Override
	public void move(long delta) {
		super.move(delta);
		if (x < leftBorder || x > rightBorder || y < topBorder || y > bottomBorder) {
			game.removeEntity(this);
		}
	}

	/**
	 * The end animation of sprites when the bullet is destroyed
	 */
	public void endSequence() {
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
			game.removeEntity(this);
			game.removeEntity(other);
		} else if (other instanceof SolidEntity) {
			hit = true;
			game.removeEntity(this);
		}
	}
}
