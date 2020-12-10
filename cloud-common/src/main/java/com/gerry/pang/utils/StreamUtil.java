package com.gerry.pang.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * stream集合操作工具类
 */
public class StreamUtil {

    private StreamUtil() {}

    /**
     * 根据过滤规则过滤集合的第一个
     *
     * @param list 数据集合
     * @param mappers 规律规则
     * @param <I>     数据类型
     * @return 过滤后的数据集合
     */
    public static <I> Optional<I> filterUsingPredicateFirst(Collection<I> list, List<Predicate<I>> mappers) {
        return list.stream()
                .filter(ele -> mappers.stream()
                        .reduce(t -> true, Predicate::and)
                        .test(ele)).findFirst();
    }
    /**
     * 根据过滤规则过滤集合
     *
     * @param list 数据集合
     * @param mappers 规律规则
     * @param <I>     数据类型
     * @return 过滤后的数据集合
     */
    public static <I> List<I> filterUsingPredicate(Collection<I> list, List<Predicate<I>> mappers) {
        return list.stream()
                .filter(ele -> mappers.stream()
                        .reduce(t -> true, Predicate::and)
                        .test(ele)).collect(Collectors.toList());
    }

    /**
     * 过滤出数字
     * @param number
     * @return
     */

    public static String filterNumber(String number) {
        number = number.replaceAll("[^([1-9]\\d*\\.?\\d*)]", "");
        return number;
    }

    /**
     *
     * @Title : filterChinese
     * @Type : FilterStr
     * @Description : 过滤出中文
     * @param chin
     * @return
     */
    public static String filterChinese(String chin) {
        chin = chin.replaceAll("[^(\\u4e00-\\u9fa5)]", "");
        return chin;
    }

    /**
     * 替换特殊字符
     * @param str
     * @return
     */
    public static String replaceSpecialChar(String str){
        String newStr = str;
        if(StringUtils.isNotBlank(str)){
            newStr = str.replace("/","//").replace("\\","/\\\\").replace("%","/%").replace("_","/_");
        }
        return newStr;
    }
}
