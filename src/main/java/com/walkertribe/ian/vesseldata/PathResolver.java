package com.walkertribe.ian.vesseldata;

import java.net.URI;
import java.net.URISyntaxException;

public interface PathResolver {
	public URI get(String path) throws URISyntaxException;
}