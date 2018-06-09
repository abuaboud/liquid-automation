package org.liquidbot.bot.client.injection;

import org.liquidbot.bot.client.injection.Injector;
import org.liquidbot.bot.client.injection.callback.ModelCallBack;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.script.api.wrappers.Model;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

/**
 * Created on 8/22/14.
 */
public class ModelInjector implements Injector {

	@Override
	public boolean canRun(ClassNode classNode) {
		return  HookReader.fields.size() > 0 && HookReader.fields.get("Renderable#getModelHeight()").getClassName().equals(classNode.name);
	}

	@Override
	public void run(ClassNode classNode) {

		for (MethodNode methodNode : (List<MethodNode>) classNode.methods)
			for (AbstractInsnNode abstractInsn : methodNode.instructions.toArray()) {
				if (abstractInsn instanceof MethodInsnNode) {
					if (((MethodInsnNode) abstractInsn).desc.contains(HookReader.fields.get("Model#getVerticesX()").getClassName())) {
						callBack(methodNode);
						break;
					}
				}
			}
	}

	private static void callBack(MethodNode mn) {
		InsnList nl = new InsnList();
		boolean b = false;
		for (int i = 0; i < mn.instructions.size(); i++) {
			AbstractInsnNode abs = mn.instructions.get(i);
			if (abs.getOpcode() == Opcodes.ASTORE && !b) {
				VarInsnNode varInsnNode = (VarInsnNode) abs;
				nl.add(abs);
				if (varInsnNode.var == 10) {
					b = true;
					nl.add(new VarInsnNode(Opcodes.ALOAD, 0));
					nl.add(new TypeInsnNode(Opcodes.NEW, Model.class.getCanonicalName().replace('.', '/')));
					nl.add(new InsnNode(Opcodes.DUP));
					nl.add(new VarInsnNode(Opcodes.ALOAD, 10));
					nl.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Model.class.getCanonicalName().replace('.', '/'), "<init>", "(Ljava/lang/Object;)V"));
					nl.add(new VarInsnNode(Opcodes.ALOAD, 0));
					nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, (ModelCallBack.class.getCanonicalName().replace('.', '/')), "add", "(L" + Model.class.getCanonicalName().replace('.', '/') + ";Ljava/lang/Object;)V"));
				}
			} else {
				nl.add(abs);
			}
		}
		mn.instructions = nl;
		mn.visitMaxs(0, 0);
		mn.visitEnd();
	}

}
