/*
 * Copyright funk 2019-01-11 Email:hao@anbai.com.
 *
 * ByFunk You
 */
package zhcn;

import java.io.*;

public class BurpJavaaGentMain {

	public static void main(String[] args) throws IOException {
		System.out.println("请使用 javaagent方式运行 \n");
		System.out.println("Linux Mac 下：\n");
		System.out.println("java -javaagent:BurpSuiteCn.jar -Xbootclasspath/p:burp-loader-keygen.jar  -Xmx1024m -jar burpsuite_pro_v1.x.x.jar\n");
		System.out.println("Windwos 下：\n");
		System.out.println("java -Dfile.encoding=utf-8 -javaagent:BurpSuiteCn.jar -Xbootclasspath/p:burp-loader-keygen.jar  -Xmx1024m -jar burpsuite_pro_v1.x.x.jar\n");
		System.out.println("By:LianZhang.org 不接受任何翻译上的反驳！");

	}

}
