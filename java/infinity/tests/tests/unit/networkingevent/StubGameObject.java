package tests.unit.networkingevent;

import infinity.gameobjects.GameObject;

import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StubGameObject extends GameObject
{

	private String value;

	public StubGameObject() {
		value="";
	}

	public StubGameObject(String value) {
		this.value = value;
	}

	@Override
	public void Draw(Graphics2D g) {
	}

	@Override
	public void Update() {
	}

	@Override
	public void readExternal(DataInput in) throws IOException {
		super.readExternal(in);
		value = in.readUTF();
	}

	@Override
	public void writeExternal(DataOutput out) throws IOException {
		super.writeExternal(out);
		out.writeUTF(value);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StubGameObject)
		{
			if(((StubGameObject)obj).value.equals(this.value))
				return true;
		}
		return false;
	}

	@Override
	public int getGameObjectTypeId() {
		// TODO Auto-generated method stub
		return -1;
	}
	
}
