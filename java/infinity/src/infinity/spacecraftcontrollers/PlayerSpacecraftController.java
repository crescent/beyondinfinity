package infinity.spacecraftcontrollers;

import java.awt.event.KeyEvent;

import infinity.GameWorld;
import infinity.MyMath;
import infinity.gameengine.Keyboard;
import infinity.gameengine.Mouse;
import infinity.gameengine.Vector2;
import infinity.gameobjects.Spacecraft;

public class PlayerSpacecraftController implements SpacecraftController
{
	Vector2 newVelocity = new Vector2();
    public void Control(GameWorld gameWorld, Spacecraft me)
    {
		newVelocity.X = (Mouse.getX() - gameWorld.getScreenCenter().X) / 5f;;
		newVelocity.Y = (Mouse.getY() - gameWorld.getScreenCenter().Y) / 5f;
		
		double direction = MyMath.GetAngle(newVelocity);
		if(Keyboard.isKeyDown(KeyEvent.VK_SPACE)) newVelocity = me.getVelocity(newVelocity).multiply(-10.0f);
		
		me.sendRotateAndMove(newVelocity);
		if (Mouse.isLeftButtonDown())
			me.sendFireBullet();
		gameWorld.setCameraPostion(me.getGamePosition());
    }
}