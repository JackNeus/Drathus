package drathus.game.com;

public class ZombieEnemy extends Enemy {
	/** Ton of variables */
	private Game game;
	private Player player;
	private boolean attacking, done, intersecting;
	private int sequence;
	private static int speed = 80;
	private static String[] costumeRefs = { "ZKill_1.png", "ZKill_2.png", "ZKill_3.png", "ZKill_4.png", "ZKill_5.png", "ZKill_6.png" };
	private static Sprite[] costumes = new Sprite[6];
	private static Sprite original;
	private Vector2 dir;

	protected ZombieEnemy(Game game, Player player, Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.player = player;
		this.game = game;
		for (int i = 0; i < 6; i++) {
			costumes[i] = game.getSprite(costumeRefs[i]);
		}
		original = sprite;
		dir = new Vector2(0, 0);
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof Player) {
			if (!attacking) {
				if (done) game.notifyDeath();
				else attacking = true;
			}
		}
		if (other instanceof SolidEntity) {
			int diffx = (int) Math.abs((x + width / 2) - (other.x + other.width / 2));
			int diffy = (int) Math.abs((y + height / 2) - (other.y + other.height / 2));
			if (diffx >= diffy) {
				if (dx > 0) x = other.x - width;
				else if (dx < 0) x = other.x + other.width;
			} else {
				if (dy > 0) y = other.y - height;
				else if (dy < 0) y = other.y + other.height;
			}
			intersecting = true;
		}
	}

	@Override
	public void move(long delta) {
		//dx = dy = 0;
		if (done) done = false;
		if (attacking && !done) {
			attack();
			return;
		}

		Vector2 v = Vector2.f(new Point(x + width / 2, x + height / 2), new Point(player.x + player.width / 2, player.y + player.height / 2));
		Vector2.normalize(v);
		dir.x += v.x;
		dir.y += v.y;
		if (intersecting) {
			dir.x = (float) (dx * Math.cos(20) - dy * Math.sin(20));
			dir.y = (float) (dx * Math.sin(20) + dy * Math.cos(20));
		}
		Vector2.normalize(dir);
		dx = (float) dir.x * speed;
		dy = (float) dir.y * speed;
		//repulse(game.getObstacle());

		/*if (player.x < x) dx = -speed;
		else if (player.x > x) dx = speed;
		if (player.y < y) dy = -speed;
		else if (player.y > y) dy = speed; */
		super.move(delta);
		intersecting = false;
	}

	public void repulse(SolidEntity obstacle) {
		Vector2 repulsion = Vector2.getForce(x + width / 2, y + height / 2, obstacle);
		dx += repulsion.x * speed;
		dy += repulsion.y * speed;
	}

	/**
	 * Carries out animation for attack
	 */
	public void attack() {
		/** We're done! */
		if (sequence == 26) {
			attacking = false;
			done = true;
			sequence = 0;
			sprite = original;
			return;
		}

		/**
		 * Update animation every five frames
		 */
		if (sequence % 5 == 0) {
			sprite = costumes[sequence / 5];
		}
		sequence++;
	}
}
