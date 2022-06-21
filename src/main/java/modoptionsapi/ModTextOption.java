package modoptionsapi;

public class ModTextOption extends ModOption<String> {
	private int maxLength;

	public ModTextOption(String name) {
		this(name, 0);
	}

	public ModTextOption(String name, Integer maxLen) {
		this(name, (int) maxLen);
	}

	public ModTextOption(String name, int maxLen) {
		this.maxLength = 0;
		this.name = name;
		this.setGlobalValue("");
		this.setMaxLength(maxLen);
	}

	public void setMaxLength(int maxLength) {
		if (maxLength < 0) {
			maxLength = 0;
		}

		this.maxLength = maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = Math.max(maxLength, 0);
	}

	public int getMaxLength() {
		return this.maxLength;
	}
}
