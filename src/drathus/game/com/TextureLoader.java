package drathus.game.com;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

/**
 * Uses an image to create a Texture
 * For more information, visit http://lwjgl.org/wiki/index.php?title=Examples:SpaceInvaders_TextureLoader
 * @author Abiram
 * @author Jackson
 */
public class TextureLoader {
	//table that contains all the different textures that can be used
	private HashMap<String, Texture> table = new HashMap<String, Texture>();

	//colour models, one includes alpha for transparency
	private ColorModel glAlphaColorModel, glColorModel;

	//buffer for texture's ID
	private IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

	/**
	 * Create new TextureLoader
	 */
	public TextureLoader() {
		glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
	}

	/**
	 * Create a new texture ID
	 * @return A new texture ID
	 */
	public int createTextureID() {
		glGenTextures(textureIDBuffer);
		return textureIDBuffer.get(0);
	}

	/**
	 * Load a texture
	 * @param resourceName The location of the resource to load
	 * @return The loaded texture
	 * @throws IOException Indicates failure to access the resource
	 */
	public Texture getTexture(String resourceName) throws IOException {
		Texture tex = table.get(resourceName);
		if (tex != null) return tex;
		tex = getTexture(resourceName, GL_TEXTURE_2D, GL_RGBA, GL_LINEAR, GL_LINEAR);
		table.put(resourceName, tex);
		return tex;
	}

	/**
	 * Load a texture into OpenGL from a image reference on disk
	 * @param resourceName The location of the resource to load
	 * @param target The GL target to load the texture against
	 * @param dstPixelFormat The pixel format of the screen
	 * @param minFilter The minimizing filter
	 * @param magFilter The maximizing filter
	 * @return The loaded texture
	 * @throws IOException Indicates a failure to access the resource
	 */
	public Texture getTexture(String resourceName, int target, int dstPixelFormat, int minFilter, int magFilter) throws IOException {
		int srcPixelFormat;

		//create textureID for this texture
		int textureID = createTextureID();
		Texture texture = new Texture(target, textureID);

		//bind this texture
		glBindTexture(target, textureID);

		BufferedImage bufferedImage = loadImage(resourceName);
		//if(bufferedImage == null) System.out.println("Resource could not be found.");
		//else System.out.println("Resource was found.");
		texture.setWidth(bufferedImage.getWidth());
		texture.setHeight(bufferedImage.getHeight());

		if (bufferedImage.getColorModel().hasAlpha()) srcPixelFormat = GL_RGBA;
		else srcPixelFormat = GL_RGB;

		//convert image into byte buffer of texture data
		ByteBuffer textureBuffer = convertImageData(bufferedImage, texture);

		if (target == GL_TEXTURE_2D) {
			glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
			glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
		}

		//produce a texture from the byte buffer
		glTexImage2D(target, 0, dstPixelFormat, get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()), 0, srcPixelFormat, GL_UNSIGNED_BYTE, textureBuffer);
		return texture;
	}

	/**
	 * Get the closest greater power of 2 to the fold number
	 * @param fold The target number
	 * @return The power of 2
	 */
	private static int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret *= 2;
		}
		return ret;
	}

	/**
	 * Convert the buffered image to a texture
	 * @param bufferedImage The image to convert to a texture
	 * @param texture the texture to store the data into
	 * @return A buffer containing the data
	 */
	private ByteBuffer convertImageData(BufferedImage bufferedImage, Texture texture) {
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;

		int texWidth = 2, texHeight = 2;

		//find closest power of 2 for the width and height of the texture
		while (texWidth < bufferedImage.getWidth())
			texWidth *= 2;
		while (texHeight < bufferedImage.getHeight())
			texHeight *= 2;

		texture.setTextureHeight(texHeight);
		texture.setTextureWidth(texWidth);

		//create a raster that can be used by OpenGL as a source for a texture
		if (bufferedImage.getColorModel().hasAlpha()) {
			//System.out.println("Image has alpha");
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
			texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
		} else {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
			texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
		}

		//copy source image into produced image
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, texWidth, texHeight);
		g.drawImage(bufferedImage, 0, 0, null);

		//build a byte buffer from the temporary image to be used by OpenGL to produce a texture
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

		imageBuffer = ByteBuffer.allocateDirect(data.length);
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);
		imageBuffer.flip();

		return imageBuffer;
	}

	/**
	 * Load a given resource as a buffered image
	 * @param ref The location of the resource to load
	 * @return The loaded buffered image
	 * @throws IOException Indicates a failure to find a resource
	 */
	private BufferedImage loadImage(String ref) throws IOException {
		Image img = new ImageIcon(ref).getImage();
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

}
