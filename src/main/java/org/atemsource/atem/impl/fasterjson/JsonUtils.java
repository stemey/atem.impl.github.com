package org.atemsource.atem.impl.fasterjson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;


public class JsonUtils
{
	public static Object convertToJava(JsonNode value)
	{
		if (value == null)
		{
			return null;
		}
		else if (value instanceof ObjectNode)
		{
			return value;
		}
		else if (value instanceof ArrayNode)
		{
			return value;
		}
		else if (value instanceof BooleanNode)
		{
			return ((BooleanNode) value).asBoolean();
		}
		else if (value instanceof IntNode)
		{
			return ((IntNode) value).intValue();
		}
		else if (value instanceof LongNode)
		{
			return ((LongNode) value).longValue();
		}
		else if (value instanceof TextNode)
		{
			return ((TextNode) value).asText();
		}
		else if (value instanceof NullNode)
		{
			return null;
		}
		else if (value instanceof DoubleNode)
		{
			return ((DoubleNode) value).doubleValue();
		}
		else
		{
			throw new UnsupportedOperationException("not supported yet " + value);
		}
	}

	public static JsonNode convertToJson(Object value)
	{
		if (value == null)
		{
			return NullNode.getInstance();
		}
		if (value instanceof JsonNode)
		{
			return (JsonNode) value;
		}
		else if (value instanceof Boolean)
		{
			return BooleanNode.valueOf((Boolean) value);
		}
		else if (value instanceof Integer)
		{
			return IntNode.valueOf((Integer) value);
		}
		else if (value instanceof Long)
		{
			return LongNode.valueOf((Long) value);
		}
		else if (value instanceof Double)
		{
			return DoubleNode.valueOf((Double) value);
		}
		else if (value instanceof String)
		{
			return TextNode.valueOf((String) value);
		}
		else
		{
			throw new UnsupportedOperationException("not supported yet " + value);
		}
	}
}
