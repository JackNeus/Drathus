package drathus.game.com;

import static org.lwjgl.opengl.GL11.*;

/**
 * This class creates a texture to draw an image.
 * For more information, visit http://lwjgl.org/wiki/index.php?title=Examples:SpaceInvaders_Texture
 * PS: when you put two stars at the beginning, it creates the javadoc comment.
 * These are good for when you hover over the class/method name somewhere else in the source, 
 * and it tells you all this information.
 * @author Abiram/Jackson inc.
 */
public class Texture {
	private int target; //GL target type

	//GL texture ID, width and height of image, texture width and height
	private int textureID, width, height, texWidth, texHeight;

	//ratio of width and height of image to the texture
	private float widthRatio, heightRatio;

	/**
	 * Create a new texture
	 * @param target The GL target
	 * @param textureID The GL texture ID
	 */
	public Texture(int target, int textureID) {
		this.target = target;
		this.textureID = textureID;
	}

	/**
	 * Bind a texture so that openGL can extract data from it
	 */
	public void bind() {
		glBindTexture(target, textureID);
	}

	/**
	 * set height of an image
	 * @param height The height of the image
	 */
	public void setHeight(int height) {
		this.height = height;
		setHeight(); //see below
	}

	/**
	 * set width of an image
	 * @param width The width of the image
	 */
	public void setWidth(int width) {
		this.width = width;
		setWidth(); //see below
	}

	/**
	 * Get the height of the original image
	 * @return The height of the original image
	 */
	public int getImageHeight() {
		return height;
	}

	/**
	 * Get the width of the original image
	 * @return The width of the original image
	 */
	public int getImageWidth() {
		return width;
	}

	/**
	 * Get height of the physical texture
	 * @return The height of physical texture
	 */
	public float getHeight() {
		return heightRatio;
	}

	/**
	 * Get width of the physical texture
	 * @return The width of physical texture
	 */
	public float getWidth() {
		return widthRatio;
	}

	/**
	 * Set the height of this texture
	 * @param texHeight The height of this texture
	 */
	public void setTextureHeight(int texHeight) {
		this.texHeight = texHeight;
		setHeight();
	}

	/**
	 * Set the width of this texture
	 * @param texWidth The width of this texture
	 */
	public void setTextureWidth(int texWidth) {
		this.texWidth = texWidth;
		setWidth();
	}

	/**
	 * Set height of texture, and update ratio.
	 */
	private void setHeight() {
		if (texHeight != 0) heightRatio = ((float) height) / texHeight;
	}

	/**
	 * Set width of texture, and update ratio.
	 */
	private void setWidth() {
		if (texWidth != 0) widthRatio = ((float) width) / texWidth;
	}
}
