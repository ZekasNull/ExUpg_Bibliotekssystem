package controller;

import state.ApplicationState;

import java.util.Observer;

/**
 * Abstrakt klass för en kontroller.
 * Den implementerar Observer för att uppdatera sin vy om förändringar i state.
 * Den har en referens till ett ApplicationState.
 */
public abstract class Controller implements Observer {
    private ApplicationState state;
    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
    }


}