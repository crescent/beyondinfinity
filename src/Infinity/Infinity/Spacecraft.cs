using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    public class Spacecraft : GameObject, IColloidableObject, IAiIdentifiableObject
    {
        protected MyTexture myTexture;
        protected MyTexture myTextureOverlay;

        private Color color;
        private Color colorOverlay;

        private static int count;
        private int myCount;

        private float direction;
        private Vector2 velocity;

        private float energy;
        private bool dead;

        private ISpacecraftController spacecraftController;

        public Vector2 GamePosition
        {
            get { return gamePosition; }
        }

        public static Spacecraft CreatePlayer(GameWorld gameWorld, Vector2 gamePosition)
        {
            var spacecraft = new Spacecraft(gameWorld, gamePosition);
            spacecraft.spacecraftController = new PlayerSpacecraftController();
//            spacecraft.myTexture = new MyTexture(MyContentManager.Spacecraft1);
//            spacecraft.myTextureOverlay = new MyTexture(MyContentManager.Spacecraft1_Overlay);
//            spacecraft.color = Color.Wheat;
//            spacecraft.colorOverlay = Color.Snow;
            return spacecraft;
        }

        public static Spacecraft CreateEnemy(GameWorld gameWorld, Vector2 gamePosition)
        {
            var spacecraft = new Spacecraft(gameWorld, gamePosition);
//            spacecraft.myTexture = new MyTexture(MyContentManager.Spacecraft2);
//            spacecraft.myTextureOverlay = new MyTexture(MyContentManager.Spacecraft2_Overlay);
//            spacecraft.color = Color.Pink;
//            spacecraft.colorOverlay = Color.LightPink;
            spacecraft.spacecraftController = new SpacecraftControllerAI();
            return spacecraft;
        }

        private Spacecraft(GameWorld gameWorld, Vector2 gamePosition) :
            base(gameWorld, gamePosition)
        {
            energy = 100;
            dead = false;
            myCount = ++count;

            switch (MyMath.Random.Next(4))
            {
                case 0: 
                    myTexture = new MyTexture(MyContentManager.Spacecraft0);
                    myTextureOverlay = new MyTexture(MyContentManager.Spacecraft0_Overlay);
                    break;
                case 1:
                    myTexture = new MyTexture(MyContentManager.Spacecraft1);
                    myTextureOverlay = new MyTexture(MyContentManager.Spacecraft1_Overlay);
                    break;
                case 2:
                    myTexture = new MyTexture(MyContentManager.Spacecraft2);
                    myTextureOverlay = new MyTexture(MyContentManager.Spacecraft2_Overlay);
                    break;
                case 3:
                    myTexture = new MyTexture(MyContentManager.Spacecraft3);
                    myTextureOverlay = new MyTexture(MyContentManager.Spacecraft3_Overlay);
                    break;
                default:
                    throw new Exception();
            }

            color = new Color(Color1(), Color1(), Color1());
            colorOverlay = new Color(Color1(), Color1(), Color1());

        }

        private static float Color1()
        {
            return (float) (0.4 + 0.6*MyMath.Random.NextDouble());
        }

        public override void Draw(SpriteBatch spriteBatch)
        {
            spriteBatch.Draw(myTexture.Texture, PositionOnScreen, null, color, (float) (Math.PI - direction), myTexture.Origin, myTexture.Scale, SpriteEffects.None, 0.0f);
            spriteBatch.Draw(myTextureOverlay.Texture, PositionOnScreen, null, colorOverlay, (float)(Math.PI - direction), myTexture.Origin, myTexture.Scale, SpriteEffects.None, 0.0f);

//            spriteBatch.Draw(MyContentManager.Bullet, TargetReticle  + PositionOnScreen, null, Color.Yellow, 0, new Vector2(5,5), 2,
//                             SpriteEffects.None, 0.0f);

            var angle = velocity.Y / velocity.X;
            Vector2 velocityLimit = 1 * new Vector2((float)Math.Cos(angle), (float)Math.Sin(angle));

            spriteBatch.DrawString(MyContentManager.Font_Debug, string.Format("{2} Spacecraft {0} Energy: {1}", myCount, (int)energy, GameObjectId), new Vector2(0, myCount * 20), Color.White);
        }

        public Vector2 TargetReticle { get; set; }

        public void FireBullet()
        {
            var distanceFromCenter = myTexture.CollisionSphere.Radius + 10;
            var normalizingValue = new Vector2((float)(distanceFromCenter * Math.Sin(direction)), (float)(distanceFromCenter * Math.Cos(direction)));
            Vector2 bulletStartingPosition = gamePosition + normalizingValue;

            float max = Math.Max(Math.Abs(velocity.X), Math.Abs(velocity.Y));
            Vector2 normalizedVelocity = new Vector2(velocity.X / max, velocity.Y / max);

            gameWorld.Add(new Bullet(gameWorld, bulletStartingPosition, normalizedVelocity * 5 + velocity, GameObjectId));
            energy -= .1f;
            //MyContentManager.Sound_Laser.Play(Math.Max(0,1-MyMath.Distance(gameWorld.CameraPosition, gamePosition)/2000.0f));
        }

        protected void Rotate(double angle)
        {
            direction = (float) angle;
        }

        public void RotataeAndMove(Vector2 velocity)
        {
            this.velocity = MyMath.LimitVelocity(velocity, 80f) / 5;
            Rotate(MyMath.GetAngle(velocity));
            gamePosition += this.velocity;
        }

        public CollisionSphere CollisionSphere
        {
            get
            {
                return myTexture.GetCollisionSphere(gamePosition);
            }
        }

        public void CollidedWith(IColloidableObject colloidableObject)
        {
            if(colloidableObject is Bullet)
            {
                Bullet bullet = colloidableObject as Bullet;
                energy -= bullet.Energy/100.0f;
            }
        }

        public override void Update()
        {
            CheckDeath();
            if(energy <100) energy+=.01f;
            spacecraftController.Control(gameWorld, this);
        }

        private void CheckDeath()
        {
            if (energy > 0) return;

            dead = true;
            for (int i = 0; i < 10; i++)
            {
                gameWorld.Add(new BulletExplosion(gameWorld, 
                    new Vector2(gamePosition.X + MyMath.Random.Next() % 20 - 10, 
                        gamePosition.Y + MyMath.Random.Next() % 20 - 10),
                    100 + MyMath.Random.Next() % 100));
                MyContentManager.Sound_Explosion.Play(Math.Max(0, 0.2f - MyMath.Distance(gameWorld.CameraPosition, gamePosition) / 4000.0f / 10));
            }
        }

        public override bool IsAlive
        {
            get { return !dead; }
        }

        public Vector2 Velocity
        {
            get { return velocity; }
        }

        public float Energy
        {
            get { return energy; }
        }

        public void PopulateGameObjectInfo(AiIdentifiableObjectInfo info)
        {
            info.Create(GameObjectId, GameObjectType.Spacecraft, gamePosition, velocity);
        }
    }
}
