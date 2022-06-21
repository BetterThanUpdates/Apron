package modoptionsapi;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Multiple Selector API with Integer -> String mappings.
 *
 * @author	Clinton Alexander
 * @version	1.0
 * @since	0.7
 */
public class ModMappedOption extends ModOption<Integer> {
	/**
	 * Collection of possible values of this selector.
	 */
	private final LinkedHashMap<Integer, String> values = new LinkedHashMap<>();

	//==============
	// Constructors
	//==============

	/**
	 * Create a multiple selector with no values.
	 *
	 * @param	name	Name of selector
	 */
	public ModMappedOption(String name) {
		this(name, name);
	}

	/**
	 * Create a multiple selector with the given keys and labels.
	 *
	 * @param	name	Name of selector
	 * @param	values	Values for the selector
	 * @param	labels	Labels for values
	 * @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	 */
	public ModMappedOption(String name, Integer[] values, String[] labels) {
		this(name, name, values, labels);
	}

	/**
	 * Create a multiple selector with the given keys and labels.
	 *
	 * @param	name	Name of selector
	 * @param	values	Values for the selector
	 * @param	labels	Labels for values
	 * @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	 */
	public ModMappedOption(String name, int[] values, String[] labels) {
		this(name, name, values, labels);
	}

	/**
	 * Create a multiple selector with the given keys and labels and ID/name.
	 *
	 * @since	0.8
	 * @param	id		ID of selector
	 * @param	name	Name of selector
	 * @param	values	Values for the selector
	 * @param	labels	Labels for values
	 * @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	 */
	public ModMappedOption(String id, String name, Integer[] values, String[] labels) {
		this(id, name);

		if (values.length != labels.length) {
			throw new IndexOutOfBoundsException("Keys and labels must have same # of entries");
		} else {
			for (int x = 0; x < values.length; x++) {
				addValue(values[x], labels[x]);
			}
		}
	}

	/**
	 * Create a multiple selector with the given keys and labels.
	 *
	 * @since	0.8
	 * @param	name	Name of selector
	 * @param	values	Values for the selector
	 * @param	labels	Labels for values
	 * @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	 */
	public ModMappedOption(String id, String name, int[] values, String[] labels) {
		this(id, name);

		if (values.length != labels.length) {
			throw new IndexOutOfBoundsException("Keys and labels must have same # of entries");
		} else {
			for (int x = 0; x < values.length; x++) {
				addValue(new Integer(values[x]), labels[x]);
			}
		}
	}

	/**
	 * Creates a multi selector with the given name/ id and no values.
	 *
	 * @since	0.8
	 * @param	id		ID of this option
	 * @param	name	Name of this option
	 */
	public ModMappedOption(String id, String name) {
		super(id, name);
	}

	//==============
	// Adders
	//==============

	/**
	 * Add a single value to this selector.
	 *
	 * @param	key		Key to add value to
	 * @param	value	Value to add
	 */
	public void addValue(Integer key, String value) {
		if (values.size() == 0) {
			this.value = key;
			localValue = key;
		}

		this.values.put(key, value);
	}

	/**
	 * Add a single value to this selector.
	 *
	 * @param	intKey	Key to add value to
	 * @param	value	Value to add
	 */
	public void addValue(int intKey, String value) {
		addValue(new Integer(intKey), value);
	}

	//==============
	// Getters
	//==============

	/**
	 * Gets the string representation.
	 *
	 * @param	key	Key to get string rep of
	 * @return	String representation of a value
	 */
	public String getStringValue(Integer key) {
		return values.get(key);
	}

	/**
	 * Gets the string representation.
	 *
	 * @param	key	Key to get string rep of
	 * @return	String representation of a value
	 */
	public String getStringValue(int key) {
		return values.get(key);
	}

	/**
	 * Gets the next value in this selector.
	 *
	 * @param	i	Current value
	 * @return	Next value
	 */
	public Integer getNextValue(Integer i) {
		Integer cur = null;
		boolean found = false;
		boolean written = false;

		// We need the first key due to this being circular
		boolean firstFound = false;
		Integer firstKey = null;

		// Find next value
		Set<Map.Entry<Integer, String>> s = values.entrySet();

		for (Map.Entry<Integer, String> entry : s) {
			// Ensure we have the first key in case of loop around
			if (!firstFound) {
				firstKey = entry.getKey();
				firstFound = true;
			}

			if (!written) {
				if (found) {
					cur = entry.getKey();
					written = true;
				}

				if (entry.getKey().equals(i)) {
					found = true;
				}
			}
		}

		// Loop around back to first key
		if (!written) {
			cur = firstKey;
		}

		return cur;
	}

	/**
	 * Gets the next value in this selector.
	 *
	 * @param	i	Current value
	 * @return	Next value
	 */
	public Integer getNextValue(int i) {
		return getNextValue(new Integer(i));
	}
}
