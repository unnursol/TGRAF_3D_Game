package com.ru.tgra.objects;

import com.ru.tgra.models.Shader;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.utilities.RandomGenerator;

public class Cone extends Object {
    private float scale = 0.2f;
    private float height = -0.2f;
    private boolean isHit = false;


    public Cone(Shader shader, float angleX, float angleZ) {
        super(shader, angleX, angleZ);
        angleY = RandomGenerator.randomFloatInRange(0,180);
        model = G3DJModelLoader.loadG3DJFromFile("cone.g3dj");
        collisionWidthback = 5f;
        collisionWidthFront = -5f;
        collisionWidthRight = 4f;
        collisionWidthLeft = -4f;

    }
    public void update(float speed) {
        if(isHit) {
            super.update(-speed);
            isHit = false;
        }
        else {
            super.update(speed);
        }
    }
    public void display(){
        super.display(height, scale);
    }

    public boolean collidingWithPlayer(Car player) {
        if((angleZ >= collisionWidthFront && angleZ <= collisionWidthback) &&
                (angleX >= player.getAngleX()+collisionWidthLeft && angleX <= player.getAngleX()+collisionWidthRight)) {
            super.update(-5);
            isHit = true;
            return true;
        }
        return false;
    }
}
