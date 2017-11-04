package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Tree {

    private Shader shader;

    private Point3D modelPosition;
    private Vector3D vec3D;
    private float angleX = 0;
    private float angleZ = 0;

    private MeshModel palmTree;
    private MeshModel oakTree;

    private Point3D groundCenter;
    private float groundRadius;

    public Tree(Shader shader, float angleX, float angleZ, int tree)
    {
        this.shader = shader;
        if(tree == 0) {
            palmTree = G3DJModelLoader.loadG3DJFromFile("Palm_Tree.g3dj");
        }
        else if(tree == 1) {
            oakTree = G3DJModelLoader.loadG3DJFromFile("Oak_Tree.g3dj");
        }
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
        angleZ += speed*rawDeltaTime;
    }

    public void display()
    {
        ModelMatrix.main.loadIdentityMatrix();
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslationBaseCoords(groundCenter.x,groundCenter.y,groundCenter.z);
        ModelMatrix.main.addRotationZ(angleX);
        ModelMatrix.main.addRotationX(-angleZ);
        ModelMatrix.main.addTranslation(0f, groundRadius-0.5f, 0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        palmTree.draw(shader);
        ModelMatrix.main.popMatrix();
    }
}
