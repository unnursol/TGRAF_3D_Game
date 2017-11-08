package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Object {

    protected MeshModel model;

    private Shader shader;
    protected float angleX = 0;
    protected float angleZ = 0;
    protected float angleY = 0;

    // For checking collision with the player
    protected float collisionWidthFront = -1f;
    protected float collisionWidthback = 1f;
    protected float collisionWidthRight = 1f;
    protected float collisionWidthLeft = -1f;

    private Point3D groundCenter;
    private float groundRadius;

    protected float xOffset;
    protected float yOffset;
    protected float zOffset;

    protected boolean rotatingSelf;
    protected float selfRotationX;
    protected float selfRotationY;
    protected float selfRotationZ;

    public Object(Shader shader, float angleX, float angleZ)
    {
        this.groundCenter = RaceGame.groundPosition;
        this.groundRadius = RaceGame.groundScale;
        this.angleX = angleX;
        this.angleZ = angleZ;
        this.shader = shader;
        this.selfRotationX = 0f;
        this.selfRotationY = 0f;
        this.selfRotationZ = 0f;
        this.rotatingSelf = false;
    }

    public void update(float speed)
    {
        angleZ += speed;
    }

    public void display(float height, float scale)
    {
        ModelMatrix.main.loadIdentityMatrix();
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslationBaseCoords(groundCenter.x,groundCenter.y,groundCenter.z);
        ModelMatrix.main.addRotationZ(angleX);
        ModelMatrix.main.addRotationX(-angleZ);
        ModelMatrix.main.addTranslation(0f+xOffset, groundRadius+height+yOffset, 0f+zOffset);
        ModelMatrix.main.addScale(scale,scale,scale);
        ModelMatrix.main.addRotationY(angleY);
        if(rotatingSelf){
            ModelMatrix.main.addRotationX(selfRotationX);
            ModelMatrix.main.addRotationY(selfRotationY);
            ModelMatrix.main.addRotationZ(selfRotationZ);
        }
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }

    public float getLane() {
        return angleX;
    }

    public boolean collidingWithPlayer(Car player) {
        if((angleZ >= collisionWidthFront && angleZ <= collisionWidthback) &&
                (angleX >= player.getAngleX() + collisionWidthLeft && angleX <= player.getAngleX() + collisionWidthRight) )
            return true;
        return false;
    }

    public boolean isOutOfBounce() {
        if(angleZ >= 90)
            return true;
        return false;
    }
}
