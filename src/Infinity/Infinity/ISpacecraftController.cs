using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;

namespace Infinity
{
    public interface ISpacecraftController
    {
        void Control(GameWorld gameWorld, Spacecraft me);
    }

    public class SpacecraftControllerAI : ISpacecraftController
    {
        private int plan = 1;
        private int targetId;
        private int targetEnimity;
        private Vector2 oldVelocity;
        private float turnVelocityLimit = 5f;
        private float moveVelocityLimit = 60f;
        private Dictionary<int, int> enimity;
        private bool facingOpponent;

        public SpacecraftControllerAI()
        {
            oldVelocity = new Vector2();
            targetId = -1;
            enimity = new Dictionary<int, int>();
        }

        public void Control(GameWorld gameWorld, Spacecraft me)
        {
            Infinity.debugString += "  " + targetId;
            var aiIdentifiableObjectInfos = gameWorld.GetIdentifiableObjects();

            var target = FindTarget(aiIdentifiableObjectInfos, me);
            if (target == null) return;
            targetId = target.GameObjectId;
            facingOpponent = facingOpponent = MyMath.MovingTowards(target.Position, me.GamePosition, oldVelocity, 0.6f); ;

            //if (me.Energy < 20) plan = 2;
            turnVelocityLimit = 10f;
            moveVelocityLimit = 60f;
            if (plan == 1)
            {
                MoveTowardsAndAttack(me, target, aiIdentifiableObjectInfos);
                if (facingOpponent &&
                    MyMath.Distance(target.Position, me.GamePosition) < 600 && 
                    MyMath.Random.NextDouble() < .4f ) me.FireBullet();
                if (MyMath.Distance(me.GamePosition, target.Position) < 50 + MyMath.Random.Next(50)) plan = 2;
            }
            else
            {
                MoveAwayFrom(me, target);
                if (MyMath.Distance(me.GamePosition, target.Position) > 200 &&
                    MyMath.Random.NextDouble() < .1f) plan = 1;
            }

            //if (MyMath.Random.NextDouble() < .01f) plan = plan%2 +1;
        }

        private AiIdentifiableObjectInfo FindTarget(List<AiIdentifiableObjectInfo> list, Spacecraft spacecraft)
        {
            //return list.Find(info => info.GameObjectId == 0);
            AiIdentifiableObjectInfo target = null;
            foreach (var identityInfo in list)
            {
                if (identityInfo.GameObjectId == targetId)
                    target = identityInfo;

                target = AnotherTargetAttackingMe(list, identityInfo, spacecraft) ?? target;
            }
            if(target != null) return target;

            return FindClosestSpacecraft(list, spacecraft);
        }

        private AiIdentifiableObjectInfo AnotherTargetAttackingMe(List<AiIdentifiableObjectInfo> list, AiIdentifiableObjectInfo identityInfo, Spacecraft spacecraft) 
        {
            AiIdentifiableObjectInfo target = null;

            if (identityInfo.GameObjectId != spacecraft.GameObjectId &&
                identityInfo.GameObjectType == GameObjectType.Bullet &&
                MyMath.Distance(identityInfo.Position, spacecraft.GamePosition) < 100 
                &&MyMath.MovingTowards(spacecraft.GamePosition, identityInfo.Position, identityInfo.Velocity, 0.7f))
            {
                if (!enimity.ContainsKey(identityInfo.GameObjectId)) enimity[identityInfo.GameObjectId] = 0;
                enimity[identityInfo.GameObjectId] += 10;
                if (enimity[identityInfo.GameObjectId] > targetEnimity)
                {
                    AiIdentifiableObjectInfo identityInfo1 = identityInfo;
                    target = list.Find(info => info.GameObjectId == identityInfo1.GameObjectId);
                    targetEnimity = enimity[identityInfo.GameObjectId];
                }
            }
            return target;
        }

        private void MoveTowardsAndAttack(Spacecraft me, AiIdentifiableObjectInfo target, List<AiIdentifiableObjectInfo> list)
        {
            var adjustedTargetVelocity = target.Position - me.GamePosition;

            foreach (var info in list)
            {
                var diffDistanceFromMe = MyMath.Distance(me.GamePosition, info.Position) -
                                         MyMath.Distance(me.GamePosition, target.Position);
                if (info.GameObjectId == me.GameObjectId && info.GameObjectType == GameObjectType.Bullet
                    && (diffDistanceFromMe) <3*MyMath.Distance(me.GamePosition, target.Position)
                    )
                {
                    var vector2 = (MyMath.ReflectAcrossLine(info.Position - me.GamePosition, target.Position - me.GamePosition));
                    adjustedTargetVelocity += vector2;
                    adjustedTargetVelocity += target.Position - me.GamePosition;
                }
            }
            adjustedTargetVelocity.Normalize();
            adjustedTargetVelocity = MyMath.Distance(me.GamePosition, target.Position)*adjustedTargetVelocity;

            me.TargetReticle = adjustedTargetVelocity;
            RotateAndMoveWithVelocitySpeedAndTurnLimits(me, adjustedTargetVelocity);
        }

        private void MoveAwayFrom(Spacecraft me, AiIdentifiableObjectInfo target)
        {
            RotateAndMoveWithVelocitySpeedAndTurnLimits(me, me.GamePosition-target.Position);
        }

        public void RotateAndMoveWithVelocitySpeedAndTurnLimits(Spacecraft me, Vector2 targetVelocity) 
        {
            targetVelocity.Normalize();
            oldVelocity += turnVelocityLimit * targetVelocity;
            oldVelocity.Normalize();
            oldVelocity = moveVelocityLimit * oldVelocity;
            me.RotataeAndMove(oldVelocity);
        }

        private AiIdentifiableObjectInfo FindClosestSpacecraft(List<AiIdentifiableObjectInfo> list, Spacecraft me)
        {
            AiIdentifiableObjectInfo closestObject = null;
            float minDistance = float.MaxValue;
            foreach (var info in list)
            {
                if(info.GameObjectId == me.GameObjectId) continue;
                if (info.GameObjectType != GameObjectType.Spacecraft) continue;
                
                var distanceFromMe = MyMath.Distance(info.Position, me.GamePosition);
                if(distanceFromMe < minDistance)
                {
                    minDistance = distanceFromMe;
                    closestObject = info;
                }
            }
            return closestObject;
        }
    }

    public class PlayerSpacecraftController : ISpacecraftController
    {
        public void Control(GameWorld gameWorld, Spacecraft me)
        {
            float x = 100 * (Mouse.GetState().X - gameWorld.ScreenCenter.X) / gameWorld.ScreenCenter.X;
            float y = 100 * (Mouse.GetState().Y - gameWorld.ScreenCenter.Y) / gameWorld.ScreenCenter.Y;
            me.RotataeAndMove(new Vector2(x, y));
            if (Mouse.GetState().LeftButton == ButtonState.Pressed) me.FireBullet();
        }
    }
}