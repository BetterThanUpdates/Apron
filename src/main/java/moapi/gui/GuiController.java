package moapi.gui;

import java.util.Hashtable;
import java.util.LinkedList;

import moapi.ModBooleanOption;
import moapi.ModKeyOption;
import moapi.ModMappedMultiOption;
import moapi.ModOption;
import moapi.ModTextOption;

/**
 * Gui controller for a single mod and all of it's suboption menus.
 * Moved from GuiController to Controller.
 *
 * @author	Clinton Alexander
 * @version	1.1
 * @since	0.1
 */
public class GuiController {
	/**
	 * List defining which options are to be displayed wide.
	 */
	private LinkedList<String> wide = new LinkedList<String>();
	/**
	 * List defining string manipulation callbacks.
	 */
	private Hashtable<ModOption, LinkedList<DisplayStringFormatter>> formatters;

	/**
	 * Create the controller for a given set of options.
	 *
	 * @param	o	Set of options
	 */
	public GuiController() {
		formatters = new Hashtable<>();
	}

	/**
	 * Sets the named option to show as a full width bar
	 * instead of the default half-width
	 *
	 * @param	name	Name of option to set wide
	 */
	public void setWide(String name) {
		wide.add(name);
	}

	/**
	 * Sets the named option to show as a full width bar instead of the default half-width.
	 *
	 * @param option Option to set wide
	 */
	public void setWide(ModOption<?> option) {
		setWide(option.getID());
	}

	/**
	 * Check if the given option is in a wide bar format
	 *
	 * @param	o	Check if this option is wide
	 */
	public boolean isWide(ModOption<?> o) {
		// Text options are always wide.
		if (o instanceof ModTextOption) {
			return true;
		} else {
			return wide.contains(o.getID());
		}
	}

	/**
	 * Set the text formatting class for a specific option's
	 * output string and removes all other formatters
	 *
	 * @param	option		Option to set a single formatter for
	 * @param	formatter	String formatter
	 */
	public void setFormatter(ModOption option, DisplayStringFormatter formatter) {
		LinkedList<DisplayStringFormatter> newList = new LinkedList<DisplayStringFormatter>();
		newList.add(formatter);
		formatters.put(option, newList);
	}

	/**
	 * Add a new formatter for this specific option's output string
	 *
	 * @since	0.6.1
	 * @param	option		Option to add a formatter to
	 * @param	formatter	Formatter to add to this options format queue
	 */
	public void addFormatter(ModOption option, DisplayStringFormatter formatter) {
		LinkedList<DisplayStringFormatter> list;
		if(formatters.containsKey(option)) {
			list = formatters.get(option);
		} else {
			list = new LinkedList<DisplayStringFormatter>();
			formatters.put(option, list);
		}

		list.add(formatter);
	}

	/**
	 * Get the display string for an option, which will use
	 * a string formatter to decide the output of the text
	 * for a global value
	 *
	 * @param	o			Get display string
	 * @return	display string
	 */
	public String getDisplayString(ModOption o) {
		return getDisplayString(o, false);
	}

	/**
	 * Get the display string for an option, which will use
	 * a string formatter to decide the output of the text
	 * Value is local value if localMode is true, otherwise global
	 *
	 * @param	o			Get display string
	 * @param	localMode	True if use local valu
	 * @return	display string
	 */
	public String getDisplayString(ModOption o, boolean localMode) {
		// String formatter
		String 			value 	= "Problem loading value";

		if((localMode) && (o.useGlobalValue())) {
			value = "GLOBAL";
		} else {
			if(o instanceof ModSliderOption) {
				ModSliderOption o2 = (ModSliderOption) o;
				value = Float.toString(o2.getValue(!localMode));
			} else if(o instanceof ModMappedMultiOption) {
				ModMappedMultiOption o2 = (ModMappedMultiOption) o;
				value = o2.getStringValue(o2.getValue(!localMode));
			} else if(o instanceof ModMultiOption) {
				value = ((ModMultiOption) o).getValue(!localMode);
			} else if(o instanceof ModBooleanOption) {
				ModBooleanOption o2 = (ModBooleanOption) o;
				value = o2.getStringValue(o2.getValue(!localMode));
			} else if(o instanceof ModTextOption) {
				value = ((ModTextOption) o).getValue(!localMode);
			} else if(o instanceof ModKeyOption) {
				value = ModKeyOption.getKeyName(((ModKeyOption) o).getValue(!localMode));
			} else {
				value = o.getValue(!localMode).toString();
			}
		}

		// If it has no formatters or is using a global value then show
		// a label: Value format
		if((!formatters.containsKey(o)) || ((localMode) && (o.useGlobalValue()))) {
			// Use different default for a key option
			if(o instanceof ModKeyOption) {
				DisplayStringFormatter s = StdFormatters.noFormat;

				return s.manipulate(o, value);
			} else {
				// Select a default object
				DisplayStringFormatter s = StdFormatters.defaultFormat;
				return s.manipulate(o, value);
			}
		} else {
			for(DisplayStringFormatter s : formatters.get(o)) {
				value = s.manipulate(o, value);
			}

			return value;
		}
	}
}
