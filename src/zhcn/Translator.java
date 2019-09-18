/*
 * Copyright funk 2019-01-11 Email:hao@anbai.com.
 * 不接受任何翻译的反驳
 * ByFunk You
 */
package zhcn;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Translator {

	private static Map<String, Translator> map = new HashMap<String, Translator>();

	String file;

	boolean debug;

	Map<String, String> literal;

	Map<Pattern, String> regexp;

	public static String translate(String langfile, String str) throws Exception {
		Translator translator = map.get(langfile);
		if (translator == null) {
			translator = new Translator(langfile);
			map.put(langfile, translator);
		}
		if ((str == null) || (str.length() == 0)) {
			return str;
		}

		StringBuilder ret = new StringBuilder();
		for (String s : str.split("\n")) {
			if (ret.length() > 0) {
				ret.append("\n");
			}
			ret.append(translator.translate(s));
		}

		return ret.toString();
	}

	Translator(String langfile) throws Exception {
		literal = new HashMap<String, String>();
		regexp = new HashMap<Pattern, String>();
		file = "cn.txt";
		if (langfile != null && langfile.equals("debug")) {
			debug = true;
		}
		if (langfile != null && (new File(langfile)).isFile()) {
			file = langfile;
		}

		// 读取文件
		try {
			BufferedReader reader  = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			System.out.println("检测到根目录下有翻译文件，读取包内文件。");
			Pattern        pattern = Pattern.compile(".*\\$[0-9].*");
			String         line;
			while ((line = reader.readLine()) != null) {
				String[] inputs = line.split("\t", 2);
				literal.put(inputs[0], inputs[1]);
				// 如果您使用正则表达式，请编译
				if (pattern.matcher(inputs[1]).matches()) {
					String re = "(?m)^" + inputs[0] + "$";
					try {
						regexp.put(Pattern.compile(re), inputs[1].replace("\"", "\\\""));
					} catch (PatternSyntaxException ignore) {
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("未检测到根目录下有翻译文件，读取包内文件。");
			InputStream    resource = Translator.class.getResourceAsStream("resources/cn.txt");
			BufferedReader readers  = new BufferedReader(new InputStreamReader(resource));
			Pattern pattern = Pattern.compile(".*\\$[0-9].*");
			String  line;
			while ((line = readers.readLine()) != null) {
				String[] inputs = line.split("\t", 2);
				literal.put(inputs[0], inputs[1]);
				// 如果您使用正则表达式，请编译
				if (pattern.matcher(inputs[1]).matches()) {
					String re = "(?m)^" + inputs[0] + "$";
					try {
						regexp.put(Pattern.compile(re), inputs[1].replace("\"", "\\\""));
					} catch (PatternSyntaxException ignore) {
					}
				}
			}
			readers.close();
		}

	}

	String translate(String src) {
		if ((src == null) || (src.length() == 0)) {
			return src;
		}

		String dst = literal.get(src);
		if (dst == null) {
			dst = src;
			Iterator<Entry<Pattern, String>> iterator = regexp.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Pattern, String> entry   = iterator.next();
				Pattern                pattern = entry.getKey();
				Matcher                matcher = pattern.matcher(dst);
				dst = matcher.replaceAll((String) entry.getValue());
			}
		}
		// 使非翻译句子标准错误。
		if (debug && (src.equals(dst)
				&& dst.length() == dst.getBytes().length // 只有那些没有翻译的
				&& !src.matches("https?://.+")           // URL忽视
				&& !src.matches("\\$?[ 0-9,./:]+")       // 忽略数字
				&& !src.matches("^[-.\\w]+:?$")          // 忽略只有一个单词的情况
				&& !src.matches("burp\\..*")             // 忽略以 burp 开头的内容。（类名?）
				&& !src.matches("lbl.*")                 // 忽略以 lbl 开头的内容（标签 名称?）
				&& src.length() > 1                      // 忽略一个字符
				&& !src.matches("[- A-Z]+s?")            // 仅忽略大写字母
				&& !src.matches("\\s+")                  // 只忽略空格
		)) {
			System.err.println("[" + src + "]");
		}
		return dst;
	}
}
