package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Crystal {

    private Shader shader;

    private Point3D modelPosition;
    private Vector3D vec3D;
    private float angleX = 0;
    private float angleZ = 0;

    private MeshModel model;

    private Point3D groundCenter;

    public Crystal(Shader shader, float angleX, float angleZ)
    {
        this.shader = shader;
        model = G3DJModelLoader.loadG3DJFromFile("Crystal.g3dj");

        this.angleX = angleX;
        float radiansX = angleX * (float)Math.PI / 180.0f;
        float x = -(float) (RaceGame.groundPosition.x + RaceGame.groundScale * Math.sin(radiansX));
        this.angleZ = angleZ;

        float radiansZ = angleZ * (float)Math.PI / 180.0f;
        float z = -(float) (RaceGame.groundPosition.x + RaceGame.groundScale * Math.sin(radiansZ));
        float y = (float) (RaceGame.groundPosition.y + RaceGame.groundScale * Math.cos(radiansX) * Math.cos(radiansZ));

        modelPosition = new Point3D(x, y, z);

    }

    public Point3D getPosition()
    {
        return modelPosition;
    }

    public void update(float rawDeltaTime, float speed)
    {
        angleZ += speed*rawDeltaTime;

        float radiansX = angleX * (float)Math.PI / 180.0f;
        float radiansZ = angleZ * (float)Math.PI / 180.0f;
        float z = -(float) (RaceGame.groundPosition.x + RaceGame.groundScale * Math.sin(radiansZ));
        float y = (float) ((RaceGame.groundPosition.y + RaceGame.groundScale * Math.cos(radiansX))-
                (RaceGame.groundPosition.y + RaceGame.groundScale * Math.cos(radiansZ)));
        modelPosition.z = z;
        modelPosition.y = y;
    }

    public void display()
    {
        ModelMatrix.main.loadIdentityMatrix();
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addRotationZ(angleX);
        ModelMatrix.main.addRotationX(-angleZ);
        ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }
}
