/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class MyContentManager {

	public static Image Background;
	public static Image Spacecraft0;
	public static Image Spacecraft0_Overlay;
	public static Image Spacecraft1;
	public static Image Spacecraft1_Overlay;
	public static Image Spacecraft2;
	public static Image Spacecraft2_Overlay;
	public static Image Spacecraft3;
	public static Image Spacecraft3_Overlay;
	private static Image Bullet;
	public static MyTexture BulletTexture;
	private static Image Smoke;
	public static MyTexture SmokeTexture;
	private static Image Explosion;
	public static MyTexture ExplosionTexture;
	public static ArrayList<Image> SmokeArray;
	private static ArrayList<Image> ExplosionArray;
	/*
	 * public static List<Rectangle> ExplosionFames ;
	 * 
	 * public static SoundEffect Sound_Laser ; public static SoundEffect
	 * Sound_Explosion ;
	 * 
	 * public static SpriteFont Font_Debug ;
	 */

	public static Object mutex = new Object();
	public static boolean loaded = false;

	public static void LoadContent(Object o) {
    	 
		// ArrayLists were having null elements without this
    	synchronized (mutex) {
    		if(loaded) return;

    	 Spacecraft0 =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship0.png")).getImage();
         Spacecraft0_Overlay =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship0overlay.png")).getImage();

         Spacecraft1 =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship1.png")).getImage();
         Spacecraft1_Overlay =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship1overlay.png")).getImage();

         Spacecraft2 =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship2.png")).getImage();
         Spacecraft2_Overlay =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship2overlay.png")).getImage();
         
         Spacecraft3 =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship3.png")).getImage();
         Spacecraft3_Overlay =new ImageIcon(o.getClass().getResource("/sprites/spacecraft/ship3overlay.png")).getImage();

         //Background = new ImageIcon(o.getClass().getResource("/sprites/background/3d_space.jpg")).getImage();
         Background = new ImageIcon(o.getClass().getResource("/sprites/background/3dspace2x.jpg")).getImage();
         
         
         Bullet = new ImageIcon(o.getClass().getResource("/sprites/bullets/bullet.png")).getImage();
         Smoke = new ImageIcon(o.getClass().getResource("/sprites/effects/smoke.png")).getImage();
         Explosion = new ImageIcon(o.getClass().getResource("/sprites/effects/explosion.png")).getImage();

         BulletTexture = new MyTexture(MyContentManager.Bullet);
         BulletTexture.filterWithColor(Color.RED);
         SmokeTexture = new MyTexture(MyContentManager.Smoke);
         ExplosionTexture = new MyTexture(MyContentManager.Explosion);
         
         SmokeArray = new ArrayList<Image>();
			for (float i = 0.1f; i <= 2.1; i += 0.1f)
				SmokeArray.add(Smoke.getScaledInstance((int) (i * Smoke.getWidth(null)), (int) (i * Smoke
						.getHeight(null)), Image.SCALE_REPLICATE));

         ExplosionArray = new ArrayList<Image>();
         for(float i=0.1f;i<= 2.1; i+=0.1f) 
        	 ExplosionArray.add(Explosion.getScaledInstance((int)(i*Explosion.getWidth(null)), (int)(i*Explosion.getHeight(null)), Image.SCALE_REPLICATE));
         //          Font_Debug = contentManager.Load<SpriteFont>(@"Fonts\DebugFont");

        /* 

        Sound_Laser = null;//contentManager.Load<SoundEffect>(@"Sounds\Effects\Laser");
        Sound_Explosion = contentManager.Load<SoundEffect>(@"Sounds\Effects\Explosion_small");*/
 		loaded=true;
		}
    }
}
