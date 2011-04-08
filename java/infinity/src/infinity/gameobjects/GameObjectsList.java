package infinity.gameobjects;

import infinity.networkingevent.AddGameObject;
import infinity.networkingevent.Collide;
import infinity.networkingevent.FireBullet;
import infinity.networkingevent.GameNetworkingEvent;
import infinity.networkingevent.KillGameObject;
import infinity.networkingevent.RotateAndMove;

import java.util.Hashtable;

import tests.unit.networkingevent.StubGameObject;

public class GameObjectsList {
	public static final int BACKGROUND=1;
	public static final int SPACECRAFT=2;
	public static final int BULLET=3;
	public static final int BULLET_EXPLOSION=4;
	
	public GameObjectsList() {
		//dictionary.put(BACKGROUND, new Background());
		//dictionary.put(SPACECRAFT, new Spacecraft());
		//dictionary.put(BULLET, new Bullet());
		//dictionary.put(BULLET_EXPLOSION, new BulletExplosion());
	}
	
	public static GameObject get(int id) throws Exception
	{
		switch(id)
		{
		case -1: return new StubGameObject();
		case SPACECRAFT: return new Spacecraft();
		case BULLET: return new Bullet();
		}
		
		throw new Exception("Game object not found");
	}
}
