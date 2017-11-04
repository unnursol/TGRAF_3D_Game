package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Crystal extends Object {

    public Crystal(Shader shader, float angleX, float angleZ) {
        super(shader, angleX, angleZ);
        model = G3DJModelLoader.loadG3DJFromFile("Crystal.g3dj");
    }
}
