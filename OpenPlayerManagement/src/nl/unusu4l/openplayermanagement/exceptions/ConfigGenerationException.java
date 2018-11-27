package nl.unusu4l.openplayermanagement.exceptions;

public class ConfigGenerationException extends Exception{
	
	private static final long serialVersionUID = 2856627096668359233L;

	public ConfigGenerationException() {
		super("The config could not be generated");
	}

}
