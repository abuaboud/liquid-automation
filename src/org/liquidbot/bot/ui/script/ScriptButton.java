package org.liquidbot.bot.ui.script;

import org.liquidbot.bot.script.loader.ScriptInfo;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kenneth on 8/8/2014.
 */
public class ScriptButton extends JButton {

    public ScriptButton(ScriptInfo info) {
        setMinimumSize(new Dimension(200, 100));
        setText("<html><center>" +info.getName() + " by " + info.getAuthor() + "<br>" + info.getDesc() + "</center></html>");
    }

}
