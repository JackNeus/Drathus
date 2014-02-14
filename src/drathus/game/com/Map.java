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
	
	ArrayList<TileSet> tilesets = new ArrayList<TileSet>();
	
	public void readData(){
		try{
			File mapFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mapFile);
			
			doc.getDocumentElement().normalize();
			
			System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());
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
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Map(String filePath, Game game){
		this.filePath = filePath;
		this.game = game;
	}
}
