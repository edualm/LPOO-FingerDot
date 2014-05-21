package pt.up.fe.lpoo.fingerdot.ui.singleplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MegaEduX on 20/05/14.
 */

public class SinglePlayerEndGameScreen implements Screen {
    final FingerDot _game = FingerDot.sharedInstance;

    int _ticksBeforeProcessingTouches = 30;

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //  Draw the end game screen.

        if (_ticksBeforeProcessingTouches > 0) {
            _ticksBeforeProcessingTouches--;

            return;
        }

        if (Gdx.input.isTouched()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    _game.setScreen(new MainMenuScreen());

                    dispose();
                }
            }, 100);
        }
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

    }
}