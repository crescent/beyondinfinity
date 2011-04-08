package infinity;

import infinity.gameengine.Vector2;

import java.util.Random;

public class MyMath {

	public static final double PI2 = Math.PI * 2;
	public static Random Random = new Random();

	public static double GetAngle(Vector2 direction) {
		float x = direction.X;
		float y = direction.Y;

		if (x == 0)
			x += .0000001f;
		double angle = Math.atan(-y / x);
		if (x > 0 && y >= 0)
			angle = Math.PI / 2 - angle; // PI/2 to 0
		if (x < 0 && y >= 0)
			angle = -Math.PI / 2 - angle; // o to -PI/2
		if (x < 0 && y < 0)
			angle = -Math.PI / 2 - angle; // -PI/2 to -2*PI/2
		if (x > 0 && y < 0)
			angle = Math.PI / 2 - angle; // 2*PI/2 to PI/2

		angle = Math.PI - angle; // Invert y
		if (angle < 0)
			angle += Math.PI + Math.PI;
		return angle;
	}

	public static Vector2 GetVector2FromAngle(double angle) {
		return new Vector2((float) Math.sin(angle), (float) Math.cos(angle));
	}

	public static float Distance(Vector2 p1, Vector2 p2) {
		return p1.distance(p2);
	}

	public static void LimitVelocity(Vector2 velocity, float maxVelocity) {
		// double angle = GetAngle(velocity);
		float mag = velocity.magnitude();
		velocity.divide(mag);
		if (mag > maxVelocity) 	velocity.multiply(maxVelocity);
		else velocity.multiply(mag);
	}

	public static boolean MovingTowards(Vector2 destionation, Vector2 source,
			Vector2 velocity, float acceptableOffsetAngle) { // acceptable
																// offset
		// [0-1] 1-> Right on
		// target

		return Math.abs(Math.acos((destionation.X * source.X + destionation.Y
				* source.Y)
				/ destionation.magnitude() / source.magnitude())) < acceptableOffsetAngle;
		/*
		 * return Distance(source, destionation) - Distance(new
		 * Vector2(source).add(velocity), destionation) > acceptableOffset
		 * Distance(velocity, Vector2.zero);
		 */
	}

	public static Vector2 ReflectAcrossLine(Vector2 source, Vector2 line) {
		// line -> y = m1 * x // perpendicular -> y = m2 * x + c
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
