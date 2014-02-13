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
	private int width, height;

	public Sprite(TextureLoader loader, String ref) {
		try {
			texture = loader.getTexture("res/" + ref);
			width = texture.getImageWidth();
			height = texture.getImageHeight();
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

	public void draw(int x, int y) {
		glPushMatrix();

		texture.bind();

		glTranslatef(x, y, 0);

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

	public void draw(int x, int y, int angle) {
		glPushMatrix();

		texture.bind();

		glTranslatef(x, y, 0);
		glRotatef(angle, 0f, 0f, 1f);

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
