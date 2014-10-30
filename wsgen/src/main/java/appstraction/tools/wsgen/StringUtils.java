package appstraction.tools.wsgen;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtils {
	public static boolean isEmpty(String s) {
		return !isNotEmpty(s);
	}

	public static boolean isNotEmpty(String s) {
		return (s != null && s.length() != 0 && !s.trim().equals(""));
	}

	public static String ellipsize(String input, int maxCharacters) {
		if (input != null && maxCharacters > 3
				&& maxCharacters < input.length())
			return input.substring(0, maxCharacters - 3) + "...";
		return input;
	}

	public static String ellipsize(String input, int maxCharacters,
			int charactersAfterEllipsis) {
		if (maxCharacters < 3) {
			throw new IllegalArgumentException(
					"maxCharacters must be at least 3 because the ellipsis already take up 3 characters");
		}
		if (maxCharacters - 3 > charactersAfterEllipsis) {
			throw new IllegalArgumentException(
					"charactersAfterEllipsis must be less than maxCharacters");
		}
		if (input == null || input.length() < maxCharacters) {
			return input;
		}
		return input.substring(0, maxCharacters - 3 - charactersAfterEllipsis)
				+ "..."
				+ input.substring(input.length() - charactersAfterEllipsis);
	}

	public static String join(Object... objs) {
		return join(Arrays.asList(objs), ", ", null);
	}

	public static String join(String delim, Object... objs) {
		return join(Arrays.asList(objs), delim, null);
	}

	public static String join(Collection<?> s) {
		return join(s, ", ", null);
	}

	public static String join(String delim, Collection<?> s) {
		return join(s, delim, null);
	}

	public static String join(Collection<?> s, String delimiter,
			TextConverter converter) {
		if (s == null)
			return "";
		StringBuilder builder = new StringBuilder();
		Iterator<?> iter = s.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o == null)
				continue;

			if (converter != null)
				builder.append(converter.toString(o));
			else {
				builder.append(convert(o));
			}
			if (!iter.hasNext()) {
				break;
			}
			builder.append(delimiter);
		}
		return builder.toString();
	}

	public static String spacePascalCase(String input) {
		if (input == null)
			return "";

		String splitString = "";

		for (int idx = 0; idx < input.length(); idx++) {
			char c = input.charAt(idx);

			if (Character.isUpperCase(c)
			// keeps abbreviations together like "Number HEI"
			// instead of making it "Number H E I"
					&& ((idx < input.length() - 1
							&& !Character.isUpperCase(input.charAt(idx + 1)) || (idx != 0 && !Character
							.isUpperCase(input.charAt(idx - 1)))))
					&& splitString.length() > 0) {
				splitString += " ";
			}

			splitString += c;
		}

		return splitString;
	}

	static TextConverter defaultTextConverter = null;

	public static TextConverter getDefaultTextConverter() {
		return defaultTextConverter;
	}

	public static void setDefaultTextConverter(TextConverter textConverter) {
		defaultTextConverter = textConverter;
	}

	public static String convert(Object o) {
		if (defaultTextConverter != null)
			return defaultTextConverter.toString(o);
		return o == null ? "" : o.toString();
	}

	public static String toTitleCase(String input) {
		if (isEmpty(input))
			return input;

		StringBuilder titleCase = new StringBuilder();
		boolean nextTitleCase = true;

		for (char c : input.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				nextTitleCase = true;
			} else if (nextTitleCase) {
				c = Character.toTitleCase(c);
				nextTitleCase = false;
			}

			titleCase.append(c);
		}

		return titleCase.toString();
	}

	public static String value(String... vals) {
		for (int i = 0; i < vals.length; i++) {
			if (isNotEmpty(vals[i]))
				return vals[i];
		}
		return "";
	}

	public static String toLowerCase(String string) {
		return string == null ? "" : string.toLowerCase();
	}

	public static String streamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String applyTemplate(String tpl, Map<String, String> vars) {
		for( Entry<String, String> entry : vars.entrySet()){
			tpl = tpl.replace("{{"+entry.getKey()+"}}", entry.getValue() == null ? "" : entry.getValue());
		}
		return tpl;
	}
}
