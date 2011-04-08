package infinity.spacecraftcontrollers;

import infinity.GameWorld;
import infinity.Infinity;
import infinity.MyMath;
import infinity.MyUUID;
import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.AiIdentifiableObjectInfo;
import infinity.gameobjectproperties.GameObjectType;
import infinity.gameobjects.Spacecraft;

import java.util.ArrayList;
import java.util.Hashtable;

public class AiSpacecraftController implements SpacecraftController
{
    private int plan = 1;
    private MyUUID targetId;
    private int targetEnimity = 0;
    private Vector2 oldVelocity;
    private Hashtable<MyUUID,Integer> enimity;
    private boolean facingOpponent;

    public AiSpacecraftController()
    {
        oldVelocity = new Vector2();
        targetId = MyUUID.random();
        enimity = new Hashtable<MyUUID,Integer>();
    }

    int timepassed=0;
    public void Control(GameWorld gameWorld, Spacecraft me)
    {
    	if(timepassed++<50)    		return;
    	
        //Infinity.debugString += "  " + targetId;
        ArrayList<AiIdentifiableObjectInfo> aiIdentifiableObjectInfos = gameWorld.GetIdentifiableObjects();

        AiIdentifiableObjectInfo target = FindTarget(aiIdentifiableObjectInfos, me);
        if (target == null) return;
        targetId = target.GameObjectId;
        facingOpponent = MyMath.MovingTowards(target.Position, me.getGamePosition(), oldVelocity, 0.2f); ;

        //if (me.Energy < 20) plan = 2;
        AiIdentifiableObjectInfo closestSpacecraft = FindClosestSpacecraft(aiIdentifiableObjectInfos, me);
        
        if((MyMath.Distance(me.getGamePosition(), closestSpacecraft.Position) < 250)) plan=3;
        else if(plan==3) plan=1;
        
        if (plan == 1)
        {
            MoveTowardsAndAttack(me, target, aiIdentifiableObjectInfos);
            if (facingOpponent &&
                MyMath.Distance(target.Position, me.getGamePosition()) < 600 && 
                MyMath.Random.nextDouble() < .4f ) me.sendFireBullet();
            
            if (MyMath.Distance(me.getGamePosition(), target.Position) < 200 + MyMath.Random.nextInt(50)) plan = 2;
        }
        else if(plan==2)
        {
        	MoveAwayFrom(me, target);
            //MoveAwayFrom(me, target);
            if (MyMath.Distance(me.getGamePosition(), target.Position) > 300 &&
                MyMath.Random.nextDouble() < .1f) plan = 1;
        }
        else MoveAwayFrom(me, closestSpacecraft);

        //if (MyMath.Random.NextDouble() < .01f) plan = plan%2 +1;
    }

    private AiIdentifiableObjectInfo FindTarget(ArrayList<AiIdentifiableObjectInfo> list, Spacecraft spacecraft)
    {
        //return list.Find(info => info.GameObjectId == 0);
        AiIdentifiableObjectInfo target = null;
        for (AiIdentifiableObjectInfo identityInfo : list)
        {
            if (identityInfo.GameObjectId.equals(targetId))
                target = identityInfo;

            AiIdentifiableObjectInfo anotherTarget = AnotherTargetAttackingMe(list, identityInfo, spacecraft);
            if(anotherTarget!=null) target = anotherTarget;
        }
        if(target != null) return target;

        return FindClosestSpacecraft(list, spacecraft);
    }

    private AiIdentifiableObjectInfo AnotherTargetAttackingMe(ArrayList<AiIdentifiableObjectInfo> list, AiIdentifiableObjectInfo bullet, Spacecraft mycraft) 
    {
        AiIdentifiableObjectInfo target = null;

        MyUUID enemyGameObjectId = bullet.GameObjectId;
        
		if (!enemyGameObjectId.equals(mycraft.getGameObjectId()) &&
            bullet.GameObjectType.equals(GameObjectType.Bullet) &&
            MyMath.Distance(bullet.Position, mycraft.getGamePosition()) < 100 
            &&MyMath.MovingTowards(mycraft.getGamePosition(), bullet.Position, bullet.Velocity, 0.7f))
        {
            if (!enimity.containsKey(enemyGameObjectId)) enimity.put(enemyGameObjectId,0);
            enimity.put(enemyGameObjectId,enimity.get(enemyGameObjectId) + 10);
            if (enimity.get(enemyGameObjectId) > targetEnimity)
            {
                AiIdentifiableObjectInfo identityInfo1 = bullet;
                for(AiIdentifiableObjectInfo info : list)
                	if(info.GameObjectId.equals(identityInfo1.GameObjectId))
                	{
                		target = info;
                		break;
                	}
                
                targetEnimity = enimity.get(enemyGameObjectId);
            }
        }
        return target;
    }

    private void MoveTowardsAndAttack(Spacecraft me, AiIdentifiableObjectInfo target, ArrayList<AiIdentifiableObjectInfo> list)
    {
        Vector2 adjustedTargetVelocity = new Vector2(target.Position).subtract(me.getGamePosition());

        for (AiIdentifiableObjectInfo info : list)
        {
            float diffDistanceFromMe = MyMath.Distance(me.getGamePosition(), info.Position) -
                                     MyMath.Distance(me.getGamePosition(), target.Position);
            if (info.GameObjectId.equals(me.getGameObjectId()) && info.GameObjectType.equals(GameObjectType.Bullet)
                && (diffDistanceFromMe) <3*MyMath.Distance(me.getGamePosition(), target.Position))
            {
                Vector2 vector2 = (MyMath.ReflectAcrossLine(new Vector2(info.Position).subtract(me.getGamePosition()), new Vector2(target.Position).subtract(me.getGamePosition())));
                adjustedTargetVelocity.add(vector2);
                adjustedTargetVelocity.add(target.Position).subtract(me.getGamePosition());
            }
        }
        adjustedTargetVelocity.normalize();
        adjustedTargetVelocity = adjustedTargetVelocity.multiply(MyMath.Distance(me.getGamePosition(), target.Position));

        me.TargetReticle = adjustedTargetVelocity;
        RotateAndMoveWithVelocitySpeedAndTurnLimits(me, adjustedTargetVelocity);
    }

    private void MoveAwayFrom(Spacecraft me, AiIdentifiableObjectInfo target)
    {
        RotateAndMoveWithVelocitySpeedAndTurnLimits(me, new Vector2(me.getGamePosition()).subtract(target.Position));
    }

    public void RotateAndMoveWithVelocitySpeedAndTurnLimits(Spacecraft me, Vector2 targetVelocity) 
    {
        targetVelocity.normalize().multiply(10);
    	me.sendRotateAndMove(targetVelocity);
    }

    private AiIdentifiableObjectInfo FindClosestSpacecraft(ArrayList<AiIdentifiableObjectInfo> list, Spacecraft me)
    {
        AiIdentifiableObjectInfo closestObject = null;
        float minDistance = Float.MAX_VALUE;
        for (AiIdentifiableObjectInfo info : list)
        {
            if(info.GameObjectId.equals(me.getGameObjectId())) continue;
            if (!info.GameObjectType.equals(GameObjectType.Spacecraft)) continue;
            
            float distanceFromMe = MyMath.Distance(info.Position, me.getGamePosition());
            if(distanceFromMe < minDistance)
            {
                minDistance = distanceFromMe;
                closestObject = info;
            }
        }
        return closestObject;
    }
}
