using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace Infinity
{
    public class MyMath
    {
        public static Random Random = new Random();

        public static double GetAngle(Vector2 direction) 
        {
            float x = direction.X;
            float y = direction.Y;
            
            if (x == 0) x += .001f;
            double angle = Math.Atan(-y / x);
            if (x > 0 && y >= 0) angle = MathHelper.PiOver2 - angle; // PI/2 to 0
            if (x < 0 && y >= 0) angle = -MathHelper.PiOver2 - angle; // o to -PI/2
            if (x < 0 && y < 0) angle = -MathHelper.PiOver2 - angle; // -PI/2 to -2*PI/2
            if (x > 0 && y < 0) angle = MathHelper.PiOver2 - angle; // 2*PI/2 to PI/2

            return Math.PI - angle; // Invert y
            //return angle;
        }

        public static float Distance(Vector2 p1, Vector2 p2)
        {
            return Vector2.Distance(p1, p2);
//            float xDiff = p1.X - p2.X;
//            float yDiff = p1.Y - p2.Y;
//            return (float) Math.Sqrt(xDiff*xDiff + yDiff*yDiff);
        }

        public static Vector2 LimitVelocity(Vector2 velocity, float maxVelocity)
        {
            var angle = GetAngle(velocity);
            Vector2 velocityLimit = maxVelocity * new Vector2((float)Math.Sin(angle), (float)Math.Cos(angle));
            if (Math.Abs(velocity.X) > Math.Abs(velocityLimit.X)) return velocityLimit;
            //if (MyMath.Distance(velocity, Vector2.Zero) < 20f) return Vector2.Zero;
            return velocity;
            //return Vector2.Zero;
        }

        public static bool MovingTowards(Vector2 destionation, Vector2 source, Vector2 velocity, float acceptableOffset)
        {
            // acceptable offset [0-1] 1-> Right on target

            return Distance(source, destionation) - Distance(source + velocity, destionation) >
                acceptableOffset * Distance(velocity, Vector2.Zero);
        }

        public static Vector2 ReflectAcrossLine(Vector2 source, Vector2 line)
        {
            // line -> y = m1 * x 
            // perpendicular -> y = m2 * x + c
            // perpendicular passes through sourcePoint and m1 * m2 = -1;

            float m1 = line.Y / line.X;
            float m2 = -1 / m1;
            float c = source.Y + -m2 * source.X;

            // point of intersection droping perpendicular from soucePoint to line
            Vector2 intersection = new Vector2();
            intersection.X = c / (m1 - m2);
            intersection.Y = m1 * intersection.X;

            // intersection is at the center of source and destination

            Vector2 destination = new Vector2();
            destination.X = 2 * intersection.X - source.X;
            destination.Y = 2 * intersection.Y - source.Y;

            return destination;
        }
    }
}
