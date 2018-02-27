package com.walkertribe.ian.vesseldata;

/**
 * Gives the locations for art assets for a model. Corresponds to the
 * &lt;art&gt; element in vesselData.xml.
 * @author rjwut
 */
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

	/**
	 * The .dxs file containing the Vessel's 3D model.
	 */
	public String getMeshFile() {
		return meshFile;
	}

	/**
	 * The .png file of the model's basic texture.   
	 */
	public String getDiffuseFile() {
		return diffuseFile;
	}

	/**
	 * The .png file for the model's glow map. 
	 */
	public String getGlowFile() {
		return glowFile;
	}

	/**
	 * The .png file for the model's specular map. 
	 */
	public String getSpecularFile() {
		return specularFile;
	}
}