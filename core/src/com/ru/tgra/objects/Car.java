package com.ru.tgra.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ru.tgra.shapes.g3djmodel.*;
import com.ru.tgra.models.*;
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

    private MeshModel model;

    private Point3D groundCenter;

    public Car(Shader shader)
    {
        modelPosition = new Point3D(0, 0, 0);
        this.shader = shader;
        model = G3DJModelLoader.loadG3DJFromFile("lpCar.g3dj");
    }
    
    public Point3D getPosition()
    {
        return modelPosition;
    }

    public void update(float rawDeltaTime)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.O))
        {
            modelPosition.x += 17 * rawDeltaTime;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.P))
        {
            modelPosition.x -= 17 * rawDeltaTime;
        }
    }

    public void display()
    {
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.loadIdentityMatrix();
        ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }
}