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
 * ������������
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
	 * Ŀ����
	 */
	@SuppressWarnings("rawtypes")
	private Class target;	
	/**
	 * ���췽��
	 * @param target
	 */
	@SuppressWarnings("rawtypes")
	public ClassSearchUtil(Class target) {
		this.target = target;
	}
	/**
	 * ָ��Ŀ���࣬��������������ʵ��
	 * @param target
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ClassSearchUtil of(Class target){
		return new ClassSearchUtil(target);
	}
	/**
	 * �������м̳�Ŀ�������
	 * @param <T> ��������Ϊ���ͷ���
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<Class<? extends T>> search(){
		//��ȡ��·����������
		List<String> classFileList = findFiles(classpath, "*.class");
		//��ȡjar����������
		classFileList.addAll(findJarFiles(libDir, includeJars));
		return extraction(target, classFileList);
	}
	/**
	 * ��ȡָ��Ŀ¼�µ��ļ��б�
	 * @param baseDirName  Ŀ��Ŀ¼
	 * @param targetFileName   Ŀ���ļ������ʽ��֧��*��?ͨ�����
	 * @return
	 */
	public static List<String> findFiles(String baseDirName, String targetFileName){
		List<String> classFiles = new ArrayList<String>();
		String tempName = null;
		//�ж�Ŀ¼�Ƿ����
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
	 * ����jar���е�class
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
				// ���Weblogic 11g��ɨ�赽�޹�class�ļ���bug
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
