package com.gtg.framework.utils;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.jfinal.kit.PathKit;

/**
 * 类搜索工具类
 * @author keyliwen
 *
 */
public class ClassSearchUtil {
	
	private static Logger log = Logger.getLogger(ClassSearchUtil.class);
	
	private static boolean includeAllJarsInLib = false;
	
	private String classpath = PathKit.getRootClassPath();
	
	private String libDir = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + "lib";
	
	private List<String> includeJars = new ArrayList<String>();
	
	/**
	 * 目标类
	 */
	@SuppressWarnings("rawtypes")
	private Class target;	
	/**
	 * 构造方法
	 * @param target
	 */
	@SuppressWarnings("rawtypes")
	public ClassSearchUtil(Class target) {
		this.target = target;
	}
	/**
	 * 指定目标类，并返回搜索工具实例
	 * @param target
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ClassSearchUtil of(Class target){
		return new ClassSearchUtil(target);
	}
	/**
	 * 搜索所有继承目标类的类
	 * @param <T> 声明方法为泛型方法
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<Class<? extends T>> search(){
		//获取类路径下所有类
		List<String> classFileList = findFiles(classpath, "*.class");
		//获取jar包中所有类
		classFileList.addAll(findJarFiles(libDir, includeJars));
		return extraction(target, classFileList);
	}
	/**
	 * 获取指定目录下的文件列表
	 * @param baseDirName  目标目录
	 * @param targetFileName   目标文件名表达式（支持*和?通配符）
	 * @return
	 */
	public static List<String> findFiles(String baseDirName, String targetFileName){
		List<String> classFiles = new ArrayList<String>();
		String tempName = null;
		//判断目录是否存在
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			log.error("search error:" + baseDir + "is not a dir!");
		}else {
			String[] files = baseDir.list();
			for (int i = 0; i < files.length; i++) {
				File readFile = new File(baseDirName + File.separator + files[i]);
				if (readFile.isDirectory()) {
					classFiles.addAll(findFiles(baseDirName + File.separator + files[i], targetFileName));
				}else {
					tempName = readFile.getName();
					if (WildCardMatcher.match(targetFileName, tempName)) {
						String tem = readFile.getAbsolutePath().toString().replaceAll("\\\\", "/");
						String className = tem.substring(tem.indexOf("/classes") + "/classes".length() + 1, 
								tem.indexOf(".class"));
						classFiles.add(className.replaceAll("/", "."));
					}
				}
			}
		}
		return classFiles;
	}
	/**
	 * 查找jar包中的class
	 * @param baseDirName
	 * @param includeJars
	 * @return
	 */
	public static List<String> findJarFiles(String baseDirName, final List<String> includeJars) {
		List<String> classFiles = new ArrayList<String>();
		try {
			File baseDir = new File(baseDirName);
			if (!baseDir.exists() || !baseDir.isDirectory()) {
				log.error("search error:" + baseDir + "is not a dir!");
			}else {
				String[] files = baseDir.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return includeAllJarsInLib || includeJars.contains(name);
					}
				});
				for (int i = 0; i < files.length; i++) {
					JarFile localJarFile = new JarFile(new File(baseDirName + File.separator + files[i]));
					Enumeration<JarEntry> entries = localJarFile.entries();
					while (entries.hasMoreElements()) {
						JarEntry jarEntry = (JarEntry) entries.nextElement();
						String entryName = jarEntry.getName();
						if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
							String className = entryName.replaceAll("/", ".").substring(0, entryName.length() - 6);
							classFiles.add(className);
						}
					}
					localJarFile.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(ClassSearchUtil.class.getName(), e);
		}
		return classFiles;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> List<Class<? extends T>> extraction(Class<T> clazz, List<String> classFileList) {
		List<Class<? extends T>> classList = new ArrayList<Class<? extends T>>();
		try {
			for (String classFile : classFileList) {
				// System.out.println("################extraction--"+classFile);
				// 解决Weblogic 11g下扫描到无关class文件的bug
				if (!classFile.startsWith("com.gtg")) {
					continue;
				}
				Class<?> classInFile = Class.forName(classFile);
				if (clazz.isAssignableFrom(classInFile) && clazz != classInFile) {
					classList.add((Class<? extends T>) classInFile);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.error(ClassSearchUtil.class.getName(), e);
		}
		
		return classList;
	}
	
}
