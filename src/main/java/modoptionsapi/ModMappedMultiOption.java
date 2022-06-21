package modoptionsapi;

/**
 * Multiple Selector API with Integer -> String mappings.
 *
 * @author	Clinton Alexander
 * @version	0.7
 * @since	0.1
 * @deprecated	Moved to {@link ModMappedOption}
 */
public class ModMappedMultiOption extends ModMappedOption {
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
