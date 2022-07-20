package com.twg.common.utils;

import com.twg.common.constant.Constants;
import com.twg.common.core.text.StrFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static final String RIVEN_TREND_1 = "●○○○○";
    public static final String RIVEN_TREND_2 = "●●○○○";
    public static final String RIVEN_TREND_3 = "●●●○○";
    public static final String RIVEN_TREND_4 = "●●●●○";
    public static final String RIVEN_TREND_5 = "●●●●●";
    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);
    /**
     * 空字符串
     */
    private static final String NULLSTR = "";
    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     *
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULLSTR;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return NULLSTR;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean ishttp(String link) {
        return StringUtils.startsWithAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     * 字符串转set
     *
     * @param str 字符串
     * @param sep 分隔符
     * @return set集合
     */
    public static Set<String> str2Set(String str, String sep) {
        return new HashSet<>(str2List(str, sep, true, false));
    }

    /**
     * 字符串转list
     *
     * @param str         字符串
     * @param sep         分隔符
     * @param filterBlank 过滤纯空白
     * @param trim        去掉首尾空白
     * @return list集合
     */
    public static List<String> str2List(String str, String sep, boolean filterBlank, boolean trim) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(str)) {
            return list;
        }

        // 过滤空白字符串
        if (filterBlank && StringUtils.isBlank(str)) {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split) {
            if (filterBlank && StringUtils.isBlank(string)) {
                continue;
            }
            if (trim) {
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串同时串忽略大小写
     *
     * @param cs                  指定字符串
     * @param searchCharSequences 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences) {
        if (isEmpty(cs) || isEmpty(searchCharSequences)) {
            return false;
        }
        for (CharSequence testStr : searchCharSequences) {
            if (containsIgnoreCase(cs, testStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase;
        // 当前字符是否大写
        boolean curreCharIsUpperCase;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCaseK(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase()).append(" ");
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs) {
        if (isEmpty(str) || isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * 获取（）中的字符串
     */
    public static String quStr(String str) {
        return getSubString(str, "(", ")");
    }

    public static String insteadOfrnString(String note, int maxLen) {
        StringBuilder retStr = new StringBuilder();
        String str1;
        String str2 = note;
        String oldStr = "/r/n";
        String newStr = "\n";
        while (str2.contains(oldStr)) {
            str1 = str2.substring(0, str2.indexOf(oldStr));
            if (str1.length() > maxLen) {
                str1 = str2.substring(0, maxLen);
                str2 = str2.substring(maxLen);
            } else {
                str2 = str2.substring(str2.indexOf(oldStr) + oldStr.length());
            }
            retStr.append(str1).append(newStr);
        }
        if (str2.length() > 0 && str2.length() <= maxLen) {
            retStr.append(str2);
        } else if (str2.length() > maxLen) {
            StringBuilder result = new StringBuilder();
            while (maxLen < str2.length()) {
                result.append(str2, 0, maxLen);
                result.append(newStr);
                str2 = str2.substring(maxLen);
            }
            if (str2.length() > 0) {
                result.append(str2);
            }
            retStr.append(new String(result));
        }
        return retStr.toString();
    }

    /**
     * 分割字符串到List
     *
     * @param note   字符串
     * @param maxLen 要分割的长度
     */
    public static List<String> insteadOfrnStrings(String note, int maxLen) {
        List<String> strs = new ArrayList<>();
        int len = note.length() / maxLen;
        int x = 0;
        String str;
        for (int i = 0; i <= len; i++) {
            str = substring(note, x, maxLen);
            if (!str.equals("") && str != null) {
                strs.add(str);
            }
            x = maxLen;
            maxLen += maxLen;
        }
        return strs;
    }

    /**
     * 删除某个字符
     *
     * @param sourceString 原字符串
     * @param chElemData   要删除的字符
     */
    public static String deleteCharString(String sourceString, char chElemData) {
        String tmpString = "";
        tmpString += chElemData;
        StringBuilder stringBuffer = new StringBuilder(sourceString);
        int iFlag = -1;
        do {
            iFlag = stringBuffer.indexOf(tmpString);
            if (iFlag != -1) {
                stringBuffer.deleteCharAt(iFlag);
            }
        } while (iFlag != -1);
        return stringBuffer.toString();
    }

    /**
     * 取两个文本之间的文本值
     *
     * @param text  源文本 比如：源文本 = 12345
     * @param left  文本前面 如:2
     * @param right 后面文本 如:4
     * @return 返回 String 不包括前后文本 返回的文本:3
     */
    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    /**
     * 取两个文本之间的文本值
     *
     * @param text  源文本 比如：源文本 = 12345
     * @param left  文本前面 如:2
     * @param right 后面文本 如:4
     * @return 返回 String 包括前后文本  返回的文本:234
     */
    public static String getSubStringEx(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return left + result + right;
    }

    /**
     * 根据某个字符分割字符串 到List数组
     *
     * @param str 要被分割的文本
     * @param sep 根据这个字符 分割
     */
    public static List<String> splitToList(String str, String sep) {
        List<String> stars = new ArrayList<>();
        String[] t = str.split(sep);
        for (String s : t) {
            stars.add(s.trim());
        }
        return stars;
    }

    /**
     * 判断字符串是否为URL
     *
     * @param urls 需要判断的String类型url
     * @return true:是URL；false:不是URL
     */
    public static boolean isHttpUrl(String urls) {
        boolean distro = false;
        String regex = "((https|http)?://)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        while (mat.find()) {
            distro = true;
        }
        return distro;
    }

    /**
     * 判断是否是数字
     *
     * @param str 字符串
     * @return true 是数字 反之
     */
    public static boolean isNumberAndDouble(String str) {
        //是否整数
        Pattern num = Pattern.compile("^\\d+$|-\\d+$");
        //小数
        Pattern dou = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");

        return num.matcher(str).matches()
                || dou.matcher(str).matches();
    }

    /**
     * 判断是否是整数
     *
     * @param str 字符串
     * @return true 整数 反之
     */
    public static boolean isNumber(String str) {
        //是否整数
        Pattern num = Pattern.compile("^\\d+$|-\\d+$");
        return num.matcher(str).matches();
    }

    /**
     * 判断是否是字母
     *
     * @param str 字符串
     * @return true 是字母 反之
     */
    public static boolean isAlpha(String str) {
        return str != null && str.matches("[a-zA-z]+");
    }

    public static String int2chineseNum(int src) {
        final String[] num = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        StringBuilder dst = new StringBuilder();
        int count = 0;
        while (src > 0) {
            dst.insert(0, (num[src % 10] + unit[count]));
            src = src / 10;
            count++;
        }
        return dst.toString().replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }

    /**
     * 获取紫卡倾向点数
     *
     * @param dot
     * @return
     */
    public static String getRivenTrendDot(double dot) {
        if (dot < 0.7) {
            return RIVEN_TREND_1;
        } else if (dot >= 0.7 && dot < 0.9) {
            return RIVEN_TREND_2;
        } else if (dot >= 0.9 && dot < 1.15) {
            return RIVEN_TREND_3;
        } else if (dot >= 1.15 && dot < 1.3) {
            return RIVEN_TREND_4;
        } else if (dot >= 1.3) {
            return RIVEN_TREND_5;
        }
        return null;
    }

   /* public static String toJsonString(String json){
        json = json.replaceFirst("\"","");
        json = replaceLast(json,"\"","");
        return json.replace("\\","").trim();
    }


    public static String replaceLast(String text, String strToReplace, String replaceWithThis) {
        return text.replaceFirst("(?s)" + strToReplace + "(?!.*?" + strToReplace + ")", replaceWithThis);
    }*/

}

