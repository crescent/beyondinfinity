using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    public abstract class GameObject
    {
        private static int gameObjectIdCount = 0;

        public int GameObjectId
        {
            get; private set;
        }

        protected Vector2 CameraPosition
        {
            get { return gameWorld.CameraPosition; }
        }

        protected Vector2 ScreenCenter
        {
            get { return gameWorld.ScreenCenter; }
        }

        protected readonly GameWorld gameWorld;
        protected Vector2 gamePosition;

        protected GameObject(GameWorld gameWorld, Vector2 gamePosition)
        {
            this.gameWorld = gameWorld;
            this.gamePosition = gamePosition;
            GameObjectId = gameObjectIdCount++;
        }

        public virtual void Update()
        {
            
        }

        public abstract void Draw(SpriteBatch spriteBatch);

        protected Vector2 PositionOnScreen
        {
            get { return ScreenCenter - CameraPosition + gamePosition; ; }
        }

        public virtual bool IsAlive
        {
            get { return true; }
        }
    }
}