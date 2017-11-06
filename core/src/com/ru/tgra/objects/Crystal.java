package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;
import com.ru.tgra.utilities.RandomGenerator;

public class Crystal extends Object {
    private float scale = 1f;
    private float height = -0.5f;
    private float selfRotationSpeed = 200;

    public Crystal(Shader shader, float angleX, float angleZ) {
        super(shader, angleX, angleZ);
        angleY = RandomGenerator.randomIntegerInRange(0,180);
        model = G3DJModelLoader.loadG3DJFromFile("Crystal.g3dj");

    }
    public void update(float rawDeltaTime, float speed) {
        super.update(speed);
        angleY += selfRotationSpeed * rawDeltaTime;
    }
    public void display(){
        super.display(height, scale);
    }
}
