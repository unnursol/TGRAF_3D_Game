package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Coin {

    private Shader shader;

    private Point3D modelPosition;
    private Vector3D vec3D;
    private float angleX = 0;
    private float angleZ = 0;
    private float angleY = 0;
    private float selfRotationSpeed = 200;

    private MeshModel model;

    private Point3D groundCenter;
    private float groundRadius;

    public Coin(Shader shader, float angleX, float angleZ)
    {
        this.shader = shader;
        model = G3DJModelLoader.loadG3DJFromFile("coin.g3dj");

        this.groundCenter = RaceGame.groundPosition;
        this.groundRadius = RaceGame.groundScale;
        this.angleX = angleX;
        this.angleZ = angleZ;

    }

    public Point3D getPosition()
    {
        return modelPosition;
    }

    public void update(float rawDeltaTime, float speed)
    {
        angleZ += speed;
        angleY += selfRotationSpeed*rawDeltaTime;
    }

    public void display()
    {
        ModelMatrix.main.loadIdentityMatrix();
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslationBaseCoords(groundCenter.x,groundCenter.y,groundCenter.z);
        ModelMatrix.main.addRotationZ(angleX);
        ModelMatrix.main.addRotationX(-angleZ);
        ModelMatrix.main.addTranslation(0f, groundRadius-0.5f, 0f);
        ModelMatrix.main.addScale(0.5f,0.5f,0.5f);
        ModelMatrix.main.addRotationY(angleY);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }
}
