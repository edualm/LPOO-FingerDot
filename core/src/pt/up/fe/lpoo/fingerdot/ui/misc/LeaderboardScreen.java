package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardEntry;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardManager;

import java.util.logging.FileHandler;

/**
 * Created by MegaEduX on 20/05/14.
 */

public class LeaderboardScreen implements Screen {
    private static final int kTableHeightHeader = 100;
    private static final int kTableHeightNormal = 50;

    private static final String kFontFileName = "hecubus.ttf";

    private Stage _stage = null;

    public LeaderboardScreen() {
        _stage = new Stage(new ScreenViewport(FingerDot.getSharedInstance().camera));
        _skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(_stage);


        Table table = new Table();
        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        FileHandle fontFile = Gdx.files.internal(kFontFileName);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 48;

        BitmapFont headerFont = generator.generateFont(param);


        _stage.addActor(table);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _stage.act(Gdx.graphics.getDeltaTime());
        _stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK))
            FingerDot.getSharedInstance().setScreen(new MainMenuScreen());
    }

    @Override public void show() {

    }

    @Override public void hide() {

    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void resize(int x, int y) {

    }

    @Override public void dispose() {
        _stage.dispose();
    }
}
