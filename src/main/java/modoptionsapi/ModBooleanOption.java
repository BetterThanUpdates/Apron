package modoptionsapi;

public class ModBooleanOption extends ModOption<Boolean> {
	private String onVal;
	private String offVal;

	public ModBooleanOption(String name) {
		this.onVal = "On";
		this.offVal = "Off";
		this.name = name;
		this.value = true;
		this.localValue = true;
	}

	public ModBooleanOption(String name, String onVal, String offVal) {
		this(name);
		this.onVal = onVal;
		this.offVal = offVal;
	}

	public String getStringValue(boolean value) {
		return value ? this.onVal : this.offVal;
	}
}
