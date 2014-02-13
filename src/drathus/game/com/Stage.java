package drathus.game.com;

public class Stage extends Entity {
	private Game game;

	protected Stage(Game game, String ref, int x, int y) {
		super(game.getSprite(ref), x, y);
		this.game = game;
	}

	@Override
	public void collidedWith(Entity other) {

	}

}
