package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;
import com.ru.tgra.utilities.RandomGenerator;

public class Cone extends Object {
    private float scale = 0.2f;
    private float height = -0.2f;

    public Cone(Shader shader, float angleX, float angleZ) {
        super(shader, angleX, angleZ);
        angleY = RandomGenerator.randomIntegerInRange(0,180);
        model = G3DJModelLoader.loadG3DJFromFile("cone.g3dj");
        collisionWidthback = 5f;
        collisionWidthFront = -5f;
        collisionWidthRight = 4f;
        collisionWidthLeft = -4f;

    }
    public void update(float speed) {
        super.update(speed);
    }
    public void display(){
        super.display(height, scale);
    }

    public boolean collidingWithPlayer(float playerXposition) {
        if((angleZ >= collisionWidthFront && angleZ <= collisionWidthback) &&
                (angleX >= playerXposition+collisionWidthLeft && angleX <= playerXposition+collisionWidthRight)) {
            super.update(-5);
            return true;
        }
        return false;
    }
}
