package pt.up.fe.lpoo.fingerdot.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import pt.up.fe.lpoo.fingerdot.MainMenuScreen;
import pt.up.fe.lpoo.fingerdot.logic.Dot;
import pt.up.fe.lpoo.fingerdot.logic.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.GameController;
import pt.up.fe.lpoo.fingerdot.logic.MPGameController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MegaEduX on 05/05/14.
 */

public class MultiPlayerScreen implements Screen {
    final FingerDot _game;

    boolean _isTouching = false;

    boolean _paused = false;

    private MPGameController _controller;

    WarpClient _warpClient;

    MultiPlayerMessenger _mpMessenger = new MultiPlayerMessenger(this);

    public static String AppWarpAppKey = "14a611b4b3075972be364a7270d9b69a5d2b24898ac483e32d4dc72b2df039ef";
    public static String AppWarpSecretKey = "55216a9a165b08d93f9390435c9be4739888d971a17170591979e5837f618059";

    public MultiPlayerScreen(final FingerDot game) {
        _game = game;

        _controller = new MPGameController(game, 1, 3);

        try {
            WarpClient.initialize(AppWarpAppKey, AppWarpSecretKey);

            _warpClient = WarpClient.getInstance();
        } catch (Exception e) {

        }
    }

    public MPGameController getController() {
        return _controller;
    }

    public void renderEnemyTouch(float x, float y, boolean correct) {

    }

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (_paused) {
            //  Do Something...

            return;
        }

        if (_controller.getLives() <= 0) {
            _game.renderer.begin(ShapeRenderer.ShapeType.Filled);

            _game.renderer.setColor(Color.BLUE);

            _game.renderer.rect(200, 200, _game.camera.viewportWidth - 400, _game.camera.viewportHeight - 400);

            _game.renderer.end();

            if (Gdx.input.isTouched()) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        _game.setScreen(new MainMenuScreen(_game));
                    }
                }, 100);
            }

            return;
        }

        if (Gdx.input.isTouched() && !_isTouching) {
            int x = Gdx.input.getX(), y = Gdx.input.getY();

            if (!(_game.camera.viewportWidth == Gdx.graphics.getWidth() && _game.camera.viewportHeight == Gdx.graphics.getHeight())) {
                x *= _game.camera.viewportWidth / Gdx.graphics.getWidth();
                y *= _game.camera.viewportHeight / Gdx.graphics.getHeight();
            }

            System.out.println("Real X: " + Gdx.input.getX() + ". Computed X: " + x);
            System.out.println("Real Y: " + Gdx.input.getY() + ". Computed Y: " + y);

            if (x > _game.camera.viewportWidth - 75 && x < _game.camera.viewportWidth + 75 && y > 675)
                pause();
            else
                _controller.performTouch(x, y);

            _isTouching = true;
        }

        if (!Gdx.input.isTouched())
            _isTouching = false;

        _controller.performTick();

        _game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Dot aDot : _controller.getDotsOnPlay()) {
            _game.renderer.setColor(1.0f, 0.0f, 0.0f, (float)aDot.getTicks() / 100.0f);

            _game.renderer.circle(aDot.getX(), aDot.getY(), aDot.getRadius());
        }

        _game.renderer.end();

        _game.batch.begin();

        _game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        _game.font.draw(_game.batch, "Score: " + _controller.getScore(), 25, 50);

        _game.font.draw(_game.batch, "Pause", _game.camera.viewportWidth / 2 - 25, 50);

        _game.font.draw(_game.batch, "Lives: " + _controller.getLives(), _game.camera.viewportWidth - 75, 50);

        _game.batch.end();
    }

    @Override public void show() {

    }

    @Override public void hide() {

    }

    @Override public void resume() {
        _paused = false;
    }

    @Override public void pause() {
        _paused = true;
    }

    @Override public void resize(int x, int y) {

    }

    @Override public void dispose() {

    }
}
