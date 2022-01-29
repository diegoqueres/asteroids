package net.diegoqueres;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Asteroid extends BaseVectorShape {
    //define the asteroid polygon shape
    private final static float[] astX = {-20, -13, 0,20,22, 20, 12, 2,-10, -22,-16};
    private final static float[] astY = {20, 23,17,20,16, -20, -22, -14,-17, -20, -5};
    private final float[] astVertices;

    public Asteroid() {
        astVertices = buildVertices(astX, astY);
        setShape(new Polygon(astVertices));
        setAlive(true);
        setRotationVelocity(0);
    }

    //rotation speed
    protected float rotationVelocity;
    public float getRotationVelocity () {
        return rotationVelocity;
    }
    public void setRotationVelocity(float v) {
        this.rotationVelocity = v;
    }

    //bouding rectangle
    public Rectangle getBounds() {
        return new Rectangle(getX()-20, getY()-20, 40, 40);
    }
    
    @Override
    public float[] getVertices() {
        return astVertices;
    }
}
