package com.ru.tgra.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ru.tgra.game.RaceGame;
import com.ru.tgra.shapes.g3djmodel.*;
import com.ru.tgra.models.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
//import com.ru.tgra.motion.BezierMotion;
//import com.ru.tgra.motion.LinearMotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Car
{
    private Shader shader;

    private Point3D modelPosition;
    private Vector3D vec3D;
    private float angle = 0;

    private MeshModel model;

    private Point3D groundCenter;

    public Car(Shader shader)
    {
        float y = (RaceGame.groundPosition.y + RaceGame.groundScale);
        modelPosition = new Point3D(0, y, 0);
        this.shader = shader;
        model = G3DJModelLoader.loadG3DJFromFile("lpCar.g3dj");
    }

    public Point3D getPosition()
    {
        return modelPosition;
    }

    public void update(float rawDeltaTime)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.O) && modelPosition.x <= 5)
        {
            //modelPosition.x += 22 * rawDeltaTime;
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