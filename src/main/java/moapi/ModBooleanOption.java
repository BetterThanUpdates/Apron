package moapi;

/**
 * Mirrors {@link modoptionsapi.ModBooleanOption}.
 *
 * @version 1.0
 * @since 0.8
 */
public class ModBooleanOption extends modoptionsapi.ModBooleanOption {
	public ModBooleanOption(String name) {
		super(name);
	}

	public ModBooleanOption(String name, String onVal, String offVal) {
		super(name, onVal, offVal);
	}
}
