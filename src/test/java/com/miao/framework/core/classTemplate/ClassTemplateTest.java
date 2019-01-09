package com.miao.framework.core.classTemplate;

import com.miao.framework.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class ClassTemplateTest {

    @Test
    public void getClassListTest() {
        String packageName = "com.miao.framework";
        Enumeration<URL> urls = null;
        try {
            urls = ClassUtil.getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if(url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        System.out.println(packagePath + " " + packageName);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
