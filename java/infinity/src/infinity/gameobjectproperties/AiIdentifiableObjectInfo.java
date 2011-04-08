package infinity.gameobjectproperties;

import infinity.MyUUID;
import infinity.gameengine.Vector2;

public class AiIdentifiableObjectInfo {

	public MyUUID GameObjectId;
	public Vector2 Position;
	public GameObjectType GameObjectType;
	public Vector2 Velocity;

    public void Create(MyUUID gameObjectId, GameObjectType gameObjectType, Vector2 position, Vector2 velocity)
    {
        GameObjectType = gameObjectType;
        GameObjectId = gameObjectId;
        Position = position;
        Velocity = velocity;
    }

}
