package com.gerry.pang.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanCopyUtils {
	
	private BeanCopyUtils() {}
	
	/**
	 * 浅拷贝集合对象<br>
	 * 
	 * 通过JSON转换String方式
	 * @param fromList
	 * @param toCLass
	 * @return
	 */
	public static <F,T> List<T> shallowCopyList(List<F> fromList, Class<T> toCLass) {
		List<T> toList = new ArrayList<>();
	    if (CollectionUtils.isEmpty(fromList)) {
	        return toList;
	    }
	    toList = JSON.parseArray(JSON.toJSONString(fromList), toCLass);
	    return toList;
	}

	/**
	 * 深拷贝集合对象<br>
	 * 
	 * 通过Spring.BeanUtils.copyProperties方法
	 * @param fromList
	 * @param toCLass
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <F,T> List<T> deepCopyList(List<F> fromList, Class<T> toCLass) 
			throws InstantiationException, IllegalAccessException {
		return deepCopyList(fromList, toCLass, null);
	}
	
	/**
	 * 深拷贝集合对象<br>
	 * 
	 * 通过Spring.BeanUtils.copyProperties方法
	 * @param fromList
	 * @param toCLass
	 * @param ignoreProperties
	 * @return
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static <F,T> List<T> deepCopyList(List<F> fromList, Class<T> toCLass, String... ignoreProperties) 
			throws InstantiationException, IllegalAccessException {
		List<T> toList = new ArrayList<>();
		if (CollectionUtils.isEmpty(fromList)) {
			return toList;
		}
		for (F from : fromList) {
			T to = deepCopyOne(from, toCLass);
			toList.add(to);
		}
		return toList;
	}
	
	/**
	 * 深拷贝对象<br>
	 * 
	 * 通过Spring.BeanUtils.copyProperties方法
	 * @param from
	 * @param toCLass
	 * @return
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static<F,T> T deepCopyOne(F from, Class<T> toCLass) 
			throws InstantiationException, IllegalAccessException {
		return deepCopyOne(from, toCLass, null);
	}

	/**
	 * 深拷贝对象<br>
	 * 
	 * 通过Spring.BeanUtils.copyProperties方法
	 * @param from
	 * @param toCLass
	 * @param ignoreProperties
	 * @return
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static<F,T> T deepCopyOne(F from, Class<T> toCLass, String... ignoreProperties) 
			throws InstantiationException, IllegalAccessException {
		if (from == null || toCLass == null) {
			return null;
		}
		Object to = null;
		try {
			to = toCLass.newInstance();
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		BeanUtils.copyProperties(from, to, ignoreProperties);
		return (T) to;
	}
}
