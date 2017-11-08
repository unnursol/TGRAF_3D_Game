package com.ru.tgra.objects;

import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.utilities.RandomGenerator;

public class Cone extends Object {
    private float scale = 0.2f;
    private float height = -0.2f;
    private boolean isHit = false;
    private boolean flyingAway = false;
    private Vector3D randomRotate;
    private Vector3D randomDirection;


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
        if(flyingAway){
            selfRotationX += randomRotate.x * (0.5 + speed) * 30;
            selfRotationY += randomRotate.y * (0.5 + speed) * 30;
            selfRotationZ += randomRotate.z * (0.5 + speed) * 30;
            yOffset += speed;
            xOffset += randomDirection.x * speed;
            zOffset += randomDirection.z * speed;
            super.update(speed);
        }
        else if(isHit) {
            super.update(-speed);
            isHit = false;
            flyingAway = true;
            rotatingSelf = true;
            randomRotate = new Vector3D(RandomGenerator.randomFloatInRange(0f,1f), RandomGenerator.randomFloatInRange(0,1), RandomGenerator.randomFloatInRange(0,1));
            randomDirection = new Vector3D(RandomGenerator.randomFloatInRange(-2,2), 0, RandomGenerator.randomFloatInRange(-2,2));
        }
        else {
            super.update(speed);
        }
    }
    public void display(){
        super.display(height, scale);
    }

    public boolean collidingWithPlayer(Car player) {
        if(!flyingAway){
            if((angleZ >= collisionWidthFront && angleZ <= collisionWidthback) &&
                    (angleX >= player.getAngleX()+collisionWidthLeft && angleX <= player.getAngleX()+collisionWidthRight)) {
                super.update(-5);
                isHit = true;
                return true;
            }
        }
        return false;
    }
}
