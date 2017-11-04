package com.ru.tgra.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Shader;
import com.ru.tgra.shapes.SphereGraphic;
import com.ru.tgra.models.*;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Ground {

    float angleX;
    private Shader shader;
    private MeshModel model;
    Point3D center;
    float scale;

    public Ground(Point3D position, float groundScale, Shader shader) {
        angleX = 0f;
        this.shader = shader;
        center = position;
        scale = groundScale;
        model = G3DJModelLoader.loadG3DJFromFile("globe.g3dj");
    }

    public Point3D getCenter(){
        return center;
    }

    public float getScale(){
        return scale;
    }

    public void update(float deltatime, float speed){
        angleX -= speed * deltatime;
    }

    public void display() {

//        shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
//        shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
//        shader.setMaterialEmission(0, 0, 0, 1);
//        shader.setShininess(50.0f);

        ModelMatrix.main.pushMatrix();

        ModelMatrix.main.addTranslation(center.x, center.y, center.z);
        ModelMatrix.main.addScale(scale,scale,scale);
        ModelMatrix.main.addRotationX(angleX);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }
}
