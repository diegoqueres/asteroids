/****************************************************************
 * Conversion of the game "Asteroids" to Libgdx (by diegoqueres)
 * Game presented in chapter 3 of the book:
 * Beginning Java Game Programming, 2nd Edition
 * by Jonathan S. Harbour
 ****************************************************************/

package net.diegoqueres;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Random;


/**
 * Primary class for the game
 */
public class MainGame extends ApplicationAdapter {
	public static final int SCENE_WIDTH = 640;
	public static final int SCENE_HEIGHT = 480;
	public static final int ASTEROIDS = 20;
	public static final int BULLETS = 10;
	public static final int SHIP_INIT_POSX = SCENE_WIDTH/2;
	public static final int SHIP_INIT_POSY = SCENE_HEIGHT/2;
	public static final int z = 0;

	public int screenWidth;
	public int screenHeight;

	SpriteBatch batch;
	PolygonSpriteBatch polyBatch;
	ShapeRenderer shapeRenderer;
	Random rand = new Random();

	Asteroid[] asteroids;
	Bullet[] bullets;
	Ship ship;

	boolean showBounds;
	int currentBullet;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		initGameAttributes();
		initGameElements();
		initInputEvents();
	}

	private void initGameAttributes() {
		showBounds = false;
		rand = new Random();
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
	}

	private void initGameElements() {
		ship = new Ship();
		ship.setX(SHIP_INIT_POSX);
		ship.setY(SHIP_INIT_POSY);

		bullets = new Bullet[BULLETS];
		for (int i = 0; i < bullets.length; i++)
			bullets[i] = new Bullet();

		asteroids = new Asteroid[ASTEROIDS];
		for (int i = 0; i < asteroids.length; i++) {
			Asteroid ast = new Asteroid();
			ast.setRotationVelocity(rand.nextInt(3) + 1);
			ast.setX(rand.nextInt(SCENE_WIDTH - 20) + 20);
			ast.setY(rand.nextInt(SCENE_HEIGHT - 20) + 20);
			ast.setMoveAngle(rand.nextInt(360));
			float angle = ast.getMoveAngle() - 90f;
			ast.setVelX(calcAngleMoveX(angle));
			ast.setVelY(calcAngleMoveY(angle));
			asteroids[i] = ast;		
		}
	}

	private void initInputEvents() {
		Gdx.input.setInputProcessor(new InputAdapter () {
			public boolean keyDown (int keycode) {
				switch (keycode) {
					//left arrow rotates ship left 5 degrees
					case Input.Keys.LEFT:
						ship.incFaceAngle(-5);
						if (ship.getFaceAngle() < 0) ship.setFaceAngle(360-5);
						break;

					//right arrow rotates ship right 5 degrees
					case Input.Keys.RIGHT:
						ship.incFaceAngle(5);
						if (ship.getFaceAngle() > 360) ship.setFaceAngle(5);
						break;

					//up arrow adds thrust to ship (1/10 normal speed)
					case Input.Keys.UP:
						ship.setMoveAngle(ship.getFaceAngle() - 90);
						ship.incVelX(calcAngleMoveX(ship.getMoveAngle())*0.1f);
						ship.incVelY(calcAngleMoveY(ship.getMoveAngle())*0.1f);
						break;		
						
					//ctrl, enter or space can be used to fire weaphon
					case Input.Keys.CONTROL_LEFT:
					case Input.Keys.CONTROL_RIGHT:
					case Input.Keys.ENTER:
					case Input.Keys.SPACE:
						//fire a bullet
						currentBullet++;
						if (currentBullet > BULLETS-1) currentBullet = 0;
						Bullet bullet = bullets[currentBullet];
						bullet.setAlive(true);

						//point bullet in same direction ship is facing
						bullet.setX(ship.getX());
						bullet.setY(ship.getY());
						bullet.setMoveAngle(ship.getFaceAngle()-90f);

						//fire bullet at angle of the ship
						float angle = bullet.getMoveAngle();
						float svx = ship.getVelX();
						float svy = ship.getVelY();
						bullet.setVelX(svx + calcAngleMoveX(angle) * 2f);
						bullet.setVelY(svy + calcAngleMoveY(angle) * 2f);

						Gdx.app.debug("fire bullet", "x="+bullet.getX()+", y="+bullet.getY());
						break;
				}
				return true;
			}
		});		 
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
	}

	@Override
	public void render() {
		draw();
		update(Gdx.graphics.getDeltaTime());

        Gdx.app.debug("Ship", "" + Math.round(ship.getX()) + "," +
            Math.round(ship.getY()));
		Gdx.app.debug("Move angle", ""+ Math.round(
            ship.getMoveAngle())+90);
		Gdx.app.debug("Face angle", "" + Math.round(
            ship.getFaceAngle()));
	}

	private void draw() {
		ScreenUtils.clear(0, 0, 0, 1);

		shapeRenderer.begin(ShapeType.Line);
		drawShip();
		drawBullets();
		drawAsteroids();
		shapeRenderer.end();
	}

	private void drawShip() {
		shapeRenderer.identity();
		shapeRenderer.translate(ship.getX(), ship.getY(), z);
		shapeRenderer.rotate(0, 0, 1, ship.getFaceAngle());
		shapeRenderer.setColor(Color.ORANGE);
		Polygon polygon = (Polygon) ship.getShape();
		shapeRenderer.polygon(polygon.getVertices());
	}

	private void drawAsteroids() {
		for (Asteroid asteroid : asteroids) {
			if (asteroid.isAlive()) {
				shapeRenderer.identity();
				shapeRenderer.translate(asteroid.getX(), asteroid.getY(), z);
				shapeRenderer.rotate(0, 0, 1, asteroid.getMoveAngle());
				shapeRenderer.setColor(Color.DARK_GRAY);
				shapeRenderer.polygon(asteroid.getVertices());
			}
		}
	}

	private void drawBullets() {
		for (Bullet bullet : bullets) {
			if (bullet.isAlive()) {
				shapeRenderer.identity();
				shapeRenderer.setColor(Color.MAGENTA);
				shapeRenderer.rect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
			}
		}
	}

	private void update(float deltaTime) {
		updateShip();
		updateBullets();
		updateAsteroids();
		checkCollisions();
	}

	private void updateShip() {
		// update ship's X position
		ship.incX(ship.getVelX());

		// wrap around left/right
		if (ship.getX() < -10)
			ship.setX(screenWidth + 10); // screen width deve estar em variavel dinamica aqui - corrigir DEBUG DIEGO
		else if (ship.getX() > screenWidth + 10)
			ship.setX(-10);

		// update ship's Y position
		ship.incY(ship.getVelY());

		// wrap around top/bottom
		if (ship.getY() < -10)
			ship.setY(screenHeight + 10);
		else if (ship.getY() > screenHeight + 10)
			ship.setY(-10);
	}

	private void updateBullets() {
		// move each of the bullets
		for (Bullet bullet : bullets) {
			if (!bullet.isAlive())
				continue;

			// update x position
			bullet.incX(bullet.getVelX());

			if (bullet.getX() < 0 || bullet.getX() > screenWidth) // disapear at left/right edge
				bullet.setAlive(false);

			// update y position
			bullet.incY(bullet.getVelY());

			if (bullet.getY() < 0 || bullet.getY() > screenHeight) // disapear at top/bottom edge
				bullet.setAlive(false);
		}
	}

	private void updateAsteroids() {
		// move each of the asteroids
		for (Asteroid asteroid : asteroids) {
			if (!asteroid.isAlive())
				continue;

			// update x position
			asteroid.incX(asteroid.getVelX());

			// warp at the screend edges
			if (asteroid.getX() < -20f) {
				asteroid.setX(screenWidth + 20f);
			} else if (asteroid.getX() > screenWidth + 20f) {
				asteroid.setX(-20f);
			}

			// update y position
			asteroid.incY(asteroid.getVelY());

			// warp at the screend edges
			if (asteroid.getY() < -20f) {
				asteroid.setY(screenHeight + 20f);
			} else if (asteroid.getY() > screenHeight + 20f) {
				asteroid.setY(-20f);
			}

			// update asteroid rotation
			asteroid.incMoveAngle(asteroid.getRotationVelocity());

			// keep the angle within 0-359 degrees
			if (asteroid.getMoveAngle() < 0f)
				asteroid.setMoveAngle(360f - asteroid.getRotationVelocity());
			else if (asteroid.getMoveAngle() > 360f)
				asteroid.setMoveAngle(asteroid.getRotationVelocity());
		}
	}

	private void checkCollisions() {
		// check asteroids collisions
		for (Asteroid asteroid : asteroids) {
			if (!asteroid.isAlive())
				continue;

			// check collisions with bullet
			for (Bullet bullet : bullets) {
				if (!bullet.isAlive())
					continue;

				// collision test
				if (asteroid.getBounds().contains(bullet.getX(), bullet.getY())) {
					bullet.setAlive(false);
					asteroid.setAlive(false);
					continue;
				}
			}

			// check collisions with ship
			if (asteroid.isAlive() && asteroid.getBounds().overlaps(ship.getBounds())) {
				asteroid.setAlive(false);
				ship.setX(SHIP_INIT_POSX);
				ship.setY(SHIP_INIT_POSY);
				ship.setFaceAngle(0f);
				ship.setVelX(0f);
				ship.setVelY(0f);
				continue;
			}
		}
	}

	private float calcAngleMoveY(float angle) {
		Double result = Double.valueOf(Math.cos((angle*Math.PI) / 180.0));
		return result.floatValue();
	}

	private float calcAngleMoveX(float angle) {
		Double result = Double.valueOf(Math.sin((angle*Math.PI) / 180.0));
		return result.floatValue();
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
	}
}
