package drathus.game.com;

public class Bullet extends Entity {
	private boolean hit = false;
	
	//We want the bullet to go offscreen before deleting it, or it won't be smooth
	//There will be a seperate animation of sprites if a bullet hits an object
	private static int leftBorder = -Game.width;
	private static int rightBorder = Game.width;
	private static int topBorder = -Game.height;
	private static int bottomBorder = Game.height;
	public float speed = 150;
	private float dx;
	private float dy;
	private static Game game;
	
	protected Bullet(String ref, int x, int y) {
		super(game.getSprite(ref), x, y);
	}

	public void reinitialize(int x, int y) { //reuse this entity
		this.x = x;
		this.y = y;
		hit = false;
	}
	
	public void move(long delta, float angle) {
		dx = (float) (speed * StrictMath.cos(angle));
		dy = (float) (speed * StrictMath.sin(angle));
		move(delta);
		if (x < leftBorder || x > rightBorder || y < topBorder || y > bottomBorder) game.removeEntity(this);
	}

	@Override
	public void collidedWith(Entity other) {
		if (hit) return;
		hit = true;
	}
}
