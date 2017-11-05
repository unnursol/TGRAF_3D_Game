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
            model = G3DJModelLoader.loadG3DJFromFile("lpCar.g3dj");
        else if(color == 1)
            model = G3DJModelLoader.loadG3DJFromFile("lpCar.g3dj");
        // ...
    }
    public void update() {
        super.update(speed);
    }
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
}
