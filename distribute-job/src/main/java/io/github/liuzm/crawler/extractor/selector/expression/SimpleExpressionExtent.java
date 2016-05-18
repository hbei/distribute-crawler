package io.github.liuzm.crawler.extractor.selector.expression;

public class SimpleExpressionExtent implements GrExpression {

	SimpleExpression leftExpression;
	SimpleExpression rightExpression;
	String logic;

	public SimpleExpressionExtent(SimpleExpression leftExpression, SimpleExpression rightExpression, String logic) {
		super();
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
		this.logic = logic;
	}

	public boolean test() {
		try {
			if ("and".equals(logic.toLowerCase())) {
				return this.leftExpression.test() && this.rightExpression.test();
			} else {
				return this.leftExpression.test() || this.rightExpression.test();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
