package net.diegoqueres;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;


public abstract class BaseVectorShape {
    private Shape2D shape;
    private boolean alive;
    private float x, y;
    private float velX, velY;
    private float moveAngle, faceAngle;

    public BaseVectorShape() {
        setShape(null);
        setAlive(false);
        setX(0.0f);
        setY(0.0f);
        setVelX(0.0f);
        setVelY(0.0f);
        setMoveAngle(0.0f);
        setFaceAngle(0.0f);
    }

    abstract Rectangle getBounds();

    public Shape2D getShape() {
        return shape;
    }

    public boolean isAlive() {
        return alive;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public float getMoveAngle() {
        return moveAngle;
    }

    public float getFaceAngle() {
        return faceAngle;
    }

    public void setShape(Shape2D shape) {
        this.shape = shape;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void incX(float x) {
        this.x += x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void incY(float y) {
        this.y += y;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void incVelX(float velX) {
        this.velX += velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void incVelY(float velY) {
        this.velY += velY;
    }

    public void setMoveAngle(float moveAngle) {
        this.moveAngle = moveAngle;
    }

    public void incMoveAngle(float moveAngle) {
        this.moveAngle += moveAngle;
    }

    public void setFaceAngle(float faceAngle) {
        this.faceAngle = faceAngle;
    }

    public void incFaceAngle(float faceAngle) {
        this.faceAngle += faceAngle;
    }

    public float[] buildVertices(float[] xCoordinates, float[] yCoordinates) {
        float[] vertices = new float[xCoordinates.length*2];
        for (int i = 0; i < xCoordinates.length; i += 2) {
            vertices[i] = xCoordinates[i];
            vertices[i+1] = yCoordinates[i];
        }
        return vertices;
    }

    public abstract float[] getVertices();
}
