package com.gtg.framework.utils;

public class WildCardMatcher {
	/**
	 * "*"代表匹配0或多次的任意字符，"?"代表匹配任意一个字符
	 * 如：*.class 匹配test.class、a.class、.class
	 *   abc?匹配abcd或abce
	 * @param pattern   匹配模式
	 * @param target    需要匹配的目标字符串
	 * @return
	 */
	public static boolean match(String pattern, String target) {
		int patternLength = pattern.length();
		int targetLength = target.length();
		int targetIndex = 0;
		char ch;
		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
			ch = pattern.charAt(patternIndex);
			if (ch == '*') {
				//通配符*表示可以匹配任意多个字符
				while(targetIndex < targetLength){
					if(match(pattern.substring(patternIndex + 1), target.substring(targetIndex))){
						return true;
					}
					targetIndex ++;
				}
			}else if (ch == '?') {
				//通配符?表示可以匹配任意一个字符
				targetIndex ++;
				if (targetIndex > targetLength) {
					//表示target中已经没有字符匹配?了
					return false;
				}
			}else {
				if((targetIndex >= targetLength) || (ch != target.charAt(targetIndex))){
					return false;
				}
				targetIndex ++;
			}
		}
		return targetIndex == targetLength;
	}

}
