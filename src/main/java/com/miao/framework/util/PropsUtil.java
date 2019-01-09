package com.miao.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性操作工具类
 */
public class PropsUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String propsName) {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            if (StringUtil.isEmpty(propsName)) {
                throw new IllegalArgumentException("属性文件名不能为空");
            }
            String suffix = ".properties";
            if (propsName.lastIndexOf(suffix) == -1) {
                propsName += suffix;
            }
            is = ClassUtil.getClassLoader().getResourceAsStream(propsName);
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException e) {
            logger.error("加载" + propsName + "文件出错", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("关闭输出流出错", e);
            }
        }
        return properties;
    }

    /**
     * 获取属性值
     */
    public static String getString(Properties props, String key) {
        String value = "";
        if (props.containsKey(key)) {
            value = props.getProperty(key);
        }
        return value;
    }
}
