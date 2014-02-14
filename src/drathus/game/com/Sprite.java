package drathus.game.com;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.IOException;

public class Sprite {
	private Texture texture;
	private float width, height;
	private float x1, y1, x2, y2; //Tells location of sprite on texture
	
	public Sprite(TextureLoader loader, String ref) {	
		try {
			texture = loader.getTexture("res/" + ref);
			width = texture.getImageWidth();
			height = texture.getImageHeight();
			x2 = texture.getHeight();
			y2 = texture.getWidth();
			x1 = 0;
			y1 = 0;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}
	public Sprite(TextureLoader loader, String ref, int xpos, int ypos, int width, int height) {
		try {
			texture = loader.getTexture("res/" + ref);
			this.width = width;
			this.height = height;
			x1 = 1.0f * xpos / texture.getImageWidth();
			y1 = 1.0f * ypos / texture.getImageHeight();
			x2 = x1 + 1.0f * width / texture.getImageWidth();
			y2 = y1 + 1.0f * height / texture.getImageHeight();
			System.out.println(texture.getImageWidth() + " " + texture.getImageHeight());
			System.out.println(x1 + " "+ x2 + " " + y1 + " "+ y2);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}
	
	public int getWidth() {
		return texture.getImageWidth();
	}

	public int getHeight() {
		return texture.getImageHeight();
	}

	public void draw(int xp, int yp) {
		glPushMatrix();

		texture.bind();
		
		glTranslatef(xp, yp, 0);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(x1, y1);
			glVertex2f(0, 0);

			glTexCoord2f(x1, y2);
			glVertex2f(0, height);

			glTexCoord2f(x2, y2);
			glVertex2f(width, height);

			glTexCoord2f(x2, y1);
			glVertex2f(width, 0);
		}
		glEnd();

		glPopMatrix();
	}

	public void draw(int x, int y, int angle) {
		glPushMatrix();

		texture.bind();

		glTranslatef(x, y, 0);
		//glRotatef(angle, 0f, 0f, 1f);

		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			glTexCoord2f(0, texture.getHeight());
			glVertex2f(0, height);

			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width, height);

			glTexCoord2f(texture.getWidth(), 0);
			glVertex2f(width, 0);
		}
		glEnd();

		glPopMatrix();
	}
}
