/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity;

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
    public static Image Bullet;
    public static Image Smoke;
    public static Image Explosion;
	public static ArrayList<Image> SmokeArray;
	private static ArrayList<Image> ExplosionArray;
    /*public static List<Rectangle> ExplosionFames ;

    public static SoundEffect Sound_Laser ;
    public static SoundEffect Sound_Explosion ;

    public static SpriteFont Font_Debug ;
     */

    public static void LoadContent(Object o) {

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
         
         SmokeArray = new ArrayList<Image>();
         for(float i=0.1f;i<= 2.1; i+=0.1f) 
        	 SmokeArray.add(Smoke.getScaledInstance((int)(i*Smoke.getWidth(null)), (int)(i*Smoke.getHeight(null)), Image.SCALE_REPLICATE));

         ExplosionArray = new ArrayList<Image>();
         for(float i=0.1f;i<= 2.1; i+=0.1f) 
        	 ExplosionArray.add(Explosion.getScaledInstance((int)(i*Explosion.getWidth(null)), (int)(i*Explosion.getHeight(null)), Image.SCALE_REPLICATE));
         //          Font_Debug = contentManager.Load<SpriteFont>(@"Fonts\DebugFont");

        /* 
        Smoke = contentManager.Load<Image>(@"Sprites\Effects\smoke");

        Explosion = contentManager.Load<Image>(@"Sprites\Effects\explosion");
        ExplosionFames = new List<Rectangle>();
        for (int i = 0; i < 4; i++)
        {
        for (int j = 0; j < 4; j++)
        {
        ExplosionFames.Add(new Rectangle(j*64,i*64,64,64));
        }
        }

        Sound_Laser = null;//contentManager.Load<SoundEffect>(@"Sounds\Effects\Laser");
        Sound_Explosion = contentManager.Load<SoundEffect>(@"Sounds\Effects\Explosion_small");*/
    }
}
