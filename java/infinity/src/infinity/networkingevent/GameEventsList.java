package infinity.networkingevent;

import java.util.Hashtable;


public class GameEventsList 
{
	public static final int ADD=1;
	public static final int KILL=2;
	public static final int SPACECRAFT_MOVE=3;
	public static final int SPACECRAFT_FIRE=4;
	public static final int COLLIDE=5;
	
	private Hashtable<Integer, GameNetworkingEvent> dictionary = new Hashtable<Integer,GameNetworkingEvent>();

	public GameEventsList() {
		dictionary.put(ADD, new AddGameObject());
		dictionary.put(KILL, new KillGameObject());
		dictionary.put(SPACECRAFT_MOVE, new RotateAndMove());
		dictionary.put(SPACECRAFT_FIRE, new FireBullet());
		dictionary.put(COLLIDE, new Collide());
	}
	
	public GameNetworkingEvent get(int id)
	{
		return dictionary.get(id);
	}
}