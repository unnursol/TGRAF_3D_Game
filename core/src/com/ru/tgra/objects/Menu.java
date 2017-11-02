package com.ru.tgra.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Shader;

public class Menu {
    // Font types
    BitmapFont fontLarge;
    BitmapFont fontMedium;
    BitmapFont fontSmall;

    private static SpriteBatch batch;
    private static GlyphLayout layout;

    public Menu() {
        batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GROBOLD.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        fontLarge = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }

    public void displayMainMenu(Shader shader) {
        Point3D position = new Point3D(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0f);
        displayText(fontLarge, "PRESS ENTER TO START THE GAME", position);

        shader.setShader();
    }
    private void displayText(BitmapFont font, String text, Point3D position) {

        layout = new GlyphLayout(font, text);

        float offsetX = layout.width /2;
        float offsetY = layout.height /2;


        float fontX = position.x - offsetX;
        float fontY = position.y + offsetY;

        batch.begin();

        font.setColor(1f, 1f, 1f, 1f);
        font.draw(batch, text, fontX, fontY);

        batch.end();


    }

    public void displayGameOver(Shader shader, int score) {
    }

    public void displayScore(Shader shader, int score) {
        Point3D position = new Point3D(200, Gdx.graphics.getHeight()-40, 0f);
        displayText(fontLarge, "SCORE: " + score, position);

        shader.setShader();
    }
}
