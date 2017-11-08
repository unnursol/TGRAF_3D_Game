package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class CarObsticle extends Object {
    private float scale = 1f;
    private float height = 0f;
    private float speed = 0f;

    public CarObsticle(Shader shader, float angleX, float angleZ, float speed, int color) {
        super(shader, angleX, angleZ);
        this.speed = speed;
        if(color == 0)
            model = G3DJModelLoader.loadG3DJFromFile("blueCar.g3dj");
        else if(color == 1)
            model = G3DJModelLoader.loadG3DJFromFile("greenCar.g3dj");
        else if(color == 2)
            model = G3DJModelLoader.loadG3DJFromFile("greyCar.g3dj");
        else
            model = G3DJModelLoader.loadG3DJFromFile("pineCar.g3dj");
        collisionWidthback = 6f;
        collisionWidthFront = -6f;
        collisionWidthRight = 4f;
        collisionWidthLeft = -4f;
    }
    public void update(float playerSpeed){
        super.update(-speed + playerSpeed);
    }
    public void update() {
        super.update(+speed);
    }
    public void oppositeUpdate() { super.update(-speed); }
    public void display(){
        super.display(height, scale);
    }

    public boolean collidingWithObj(Object obj) {
        if(angleZ >= obj.angleZ-15f && angleZ <= obj.angleZ+10f)
            return true;
        return false;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return this.speed;
    }

    public boolean isOppositeOutOfBounce() {
        if(angleZ <= -50)
            return true;
        return false;
    }

    public boolean collidingWithPlayer(Car player) {
        if((angleZ >= collisionWidthFront && angleZ <= collisionWidthback) &&
           (angleX >= player.getAngleX() + collisionWidthLeft && angleX <= player.getAngleX() + collisionWidthRight) ) {

            if(angleX > player.getAngleX())
               player.hitCarToRight();
            else if(angleX < player.getAngleX())
                player.hitCarToTheLeft();
            return true;
        }
        return false;
    }
}
