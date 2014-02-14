package drathus.game.com;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.io.File;

public class Map {
	private Game game;
	
	private String filePath;
	
	private int mapWidth;
	private int mapHeight;
	private int tileWidth;
	private int tileHeight;
	
	public Tile[][] mapTiles;
	
	ArrayList<TileSet> tilesets = new ArrayList<TileSet>();
	
	public void readData(){
		try{
			File mapFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mapFile);
			
			doc.getDocumentElement().normalize();
			
			//System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());
			Element map = doc.getDocumentElement();
			mapWidth = Integer.parseInt(map.getAttribute("width"));
			mapHeight = Integer.parseInt(map.getAttribute("height"));
			tileWidth = Integer.parseInt(map.getAttribute("tilewidth"));
			tileHeight = Integer.parseInt(map.getAttribute("tileheight"));
			
			NodeList tileSetTags = doc.getElementsByTagName("tileset"), imageTags;
			
			for(int i = 0; i < tileSetTags.getLength(); i++){
				Element curr = (Element) tileSetTags.item(i);
				int tilesetTileWidth = Integer.parseInt(curr.getAttribute("tilewidth"));
				int tilesetTileHeight = Integer.parseInt(curr.getAttribute("tileheight"));
				int firstGid = Integer.parseInt(curr.getAttribute("firstgid"));
				String tilesetName = curr.getAttribute("name");
				imageTags = curr.getElementsByTagName("image");
				Element image = (Element) imageTags.item(0);
				int imageWidth = Integer.parseInt(image.getAttribute("width"));
				int imageHeight = Integer.parseInt(image.getAttribute("height"));
				String tilesetImagePath = image.getAttribute("source");
				tilesets.add(new TileSet(firstGid, tilesetName, tilesetTileWidth, tilesetTileHeight, tilesetImagePath, imageWidth, imageHeight));
			}			
			
			mapTiles = new Tile[mapWidth][];
			for(int i = 0; i < mapWidth; i++) mapTiles[i] = new Tile[mapHeight];
			
			NodeList layers = doc.getElementsByTagName("layer"), tiles;
			
			for(int i = 0; i < layers.getLength(); i++){
				if(i > 0) break; //TEMPORARY
				Element currLayer = (Element) layers.item(i);
				
				int width = Integer.parseInt(currLayer.getAttribute("width"));
				int height = Integer.parseInt(currLayer.getAttribute("height"));
				
				tiles = currLayer.getElementsByTagName("tile");
				
				for(int j = 0; j < tiles.getLength(); j++){
					Element currTile = (Element) tiles.item(j);
					int x = j % mapWidth, y = j / mapWidth;
					
					double gid = Integer.parseInt(currTile.getAttribute("gid"));
					
					if(gid == 0) continue; //Better way to handle this?
					
					TileSet currTileSet = null;
					for(int k = 0; k < tilesets.size(); k++){ //Finds tileset which gid belongs to
						currTileSet = tilesets.get(k);
						if(gid >= currTileSet.firstgid && gid <= currTileSet.lastgid) break;
					}
					if(currTileSet == null) System.out.println("Invalid gid");
					
					int destX = x * tileWidth, destY = y * tileWidth;
					gid -= currTileSet.firstgid - 1;
					double origGid = gid;
					int srcY = (int) (gid / currTileSet.tileAmountWidth);
					gid %= currTileSet.tileAmountWidth;
					int srcX = (int) (gid - 1);
					srcY *= tileHeight;
					srcX *= tileWidth;
					//srcY = currTileSet.imageHeight - srcY;
					//int srcX = (int) gid - (currTileSet.tileAmountWidth * srcY) - 1;
					//System.out.println(origGid + " " + srcX + " " + srcY + " " + currTileSet.tileAmountWidth);
					mapTiles[x][y] = new Tile(game, currTileSet.source, destX, destY, srcX, srcY, tileWidth, tileHeight);
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public int getTilesAcross(){
		return mapWidth;
	}
	public int getTilesDown(){
		return mapHeight;
	}
	
	
	public Map(String filePath, Game game){
		this.filePath = filePath;
		this.game = game;
	}
}
