using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    public class Bullet : GameObject, IColloidableObject, IAiIdentifiableObject
    {
        private readonly int ownerId;
        private MyTexture myTexture;

        private static int count;
        private readonly int myCount;

        public float Energy { get; private set; }
        private bool energyUp;
        private bool dead;
        const int initialEnergy = 50;

        private Vector2 Velocity { get; set; }

        public Bullet(GameWorld gameWorld, Vector2 gamePosition, Vector2 velocity, int ownerId)
            : base(gameWorld, gamePosition)
        {
            this.ownerId = ownerId;
            myTexture = new MyTexture(MyContentManager.Bullet);
            myCount = count++;
            CreateBullet(gamePosition, velocity);
        }

        private void CreateBullet(Vector2 gamePosition, Vector2 velocity)
        {
            this.gamePosition = gamePosition;
            Velocity = velocity;
            Energy = initialEnergy;
            energyUp = true;
            dead = false;
        }

        public override void Draw(SpriteBatch spriteBatch)
        {
            if(!IsAlive) return;

            //spriteBatch.DrawString(font, ((gamePosition - CameraPosition).X + " " + (gamePosition - CameraPosition).Y), new Vector2(0, (myCount%30) * 20 + 100), Color.White);
            spriteBatch.Draw(myTexture.Texture, PositionOnScreen, null, Color.Red, 0, myTexture.Origin, TextureScale,
                             SpriteEffects.None, 0.0f);
        }

        public override void Update()
        {
            gamePosition += Velocity;
            if (energyUp && Energy >= 100) energyUp = false;
            if (energyUp) Energy+=10;
            if (!energyUp) Energy-=.25f;
        }

        private float TextureScale
        {
            get
            {
                return Energy / 50.0f;
            }
        }

        public override bool IsAlive
        {
            get
            {
                return !dead && Energy >= initialEnergy;
            }
        }

        public CollisionSphere CollisionSphere
        {
            get
            {
                myTexture.Scale = TextureScale;
                return myTexture.GetCollisionSphere(gamePosition);
            }
        }

        public void CollidedWith(IColloidableObject colloidableObject)
        {
            MyContentManager.Sound_Explosion.Play(Math.Max(0, 0.1f - MyMath.Distance(gameWorld.CameraPosition, gamePosition) / 4000.0f / 10));
            gameWorld.Add(new BulletExplosion(gameWorld, gamePosition, Energy));
            dead = true;
        }

        public void PopulateGameObjectInfo(AiIdentifiableObjectInfo info)
        {
            info.Create(ownerId, GameObjectType.Bullet, gamePosition, Velocity);
        }
    }
}
