package infinity;

import infinity.gameengine.Vector2;

import java.awt.Graphics2D;


public abstract class GameObject
    {
        private static int gameObjectIdCount = 0;

                private  int gameObjectId;

        public int getGameObjectId()
        {
             return gameObjectId;
        }

        protected Vector2 CameraPosition()
        {
            return gameWorld.getCameraPosition();
        }

        protected Vector2 ScreenCenter()
        {
             return gameWorld.getScreenCenter();
        }

        protected  GameWorld gameWorld;
        protected Vector2 gamePosition;

        protected GameObject(GameWorld gameWorld, Vector2 gamePosition)
        {
            this.gameWorld = gameWorld;
            this.gamePosition = gamePosition;
            gameObjectId = gameObjectIdCount++;
        }

        public abstract void Update();

        public abstract void Draw(Graphics2D g);//SpriteBatch spriteBatch);

        protected Vector2 PositionOnScreen()
        {
            return new Vector2().add(ScreenCenter()).subtract(CameraPosition()).add(gamePosition);
        }

        public boolean IsAlive()
        {
             return true;
        }
        
    }