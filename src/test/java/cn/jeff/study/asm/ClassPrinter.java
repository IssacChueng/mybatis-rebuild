package cn.jeff.study.asm;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM7;

/**
 * @author swzhang
 * @date 2019/09/29
 */
public class ClassPrinter extends ClassVisitor {

    public ClassPrinter() {
        super(ASM7);
    }

    public ClassPrinter(int api) {
        super(api);
    }

    public ClassPrinter(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println(name + " extends " + superName + "{");
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println(" " + name + descriptor);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println(" " + name + " " + descriptor + " " + signature);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
        System.out.println("attribute type " + attribute.type + " attribute " + attribute.toString());
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        System.out.println("}");
    }
}
