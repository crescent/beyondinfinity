package infinity.gameobjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import infinity.*;
import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.*;

public class Bullet extends GameObject implements ColloidableObject
// IAiIdentifiableObject
{
	private int ownerId;
	private MyTexture myTexture;

	private static int count;
	private int myCount;

	public float Energy;
	private boolean energyUp;
	private boolean dead;
	int initialEnergy = 50;

	private Vector2 Velocity;

	public Bullet(GameWorld gameWorld, Vector2 gamePosition, Vector2 velocity,
			int ownerId) {
		super(gameWorld, gamePosition);
		this.ownerId = ownerId;
		myTexture = new MyTexture(MyContentManager.Bullet);
		myTexture.filterWithColor(Color.RED);
		myCount = count++;
		CreateBullet(gamePosition, velocity);
	}

	private void CreateBullet(Vector2 gamePosition, Vector2 velocity) {
		this.gamePosition = gamePosition;
		Velocity = velocity;
		Energy = initialEnergy;
		energyUp = true;
		dead = false;
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
		return Energy / 50.0f;
	}

	public boolean IsAlive() {
		return !dead && Energy >= initialEnergy;
	}

	@Override
	public void Update() {
		gamePosition.add(Velocity);
		if (energyUp && Energy >= 100)
			energyUp = false;
		if (energyUp)
			Energy += 10;
		if (!energyUp)
			Energy -= .25f;

	}

	public CollisionSphere CollisionSphere() {
		myTexture.setScale(TextureScale());
		;
		return myTexture.GetCollisionSphere(gamePosition);
	}

	public void CollidedWith(ColloidableObject colloidableObject) {
		//MyContentManager.Sound_Explosion.Play(Math.max(0, 0.1f - MyMath.Distance(gameWorld.CameraPosition, gamePosition) / 4000.0f / 10));
		gameWorld.Add(new BulletExplosion(gameWorld, gamePosition, Energy));
		dead = true;
	}

	/*
	 * public void PopulateGameObjectInfo(AiIdentifiableObjectInfo info) {
	 * info.Create(ownerId, GameObjectType.Bullet, gamePosition, Velocity); }
	 */
}
