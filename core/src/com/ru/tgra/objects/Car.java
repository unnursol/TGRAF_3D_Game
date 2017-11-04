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
    private float angleX = 0f;
    private float destAngleX = 0f;
    private float angleZ = 0f;
    private float destAngleZ = 0f;
    private float maxAngleZ = 45f;
    private float turnSpeed = 30f;

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
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            destAngleX = destAngleX <= -16 ? destAngleX : destAngleX - 8;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            destAngleX = destAngleX >= 16 ? destAngleX : destAngleX + 8;
        }
        if(angleX < destAngleX){
            angleX += rawDeltaTime * turnSpeed;
            if(angleX > destAngleX){
                angleX = destAngleX;
                angleZ = 0;
            } else {
                destAngleZ = -(Math.abs(destAngleX - angleX) / 8) * maxAngleZ;
                angleZ -= rawDeltaTime * turnSpeed * 3;
                if (angleZ < destAngleZ) {
                    angleZ = destAngleZ;
                }
            }
        } else if(angleX > destAngleX){
            angleX -= rawDeltaTime * turnSpeed;
            if(angleX < destAngleX){
                angleX = destAngleX;
                angleZ = 0;
            } else {
                destAngleZ = (Math.abs(destAngleX - angleX)/8) * maxAngleZ;
                angleZ += rawDeltaTime * turnSpeed*3;
                if(angleZ > destAngleZ){
                    angleZ = destAngleZ;
                }
            }
        }
    }

    public void display()
    {
        ModelMatrix.main.loadIdentityMatrix();
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslationBaseCoords(0f, -20f, 0f);
        ModelMatrix.main.addRotationZ(angleX);
        ModelMatrix.main.addTranslation(0f, 20.27f, 0f);
        ModelMatrix.main.addRotationY(angleZ);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }

    public float getLane() {
        return angleX;
    }
}