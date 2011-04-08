package infinity;

import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.AiIdentifiableObjectInfo;
import infinity.gameobjectproperties.GameObjectType;
import infinity.gameobjects.Spacecraft;

import java.util.ArrayList;
import java.util.Hashtable;

public class AiSpacecraftController implements SpacecraftController
{
    private int plan = 1;
    private int targetId;
    private int targetEnimity = 0;
    private Vector2 oldVelocity;
    private Hashtable<Integer,Integer> enimity;
    private boolean facingOpponent;

    public AiSpacecraftController()
    {
        oldVelocity = new Vector2();
        targetId = -1;
        enimity = new Hashtable<Integer,Integer>();
    }

    public void Control(GameWorld gameWorld, Spacecraft me)
    {
        Infinity.debugString += "  " + targetId;
        ArrayList<AiIdentifiableObjectInfo> aiIdentifiableObjectInfos = gameWorld.GetIdentifiableObjects();

        AiIdentifiableObjectInfo target = FindTarget(aiIdentifiableObjectInfos, me);
        if (target == null) return;
        targetId = target.GameObjectId;
        facingOpponent = MyMath.MovingTowards(target.Position, me.gamePosition, oldVelocity, 0.2f); ;

        //if (me.Energy < 20) plan = 2;
        if (plan == 1)
        {
            MoveTowardsAndAttack(me, target, aiIdentifiableObjectInfos);
            if (facingOpponent &&
                MyMath.Distance(target.Position, me.gamePosition) < 600 && 
                MyMath.Random.nextDouble() < .4f ) me.FireBullet();
            if (MyMath.Distance(me.gamePosition, target.Position) < 50 + MyMath.Random.nextInt(50)) plan = 2;
        }
        else
        {
            MoveAwayFrom(me, target);
            if (MyMath.Distance(me.gamePosition, target.Position) > 200 &&
                MyMath.Random.nextDouble() < .1f) plan = 1;
        }

        //if (MyMath.Random.NextDouble() < .01f) plan = plan%2 +1;
    }

    private AiIdentifiableObjectInfo FindTarget(ArrayList<AiIdentifiableObjectInfo> list, Spacecraft spacecraft)
    {
        //return list.Find(info => info.GameObjectId == 0);
        AiIdentifiableObjectInfo target = null;
        for (AiIdentifiableObjectInfo identityInfo : list)
        {
            if (identityInfo.GameObjectId == targetId)
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

        int enemyGameObjectId = bullet.GameObjectId;
        
		if (enemyGameObjectId != mycraft.getGameObjectId() &&
            bullet.GameObjectType == GameObjectType.Bullet &&
            MyMath.Distance(bullet.Position, mycraft.gamePosition) < 100 
            &&MyMath.MovingTowards(mycraft.gamePosition, bullet.Position, bullet.Velocity, 0.7f))
        {
            if (!enimity.containsKey(enemyGameObjectId)) enimity.put(enemyGameObjectId,0);
            enimity.put(enemyGameObjectId,enimity.get(enemyGameObjectId) + 10);
            if (enimity.get(enemyGameObjectId) > targetEnimity)
            {
                AiIdentifiableObjectInfo identityInfo1 = bullet;
                for(AiIdentifiableObjectInfo info : list)
                	if(info.GameObjectId == identityInfo1.GameObjectId)
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
        Vector2 adjustedTargetVelocity = new Vector2(target.Position).subtract(me.gamePosition);

        for (AiIdentifiableObjectInfo info : list)
        {
            float diffDistanceFromMe = MyMath.Distance(me.gamePosition, info.Position) -
                                     MyMath.Distance(me.gamePosition, target.Position);
            if (info.GameObjectId == me.getGameObjectId() && info.GameObjectType == GameObjectType.Bullet
                && (diffDistanceFromMe) <3*MyMath.Distance(me.gamePosition, target.Position)
                )
            {
                Vector2 vector2 = (MyMath.ReflectAcrossLine(new Vector2(info.Position).subtract(me.gamePosition), new Vector2(target.Position).subtract(me.gamePosition)));
                adjustedTargetVelocity.add(vector2);
                adjustedTargetVelocity.add(target.Position).subtract(me.gamePosition);
            }
        }
        adjustedTargetVelocity.normalize();
        adjustedTargetVelocity = adjustedTargetVelocity.multiply(MyMath.Distance(me.gamePosition, target.Position));

        me.TargetReticle = adjustedTargetVelocity;
        RotateAndMoveWithVelocitySpeedAndTurnLimits(me, adjustedTargetVelocity);
    }

    private void MoveAwayFrom(Spacecraft me, AiIdentifiableObjectInfo target)
    {
        RotateAndMoveWithVelocitySpeedAndTurnLimits(me, new Vector2(me.gamePosition).subtract(target.Position));
    }

    public void RotateAndMoveWithVelocitySpeedAndTurnLimits(Spacecraft me, Vector2 targetVelocity) 
    {
        /*targetVelocity.normalize();
        oldVelocity.add(targetVelocity).multiply(turnVelocityLimit) ;
        oldVelocity.normalize();
        oldVelocity.assign(oldVelocity).multiply(moveVelocityLimit);
        me.RotataeAndMove(oldVelocity);*/
    	me.RotataeAndMove(targetVelocity);
    }

    private AiIdentifiableObjectInfo FindClosestSpacecraft(ArrayList<AiIdentifiableObjectInfo> list, Spacecraft me)
    {
        AiIdentifiableObjectInfo closestObject = null;
        float minDistance = Float.MAX_VALUE;
        for (AiIdentifiableObjectInfo info : list)
        {
            if(info.GameObjectId == me.getGameObjectId()) continue;
            if (info.GameObjectType != GameObjectType.Spacecraft) continue;
            
            float distanceFromMe = MyMath.Distance(info.Position, me.gamePosition);
            if(distanceFromMe < minDistance)
            {
                minDistance = distanceFromMe;
                closestObject = info;
            }
        }
        return closestObject;
    }
}
