package infinity.gameobjectproperties;

import infinity.gameengine.Vector2;

public class AiIdentifiableObjectInfo {

	public int GameObjectId;
	public Vector2 Position;
	public GameObjectType GameObjectType;
	public Vector2 Velocity;

    public void Create(int gameObjectId, GameObjectType gameObjectType, Vector2 position, Vector2 velocity)
    {
        GameObjectType = gameObjectType;
        GameObjectId = gameObjectId;
        Position = position;
        Velocity = velocity;
    }

}
