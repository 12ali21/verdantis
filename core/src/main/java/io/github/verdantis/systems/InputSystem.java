package io.github.verdantis.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputSystem extends EntitySystem implements InputProcessor {
    private final Camera camera;
    private boolean isClicked = false;
    private Vector2 clickedPosition = new Vector2();
    private final Vector2 clickedPositionWorld = new Vector2();
    private Vector3 tmp = new Vector3();

    public InputSystem(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
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
        clickedPosition.set(screenX, screenY);
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


    public Vector2 getClickedPositionInWorld() {
        tmp.set(clickedPosition.x, clickedPosition.y, 0);
        tmp = camera.unproject(tmp);
        clickedPositionWorld.set(tmp.x, tmp.y);
        return clickedPositionWorld;
    }

    public boolean isMouseDown() {
        return isClicked;
    }
}
