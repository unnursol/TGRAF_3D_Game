package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;
import com.ru.tgra.utilities.RandomGenerator;

public class Coin extends Object{

    private float selfRotationSpeed = 200;
    private float scale = 0.5f;
    private float height = -0.5f;


    public Coin(Shader shader, float angleX, float angleZ)
    {
        super(shader, angleX, angleZ);
        angleY = RandomGenerator.randomIntegerInRange(0,360);
        model = G3DJModelLoader.loadG3DJFromFile("coin.g3dj");
        collisionWidthback = 5f;
        collisionWidthFront = -5f;
        collisionWidthRight = 3f;
        collisionWidthLeft = -3f;
    }

    public void update(float rawDeltaTime, float speed)
    {
        super.update(speed);
        angleY += selfRotationSpeed * rawDeltaTime;
    }

    public void display(){
        super.display(height, scale);
    }
}
