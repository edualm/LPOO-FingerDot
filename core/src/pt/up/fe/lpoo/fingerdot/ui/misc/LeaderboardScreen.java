package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
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

        Gdx.input.setInputProcessor(_stage);

        Table table = new Table();
        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        FileHandle fontFile = Gdx.files.internal(kFontFileName);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 48;

        BitmapFont headerFont = generator.generateFont(param);

        param.size = 32;

        BitmapFont font = generator.generateFont(param);

        generator.dispose();

        Label.LabelStyle fontStyle = new Label.LabelStyle(headerFont, Color.WHITE);

        table.add(new Label("#", fontStyle)).width(50).height(kTableHeightHeader);

        table.add(new Label("Nickname", fontStyle)).width(500).height(kTableHeightHeader);

        table.add(new Label("Score", fontStyle)).width(300).height(kTableHeightHeader);

        table.row();

        fontStyle = new Label.LabelStyle(font, Color.WHITE);

        if (LeaderboardManager.sharedManager().retrieveOnlineLeaderboard()) {
            int posCounter = 1;

            for (LeaderboardEntry lbe : LeaderboardManager.sharedManager().getOnlineLeaderboard()) {
                table.add(new Label(Integer.toString(posCounter), fontStyle )).width(50).height(kTableHeightNormal);
                table.add(new Label(lbe.username, fontStyle)).width(500).height(kTableHeightNormal);
                table.add(new Label(Integer.toString(lbe.score), fontStyle)).width(300).height(kTableHeightNormal);

                table.row();

                posCounter++;
            }
        }

        _stage.addActor(table);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _stage.act(Gdx.graphics.getDeltaTime());
        _stage.draw();
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
