package org.liquidbot.bot.client.injection;

import org.liquidbot.bot.client.injection.callback.ObjectDefinitionCallBack;
import org.liquidbot.bot.client.parser.HookReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

/**
 * Created by Hiasat on 8/24/14.
 */
public class ObjectDefinitionInjector implements Injector {
	@Override
	public boolean canRun(ClassNode classNode) {
		return HookReader.methods.get("Client#getGameObjectComposite()").getClassName().equals(classNode.name);
	}

	@Override
	public void run(ClassNode classNode) {
		ListIterator<MethodNode> mnIt = classNode.methods.listIterator();
		while (mnIt.hasNext()) {
			MethodNode mn = mnIt.next();
			if (mn.desc.equals(HookReader.methods.get("Client#getGameObjectComposite()").getType())) {
				callBack(mn);
			}
		}
	}


	private static void callBack( MethodNode mn) {
		InsnList nl = new InsnList();
		AbstractInsnNode[] mnNodes = mn.instructions.toArray();
		for (AbstractInsnNode abstractInsnNode : mnNodes) {
			if (abstractInsnNode.getOpcode() == Opcodes.ARETURN) {
				nl.add(new VarInsnNode(Opcodes.ILOAD, 0));
				nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ObjectDefinitionCallBack.class.getCanonicalName().replace('.','/'), "add", "(" + "Ljava/lang/Object;I" + ")V"));
				nl.add(new VarInsnNode(Opcodes.ALOAD, 2));

			}
			nl.add(abstractInsnNode);
		}
		mn.instructions = nl;
		mn.visitMaxs(0, 0);
		mn.visitEnd();
	}

}
