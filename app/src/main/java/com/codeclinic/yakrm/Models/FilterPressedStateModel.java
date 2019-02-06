package com.codeclinic.yakrm.Models;

public class FilterPressedStateModel {
    private boolean isPressed;

    public FilterPressedStateModel(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }
}
