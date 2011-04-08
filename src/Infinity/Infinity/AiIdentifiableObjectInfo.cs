using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace Infinity
{
    public class AiIdentifiableObjectInfo
    {
        public GameObjectType GameObjectType { get; set; }
        public int GameObjectId { get; set; }
        public Vector2 Position { get; set; }
        public Vector2 Velocity { get; set; }

        public void Create(int gameObjectId, GameObjectType gameObjectType, Vector2 position, Vector2 velocity)
        {
            GameObjectType = gameObjectType;
            GameObjectId = gameObjectId;
            Position = position;
            Velocity = velocity;
        }
    }

    public enum GameObjectType
    {
        Spacecraft,
        Bullet
    }
}
