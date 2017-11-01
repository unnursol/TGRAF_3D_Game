package com.ru.tgra.materials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.nio.FloatBuffer;

public class Shader {

    private int renderingProgramID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int positionLoc;
    private int normalLoc;
    private int uvLoc;

    private int modelMatrixLoc;
    private int viewMatrixLoc;
    private int projectionMatrixLoc;

    private boolean usesDiffuseTexture = false;
    private int usesDiffuseTexLoc;
    private int diffuseTextureLoc;

    private boolean usesSpecularTexture = false;
    private int usesSpecularTexLoc;
    private int specularTextureLoc;

    private int eyePosLoc;

    private int globalAmbLoc;
    private int shininessFactorLoc;
    private int lightPosLoc;

    private int spotDirLoc;
    private int spotExpLoc;
    private int constantAttLoc;
    private int linearAttLoc;
    private int quadraticAttLoc;

    // Light

    private int lightDifLoc;
    private int matDifLoc;
    private int matSpecLoc;
    private int matAmbLoc;
    private int matEmissionLoc;
    private int matTransLoc;
    private int matShineLoc;



    String vertexShaderString;
    String fragmentShaderString;

    public Shader() {
        vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
        fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

        vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
        Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

        Gdx.gl.glCompileShader(vertexShaderID);
        Gdx.gl.glCompileShader(fragmentShaderID);

        System.out.println(Gdx.gl.glGetShaderInfoLog(vertexShaderID));
        System.out.println(Gdx.gl.glGetShaderInfoLog(fragmentShaderID));

        renderingProgramID = Gdx.gl.glCreateProgram();

        Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
        Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);

        Gdx.gl.glLinkProgram(renderingProgramID);

        Gdx.gl.glUseProgram(renderingProgramID);


        // DEBUG
        System.out.println(Gdx.gl.glGetShaderInfoLog(vertexShaderID));
        System.out.println(Gdx.gl.glGetShaderInfoLog(fragmentShaderID));

        initLocs();
    }

    public void setMaterialDiffuse(float r, float g, float b, float a) {
        Gdx.gl.glUniform4f(matDifLoc, r, g, b, a);
    }

    public void setLightDiffuse(float r, float g, float b, float a) {
        Gdx.gl.glUniform4f(lightDifLoc, r, g, b, a);
    }

    public void setLightPosition(float x, float y, float z, float a) {
        Gdx.gl.glUniform4f(lightPosLoc, x, y, z, a);
    }

    public void setEyePosition(float x, float y, float z, float a) {
        Gdx.gl.glUniform4f(eyePosLoc, x, y, z, a);
    }

    public int getVertexPointer() {
        return positionLoc;
    }

    public int getNormalPointer() {
        return normalLoc;
    }

    public int getUVPointer() { return uvLoc; }

    public void setModelMatrix(FloatBuffer matrix) {
        Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
    }

    public void setViewMatrix(FloatBuffer matrix) {
        Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
    }

    public void setProjectionMatrix(FloatBuffer matrix) {
        Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
    }

//    public void setMaterialDiffuse(float r, float g, float b, float a)
//    {
//        Gdx.gl.glUniform4f(materialDiffuseLoc, color.r, color.g, color.b, color.a);
//    }
//
//    public void setMaterialSpecular(float r, float g, float b, float a)
//    {
//        Gdx.gl.glUniform4f(materialSpecularLoc, color.r, color.g, color.b, color.a);
//    }
//
//    public void setMaterialAmbience(float r, float g, float b, float a)
//    {
//        Gdx.gl.glUniform4f(materialAmbienceLoc, color.r, color.g, color.b, color.a);
//    }
//
//    public void setMaterialEmission(float r, float g, float b, float a)
//    {
//        Gdx.gl.glUniform4f(materialEmissionLoc, color.r, color.g, color.b, color.a);
//    }
//
//    public void setMaterialTransparency(float transparency)
//    {
//        Gdx.gl.glUniform1f(materialTransparencyLoc, transparency);
//    }

    public void setShininessFactor(float f)
    {
        Gdx.gl.glUniform1f(shininessFactorLoc, f);
    }

//    public void setBrightness(float f)
//    {
//        Gdx.gl.glUniform1f(brightnessLoc, f);
//    }

//    public void setLightPosition(float x, float y, float z, float a)
//    {
//        Gdx.gl.glUniform4f(lightPosLoc[lightID], position.x, position.y, position.z, 1.0f);
//    }
//
//    public void setLightColor(float r, float g, float b, float a)
//    {
//        Gdx.gl.glUniform4f(lightColorLoc[lightID], color.r, color.g, color.b, color.a);
//    }
//
//    public void setLightDirection(int lightID, Vector3D direction)
//    {
//        Gdx.gl.glUniform4f(lightDirectionLoc[lightID], direction.x, direction.y, direction.z, 0f);
//    }
//
//    public void setSpotFactor(int lightID, float f)
//    {
//        Gdx.gl.glUniform1f(lightSpotFactorLoc[lightID], f);
//    }
//
//    public void setConstantAttenuation(int lightID, float f)
//    {
//        Gdx.gl.glUniform1f(lightConstAttLoc[lightID], f);
//    }
//
//    public void setLinearAttenuation(int lightID, float f)
//    {
//        Gdx.gl.glUniform1f(lightLinearAttLoc[lightID], f);
//    }
//
//    public void setQuadraticAttenuation(int lightID, float f)
//    {
//        Gdx.gl.glUniform1f(lightQuadAttLoc[lightID], f);
//    }
//
//    public void setGlobalAmbience(Color color)
//    {
//        Gdx.gl.glUniform4f(globalAmbLoc, color.r, color.g, color.b, color.a);
//    }


    public void setDiffuseTexture(Texture tex) {
        if(tex == null)
        {
            Gdx.gl.glUniform1f(usesDiffuseTexLoc, 0.0f);
            usesDiffuseTexture = false;
        }
        else
        {
            tex.bind(0);
            Gdx.gl.glUniform1i(diffuseTextureLoc, 0);
            Gdx.gl.glUniform1f(usesDiffuseTexLoc, 1.0f);
            usesDiffuseTexture = true;
        }
    }

    public void setSpecularTexture(Texture tex) {
        if(tex == null)
        {
            Gdx.gl.glUniform1f(usesSpecularTexLoc, 0.0f);
            usesSpecularTexture = false;
        }
        else
        {
            tex.bind(1);
            Gdx.gl.glUniform1i(specularTextureLoc, 1);
            Gdx.gl.glUniform1f(usesSpecularTexLoc, 1.0f);
            usesSpecularTexture = true;
        }
    }

    private void initLocs() {
        positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
        Gdx.gl.glEnableVertexAttribArray(positionLoc);

        normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
        Gdx.gl.glEnableVertexAttribArray(normalLoc);

        modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
        viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
        projectionMatrixLoc	    = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

        lightPosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition");
        lightDifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDiffuse");
        eyePosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");

        globalAmbLoc            = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbience");
        matSpecLoc              = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
        matDifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
        matAmbLoc	            = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialAmbience");
        matEmissionLoc	        = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");
        matTransLoc             = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialTransparency");
        shininessFactorLoc      = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_shininessFactor");
    }
}


