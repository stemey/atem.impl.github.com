package org.atemsource.atem.impl.json.schema;

import java.util.Map;

public class JsonSchema implements JsonSchemaRef {
	private Map<String, Property> properties;
	public Map<String, Property> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public JsonSchemaUri getSuperType() {
		return superType;
	}
	public void setSuperType(JsonSchemaUri superType) {
		this.superType = superType;
	}
	public SimpleType[] getType() {
		return type;
	}
	public void setType(SimpleType[] type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	private String description;
	private JsonSchemaUri superType;
	private SimpleType[] type;
	private String id;
	private String ref;
}
