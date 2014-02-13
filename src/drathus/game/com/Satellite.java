package drathus.game.com;

public class Satellite extends Entity {
	Game game;
	Sprite sprite;
	Player player;
	private int radius;
	public int satSpeed = 4; 
	private float angle = 0.0f;
	
	protected Satellite(Game game, Player player, String ref, int r) {
		super(game.getSprite(ref), player.getX(), player.getY() + r);
		this.player = player;
		this.game = game;
		this.radius = r;
	}

	@Override
	public void move(long delta) {
		angle += 0.1f;
		x = player.x + player.width / 2;
		y = player.y + player.height / 2;
		dx = (float) (radius * StrictMath.sin(angle));
		dy = (float) (radius * StrictMath.cos(angle));
		super.move(delta);
	}
	
	@Override
	public void collidedWith(Entity other) {
		
	}
}
