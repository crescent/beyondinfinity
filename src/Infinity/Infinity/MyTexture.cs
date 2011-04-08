using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Infinity
{
    public class MyTexture
    {

        public MyTexture(Texture2D texture) : this(texture, 1.0f)
        {
        }

        public MyTexture(Texture2D texture, float scale)
        {
            Texture = texture;
            Size = new Vector2(texture.Width, texture.Height);
            Scale = scale;
            Origin = Size/2.0f;
            CollisionSphere = new CollisionSphere();
        }

        public Vector2 ScaledSize { get; private set; }
        
        public Vector2 Origin { get; private set; }

        private float scale;

        public float Scale
        {
            get { return scale; }
            set
            {
                scale = value;
                ScaledSize = Scale * Size;
            }
        }

        public Vector2 Size { get; private set; }

        public Texture2D Texture { get; private set; }
        
        public CollisionSphere CollisionSphere { get; private set; }

        public CollisionSphere GetCollisionSphere(Vector2 gamePosition)
        {
            CollisionSphere.Center = gamePosition;
            CollisionSphere.Radius = Math.Max(ScaledSize.X, ScaledSize.Y) / 2.0f;
            return CollisionSphere;
        }
             
    }
}
