package org.liquidbot.bot.client.input.algorithms;

import java.awt.*;

/*
 * Created by Hiasat on 8/2/14
 */
public interface MouseAlgorithm {

    public Point[] makeMousePath(final int x1, final int y1, final int x2, final int y2);

    public String getName();
}
