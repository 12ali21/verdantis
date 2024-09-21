package io.github.verdantis.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.verdantis.GameState;
import io.github.verdantis.utils.UpdatesWhenPaused;

public class InputSystem extends EntitySystem implements InputProcessor {
    private final Engine engine;
    private final Camera camera;
    private boolean isClicked = false;
    private Vector2 tmp2 = new Vector2();
    private final Vector2 clickedPositionWorld = new Vector2();
    private Vector3 tmp3 = new Vector3();
    private boolean isPaused;

    private InputState currentState = InputState.DEFAULT;
    public InputSystem(Engine engine, GameState gameState, Camera camera) {
        gameState.registerCallback((state) -> {
            if (state == GameState.State.DEFEAT) {
                changePause(true);
            }
        });

        this.engine = engine;
        this.camera = camera;
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            isPaused = !isPaused;
            changePause(isPaused);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isClicked = true;
        tmp2.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isClicked = false;
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


    private void changePause(boolean paused) {
        ImmutableArray<EntitySystem> systems = engine.getSystems();
        for (EntitySystem system : systems) {
            if (system instanceof UpdatesWhenPaused) {
                ((UpdatesWhenPaused) system).onPauseChange(paused);
            } else {
                system.setProcessing(!paused);
            }
        }
    }

    public Vector2 getClickedPositionInWorld() {
        tmp3.set(tmp2.x, tmp2.y, 0);
        tmp3 = camera.unproject(tmp3);
        tmp2.set(tmp3.x, tmp3.y);
        return tmp2;
    }

    public Vector2 getMousePositionInWorld() {
        tmp3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        tmp3 = camera.unproject(tmp3);
        tmp2.set(tmp3.x, tmp3.y);
        return tmp2;
    }

    public boolean isMouseDown() {
        return isClicked;
    }

    public InputState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(InputState currentState) {
        this.currentState = currentState;
    }

    public enum InputState {
        DEFAULT, DRAGGING
    }
}
