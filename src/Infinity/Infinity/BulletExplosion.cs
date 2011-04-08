using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    internal class BulletExplosion : GameObject
    {
        protected MyTexture smoke;
        protected MyTexture explosion;
        protected int explosionFrame;

        private float energy;

        public BulletExplosion(GameWorld gameWorld, Vector2 gamePosition, float energy) : base(gameWorld, gamePosition)
        {
            this.energy = energy;
            smoke = new MyTexture(MyContentManager.Smoke);
            explosion = new MyTexture(MyContentManager.Explosion);
            explosionFrame = 0;
        }

        public override void Draw(SpriteBatch spriteBatch)
        {
            spriteBatch.Draw(smoke.Texture, PositionOnScreen, null,Color.White, 0, smoke.Origin, energy / 100.0f,SpriteEffects.None,0);
            if(explosionFrame<64)
            spriteBatch.Draw(explosion.Texture, PositionOnScreen, MyContentManager.ExplosionFames[explosionFrame/4],
                Color.White, 0, smoke.Origin, energy / 100.0f,SpriteEffects.None,0);
            explosionFrame++;
        }

        public override void Update()
        {
            energy--;
        }

        public override bool IsAlive
        {
            get { return energy>0; }
        }
    }
}