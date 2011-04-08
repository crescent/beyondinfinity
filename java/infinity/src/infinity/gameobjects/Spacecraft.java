package infinity.gameobjects;

import infinity.GameWorld;
import infinity.Infinity;
import infinity.MyContentManager;
import infinity.MyMath;
import infinity.MyTexture;
import infinity.gameengine.Vector2;
import infinity.gameobjectproperties.AiIdentifiableObject;
import infinity.gameobjectproperties.AiIdentifiableObjectInfo;
import infinity.gameobjectproperties.CollisionSphere;
import infinity.gameobjectproperties.ColloidableObject;
import infinity.gameobjectproperties.GameObjectType;
import infinity.networking.GameServerCommand;
import infinity.networkingevent.AddGameObject;
import infinity.networkingevent.GameNetworkingEvent;
import infinity.networkingevent.KillGameObject;
import infinity.networkingevent.FireBullet;
import infinity.networkingevent.RotateAndMove;
import infinity.spacecraftcontrollers.AiSpacecraftController;
import infinity.spacecraftcontrollers.NullSpacecraftController;
import infinity.spacecraftcontrollers.PlayerSpacecraftController;
import infinity.spacecraftcontrollers.SpacecraftController;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import sun.awt.SunToolkit.InfiniteLoop;
import tests.unit.networkingevent.GameNetworkingEventTest;

public class Spacecraft extends GameObject implements ColloidableObject,
		AiIdentifiableObject

