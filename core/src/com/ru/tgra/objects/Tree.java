package com.ru.tgra.objects;

import com.ru.tgra.game.RaceGame;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Tree extends Object{

    public Tree(Shader shader, float angleX, float angleZ, int tree) {
        super(shader, angleX, angleZ);
        if (tree == 0) {
            model = G3DJModelLoader.loadG3DJFromFile("Palm_Tree.g3dj");
        } else if (tree == 1) {
            model = G3DJModelLoader.loadG3DJFromFile("Oak_Tree.g3dj");
        }
    }
}
