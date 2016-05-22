package com.walkertribe.ian.vesseldata;

public class Art {
	private String meshFile;
	private String diffuseFile;
	private String glowFile;
	private String specularFile;

	Art(String meshFile, String diffuseFile, String glowFile, String specularFile) {
		this.meshFile = meshFile;
		this.diffuseFile = diffuseFile;
		this.glowFile = glowFile;
		this.specularFile = specularFile;
	}

	public String getMeshFile() {
		return meshFile;
	}

	public String getDiffuseFile() {
		return diffuseFile;
	}

	public String getGlowFile() {
		return glowFile;
	}

	public String getSpecularFile() {
		return specularFile;
	}
}