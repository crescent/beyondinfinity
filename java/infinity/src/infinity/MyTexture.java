/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package infinity;

import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

public class MyTexture {

	public MyTexture(Image texture) {
		this(texture, 1.0f);
	}

	public MyTexture(Image texture, float scale) {
		Texture = texture;
		Size = new Vector2(texture.getWidth(null), texture.getHeight(null));
		ScaledSize = new Vector2();
		Origin = new Vector2();
		setScale(scale);
		Origin.assign(Size).divide(2.0f);
		CollisionSphere = new CollisionSphere();
		CollisionSphere.Center = new Vector2();
	}

	public Vector2 ScaledSize;

	public Vector2 Origin;

	private float scale;

	public float getScale() {
		return scale;
	}

	public void setScale(float value) {
		scale = value;
		ScaledSize.assign(Size).multiply(scale);
	}

	public Vector2 Size;

	public Image Texture;

	public CollisionSphere GetCollisionSphere(Vector2 gamePosition) {
		CollisionSphere.Center.assign(gamePosition);
		CollisionSphere.Radius = Math.max(ScaledSize.X, ScaledSize.Y) / 2.0f;
		return CollisionSphere;
	}

	public CollisionSphere CollisionSphere;

	public void filterWithColor(Color color) {
		Texture = new Frame().createImage(new FilteredImageSource(Texture.getSource(), new RGBColorFilter(color)));
		
	}

}

class RGBColorFilter extends RGBImageFilter {
    private final Color color;

	public RGBColorFilter (Color color) {
        this.color = color;
		// The filter's operation does not depend on the
        // pixel's location, so IndexColorModels can be
        // filtered directly.
        canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {

        //int r =  (rgb >> 16) & color.getRed();
        //int g = (rgb >> 8) & color.getGreen();
        //int b = (rgb >> 0) & color.getBlue();
    	int r =  ((rgb >> 16)&0xff )* color.getRed() / 0xff;
        int g = ((rgb >> 8)&0xff ) * color.getGreen() / 0xff;
        int b = ((rgb >> 0)&0xff ) * color.getBlue() / 0xff;

        // Return the result
        return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
        
        //return (rgb & color.getRGB());
    }
}
