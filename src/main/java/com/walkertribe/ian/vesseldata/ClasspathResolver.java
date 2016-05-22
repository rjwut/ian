package com.walkertribe.ian.vesseldata;

import java.net.URI;
import java.net.URISyntaxException;

class ClasspathResolver implements PathResolver {
	@Override
	public URI get(String path) throws URISyntaxException {
		return getClass().getResource(path).toURI();
	}
}