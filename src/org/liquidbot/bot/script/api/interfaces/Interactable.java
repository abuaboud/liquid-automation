package org.liquidbot.bot.script.api.interfaces;

import java.awt.*;

/**
 * Created on 7/30/2014.
 */
public interface Interactable {

    public Point getInteractPoint();

    public boolean interact(final String action, final String option);

    public boolean interact(final String action);

    public boolean click(final boolean left);

    public boolean click();

}
