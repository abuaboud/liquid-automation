package org.liquidbot.bot.script.loader.scanner;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/*
 * Created on 8/3/14
 */
public class AsmScanner extends CodeScanner {

    @Override
    public boolean scan(ClassNode classNode) {
        for (MethodNode methodNode : (java.util.List<MethodNode>) classNode.methods) {
            if (methodNode.desc.contains("org/objectweb/asm")) {
                report("ASM");
                return true;
            }
            for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
                    if (methodInsnNode.owner.contains("org/objectweb/asm")) {
                        report("ASM");
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
