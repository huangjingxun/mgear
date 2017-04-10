package com.hjx.mgear.utils.common;

import java.io.File;

/**
 * @author Jingxun Huang
 * @since 2016年12月19日
 */
public class Utils {
    public static String getAppParentPath() {

        File jarPath = new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String parentPath = jarPath.getParentFile().getAbsolutePath();
        return parentPath;
    }
}
