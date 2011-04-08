using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    public class GameWorld
    {
        private readonly Vector2 screenCenter;
        private List<GameObject> gameObjects;

        public GameWorld(Vector2 cameraPosition, Vector2 screenCenter)
        {
            this.CameraPosition = cameraPosition;
            this.screenCenter = screenCenter;
            gameObjects = new List<GameObject>();
        }

        public Vector2 ScreenCenter
        {
            get { return screenCenter; }
        }

        public Vector2 CameraPosition { get; private set; }

        public void Add(GameObject gameObject)
        {
            gameObjects.Add(gameObject);
        }

//        public GameObject Recycle(Type type)
//        {
//            foreach (var gameObject in gameObjects)
//                if(!gameObject.IsAlive && gameObject.GetType() == type)
//        }

        public void UpdateCamera(Vector2 position)
        {
            CameraPosition = position;
        }

        public void Draw(SpriteBatch spriteBatch)
        {
            foreach (var gameObject in gameObjects)
                if (!(gameObject is Bullet || gameObject is BulletExplosion))
                    gameObject.Draw(spriteBatch);

            spriteBatch.End();

            spriteBatch.Begin(SpriteBlendMode.Additive);

            foreach (var gameObject in gameObjects)
                if ((gameObject is Bullet || gameObject is BulletExplosion))
                    gameObject.Draw(spriteBatch);

            spriteBatch.End();
            spriteBatch.Begin();
        }

        public void Update()
        {
            gameObjects.RemoveAll(o => !o.IsAlive);
            foreach (var gameObject in gameObjects.ToArray())
            {
                if (gameObject is Spacecraft) CheckCollisions(gameObject as IColloidableObject);
                gameObject.Update();
            }
        }

        public List<AiIdentifiableObjectInfo> GetIdentifiableObjects()
        {
            List<AiIdentifiableObjectInfo> gameObjectInfos = new List<AiIdentifiableObjectInfo>();

            foreach (GameObject gameObject in gameObjects)
            {
                if (gameObject is IAiIdentifiableObject)
                {
                    var gameObjectInfo = new AiIdentifiableObjectInfo();
                    (gameObject as IAiIdentifiableObject).PopulateGameObjectInfo(gameObjectInfo);
                    gameObjectInfos.Add(gameObjectInfo);
                }
            }
            return gameObjectInfos;
        }

        public void CheckCollisions(IColloidableObject spacecraft)
        {
            foreach (var gameObject in gameObjects.ToArray())
            {
                if(gameObject is IColloidableObject)
                {
                    var colloidableObject = gameObject as IColloidableObject;
                    if(spacecraft.CollisionSphere.Intersects(colloidableObject.CollisionSphere))
                    {
                        spacecraft.CollidedWith(colloidableObject);
                        colloidableObject.CollidedWith(spacecraft);
                    }
                }
            }
        }
    }
}
