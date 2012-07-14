package org.atemsource.atem.impl.json;

import java.lang.reflect.Array;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.LongNode;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;

public class JsonUtils {
	public static JsonNode convertToJson(Object value) {
		  if (value instanceof JsonNode) {
			return (JsonNode) value;
		} else if (value instanceof Boolean) {
			return BooleanNode.valueOf((Boolean) value);
		} else if (value instanceof Integer) {
			return IntNode.valueOf((Integer) value);
		} else if (value instanceof Long) {
			return LongNode.valueOf((Long) value);
		} else if (value instanceof String) {
			return TextNode.valueOf((String) value);
		} else if (value == null) {
			return NullNode.getInstance();
		} else if (value instanceof ArrayNode) {
			return NullNode.getInstance();
		}else {
			throw new UnsupportedOperationException("not supported yet "
					+ value);
		}
	}


	


	public static Object convertToJava(JsonNode value) {
		if (value instanceof ObjectNode) {
			return value;
		} else if (value instanceof ArrayNode) {
			return value;
		} else if (value instanceof BooleanNode) {
			return ((BooleanNode) value).getBooleanValue();
		} else if (value instanceof IntNode) {
			return ((IntNode) value).getIntValue();
		} else if (value instanceof LongNode) {
			return ((LongNode) value).getLongValue();
		} else if (value instanceof TextNode) {
			return ((TextNode) value).getTextValue();
		} else {
			throw new UnsupportedOperationException("not supported yet "
					+ value);
		}
	}
}
