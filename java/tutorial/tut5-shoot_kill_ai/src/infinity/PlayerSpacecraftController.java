package infinity;

import infinity.gameengine.Mouse;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Spacecraft;

public class PlayerSpacecraftController implements SpacecraftController
{
    public void Control(GameWorld gameWorld, Spacecraft me)
    {
		me.RotataeAndMove(new Vector2((Mouse.getX() - gameWorld.getScreenCenter().X) / 5f,
				(Mouse.getY() - gameWorld.getScreenCenter().Y) / 5f));
		if (Mouse.isLeftButtonDown())
			me.FireBullet();
    }
}