package org.atemsource.atem.impl.json.type;

import java.net.URI;

public class JsonSchemaUri implements JsonSchemaRef{
private URI uri;

public URI getUri() {
	return uri;
}

public void setUri(URI uri) {
	this.uri = uri;
}
}
