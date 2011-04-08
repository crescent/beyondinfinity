using Microsoft.Xna.Framework;

namespace Infinity
{
    public interface IColloidableObject
    {
        CollisionSphere CollisionSphere { get; }
        void CollidedWith(IColloidableObject colloidableObject);
    }

    public class CollisionSphere
    {
        public Vector2 Center { get; set; }
        public float Radius { get; set; }

        public bool Intersects(CollisionSphere sphere)
        {
            return MyMath.Distance(this.Center, sphere.Center) < this.Radius + sphere.Radius;
        }
    }
}