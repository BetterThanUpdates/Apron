package moapi;

/**
 * Mirrors {@link modoptionsapi.ModMappedMultiOption}.
 *
 * @version 1.0
 * @since 0.8
 * @deprecated Moved to {@link ModMappedOption}
 */
public class ModMappedMultiOption extends modoptionsapi.ModMappedMultiOption {
	public ModMappedMultiOption(String name) {
		super(name);
	}

	public ModMappedMultiOption(String name, Integer[] values, String[] labels) {
		super(name, values, labels);
	}

	public ModMappedMultiOption(String name, int[] values, String[] labels) {
		super(name, values, labels);
	}

	public ModMappedMultiOption(String id, String name, Integer[] values, String[] labels) {
		super(id, name, values, labels);
	}

	public ModMappedMultiOption(String id, String name, int[] values, String[] labels) {
		super(id, name, values, labels);
	}

	public ModMappedMultiOption(String id, String name) {
		super(id, name);
	}
}
