package com.ru.tgra.objects;

import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Shader;
import com.ru.tgra.shapes.SphereGraphic;
import com.ru.tgra.models.*;

public class Ground {

    Point3D center;
    float scale;

    public Ground(Point3D position, float groundScale) {
        center = position;
        scale = groundScale;
    }

    public Point3D getCenter(){
        return center;
    }

    public float getScale(){
        return scale;
    }

    public void display(Shader shader) {

        shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
        shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
        shader.setMaterialEmission(0, 0, 0, 1);
        shader.setShininess(50.0f);

        ModelMatrix.main.pushMatrix();

        ModelMatrix.main.addTranslation(center.x, center.y, center.z);
        ModelMatrix.main.addScale(scale,scale,scale);
        //ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        ModelMatrix.main.popMatrix();

        SphereGraphic.drawSolidSphere(shader, null, null);

    }
}
