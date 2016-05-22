package com.walkertribe.ian.vesseldata;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

class FilePathResolver implements PathResolver {
	private File artemisInstallPath;

	FilePathResolver(File artemisInstallPath) {
		this.artemisInstallPath = artemisInstallPath;
	}

	@Override
	public URI get(String path) throws URISyntaxException {
		return new File(artemisInstallPath, path).toURI();
	}

}