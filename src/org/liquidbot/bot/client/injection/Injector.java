package org.liquidbot.bot.client.injection;

import org.objectweb.asm.tree.ClassNode;

/**
 * Created on 8/22/14.
 */
public interface Injector {

	public abstract boolean canRun(ClassNode classNode);

	public abstract void run(ClassNode classNode);
}
