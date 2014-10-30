//// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.
//
//package appstraction.app.style.css.selector;
//
//import appstraction.app.style.css.CSSellyException;
////import jodd.util.StringPool;
////import jodd.util.StringUtil;
//
///**
// * {@link PseudoFunction Pseudo function expression}, in form: <code>an + b</code>.
// */
//public class PseudoFunctionExpression {
//
//	protected final int a;
//	protected final int b;
//
//	public PseudoFunctionExpression(String expression) {
//		expression = removeChars(expression, "+ \t\n\r\n");
//		if (expression.equals("odd")) {
//			a = 2;
//			b = 1;
//		} else if (expression.equals("even")) {
//			a = 2;
//			b = 0;
//		} else {
//			int nndx = expression.indexOf('n');
//			if (nndx != -1) {
//				String aVal = expression.substring(0, nndx).trim();
//				if (aVal.length() == 0) {
//					a = 1;
//				} else {
//					if (aVal.equals("-")) {
//						a = -1;
//					} else {
//						a = parseInt(aVal);
//					}
//				}
//				String bVal = expression.substring(nndx + 1);
//				if (bVal.length() == 0) {
//					b = 0;
//				} else {
//					b = parseInt(bVal);
//				}
//			} else {
//				a = 0;
//				b = parseInt(expression);
//			}
//		}
//	}
//
//	/**
//	 * Parses int value or throws <code>CSSellyException</code> on failure.
//	 */
//	protected int parseInt(String value) {
//		try {
//			return Integer.parseInt(value);
//		} catch (NumberFormatException nfex) {
//			throw new CSSellyException(nfex);
//		}
//	}
//
//
//	/**
//	 * Returns <b>a</b> value of the function expression.
//	 */
//	public int getValueA() {
//		return a;
//	}
//
//	/**
//	 * Returns <b>b</b> value of the function expression.
//	 */
//	public int getValueB() {
//		return b;
//	}
//
//	/**
//	 * Matches expression with the value.
//	 */
//	public boolean match(int value) {
//		if (a == 0) {
//			return value == b;
//		}
//
//		if (a > 0) {
//			if (value < b) {
//				return false;
//			}
//			return (value - b) % a == 0;
//		}
//
//		if (value > b) {
//			return false;
//		}
//		return (b - value) % (-a) == 0;
//	}
//	
//	public static String removeChars(String src, String chars) {
//		int i = src.length();
//		StringBuilder sb = new StringBuilder(i);
//		for (int j = 0; j < i; j++) {
//			char c = src.charAt(j);
//			if (chars.indexOf(c) == -1) {
//				sb.append(c);
//			}
//		}
//		return sb.toString();
//	}
//
//}