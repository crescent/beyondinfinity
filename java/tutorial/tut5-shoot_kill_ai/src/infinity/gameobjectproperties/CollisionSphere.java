package infinity.gameobjectproperties;

import infinity.*;
import infinity.gameengine.Vector2;

public class CollisionSphere
{
    public Vector2 Center;
    public float Radius;

    public boolean Intersects(CollisionSphere sphere)
    {
        return MyMath.Distance(this.Center, sphere.Center) < this.Radius + sphere.Radius;
    }
}

