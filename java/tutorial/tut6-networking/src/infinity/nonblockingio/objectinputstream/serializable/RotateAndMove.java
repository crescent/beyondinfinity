package infinity.nonblockingio.objectinputstream.serializable;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;


public class RotateAndMove implements Serializable {
	
	String gameObjectId;
	private Vector2 velocity;
	private Vector2 position;
	private float direction;
	
	public RotateAndMove(String spacecraftGameObjectId, Vector2 newVelocity, Vector2 newPosition, float newDir)
	{
		gameObjectId = spacecraftGameObjectId;
		velocity = newVelocity;
		position = newPosition;
		direction = newDir;
	}


	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getPosition() {
		return position;
	}

	public float getDirection() {
		return direction;
	}



	public String getGameObjectId() {
		// TODO Auto-generated method stub
		return gameObjectId;
	}

	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" Id: ");
		stringBuilder.append(gameObjectId);
		stringBuilder.append(" Velocity: ");
		stringBuilder.append(velocity.X);
		stringBuilder.append(" ");
		stringBuilder.append(velocity.Y);
		stringBuilder.append(" Position: ");
		stringBuilder.append(position.X);
		stringBuilder.append(" ");
		stringBuilder.append(position.Y);
		stringBuilder.append(" direction ");
		stringBuilder.append(direction);
		return stringBuilder.toString();
	}
}
