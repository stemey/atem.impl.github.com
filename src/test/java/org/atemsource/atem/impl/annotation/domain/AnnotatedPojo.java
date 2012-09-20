package org.atemsource.atem.impl.annotation.domain;

import javax.validation.constraints.Max;

public class AnnotatedPojo {

	@Max(23)
	private String maxedValue;

	public String getMaxedValue() {
		return maxedValue;
	}

	public void setMaxedValue(String maxedValue) {
		this.maxedValue = maxedValue;
	}
}
