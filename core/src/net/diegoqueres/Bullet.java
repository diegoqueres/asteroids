package net.diegoqueres;

import com.badlogic.gdx.math.Rectangle;

public class Bullet extends BaseVectorShape {
    private final float width;
    private final float height;

    public Bullet() {
        width = 1f;
        height = 1f;
        setShape(new Rectangle(0, 0, width, height));
        setAlive(false);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public float[] getVertices() {
        return null;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
