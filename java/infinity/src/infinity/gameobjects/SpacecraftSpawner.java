package infinity.gameobjects;

import infinity.GameWorld;
import infinity.MyMath;
import infinity.gameengine.Vector2;
import infinity.networking.GameServerCommand;
import infinity.networking.GameServerCommandType;
import infinity.networkingevent.AddGameObject;

import java.awt.Graphics2D;
import java.io.IOException;

public class SpacecraftSpawner extends GameObject {

	int timeToSpawn = 180;

	private final Spacecraft spacecraft;

	public SpacecraftSpawner(Spacecraft spacecraft, GameWorld gameWorld) {
		this.spacecraft = spacecraft;
		this.gameWorld = gameWorld;
		this.gamePosition = new Vector2(MyMath.Random.nextInt(2000)-1000,MyMath.Random.nextInt(2000)-1000);
	}

	@Override
	public void Draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		timeToSpawn--;
		if (timeToSpawn == 0)
		{
			spacecraft.bringToLife(gamePosition);
			gameWorld.add(spacecraft);
			try {
				gameWorld.getGameServer().sendToAll(new GameServerCommand().create(new AddGameObject(spacecraft)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getGameObjectTypeId() {
		// TODO Auto-generated method stub
		return 0;
	}


}
