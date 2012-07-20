package org.atemsource.atem.impl.json.schema;

import java.util.Map;


public class JsonSchema implements JsonSchemaRef
{
	private String description;

	private String id;

	private JsonSchemaUri mixinTypes;

	private Map<String, Property> properties;

	private String ref;

	private JsonSchemaUri superType;

	private SimpleType[] type;

	public String getDescription()
	{
		return description;
	}

	public String getId()
	{
		return id;
	}

	public Map<String, Property> getProperties()
	{
		return properties;
	}

	public String getRef()
	{
		return ref;
	}

	public JsonSchemaUri getSuperType()
	{
		return superType;
	}

	public SimpleType[] getType()
	{
		return type;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setProperties(Map<String, Property> properties)
	{
		this.properties = properties;
	}

	public void setRef(String ref)
	{
		this.ref = ref;
	}

	public void setSuperType(JsonSchemaUri superType)
	{
		this.superType = superType;
	}

	public void setType(SimpleType[] type)
	{
		this.type = type;
	}
}
