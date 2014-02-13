package drathus.game.com;

public abstract class Enemy extends Entity {
	public boolean alive = true;

	protected Enemy(Sprite sprite, int x, int y) {
		super(sprite, x, y);
	}

	public void reinitialize(int x, int y) {
		this.x = x;
		this.y = y;
		alive = true;
	}
}
