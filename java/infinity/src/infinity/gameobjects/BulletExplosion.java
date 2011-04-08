package infinity.gameobjects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import infinity.*;
import infinity.gameengine.Vector2;

    public class BulletExplosion extends GameObject
    {
        protected MyTexture smoke;
        protected MyTexture explosion;
        protected int explosionFrame;

        private float energy;

        public BulletExplosion(GameWorld gameWorld, Vector2 gamePosition, float energy) 
        {
        	super(gameWorld, gamePosition);
            this.energy = energy;
            smoke = MyContentManager.SmokeTexture;
            explosion = MyContentManager.ExplosionTexture;
            explosionFrame = 0;
        }

        public void Draw(Graphics2D g)
        {
        	AffineTransform affineTransform = new AffineTransform();
        	affineTransform.translate(PositionOnScreen().X, PositionOnScreen().Y);
        	Image image = MyContentManager.SmokeArray.get(
        			MyMath.MapToRange((int) energy,0,200,0,MyContentManager.SmokeArray.size()-1));
        	affineTransform.translate(-image.getWidth(null)/2, -image.getHeight(null)/2);
        	g.drawImage(image, affineTransform, null);
            //spriteBatch.Draw(smoke.Texture, PositionOnScreen, null,Color.White, 0, smoke.Origin, energy / 100.0f,SpriteEffects.None,0);
        	//if(explosionFrame<64)
            //	g.drawImage(explosion.Texture, affineTransform, null);
            //spriteBatch.Draw(explosion.Texture, PositionOnScreen, MyContentManager.ExplosionFames[explosionFrame/4],
            //    Color.White, 0, smoke.Origin, energy / 100.0f,SpriteEffects.None,0);
            explosionFrame++;
        }

        public void Update()
        {
            energy--;
        }

        public boolean IsAlive()
        {
             return energy>0;
        }

		@Override
		public int getGameObjectTypeId() {
			// TODO Auto-generated method stub
			return 0;
		}
    }
