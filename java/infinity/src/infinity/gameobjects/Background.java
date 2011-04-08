
package infinity.gameobjects;

import infinity.GameWorld;
import infinity.MyContentManager;
import infinity.MyTexture;
import infinity.gameengine.Vector2;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;


    public class Background extends GameObject
    {
        protected MyTexture myTexture;

        public Background(GameWorld gameWorld) 
        {
            super(gameWorld,new Vector2());
            myTexture = new MyTexture(MyContentManager.Background, 2);
        }

        Vector2 _drawPicturePosition = new Vector2();
        
        public void Draw(Graphics2D g)
        {
            /*var arr = new SpriteEffects[3][]
                          {
                              new SpriteEffects[3],
                              new SpriteEffects[3],
                              new SpriteEffects[3]
                          };

            arr[0][0] = arr[2][0] = arr[0][2] = arr[2][2] = SpriteEffects.FlipVertically | SpriteEffects.FlipHorizontally;
            arr[0][1] = arr[2][1] = SpriteEffects.FlipHorizontally;
            arr[1][0] = arr[1][2] = SpriteEffects.FlipVertically;
            arr[1][1] = SpriteEffects.None;*/

            Vector2 position1 = PositionOnScreen();
            //position1.X = position1.X % (myTexture.ScaledSize.X*2);
            //position1.Y = position1.Y % (myTexture.ScaledSize.Y*2);
            position1.X = position1.X % (myTexture.Size.X);
            position1.Y = position1.Y % (myTexture.Size.Y);

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    //DrawPictureWithWrap(null, new Vector2(position1.X + i * myTexture.ScaledSize.X * 2, position1.Y + j * myTexture.ScaledSize.Y * 2), g);
                	_drawPicturePosition.X = position1.X + i * myTexture.Size.X;
                	_drawPicturePosition.Y = position1.Y + j * myTexture.Size.Y;
                	DrawPictureWithWrap(null, _drawPicturePosition, g);
                }
            }
        }

        private void DrawPictureWithWrap(Object arr, Vector2 position1, Graphics2D g)
        {
            //for (int i = 0; i < 3; i++)
            {
                ;
                //for (int j = 0; j < 3; j++)
                {
                    //SpriteEffects spriteEffects = arr[i][j];
//                    spriteBatch.DrawString(font, (CameraPosition.X + " " + CameraPosition.Y + " " + gamePosition.X + " " + gamePosition.Y).ToString(), new Vector2(0, j * 100 + 200), Color.White);
                    AffineTransform affineTransform = new AffineTransform();
                    affineTransform.translate(position1.X , position1.Y);
                    //int xx=position1.X + myTexture.ScaledSize.X * i;
                   // int yy=position1.Y + myTexture.ScaledSize.Y * j;
//                    g.drawString("AAA 123"+String.valueOf(xx)
//                            +"xx"+String.valueOf(yy), 300, 100);
                    //g.drawImage(myTexture.Texture, position1.X,position1.Y, null);
                    g.drawImage(myTexture.Texture, affineTransform, null);
                    //g.Draw(myTexture.Texture, new Vector2(position1.X + myTexture.ScaledSize.X * i, position1.Y + myTexture.ScaledSize.Y * j),
                    //                 null, Color.White, 0, new Vector2(0, 0), myTexture.Scale, spriteEffects, 1.0f);

//                    spriteBatch.DrawString(MyContentManager.Font_Debug, spriteEffects.ToString(), new Vector2(position1.X + myTexture.ScaledSize.X * i, position1.Y + myTexture.ScaledSize.Y * j), Color.White);

                }
            }
        }

    @Override
    public void Update() {
    }

	@Override
	public int getGameObjectTypeId() {
		// TODO Auto-generated method stub
		return 0;
	}

    }
