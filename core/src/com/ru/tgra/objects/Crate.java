package com.ru.tgra.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Crate {
    private Shader shader;

    private Point3D modelPosition;
    private Vector3D vec3D;
    private float angle = 0;

    private MeshModel model;

    private Point3D groundCenter;

    public Crate(Shader shader, float angle)
    {
        //float y = (RaceGame.groundPosition.y + RaceGame.groundScale);

        this.shader = shader;
//        model = G3DJModelLoader.loadG3DJFromFile("sumthin");
        this.angle = angle;
        float radians = angle * (float)Math.PI / 180.0f;
        float x = -(float) (RaceGame.groundPosition.x + RaceGame.groundScale * Math.sin(radians));
        float y = (float) (RaceGame.groundPosition.y + RaceGame.groundScale * Math.cos(radians));
        modelPosition = new Point3D(x, y, 0);

    }

    public Point3D getPosition()
    {
        return modelPosition;
    }

    public void update(float rawDeltaTime)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.O) && modelPosition.x <= 5)
        {
            angle -= 1;
            float radians = angle * (float)Math.PI / 180.0f;
            float x = -(float) (RaceGame.groundPosition.x + RaceGame.groundScale * Math.sin(radians));
            float y = (float) (RaceGame.groundPosition.y + RaceGame.groundScale * Math.cos(radians));
            modelPosition.y = y;
            modelPosition.x = x;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.P) && modelPosition.x >= -5)
        {
            angle += 1;
            float radians = angle * (float)Math.PI / 180.0f;
            float x = -(float) (RaceGame.groundPosition.x + RaceGame.groundScale * Math.sin(radians));
            float y = (float) (RaceGame.groundPosition.y + RaceGame.groundScale * Math.cos(radians));
            modelPosition.y = y;
            modelPosition.x = x;
        }
    }

    public void display()
    {
        ModelMatrix.main.loadIdentityMatrix();
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y+0.27f, modelPosition.z);
        ModelMatrix.main.addRotationZ(angle);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }
}
