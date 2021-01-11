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
			throw new ClassNotFoundException("读取文件发生I/O异常", e);
		}
	}
	
	/**
	 * 通过反射调用Hello类的hello方法
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
	 * 对字节码进行解密操作
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
	 * 获取到 不含有扩展名的类名
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
