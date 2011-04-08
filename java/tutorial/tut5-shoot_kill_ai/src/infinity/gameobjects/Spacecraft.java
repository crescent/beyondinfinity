package infinity.gameobjects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

import infinity.*;
import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.*;

public class Spacecraft extends GameObject implements ColloidableObject,
		AiIdentifiableObject

{
	protected MyTexture myTexture;
	protected MyTexture myTextureOverlay;

	private static int count;
	private int myCount;

	private float direction;
	private Vector2 velocity;

	private float energy;
	private boolean dead;
	public Vector2 TargetReticle;

	private SpacecraftController spacecraftController;

	public Vector2 getGamePosition() {
		return gamePosition;
	}

	public static Spacecraft CreatePlayer(GameWorld gameWorld,
			Vector2 gamePosition) {
		Spacecraft spacecraft = new Spacecraft(gameWorld, gamePosition);
		spacecraft.spacecraftController = new PlayerSpacecraftController();
		spacecraft.maxVelocity = 60f;
		spacecraft.maxTurnAngle = 0.2;
		return spacecraft;
	}

	public static Spacecraft CreateEnemy(GameWorld gameWorld,
			Vector2 gamePosition) {
		Spacecraft spacecraft = new Spacecraft(gameWorld, gamePosition);
		spacecraft.spacecraftController = new AiSpacecraftController();
		spacecraft.maxVelocity = 50f;
		spacecraft.maxTurnAngle = 0.1;
		return spacecraft;
	}

	Spacecraft(GameWorld gameWorld, Vector2 gamePosition) {
		super(gameWorld, gamePosition);

		energy = 100;
		dead = false;
		myCount = ++count;
		velocity = new Vector2();
		aiIdentifiableObjectInfo = new AiIdentifiableObjectInfo();

		switch (MyMath.Random.nextInt(4)) {
		default:
		case 0:
			myTexture = new MyTexture(MyContentManager.Spacecraft0);
			myTextureOverlay = new MyTexture(
					MyContentManager.Spacecraft0_Overlay);
			break;
		case 1:
			myTexture = new MyTexture(MyContentManager.Spacecraft1);
			myTextureOverlay = new MyTexture(
					MyContentManager.Spacecraft1_Overlay);
			break;
		case 2:
			myTexture = new MyTexture(MyContentManager.Spacecraft2);
			myTextureOverlay = new MyTexture(
					MyContentManager.Spacecraft2_Overlay);
			break;
		case 3:
			myTexture = new MyTexture(MyContentManager.Spacecraft3);
			myTextureOverlay = new MyTexture(
					MyContentManager.Spacecraft3_Overlay);
			break;
		}

		myTexture.filterWithColor(new Color(Color1(), Color1(), Color1()));
		myTextureOverlay.filterWithColor(new Color(Color1(), Color1(), Color1()));
	}

	private static float Color1() {
		return (float) (0.4 + 0.6 * MyMath.Random.nextDouble());
	}

	public void Draw(Graphics2D g) {
		AffineTransform affineTransform = new AffineTransform();
		// affineTransform.rotate(Math.PI/2.0);
		affineTransform.translate(PositionOnScreen().X, PositionOnScreen().Y);
		affineTransform.rotate((float) (Math.PI - direction));
		affineTransform.translate(-myTexture.Size.X / 2, -myTexture.Size.Y / 2);
		g.drawImage(myTexture.Texture, affineTransform, null);
		g.drawImage(myTextureOverlay.Texture, affineTransform, null);

		if(spacecraftController instanceof PlayerSpacecraftController)
		{
			if(Math.abs(difff)>1)
			g.drawString("Hello " + difff, 10, 10);
			g.drawString("Hello " + nnnewVel, 10, 20);
			g.drawString("Hello " + nnnewVel2, 10, 30);
		}

		/*
		 * spriteBatch.Draw(myTexture.Texture, PositionOnScreen, null, color,
		 * (float) (Math.PI - direction), myTexture.Origin, myTexture.Scale,
		 * SpriteEffects.None, 0.0f); /
		 * 
		 * // spriteBatch.Draw(MyContentManager.Bullet, TargetReticle + //
		 * PositionOnScreen, null, Color.Yellow, 0, new Vector2(5,5), 2, //
		 * SpriteEffects.None, 0.0f); /* var angle = velocity.Y / velocity.X;
		 * Vector2 velocityLimit = 1 * new Vector2((float) Math.Cos(angle),
		 * (float) Math.Sin(angle));
		 * 
		 * spriteBatch.DrawString(MyContentManager.Font_Debug, string.Format(
		 * "{2} Spacecraft {0} Energy: {1}", myCount, (int) energy,
		 * GameObjectId), new Vector2(0, myCount * 20), Color.White);
		 */
		g.drawString(getGameObjectId()+" S[acecraft "+myCount+" Energy: "+(int)energy, 0, myCount*20+50);
	}

	// public Vector2 TargetReticle { get; set; }

	public void FireBullet() {
		float distanceFromCenter = 50;// myTexture.CollisionSphere.Radius + 10;

		Vector2 normalizingValue = new Vector2(
				(float) (distanceFromCenter * Math.sin(direction)),
				(float) (distanceFromCenter * Math.cos(direction)));
		Vector2 bulletStartingPosition = new Vector2().assign(gamePosition)
				.add(normalizingValue);

		float max = Math.max(Math.abs(velocity.X), Math.abs(velocity.Y));
		Vector2 normalizedVelocity = new Vector2(velocity.X / max, velocity.Y
				/ max);

		gameWorld
				.Add(new Bullet(gameWorld, bulletStartingPosition,
						normalizedVelocity.multiply(5).add(velocity),
						getGameObjectId()));
		energy -= .1f;
		// MyContentManager.Sound_Laser.Play(Math.Max(0,1-MyMath.Distance(gameWorld.CameraPosition,
		// gamePosition)/2000.0f));
	}

	protected void Rotate(double angle) {
		direction = (float) angle;
	}

	
	public double difff;
	public double nnnewVel;
	public double nnnewVel2;
	private float maxVelocity;
	private double maxTurnAngle;
	
	public void RotataeAndMove(Vector2 newVelocity) {
		double angle = velocity.angle();
		double newAngle = newVelocity.angle();
		
		double rightTurnDiff = newAngle > angle ? newAngle - angle :MyMath.PI2 - angle + newAngle;
		double leftTurnDiff = newAngle < angle ? angle - newAngle  : (MyMath.PI2 + angle - newAngle);
		double diff;
		
		leftTurnDiff = leftTurnDiff % MyMath.PI2;		// Sometimes one of them exceeds PI2 while the other is almost PI2
		rightTurnDiff = rightTurnDiff %  MyMath.PI2;	// causing shaking
		
		if(leftTurnDiff <= rightTurnDiff) diff = -leftTurnDiff;
		else diff = rightTurnDiff;
		
		difff=diff;
		nnnewVel=leftTurnDiff;
		nnnewVel2=rightTurnDiff;
		
		if(diff>maxTurnAngle) diff = maxTurnAngle;
		else if(diff<-maxTurnAngle) diff = -maxTurnAngle;
		
		double targetAngle = angle+diff;
		newVelocity = MyMath.GetVector2FromAngle(targetAngle).multiply(newVelocity.magnitude());

		MyMath.LimitVelocity(newVelocity, maxVelocity);
		this.velocity = newVelocity.divide(5);
		
		Rotate(targetAngle);
		gamePosition.add(this.velocity);
	}

	public void Update() {
		 CheckDeath();
		if (energy < 100)
			energy += .01f;

		spacecraftController.Control(gameWorld, this);
	}

	private void CheckDeath() {
		if (energy > 0)
			return;

		dead = true;
		for (int i = 0; i < 10; i++) {
			gameWorld.Add(new BulletExplosion(gameWorld, new Vector2(
					gamePosition.X + MyMath.Random.nextInt(20) - 10,
					gamePosition.Y + MyMath.Random.nextInt(20) - 10),
					100 + MyMath.Random.nextInt(100)));
			// MyContentManager.Sound_Explosion.Play(Math.Max(0, 0.2f -
			// MyMath.Distance(gameWorld.CameraPosition, gamePosition) / 4000.0f
			// / 10));
		}
	}

	public boolean IsAlive() {
		return !dead;
	}

	public Vector2 Velocity() {
		return velocity;
	}

	public float Energy() {
		return energy;
	}

	AiIdentifiableObjectInfo aiIdentifiableObjectInfo;
	
	public AiIdentifiableObjectInfo getAiIdentifiableObjectInfo() {
		aiIdentifiableObjectInfo.Create(
				getGameObjectId(), GameObjectType.Spacecraft, gamePosition,
				velocity);
		return aiIdentifiableObjectInfo;
	}

	public CollisionSphere CollisionSphere() {
		return myTexture.GetCollisionSphere(gamePosition);
	}

	public void CollidedWith(ColloidableObject colloidableObject) {
		if (colloidableObject instanceof Bullet) {
			Bullet bullet = (Bullet) colloidableObject;
			energy -= bullet.Energy / 100.0f;
		}

	}
}