{
	protected MyTexture myTexture;
	protected MyTexture myTextureOverlay;

	private static int count=0;
	private int myCount;

	private float direction;
	private Vector2 velocity;

	private float energy;
	private boolean dead;
	public Vector2 TargetReticle;

	final int maxEnergy = 100;

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
		try {
			spacecraft.shipController=InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return spacecraft;
	}

	public static Spacecraft CreateEnemy(GameWorld gameWorld,
			Vector2 gamePosition) {
		Spacecraft spacecraft = new Spacecraft(gameWorld, gamePosition);
		spacecraft.spacecraftController = new AiSpacecraftController();
		spacecraft.maxVelocity = 50f;
		spacecraft.maxTurnAngle = 0.1;
		spacecraft.shipController="ai";
		return spacecraft;
	}

	Spacecraft(GameWorld gameWorld, Vector2 gamePosition) {
		super(gameWorld, gamePosition);
		init(gameWorld, gamePosition, new Vector2(), maxEnergy, MyMath.Random.nextInt(4), randomColor(), randomColor(), new NullSpacecraftController());
	}
	
	public Spacecraft() // Deserialization
	{
		//this(null,new Vector2(0,0));
	}

	private static int randomColor() {
		float r = (float) (0.4 + 0.6 * MyMath.Random.nextDouble());
		float g = (float) (0.4 + 0.6 * MyMath.Random.nextDouble());
		float b = (float) (0.4 + 0.6 * MyMath.Random.nextDouble());
		return new Color(r,g,b).getRGB();
	}

	public void Draw(Graphics2D g) {
		AffineTransform affineTransform = new AffineTransform();
		// affineTransform.rotate(Math.PI/2.0);
		affineTransform.translate(PositionOnScreen().X, PositionOnScreen().Y);
		affineTransform.rotate((float) (Math.PI - direction));
		affineTransform.translate(-myTexture.Size.X / 2, -myTexture.Size.Y / 2);
		g.drawImage(myTexture.Texture, affineTransform, null);
		g.drawImage(myTextureOverlay.Texture, affineTransform, null);

		/*
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
		String spacecraftName = spacecraftController instanceof PlayerSpacecraftController ? shipController : ""+myCount;
		g.drawString(" Spacecraft: "+spacecraftName+" Energy: "+(int)energy, 0, myCount%50*15+50);
		
		if(spacecraftController instanceof PlayerSpacecraftController)
		{
        	for (GameObject gameObject : gameWorld.getGameObjectsClone()) {
				if(gameObject instanceof Spacecraft)
				{
					g.setColor(Color.GREEN);
					int X = (int)(gameWorld.getScreenCenter().X*2-200 + (gameObject.getGamePosition().X - gamePosition.X)/50);
					int Y = (int)(gameWorld.getScreenCenter().Y*2-200 + (gameObject.getGamePosition().Y - gamePosition.Y)/50);

					if(gameObject.getGameObjectId().equals(getGameObjectId())) 
					{
						g.setColor(Color.YELLOW);
						g.drawOval(X, Y, 4, 4);
						g.drawOval(X, Y, 3, 4);
						g.drawOval(X, Y, 4, 3);
						g.drawOval(X, Y, 5, 4);
						g.drawOval(X, Y, 4, 5);
						g.drawOval(X, Y, 5, 5);
					}
					g.drawOval(X, Y, 1, 1);
					g.drawOval(X, Y, 2, 2);
					g.drawOval(X, Y, 3, 2);
					g.drawOval(X, Y, 2, 3);
					g.drawOval(X, Y, 3, 3);
				}
			}
        	g.setColor(Color.WHITE);
		}
	}

	// public Vector2 TargetReticle { get; set; }
	private Vector2 _fireBulletAngle = new Vector2();
	private Vector2 _fireBulletPosition = new Vector2();
	
	public void FireBullet() {
		float distanceFromCenter = 50;// myTexture.CollisionSphere.Radius + 10;

		_fireBulletPosition.assign(getGamePosition());
		
		_fireBulletPosition.X += (float) (distanceFromCenter * Math.sin(direction));
		_fireBulletPosition.Y += (float) (distanceFromCenter * Math.cos(direction));

		Bullet bullet = new Bullet(gameWorld, _fireBulletPosition,
				_fireBulletAngle.fromAngle(direction).multiply(10).add(velocity),
				getGameObjectId());
		
		try {
			gameWorld.add(bullet);
			gameWorld.getGameServer().sendToAll(new GameServerCommand().create(new AddGameObject(bullet)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		energy -= .125f;
		// MyContentManager.Sound_Laser.Play(Math.Max(0,1-MyMath.Distance(gameWorld.CameraPosition,
		// gamePosition)/2000.0f));
	}

	protected void Rotate(double angle) {
		direction = (float) angle;
	}

	private float maxVelocity;
	private double maxTurnAngle;
	private float maxVelocityChange = 50f;

	
	public void sendRotateAndMove(Vector2 newVelocity)
	{
		try {
			//gameWorld.getGameServerClient().sendNetworkEvent(new RotateAndMove(getGameObjectId(), new Vector2(newVelocity)));
			gameWorld.getGameServerClient().sendNetworkEvent(_rotateAndMoveEvent.init(getGameObjectId(), newVelocity));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendFireBullet()
	{
		try {
			gameWorld.getGameServerClient().sendNetworkEvent(new FireBullet(getGameObjectId()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Vector2 _rotateAndMoveAngle = new Vector2();
	private Vector2 _targetVelocity = new Vector2();

	public void rotateAndMove(Vector2 newVelocity) {
		
		_rotateAndMoveAngle.assign(newVelocity);
		double angle = direction;
		double newAngle = _rotateAndMoveAngle.angle();

		double targetAngle = MyMath.limitAngleDifference(angle, newAngle, maxTurnAngle);

		_targetVelocity.fromAngle(targetAngle).
			multiply(_rotateAndMoveAngle.magnitude());

		MyMath.LimitVelocity(_targetVelocity, maxVelocity);
		//this.velocity = newVelocity;

		this.velocity.add(new Vector2(_targetVelocity).subtract(velocity).divide(maxVelocityChange));
		MyMath.LimitVelocity(velocity, maxVelocity);

		Rotate(targetAngle);
		gamePosition.add(this.velocity);
	}


	public void Update() {
		if(!gameWorld.isClient() && energy <=0)
		{
			try {
				gameWorld.getGameServer().sendToAll(
						new GameServerCommand().create(
						new KillGameObject(getGameObjectId())));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dead = true;
			gameWorld.add(new SpacecraftSpawner(this, gameWorld));
		}
		
		//if(dead) explode();
		if(dead) return;
		
		if(energy < maxEnergy) energy += .01f;

		spacecraftController.Control(gameWorld, this);
		//new RotateAndMove(this,newVelocity);
	}

	private void explode() {
			for (int i = 0; i < 10; i++) {
			gameWorld.add(new BulletExplosion(gameWorld, new Vector2(
					getGamePosition().X + MyMath.Random.nextInt(50) - 25,
					getGamePosition().Y + MyMath.Random.nextInt(50) - 25),
					100 + MyMath.Random.nextInt(100)));
			// MyContentManager.Sound_Explosion.Play(Math.Max(0, 0.2f -
			// MyMath.Distance(gameWorld.CameraPosition, gamePosition) / 4000.0f
			// / 10));
		}
	}
	
	public void bringToLife(Vector2 gamePosition)
	{
		this.gamePosition = gamePosition;
		this.velocity = new Vector2();
		this.energy = maxEnergy;
		dead = false;
	}

	public boolean IsAlive() {
		return !dead;
	}

	public float Energy() {
		return energy;
	}

	AiIdentifiableObjectInfo aiIdentifiableObjectInfo;
	private int shipType;
	private int shipColor;
	private int shipOverlayColor;
	private String shipController;
	private RotateAndMove _rotateAndMoveEvent;
	
	public AiIdentifiableObjectInfo getAiIdentifiableObjectInfo() {
		aiIdentifiableObjectInfo.Create(
				getGameObjectId(), GameObjectType.Spacecraft, getGamePosition(),
				velocity);
		return aiIdentifiableObjectInfo;
	}

	public CollisionSphere CollisionSphere() {
		return myTexture.GetCollisionSphere(getGamePosition());
	}

	public void CollidedWith(ColloidableObject colloidableObject) {
		if (colloidableObject instanceof Bullet) {
			Bullet bullet = (Bullet) colloidableObject;
			energy -= bullet.energy / 100.0f;
		}
		else if(colloidableObject instanceof Spacecraft)
		{
			Spacecraft collidedWith = (Spacecraft)colloidableObject;
			Vector2 pos = new Vector2(getGamePosition()).add(collidedWith.getGamePosition()).divide(2);
			pos.X += MyMath.Random.nextInt(20)-10;
			pos.Y += MyMath.Random.nextInt(20)-10;
			gameWorld.add(new BulletExplosion(gameWorld, pos, 100));
			energy -= 10;
		}
	}

	public void createFromStream(GameWorld gameWorld, DataInput in) throws IOException
	{
		gamePosition = new Vector2();
		velocity = new Vector2();
		maxVelocity = 20f;
		maxTurnAngle = 0.1f;
		readExternal(in);
		SpacecraftController mySpacecraftController = new NullSpacecraftController();
		if(gameWorld.isClient())
			{
				if(shipController.equals("ai")) mySpacecraftController=new AiSpacecraftController();
				else if(shipController.equals(InetAddress.getLocalHost().toString())) 
					mySpacecraftController =  new PlayerSpacecraftController();		
				myCount = ++count;

			}
		
		init(gameWorld, gamePosition, velocity, energy, shipType, shipColor, shipOverlayColor, mySpacecraftController);
	}

	private void init(GameWorld gameWorld, Vector2 gamePosition, Vector2 velocity, float energy, int shipType, int shipColor, int shipOverlayColor, SpacecraftController spacecraftController) {
		this.gameWorld = gameWorld;
		this.energy = energy;
		this.spacecraftController = spacecraftController;
		this.shipType = shipType;
		this.shipColor = shipColor;
		this.shipOverlayColor = shipOverlayColor;

		this.gamePosition = gamePosition;
		this.velocity = velocity;
		this._rotateAndMoveEvent = new RotateAndMove();

		aiIdentifiableObjectInfo = new AiIdentifiableObjectInfo();
		dead = false;
		
		switch (shipType) {
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

		myTexture.filterWithColor(new Color(shipColor));
		myTextureOverlay.filterWithColor(new Color(shipOverlayColor));
	}

	public void readExternal(DataInput in) throws IOException  {
		super.readExternal(in);
		
		energy=in.readFloat();
		shipType=in.readInt();
		shipColor=in.readInt();
		shipOverlayColor=in.readInt();
		shipController=in.readUTF();
		
		gamePosition.readExternal(in);
		velocity.readExternal(in);
		direction= in.readFloat();
	}

	public void writeExternal(DataOutput out) throws IOException {
		super.writeExternal(out);
		
		out.writeFloat(energy);
		out.writeInt(shipType);
		out.writeInt(shipColor);
		out.writeInt(shipOverlayColor);
		out.writeUTF(shipController);
		
		gamePosition.writeExternal(out);

		velocity.writeExternal(out);

		out.writeFloat(direction);
	}

	public Vector2 getVelocity(Vector2 vel) {
		return vel.assign(velocity);
	}
	

	public Vector2 getPosition(Vector2 pos) {
		return pos.assign(gamePosition);
	}

	public void event(GameNetworkingEvent event) {
		if(event instanceof KillGameObject)
		{
			energy=0;
			dead=true;
			explode();
		}
		else if(event instanceof RotateAndMove)
		{
			gamePosition.assign(((RotateAndMove)event).getPosition());
			velocity.assign(((RotateAndMove)event).getVelocity());
			direction = ((RotateAndMove)event).getDirection();
		}
		else if(event instanceof FireBullet)
		{
			energy = ((FireBullet)event).getEnergy();
		}
	}

	public float getDirection() {
		return direction;
	}

	@Override
	public int getGameObjectTypeId() {
		// TODO Auto-generated method stub
		return GameObjectsList.SPACECRAFT;
	}
}



