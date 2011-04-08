using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    public class Background : GameObject
    {
        protected MyTexture myTexture;

        public Background(GameWorld gameWorld) : base(gameWorld,new Vector2())
        {
            myTexture = new MyTexture(MyContentManager.Background, 2);
        }

        public override void Draw(SpriteBatch spriteBatch)
        {
            var arr = new SpriteEffects[3][]
                          {
                              new SpriteEffects[3], 
                              new SpriteEffects[3], 
                              new SpriteEffects[3]
                          };

            arr[0][0] = arr[2][0] = arr[0][2] = arr[2][2] = SpriteEffects.FlipVertically | SpriteEffects.FlipHorizontally;
            arr[0][1] = arr[2][1] = SpriteEffects.FlipHorizontally;
            arr[1][0] = arr[1][2] = SpriteEffects.FlipVertically;
            arr[1][1] = SpriteEffects.None;

            var position1 = PositionOnScreen;
            position1.X = position1.X % (myTexture.ScaledSize.X*2);
            position1.Y = position1.Y % (myTexture.ScaledSize.Y*2);

            for (int i = -1; i < 2; i++)
                for (int j = -1; j < 2; j++)
                    DrawPictureWithWrap(arr, new Vector2(position1.X + i * myTexture.ScaledSize.X * 2, position1.Y + j * myTexture.ScaledSize.Y * 2), spriteBatch);
        }

        private void DrawPictureWithWrap(SpriteEffects[][] arr, Vector2 position1, SpriteBatch spriteBatch)
        {
            for (int i = 0; i < 3; i++)
            {
                ;
                for (int j = 0; j < 3; j++)
                {
                    SpriteEffects spriteEffects = arr[i][j];
//                    spriteBatch.DrawString(font, (CameraPosition.X + " " + CameraPosition.Y + " " + gamePosition.X + " " + gamePosition.Y).ToString(), new Vector2(0, j * 100 + 200), Color.White);
                    spriteBatch.Draw(myTexture.Texture, new Vector2(position1.X + myTexture.ScaledSize.X * i, position1.Y + myTexture.ScaledSize.Y * j), 
                                     null, Color.White, 0, new Vector2(0, 0), myTexture.Scale, spriteEffects, 1.0f);

//                    spriteBatch.DrawString(MyContentManager.Font_Debug, spriteEffects.ToString(), new Vector2(position1.X + myTexture.ScaledSize.X * i, position1.Y + myTexture.ScaledSize.Y * j), Color.White);

                }
            }
        }
    }
}