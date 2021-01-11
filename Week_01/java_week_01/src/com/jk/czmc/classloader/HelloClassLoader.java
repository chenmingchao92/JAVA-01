package com.jk.czmc.classloader;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloClassLoader extends ClassLoader{

	public static void main(String[] args) {
		HelloClassLoader helloClassLoader = new HelloClassLoader();
		try {
			Class helloClass = helloClassLoader.findClass("resource"+File.separatorChar+"Hello.xlass");
			helloClassLoader.executeMethodInvoce(helloClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Path classPath = Paths.get(name);
		String classFileNameNoExtensionName = getFileNameNoExtensionName(classPath);
		try {
			byte[] classFileByteDecodes = analyzeClassFile(classPath);
			return defineClass(classFileNameNoExtensionName, classFileByteDecodes, 0, classFileByteDecodes.length);
		} catch (IOException e) {
			throw new ClassNotFoundException("��ȡ�ļ�����I/O�쳣", e);
		}
	}
	
	/**
	 * ͨ���������Hello���hello����
	 * @param helloClass
	 * @throws Throwable
	 */
	private void executeMethodInvoce(Class helloClass) throws Throwable {
		Lookup helloLoopup = MethodHandles.lookup();
		MethodType helloMethodType = MethodType.methodType(void.class);
		MethodHandle helloMethodHandle = helloLoopup.findVirtual(helloClass, "hello", helloMethodType);
		helloMethodHandle.invoke(helloClass.newInstance());
	}
	
	/**
	 * ���ֽ�����н��ܲ���
	 * @param classPath
	 * @return
	 * @throws IOException
	 */
	private byte[] analyzeClassFile(Path classPath) throws IOException {
		byte[] classFileBytes = Files.readAllBytes(classPath);
		for (int i = 0; i < classFileBytes.length; i++) {
			classFileBytes[i] = (byte) (classFileBytes[i]^255);
		}
		return classFileBytes;
	}
	/**
	 * ��ȡ�� ��������չ��������
	 * @param classPath
	 * @return
	 */
	private String getFileNameNoExtensionName(Path classPath) {
		String classFileName = classPath.getFileName().toString();
		int extensionNameIndex = classFileName.lastIndexOf('.');		
		String classFileNameNoExtensionName = classFileName.substring(0, extensionNameIndex);
		return classFileNameNoExtensionName;
	}
}
