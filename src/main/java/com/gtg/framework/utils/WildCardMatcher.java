package com.gtg.framework.utils;

public class WildCardMatcher {
	/**
	 * "*"����ƥ��0���ε������ַ���"?"����ƥ������һ���ַ�
	 * �磺*.class ƥ��test.class��a.class��.class
	 *   abc?ƥ��abcd��abce
	 * @param pattern   ƥ��ģʽ
	 * @param target    ��Ҫƥ���Ŀ���ַ���
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
				//ͨ���*��ʾ����ƥ���������ַ�
				while(targetIndex < targetLength){
					if(match(pattern.substring(patternIndex + 1), target.substring(targetIndex))){
						return true;
					}
					targetIndex ++;
				}
			}else if (ch == '?') {
				//ͨ���?��ʾ����ƥ������һ���ַ�
				targetIndex ++;
				if (targetIndex > targetLength) {
					//��ʾtarget���Ѿ�û���ַ�ƥ��?��
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
