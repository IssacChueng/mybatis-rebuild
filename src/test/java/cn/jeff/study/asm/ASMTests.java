package cn.jeff.study.asm;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.reflect.Array;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author swzhang
 * @date 2019/09/29
 */
public class ASMTests {

    @Test
    public void testAsm() throws IOException {
        ClassPrinter classPrinter = new ClassPrinter();
        ClassReader classReader = new ClassReader(Array.class.getName());
        classReader.accept(classPrinter, 0);
    }

    public void classWrite() {
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "c./jeff/study/Comparable", null, "java/lang/Object",
                null);
        classWriter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, new Integer(-1)).visitEnd();
        classWriter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, new Integer(0)).visitEnd();
        classWriter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GRATER", "I", null, new Integer(1)).visitEnd();
        //classWriter.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "L(java/lang/Object;)I", null, null).visitEnd();

        classWriter.visitEnd();
        byte[] classComparable = classWriter.toByteArray();


    }
}
