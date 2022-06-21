package moapi;

/**
 * Mirrors {@link modoptionsapi.ModMappedOption}.
 *
 * @since 0.8
 * @version 1.0
 */
public class ModMappedOption extends modoptionsapi.ModMappedOption {
	public ModMappedOption(String name) {
		super(name);
	}

	public ModMappedOption(String name, Integer[] values, String[] labels) {
		super(name, values, labels);
	}

	public ModMappedOption(String name, int[] values, String[] labels) {
		super(name, values, labels);
	}

	public ModMappedOption(String id, String name, Integer[] values, String[] labels) {
		super(id, name, values, labels);
	}

	public ModMappedOption(String id, String name, int[] values, String[] labels) {
		super(id, name, values, labels);
	}

	public ModMappedOption(String id, String name) {
		super(id, name);
	}
}
