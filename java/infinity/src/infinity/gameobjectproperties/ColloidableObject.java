package infinity.gameobjectproperties;

public interface ColloidableObject {

	CollisionSphere CollisionSphere();

	void CollidedWith(ColloidableObject colloidableObject);
}

