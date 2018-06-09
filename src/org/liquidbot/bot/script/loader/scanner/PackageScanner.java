package org.liquidbot.bot.script.loader.scanner;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/*
 * Created on 8/3/14
 */
public class PackageScanner extends CodeScanner {

	@Override
	public boolean scan(ClassNode classNode) {
		for (MethodNode methodNode : (java.util.List<MethodNode>) classNode.methods) {
			if (methodNode.desc.contains("org/liquidbot/") && !methodNode.desc.contains("org/liquidbot/bot/script/")
					&& !methodNode.desc.contains("org/liquidbot/bot/utils/Logger")
					&& !methodNode.desc.contains("org/liquidbot.bot/script.randevent/RandomEventHandler")) {
				report("Client Access");
				return true;
			}
			for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
				if (abstractInsnNode instanceof MethodInsnNode) {
					MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
					if (methodInsnNode.owner.contains("org/liquidbot/") && !methodInsnNode.owner.contains("org/liquidbot/bot/script/")
							&& !methodInsnNode.owner.contains("org/liquidbot/bot/utils/Logger")
							&& !methodInsnNode.owner.contains("org/liquidbot.bot/script.randevent/RandomEventHandler")) {
						report("Client Access");
						return true;
					}
				}
			}
		}
		return false;
	}
}
