package drathus.game.com;

public class Tile extends Entity{
	private Game game;
	
	protected Tile(Game game, String ref, int x, int y, int texX, int texY, int width, int height) {
		//super(game.getSprite(ref), x, y);
		super(game.getSprite(ref, texX, texY, width, height), x, y);
		this.game = game;
	}

	@Override
	public void collidedWith(Entity other) {
		// TODO Auto-generated method stub
		
	}
	
	public void move(long delta){
	}
	
}
