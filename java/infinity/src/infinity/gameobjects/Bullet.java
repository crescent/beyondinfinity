package infinity.gameobjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import infinity.*;
import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.*;

public class Bullet extends GameObject implements ColloidableObject
// IAiIdentifiableObject
{
	//private String ownerId;
	private MyTexture myTexture;

	public float energy;
	private boolean energyUp;
	private boolean dead;
	int initialEnergy = 50;

	private Vector2 velocity;

	public Bullet(GameWorld gameWorld, Vector2 gamePosition, Vector2 velocity,
			MyUUID ownerId) {
		this.gamePosition = new Vector2();
		this.velocity = new Vector2();
		init(gameWorld, gamePosition, velocity, initialEnergy, true);
	}
	
	public Bullet()
	{
	}

	public void Draw(Graphics2D g) {
		if (!IsAlive())
			return;

		// g.Draw(myTexture.Texture, PositionOnScreen, null, Color.Red, 0,
		// myTexture.Origin, TextureScale, SpriteEffects.None, 0.0f);

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate(PositionOnScreen().X, PositionOnScreen().Y);
		// affineTransform.scale(TextureScale(), TextureScale());
		affineTransform.translate(-myTexture.Size.X / 2, -myTexture.Size.Y / 2);
		// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
		g.drawImage(myTexture.Texture, affineTransform, null);
	}

	private float TextureScale() {
		return energy / 50.0f;
	}

	public boolean IsAlive() {
		return !dead && energy >= initialEnergy;
	}

	@Override
	public void Update() {
		getGamePosition().add(velocity);
		if (energyUp && energy >= 100)
			energyUp = false;
		if (energyUp)
			energy += 10;
		if (!energyUp)
			energy -= .25f;

	}

	public CollisionSphere CollisionSphere() {
		//myTexture.setScale(TextureScale());
		return myTexture.GetCollisionSphere(getGamePosition());
	}

	public void CollidedWith(ColloidableObject colloidableObject) {
		//MyContentManager.Sound_Explosion.Play(Math.max(0, 0.1f - MyMath.Distance(gameWorld.CameraPosition, gamePosition) / 4000.0f / 10));
		gameWorld.add(new BulletExplosion(gameWorld, getGamePosition(), energy));
		dead = true;
	}

	public void createFromStream(GameWorld gameWorld, DataInput in) throws IOException
	{
		gamePosition = new Vector2();
		velocity = new Vector2();
		readExternal(in);
		init(gameWorld, gamePosition, velocity, energy, energyUp);
	}
	
	private void init(GameWorld gameWorld, Vector2 gamePosition, Vector2 velocity, float energy,
			boolean energyUp) {
		this.gameWorld = gameWorld;
		this.gamePosition.assign(gamePosition);
		this.velocity.assign(velocity);
		this.energy = energy;
		this.energyUp = energyUp;
		dead = false;
		myTexture = MyContentManager.BulletTexture;
		//myTexture.filterWithColor(Color.RED);
	}

	@Override
	public void readExternal(DataInput in) throws IOException  {
		super.readExternal(in);
		
		gamePosition.readExternal(in);
		velocity.readExternal(in);
		
		energy = in.readFloat();
		energyUp = in.readBoolean();
	}

	@Override
	public void writeExternal(DataOutput out) throws IOException {
		super.writeExternal(out);
		
		gamePosition.writeExternal(out);
		velocity.writeExternal(out);
		out.writeFloat(energy);
		out.writeBoolean(energyUp);
	}

	@Override
	public int getGameObjectTypeId() {
		// TODO Auto-generated method stub
		return GameObjectsList.BULLET;
	}

	/*
	 * public void PopulateGameObjectInfo(AiIdentifiableObjectInfo info) {
	 * info.Create(ownerId, GameObjectType.Bullet, gamePosition, Velocity); }
	 */
}
