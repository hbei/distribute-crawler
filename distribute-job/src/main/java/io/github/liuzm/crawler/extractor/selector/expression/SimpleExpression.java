package io.github.liuzm.crawler.extractor.selector.expression;

public class SimpleExpression {
	String left;
	String right;
	String op;

	public SimpleExpression(String left, String right, String op) {
		super();
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public boolean test() throws Exception {
		if ("=".equals(op)) {
			return left.equals(right);
		} else if (">".equals(op)) {
			return Integer.parseInt(left) > Integer.parseInt(right);
		} else if ("!=".equals(op)) {
			return !left.equals(right);
		} else if (">=".equals(op)) {
			return Integer.parseInt(left) >= Integer.parseInt(right);
		} else if ("<".equals(op)) {
			return Integer.parseInt(left) < Integer.parseInt(right);
		} else if ("<=".equals(op)) {
			return Integer.parseInt(left) <= Integer.parseInt(right);
		} else {
			throw new Exception("无效的表达式：" + op);
		}
	}
}