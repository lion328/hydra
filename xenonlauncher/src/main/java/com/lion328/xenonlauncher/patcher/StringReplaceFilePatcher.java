package com.lion328.xenonlauncher.patcher;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class StringReplaceFilePatcher implements FilePatcher
{

    private final String find;
    private final String replace;

    public StringReplaceFilePatcher(String find, String replace)
    {
        this.find = find;
        this.replace = replace;
    }

    @Override
    public byte[] patchFile(String name, byte[] original)
    {
        if (!name.endsWith(".class"))
        {
            return original;
        }

        ClassReader reader = new ClassReader(original);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        List<FieldNode> fields = node.fields;
        for (FieldNode field : fields)
        {
            if (field.value instanceof String)
            {
                field.value = ((String) field.value).replace(find, replace);
            }
        }

        List<MethodNode> methods = node.methods;
        for (MethodNode method : methods)
        {
            InsnList insns = method.instructions;

            for (int i = 0; i < insns.size(); i++)
            {
                AbstractInsnNode insn = insns.get(i);

                if (!(insn instanceof LdcInsnNode))
                {
                    continue;
                }

                LdcInsnNode ldc = (LdcInsnNode) insn;

                if (!(ldc.cst instanceof String))
                {
                    continue;
                }

                ldc.cst = ((String) ldc.cst).replace(find, replace);
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);

        return writer.toByteArray();
    }
}
