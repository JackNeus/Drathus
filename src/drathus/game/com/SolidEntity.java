package drathus.game.com;

public class SolidEntity extends Entity {
	public int mass = 2200;

	public SolidEntity(Sprite sprite, int x, int y) {
		super(sprite, x, y);
	}

	@Override
	public void collidedWith(Entity other) {

	}
}
