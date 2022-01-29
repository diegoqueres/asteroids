package net.diegoqueres;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Ship extends BaseVectorShape {
    //define the ship polygon
    private final static float[] shipX = {-6, -3, 0, 3, 6, 0};
    private final static float[] shipY = {6, 7, 7, 7, 6, -7};
    private final float[] shipVertices;

    public Ship() {
        shipVertices = buildVertices(shipX, shipY);
        setShape(new Polygon(shipVertices));
        setAlive(true);
    }

    @Override
    public Rectangle getBounds() {
        Rectangle r = new Rectangle(getX()-6, getY()-6, 12, 12);
        return r;
    }

    @Override
    public float[] getVertices() {
        return shipVertices;
    }
}
