package com.ru.tgra.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.shapes.BoxGraphic;

public class SkyBox {
    private Point3D position;
    private float size;
    private Texture tex;

    public SkyBox() {
        this.tex = new Texture(Gdx.files.internal("textures/city1.jpg"));
    }

    public void display(Shader shader) {
        //shader.setMaterialDiffuse(s, 0.4f, c, 1.0f);
        shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
        shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
        //shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
        shader.setMaterialEmission(0, 0, 0, 1);
        shader.setShininess(50.0f);

        ModelMatrix.main.pushMatrix();

        ModelMatrix.main.addTranslation(0.0f, 4.0f, 0.0f);
        ModelMatrix.main.addScale(50f,50f,50f);
        //ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        ModelMatrix.main.popMatrix();

        BoxGraphic.drawSolidCube(shader, tex);

    }
}
