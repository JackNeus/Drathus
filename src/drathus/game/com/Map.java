package drathus.game.com;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Map {
	String filePath;
	
	public void readXML(){
		try{
			File mapFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Map(String filePath){
		this.filePath = filePath;
	}
}
