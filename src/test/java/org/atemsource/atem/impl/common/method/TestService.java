package org.atemsource.atem.impl.common.method;

import javax.validation.constraints.Min;

public class TestService {
	public Example execute(@Min(10) int count, Example param2) {
		Example example = new Example();
		example.setCount(count);
		return example;
	}
}
