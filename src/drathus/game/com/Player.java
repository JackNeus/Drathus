package drathus.game.com;

public class Player extends Entity {
	private Game game;

	private static int leftBorder, rightBorder, topBorder, bottomBorder, framesPressed = 0, swap = 7, dir = 1, slow = 16;
	public float speed = 80; //default speed
	public static String costumeRefs[] = { "man1_fr1.gif", "man1_fr2.gif", "man1_bk1.gif", "man1_bk2.gif", "man1_lf1.gif", "man1_lf2.gif", "man1_rt1.gif", "man1_rt2.gif" };
	public static Sprite costumes[] = new Sprite[8]; //down up left right

	protected Player(Game game, String ref, int x, int y) {
		super(game.getSprite(ref), x, y);
		this.game = game;
		leftBorder = 0;
		rightBorder = game.width - super.width;
		topBorder = 0;
		bottomBorder = game.height - super.height;
		for (int i = 0; i < 8; i++) {
			costumes[i] = game.getSprite(costumeRefs[i]);
		}
	}

	@Override
	public void move(long delta) {
		slow = (int) (speed * 0.2);
		swap = (int) (slow * 0.5 - 1);
		if (dx == 0 && dy == 0) {
			sprite = costumes[dir * 2 + 1];
			framesPressed = 0;
		} else {
			if (dx > 0) {
				if (dir == 3) framesPressed = (framesPressed + 1) % slow;
				else {
					dir = 3;
					framesPressed = 0;
				}
				if (framesPressed > swap) sprite = costumes[7];
				else sprite = costumes[6];
			} else if (dx < 0) {
				if (dir == 2) framesPressed = (framesPressed + 1) % slow;
				else {
					dir = 2;
					framesPressed = 0;
				}
				if (framesPressed > swap) sprite = costumes[5];
				else sprite = costumes[4];
			} else {
				if (dy > 0) {
					if (dir == 0) framesPressed = (framesPressed + 1) % slow;
					else dir = framesPressed = 0;
					if (framesPressed > swap) sprite = costumes[1];
					else sprite = costumes[0];
				} else {
					if (dir == 1) framesPressed = (framesPressed + 1) % slow;
					else {
						dir = 1;
						framesPressed = 0;
					}
					if (framesPressed > swap) sprite = costumes[3];
					else sprite = costumes[2];
				}
			}
		}

		super.move(delta);
		if (x < leftBorder) x = leftBorder;
		if (x > rightBorder) x = rightBorder;
		if (y < topBorder) y = topBorder;
		if (y > bottomBorder) y = bottomBorder;
	}

	@Override
	public void collidedWith(Entity other) {
	}
}
