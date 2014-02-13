package drathus.game.com;

public class ZombieEnemy extends Enemy {
	/** Ton of variables */
	private Game game;
	private Player player;
	private boolean attacking, done;
	private int sequence;
	private static int speed = 80;
	private static String[] costumeRefs = { "ZKill_1.png", "ZKill_2.png", "ZKill_3.png", "ZKill_4.png", "ZKill_5.png", "ZKill_6.png" };
	private static Sprite[] costumes = new Sprite[6];
	private static Sprite original;

	protected ZombieEnemy(Game game, Player player, Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.player = player;
		this.game = game;
		for (int i = 0; i < 6; i++) {
			costumes[i] = game.getSprite(costumeRefs[i]);
		}
		original = sprite;
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof Player) {
			if (!attacking) {
				if (done) game.notifyDeath();
				else attacking = true;
			}
		}
	}

	@Override
	public void move(long delta) {
		dx = dy = 0;
		if (done) done = false;
		if (attacking && !done) {
			attack();
			return;
		}
		if (player.x < x) dx = -speed;
		else if (player.x > x) dx = speed;
		if (player.y < y) dy = -speed;
		else if (player.y > y) dy = speed;
		super.move(delta);
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
