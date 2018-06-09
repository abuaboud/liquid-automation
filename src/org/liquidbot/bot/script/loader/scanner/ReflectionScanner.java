package org.liquidbot.bot.script.loader.scanner;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/*
 * Created on 8/3/14
 */
public class ReflectionScanner extends CodeScanner {

    @Override
    public boolean scan(ClassNode classNode) {
        for (MethodNode methodNode : (java.util.List<MethodNode>) classNode.methods) {
            if (methodNode.desc.contains("java/lang/reflect")) {
                report("Reflection");
                return true;
            }
            for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
                    if (methodInsnNode.owner.contains("java/lang/reflect")) {
                        report("Reflection");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
