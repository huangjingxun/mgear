package com.hjx.mgear;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Jingxun Huang
 * @since 2017年4月11日
 */
public class UtilTest {

    @Test
    public void test() {

        Pattern p = Pattern.compile("(?<=Page=)\\d+");
        Matcher m = p.matcher("?ViewAction=View&ObjectID=37498840&PageSize=30&Page=223");
        System.out.println(m.find());
        System.out.println(m.group());
    }
}
