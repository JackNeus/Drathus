package drathus.game.com;

public class Player extends Entity {

	private static int leftBorder, rightBorder, topBorder, bottomBorder, framesPressed = 0, swap = 7, dir = 1, slow = 16;
	public float speed = 80; //default speed
	private static String[] costumeRefs = { "man1_fr1.gif", "man1_fr2.gif", "man1_bk1.gif", "man1_bk2.gif", "man1_lf1.gif", "man1_lf2.gif", "man1_rt1.gif", "man1_rt2.gif" };
	private static Sprite[] costumes = new Sprite[8]; //down up left right

	private int shotDirection, frameCount;

	protected Player(Game game, String ref, int x, int y) {
		super(game.getSprite(ref), x, y);
		leftBorder = 0;
		rightBorder = Game.width - super.width;
		topBorder = 0;
		bottomBorder = Game.height - super.height;
		for (int i = 0; i < 8; i++) {
			costumes[i] = game.getSprite(costumeRefs[i]);
		}
		shotDirection = -1;
		frameCount = 0;
	}

	public void setDirection(int dir) {
		shotDirection = dir;
		this.dir = shotDirection;
	}

	@Override
	public void move(long delta) {
		if (shotDirection != -1) {
			frameCount++;
			if (frameCount > 5) {
				shotDirection = -1;
				frameCount = 0;
			}
		}
		slow = (int) (speed * 0.2);
		swap = (int) (slow * 0.5 - 1);
		if (dx == 0 && dy == 0) {
			sprite = costumes[dir * 2 + 1];
			framesPressed = 0;
		} else {
			if (dx > 0) {
				if (dir == 3 || dir == shotDirection) framesPressed = (framesPressed + 1) % slow;
				else {
					if (shotDirection == -1) dir = 3;
					else dir = shotDirection;
					framesPressed = 0;
				}
				if (framesPressed > swap) {
					sprite = costumes[dir * 2 + 1];
				} else sprite = costumes[dir * 2];
			} else if (dx < 0) {
				if (dir == 2 || dir == shotDirection) framesPressed = (framesPressed + 1) % slow;
				else {
					if (shotDirection == -1) dir = 2;
					else dir = shotDirection;
					framesPressed = 0;
				}
				if (framesPressed > swap) sprite = costumes[dir * 2 + 1];
				else sprite = costumes[dir * 2];
			} else {
				if (dy > 0) {
					if (dir == 0 || dir == shotDirection) framesPressed = (framesPressed + 1) % slow;
					else {
						if (shotDirection == -1) dir = 0;
						else dir = shotDirection;
						framesPressed = 0;
					}
					if (framesPressed > swap) sprite = costumes[dir * 2 + 1];
					else sprite = costumes[dir * 2];
				} else {
					if (dir == 1 || dir == shotDirection) framesPressed = (framesPressed + 1) % slow;
					else {
						if (shotDirection == -1) dir = 1;
						else dir = shotDirection;
						framesPressed = 0;
					}
					if (framesPressed > swap) sprite = costumes[dir * 2 + 1];
					else sprite = costumes[dir * 2];
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
