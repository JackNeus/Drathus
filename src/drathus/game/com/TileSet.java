package drathus.game.com;

public class TileSet{
	public Sprite sprite;
	
	public String name;
	public String source;

	public int firstgid;
	public int lastgid;
	
	public int tileHeight;
	public int tileWidth;
	public int imageHeight;
	public int imageWidth;
	
	public int tileAmountWidth;
	
	public TileSet(int firstgid, String name, int tileWidth, int tileHeight, String source, int imageWidth, int imageHeight){
		this.firstgid = firstgid;
		this.name = name;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.source = source;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		tileAmountWidth = (int) Math.floor(imageWidth / tileWidth);
		lastgid = tileAmountWidth * (int) Math.floor(imageHeight / tileHeight) + firstgid - 1;
	}
}
