package infinity.gameengine;

import infinity.MyMath;

public class Vector2 {

	public static Vector2 zero = new Vector2();

	public Vector2(float x, float y) {
		this.X = x;
		this.Y = y;
	}

	public float X, Y;

	public Vector2() {
		X = 0;
		Y = 0;
	}

	public Vector2(Vector2 v) {
		assign(v);
	}

	public Vector2 add(Vector2 v) {
		X += v.X;
		Y += v.Y;
		return this;// new Vector2(x+v.x, y+v.y);
	}

	public Vector2 assign(Vector2 v) {
		X = v.X;
		Y = v.Y;
		return this;
	}

	public Vector2 divide(float k) {
		X /= k;
		Y /= k;
		return this;
	}

	public Vector2 multiply(float k) {
		X *= k;
		Y *= k;
		return this;
	}

	
	public Vector2 subtract(Vector2 v) {
		X -= v.X;
		Y -= v.Y;
		return this;
	}

	public float distance(Vector2 v) {
		// TODO Auto-generated method stub
		return (float) Math.sqrt((X - v.X) * (X - v.X) + (Y - v.Y) * (Y - v.Y));
	}

	public Vector2 normalize() {
		// TODO Auto-generated method stub

		float mag = (float) Math.sqrt(X * X + Y * Y);
		return divide(mag);
	}

	public double angle() {
		return MyMath.GetAngle(this);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "X: " + X + "Y: " + Y;
	}

	public float magnitude() {
		return (float) Math.sqrt(X * X + Y * Y);
	}

}
