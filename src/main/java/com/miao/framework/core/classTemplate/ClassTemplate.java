package com.miao.framework.core.classTemplate;

import com.miao.framework.util.ClassUtil;
import com.miao.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 用于获取类的模板类
 */
public abstract class ClassTemplate {

    private static final Logger logger = LoggerFactory.getLogger(ClassTemplate.class);

    protected final String packageName; // protected

    public ClassTemplate(String packageName) {
        this.packageName = packageName;
    }

    public List<Class<?>> getClassList() {
        List<Class<?>> result = new ArrayList<Class<?>>();
        try {
            // 包名获取 URL 资源
            Enumeration<URL> urls = ClassUtil.getClassLoader().getResources(packageName.replace(".", "/"));
            // 循环遍历资源
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if(url != null) {
                    // 获取协议名（file或jar）
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        // 资源在 target/classes 目录下
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(result, packagePath, packageName);
                    }else if (protocol.equals("jar")) {
                        // 资源在 jar 包中，解析jar包
                        JarURLConnection connection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = connection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            // 判断该Entry是否为class
                            if (jarEntryName.endsWith(".class")) {
                                // 获取类名，执行添加操作
                                String className = jarEntryName.substring(0,
                                        jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                doAddClass(result, className);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("获取类出错", e);
        }
        return result;
    }

    private void addClass(List<Class<?>> classList, String packagePath, String packageName) {
        try {
            // 获取所有包路径下的文件与目录
            File[] files = new File(packagePath).listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory();
                }
            });
            // 遍历
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile()) {
                    // 是文件，则构造其全限定名
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    if (StringUtil.isNotEmpty(packageName)) {
                        className = packageName + "." + className;
                    }
                    // 反射得到Class对象，加入集合
                    doAddClass(classList, className);
                }else {
                    // 目录，则递归
                    String subPackageName = fileName;
                    if (StringUtil.isNotEmpty(packageName)) {
                        subPackageName = packageName + "." + subPackageName;
                    }

                    String subPackagePath = fileName;
                    if (StringUtil.isNotEmpty(packagePath)) {
                        subPackagePath = packagePath + "/" + subPackagePath;
                    }
                    addClass(classList, subPackagePath, subPackageName);
                }
            }
        } catch (Exception e) {
            logger.error("添加类出错", e);
        }
    }

    private void doAddClass(List<Class<?>> classList, String className) {
        // 反射，加载类
        Class<?> cls = ClassUtil.loadClass(className, false);
        // 判断是否符合要求
        if (checkAddClass(cls)) {
            classList.add(cls);
        }
    }

    /**
     * 验证类是否符合条件
     */
    public abstract boolean checkAddClass(Class<?> cls);
}
