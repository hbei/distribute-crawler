package io.github.liuzm.crawler.vo;

import org.apache.commons.lang3.StringUtils;

public class Action {
	
	String operation;
	String exp;
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	
	public boolean isNull(){
		return !(StringUtils.isNotBlank(operation)&&StringUtils.isNotBlank(exp));
	}
	
	@Override
	public String toString() {
		return "Action [operation=" + operation + ", exp=" + exp + "]";
	}

	
}
