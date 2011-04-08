package infinity.gameengine;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import infinity.MyMath;

public class Vector2 //implements Externalizable
{

	public static final Vector2 zero = new Vector2();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(X);
		result = prime * result + Float.floatToIntBits(Y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2 other = (Vector2) obj;
		if (Float.floatToIntBits(X) != Float.floatToIntBits(other.X))
			return false;
		if (Float.floatToIntBits(Y) != Float.floatToIntBits(other.Y))
			return false;
		return true;
	}

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
		if(mag==0) return zero;
		return divide(mag);
	}

	public double angle() {
		return MyMath.GetAngle(this);
	}

	public Vector2 fromAngle(double angle) {
		//v = new Vector2();
		X=(float) Math.sin(angle);
		Y=(float) Math.cos(angle);
		return this;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "X: " + X + "Y: " + Y;
	}

	public float magnitude() {
		return (float) Math.sqrt(X * X + Y * Y);
	}

	public void readExternal(DataInput in) throws IOException  {
		X = in.readFloat();
		Y = in.readFloat();
	}

	public void writeExternal(DataOutput out) throws IOException {
		out.writeFloat(X);
		out.writeFloat(Y);
		
	}

}
