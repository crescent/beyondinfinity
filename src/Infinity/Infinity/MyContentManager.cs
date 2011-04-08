using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    public class MyContentManager
    {
        public static Texture2D Background { get; private set; }

        public static Texture2D Spacecraft0 { get; private set; }
        public static Texture2D Spacecraft0_Overlay { get; private set; }

        public static Texture2D Spacecraft1 { get; private set; }
        public static Texture2D Spacecraft1_Overlay { get; private set; }
        
        public static Texture2D Spacecraft2 { get; private set; }
        public static Texture2D Spacecraft2_Overlay { get; private set; }
        
        public static Texture2D Spacecraft3 { get; private set; }
        public static Texture2D Spacecraft3_Overlay { get; private set; }
        
        public static Texture2D Bullet { get; private set; }
        public static Texture2D Smoke { get; private set; }

        public static Texture2D Explosion { get; private set; }
        public static List<Rectangle> ExplosionFames { get; private set; }
        
        public static SoundEffect Sound_Laser { get; private set; }
        public static SoundEffect Sound_Explosion { get; private set; }

        public static SpriteFont Font_Debug { get; private set; }

        public static void LoadContent(ContentManager contentManager)
        {
            Font_Debug = contentManager.Load<SpriteFont>(@"Fonts\DebugFont");

            Background = contentManager.Load<Texture2D>(@"Sprites\Background\3d_space_6");
            
            Spacecraft0 = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship0");
            Spacecraft0_Overlay = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship0Overlay");
            
            Spacecraft1 = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship1");
            Spacecraft1_Overlay = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship1Overlay");
            
            Spacecraft2 = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship2");
            Spacecraft2_Overlay = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship2Overlay");

            Spacecraft3 = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship3");
            Spacecraft3_Overlay = contentManager.Load<Texture2D>(@"Sprites\Spacecraft\ship3Overlay");

            Bullet = contentManager.Load<Texture2D>(@"Sprites\Bullets\bullet");
            Smoke = contentManager.Load<Texture2D>(@"Sprites\Effects\smoke");
            
            Explosion = contentManager.Load<Texture2D>(@"Sprites\Effects\explosion");
            ExplosionFames = new List<Rectangle>();
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    ExplosionFames.Add(new Rectangle(j*64,i*64,64,64));
                }
            }

            Sound_Laser = null;//contentManager.Load<SoundEffect>(@"Sounds\Effects\Laser");
            Sound_Explosion = contentManager.Load<SoundEffect>(@"Sounds\Effects\Explosion_small");
        }
    }
}
