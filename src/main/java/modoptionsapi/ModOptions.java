package modoptionsapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import modoptionsapi.gui.DisplayStringFormatter;
import modoptionsapi.gui.GuiController;

import net.minecraft.client.Minecraft;

/**
 * An abstract representation of a set of options for a single mod.
 *
 * @author	Clinton Alexander
 * @version	1.0
 * @since	0.1
 */
public class ModOptions {
	/**
	 * Map of option selectors.
	 */
	private LinkedHashMap<String, ModOption<?>> options = new LinkedHashMap<>();
	/**
	 * Map of sub option menus for this menu.
	 */
	private LinkedHashMap<String, ModOptions> subOptions = new LinkedHashMap<>();
	/**
	 * Display details.
	 */
	private static final GuiController gui = new GuiController();

	/**
	 * ID of this menu, which is used for the file structure and to avoid coupling
	 * the display name to the underlying values.
	 *
	 * @since 0.8
	 */
	private String id;
	/**
	 * Name of this menu.
	 */
	private String name;
	/**
	 * Parent of this set of options. (null implies it's a mod package)
	 */
	private ModOptions	parent = null;
	/**
	 * Whether this set of options can have server specific options.
	 *
	 * @implNote If false, all children will act as false
	 */
	private boolean multiplayer = false;
	/**
	 * Whether this set of options can have single player world options.
	 *
	 * @implNote If set false, all children will act as false
	 */
	private boolean singleplayer = true;

	/**
	 * The pattern matcher to detect illegal filenames.
	 *
	 * @since	0.8
	 */
	private static final Pattern illegalNamePattern = Pattern.compile("[/?%*:|\"<>]+");

	/**
	 * Create a new set of options with no parent. A mod package.
	 *
	 * @implNote Valid folder names only.
	 *
	 * @param name Name of option list (menu name)
	 * @throws PatternSyntaxException When an invalid folder name is specified
	 */
	public ModOptions(String name) {
		this(name, name);
	}

	/**
	 * Create a sub options menu for a mod package.
	 *
	 * @param name	Name of this option list
	 * @param p Parent of this option list
	 * @throws 	PatternSyntaxException	When an invalid folder name is specified
	 */
	public ModOptions(String name, ModOptions p) {
		this(name, name);
		parent = p;
	}

	/**
	 * Create a sub option menu with no parent.
	 *
	 * @param id ID of this option list
	 * @param name Name of this option set
	 * @throws PatternSyntaxException When an invalid folder name is specified
	 * @since 0.8
	 */
	public ModOptions(String id, String name) {
		setID(id);
		this.name = name;
	}

	//=========================
	// Adding Options Methods
	//=========================

	/**
	 * Add a manually created option to this menu.
	 *
	 * @param option Option selector to add
	 * @return Returns the option just added for further operations
	 */
	public <T> ModOption<T> addOption(ModOption<T> option) {
		options.put(option.getID(), option);
		return option;
	}

	/**
	 * Add a text option with infinite max length.
	 *
	 * @param name	Name of text input
	 * @return Returns the option just added for further operations
	 * @since	0.7
	 */
	public ModOption<String> addTextOption(String name) {
		ModTextOption option = new ModTextOption(name);
		return addOption(option);
	}

	/**
	 * Add a text option with infinite max length with a default value.
	 *
	 * @param name	Name of text input
	 * @param value	Default value of input
	 * @return Returns the option just added for further operations
	 * @since	0.7
	 */
	public ModOption<String> addTextOption(String name, String value) {
		ModTextOption option = new ModTextOption(name);
		option.setGlobalValue(value);
		return addOption(option);
	}

	/**
	 * Add a text option with infinite max length.
	 *
	 * @param name	Name of text input
	 * @param maxlen	Maximum length the user can input. 0 or less is infinite
	 * @return Returns the option just added for further operations
	 * @since	0.7
	 */
	public ModOption<String> addTextOption(String name, int maxlen) {
		ModTextOption option = new ModTextOption(name, maxlen);
		return addOption(option);
	}

	/**
	 * Add a text option with infinite max length with a default.
	 * value
	 *
	 * @param name	Name of text input
	 * @param value	Default value for input
	 * @param maxlen	Maximum length the user can input. 0 or less is infinite
	 * @return Returns the option just added for further operations
	 * @since	0.7
	 */
	public ModOption<String> addTextOption(String name, String value, int maxlen) {
		ModTextOption option = new ModTextOption(name, maxlen);
		option.setGlobalValue(value);
		return addOption(option);
	}

	/**
	 * Add a text option with infinite max length with a default.
	 * value
	 *
	 * @param name	Name of text input
	 * @param value	Default value for input
	 * @param maxlen	Maximum length the user can input. 0 or less is infinite
	 * @return Returns the option just added for further operations
	 * @since	0.7
	 */
	public ModOption<String> addTextOption(String name, String value, Integer maxlen) {
		return addTextOption(name, value, (int) maxlen);
	}

	/**
	 * Adds a character binding which is unique and will not class with any
	 * other character binding. This default will not set a binding. If
	 * a clash occurs during loading of your player's saved preferences
	 * then the latter of the two mods will have their bindings undone.
	 * Please use this method and avoid setting a default value.
	 *
	 * @param name	Name of character binding
	 * @return Returns the option just added for further operations
	 * @since	0.7
	 */
	public ModOption<Integer> addKeyBinding(String name) {
		ModKeyOption option = new ModKeyOption(name);
		return addOption(option);
	}

	/**
	 * Adds a character binding which is unique and will not class with any
	 * other character binding. This default will not set a binding. If
	 * a clash occurs during loading of your player's saved preferences
	 * then the latter of the two mods will have their bindings undone.
	 * Please use this method and avoid setting a default value.
	 *
	 * @param name	Name of character binding
	 * @return Returns the option just added for further operations
	 * @since	0.7
	 */
	public ModOption<Integer> addKeyOption(String name) {
		ModKeyOption option = new ModKeyOption(name);
		return addOption(option);
	}

	/**
	 * Add a multiple selector.
	 *
	 * @param name	Name of selector
	 * @param values	Set of values to display
	 * @return Returns the option just added for further operations
	 */
	public ModOption<String> addMultiOption(String name, String[] values) {
		ModMultiOption option = new ModMultiOption(name, values);
		return addOption(option);
	}

	/**
	 * Adds a mapped multi option.
	 *
	 * @param name	Name of selector
	 * @param keys	Keys for selector
	 * @param values	Values for selector
	 * @return Returns the option just added for further operations
	 * @throws 	IndexOutOfBoundsException if supplied arrays are not the same length
	 */
	public ModOption<Integer> addMappedMultiOption(String name, Integer[] keys, String[] values)
			throws IndexOutOfBoundsException {
		if (keys.length != values.length) {
			throw new IndexOutOfBoundsException("Arrays are not same length");
		} else {
			ModMappedOption option = new ModMappedOption(name);

			for (int x = 0; x < keys.length; x++) {
				option.addValue(keys[x], values[x]);
			}

			return addOption(option);
		}
	}

	/**
	 * Adds a mapped multi option.
	 *
	 * @param name Name of selector
	 * @param keys Keys for selector
	 * @param values Values for selector
	 * @return Returns the option just added for further operations
	 * @throws IndexOutOfBoundsException if supplied arrays are not the same length
	 */
	public ModOption addMappedMultiOption(String name, int[] keys, String[] values) throws IndexOutOfBoundsException {
		if (keys.length != values.length) {
			throw new IndexOutOfBoundsException("Arrays are not same length");
		} else {
			ModMappedOption option = new ModMappedOption(name);

			for (int x = 0; x < keys.length; x++) {
				option.addValue(new Integer(keys[x]), values[x]);
			}

			return addOption(option);
		}
	}

	/**
	 * Add a toggle/boolean selector.
	 *
	 * @param name Name of boolean selector
	 * @return Returns the option just added for further operations
	 */
	public ModOption addToggle(String name) {
		ModBooleanOption option = new ModBooleanOption(name);
		return addOption(option);
	}

	/**
	 * Add a numeric slider ranging from 0 to 100.
	 *
	 * @param name Name of slider
	 * @return The option just added for further operations
	 */
	public ModOption addSlider(String name) {
		ModSliderOption option = new ModSliderOption(name);
		return addOption(option);
	}

	/**
	 * Add a numeric slider with a range.
	 *
	 * @param name	Name of slider
	 * @param low Lowest value of slider
	 * @param high	Highest value of slider
	 * @return Returns the option just added for further operations
	 */
	public ModOption addSlider(String name, int low, int high) {
		ModSliderOption option = new ModSliderOption(name, low, high);
		return addOption(option);
	}

	//=========================
	// SubOptions Methods
	//=========================

	/**
	 * Add a sub menu of options.
	 *
	 * @param m Set of sub-options
	 * @return The current object, for building
	 */
	public ModOptions addSubOptions(ModOptions m) {
		m.setParent(this);
		subOptions.put(m.getID(), m);

		return this;
	}

	/**
	 * Check if this mod options has a given sub options menu.
	 *
	 * @param id of sub options
	 * @return true if sub options exist in this menu
	 */
	public boolean containsSubOptions(String id) {
		return subOptions.containsKey(id);
	}

	/**
	 * Get all sets of sub-options for this set.
	 *
	 * @return Array of all sub options for this set
	 */
	public ModOptions[] getSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = subOptions.entrySet();
		ModOptions[] m = new ModOptions[s.size()];
		int i = 0;

		for (Map.Entry<String, ModOptions> e : s) {
			m[i] = e.getValue();
			i++;
		}

		return m;
	}

	/**
	 * Get all sets of sub-options for this set for multiplayer.
	 *
	 * @return Array of all sub options for this set
	 */
	public ModOptions[] getMultiplayerSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = subOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<ModOptions>();

		for (Map.Entry<String, ModOptions> e : s) {
			if (e.getValue().isMultiplayerMod()) {
				m.add(e.getValue());
			}
		}

		return m.toArray(new ModOptions[0]);
	}

	/**
	 * Get all sets of sub-options for this set for singleplayer.
	 *
	 * @return Array of all sub options for this set
	 */
	public ModOptions[] getSingleplayerSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = subOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<ModOptions>();

		for (Map.Entry<String, ModOptions> e : s) {
			if (e.getValue().isSingleplayerMod()) {
				m.add(e.getValue());
			}
		}

		return m.toArray(new ModOptions[0]);
	}

	/**
	 * Get a named set of sub-options.
	 *
	 * @param id ID of sub-options
	 * @return A sub-set of options
	 */
	public ModOptions getSubOption(String id) {
		return subOptions.get(id);
	}

	//=========================
	// Mass Option Methods
	//=========================

	/**
	 * Sets all values global values to the parameter.
	 *
	 * @param global	New value
	 */
	public void globalReset(boolean global) {
		if (((ModOptionsAPI.isMultiplayerWorld()) && (multiplayer))
			|| ((!ModOptionsAPI.isMultiplayerWorld()) && (singleplayer))) {
			// Set all local options
			ModOption[] options = getOptions();

			for (ModOption option : options) {
				option.setGlobal(global);
			}

			// If we're in single player, reset all single player ones
			ModOptions[] subMenus = new ModOptions[0];

			if ((!ModOptionsAPI.isMultiplayerWorld()) && (singleplayer)) {
				subMenus = getSingleplayerSubOptions();

				for (ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}

			// If we're in multiplayer, reset all multiplayer ones
			if ((ModOptionsAPI.isMultiplayerWorld()) && (multiplayer)) {
				subMenus = getMultiplayerSubOptions();

				for (ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}
		}
	}

	//=========================
	// Direct Option Methods
	//=========================

	/**
	 * Return a single named option.
	 *
	 * @param id ID of option selector
	 * @return Option selector
	 */
	public ModOption getOption(String id) {
		return options.get(id);
	}

	/**
	 * Returns a single named option's internal value.
	 *
	 * @return Value of option, as a string
	 */
	public String getOptionValue(String id) {
		ModOption option = options.get(id);

		if (option instanceof ModSliderOption) {
			return Integer.toString(((ModSliderOption) option).getValue().intValue());
		} else {
			return option.getValue().toString();
		}
	}

	/**
	 * Get the value of a text input field.
	 *
	 * @param id ID of option
	 * @return Value of a toggle option
	 * @throws NoSuchOptionException	When no option is present
	 * @throws IncompatibleOptionTypeException
	 * @since 0.7
	 */
	public String getTextValue(String id) throws NoSuchOptionException {
		ModOption option = options.get(id);

		if (option == null) {
			throw new NoSuchOptionException("No option identified by " + id);
		} else if (!(option instanceof ModTextOption)) {
			throw new IncompatibleOptionTypeException("Option " + id + " is not a text option");
		} else {
			return ((ModTextOption) option).getValue();
		}
	}

	/**
	 * Returns a single named toggle value.
	 *
	 * @param id ID of the option
	 * @return Value of a toggle option
	 * @throws NoSuchOptionException When no option is present
	 * @throws IncompatibleOptionTypeException
	 * @since 0.6.1
	 */
	public boolean getToggleValue(String id) throws NoSuchOptionException {
		ModOption option = options.get(id);

		if (option == null) {
			throw new NoSuchOptionException("No option identified by " + id);
		} else if (!(option instanceof ModBooleanOption)) {
			throw new IncompatibleOptionTypeException("Option " + id + " is not a toggle option");
		} else {
			return ((ModBooleanOption) option).getValue();
		}
	}

	/**
	 * Returns a single named slider option's value.
	 *
	 * @param id ID of the option
	 * @return Value of a slider option
	 * @throws NoSuchOptionException When no option is present
	 * @throws IncompatibleOptionTypeException
	 * @since 0.6.1
	 */
	public float getSliderValue(String id) throws NoSuchOptionException {
		ModOption option = options.get(id);

		if (option == null) {
			throw new NoSuchOptionException("No option identified by " + id);
		} else if (!(option instanceof ModSliderOption)) {
			throw new IncompatibleOptionTypeException("Option " + id + " is not a slider option");
		} else {
			return ((ModSliderOption) option).getValue().floatValue();
		}
	}

	/**
	 * Returns a single named mapped multi option's value.
	 *
	 * @param id ID of the option
	 * @return Value of a mapped multi option
	 * @throws NoSuchOptionException	When no option is present
	 * @throws IncompatibleOptionTypeException
	 * @since	0.6.1
	 */
	public int getMappedValue(String id) throws NoSuchOptionException {
		ModOption option = options.get(id);

		if (option == null) {
			throw new NoSuchOptionException("No option identified by " + id);
		} else if (!(option instanceof ModMappedOption)) {
			throw new IncompatibleOptionTypeException("Option " + id + " is not a mapped (multi) option");
		} else {
			return ((ModMappedOption) option).getValue();
		}
	}

	/**
	 * Set a single named boolean options global value.
	 *
	 * @param id ID of boolean toggle to change
	 * @param value New value of toggle
	 * @return This object for building
	 * @throws IncompatibleOptionTypeException
	 * @throws NoSuchOptionException When the option doesn't already exist
	 */
	public ModOptions setOptionValue(String id, boolean value) {
		ModOption m = this.getOption(id);

		if (m == null) {
			throw new NoSuchOptionException();
		} else if (m instanceof ModBooleanOption) {
			ModBooleanOption bo = (ModBooleanOption) m;
			bo.setGlobalValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}

		return this;
	}

	/**
	 * Set a single slider or mapped multi option's global value.
	 *
	 * @param id ID of slider option to change
	 * @param value New value of toggle
	 * @return This object for building
	 * @throws IncompatibleOptionTypeException
	 * @throws NoSuchOptionException When the option doesn't already exist
	 * @since 0.7
	 */
	public ModOptions setOptionValue(String id, Integer value) {
		ModOption m = this.getOption(id);

		if (m == null) {
			throw new NoSuchOptionException();
		} else if (m instanceof ModSliderOption) {
			((ModSliderOption) m).setGlobalValue(value);
		} else if (m instanceof ModMappedMultiOption) {
			m.setGlobalValue(value);
		} else if (m instanceof ModKeyOption) {
			m.setGlobalValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}

		return this;
	}

	/**
	 * Set a single slider or mapped multi option's global value.
	 *
	 * @param id ID of slider option to change
	 * @param value New value of toggle
	 * @return This object for building
	 * @throws IncompatibleOptionTypeException
	 * @throws NoSuchOptionException When the option doesn't already exist
	 */
	public ModOptions setOptionValue(String id, int value) {
		return setOptionValue(id, new Integer(value));
	}

	/**
	 * Set a single multi option's global value.
	 *
	 * @param id ID of multi toggle to change
	 * @param value New value of toggle
	 * @return This object for building
	 * @throws IncompatibleOptionTypeException
	 * @throws NoSuchOptionException When the option doesn't already exist
	 */
	public ModOptions setOptionValue(String id, String value) {
		ModOption m = this.getOption(id);

		if (m == null) {
			throw new NoSuchOptionException();
		} else if (m instanceof ModMultiOption) {
			ModMultiOption mo = (ModMultiOption) m;
			mo.setGlobalValue(value);
		} else if (m instanceof ModTextOption) {
			((ModTextOption) m).setGlobalValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}

		return this;
	}

	/**
	 * Return all option selectors for this menu.
	 *
	 * @return Array of all option selectors for this menu
	 */
	public ModOption[] getOptions() {
		Set<Map.Entry<String, ModOption<?>>> s = options.entrySet();
		ModOption[] m = new ModOption[s.size()];
		int i = m.length - 1;

		for (Map.Entry<String, ModOption<?>> e : s) {
			m[i] = e.getValue();
			i--;
		}

		return m;
	}

	//=========================
	// GUI Related
	//=========================

	/**
	 * Set a named Option to wide.
	 *
	 * @param 	id	 ID of option to set wide
	 * @return This object for building
	 */
	public ModOptions setWideOption(String id) {
		gui.setWide(id);

		return this;
	}

	/**
	 * Set a given option to wide.
	 *
	 * @since	0.7
	 * @param option	Option object
	 * @return This object for building
	 */
	public ModOptions setWideOption(ModOption option) {
		return setWideOption(option.getID());
	}

	/**
	 * Set an option's string format, will remove any other formatters.
	 *
	 * @param id ID of option
	 * @param formatter Formatter
	 * @return This object for building
	 */
	public ModOptions setOptionStringFormat(String id, DisplayStringFormatter formatter) {
		gui.setFormatter(getOption(id), formatter);

		return this;
	}

	/**
	 * Add a formatter to the set of formatters for the given option.
	 *
	 * @param id ID of option
	 * @param formatter Formatter to add
	 * @return This object for building
	 * @since 0.6.1
	 */
	public ModOptions addOptionFormatter(String id, DisplayStringFormatter formatter) {
		gui.addFormatter(getOption(id), formatter);

		return this;
	}

	/**
	 * Set an option's string format, will remove any other formatters.
	 *
	 * @param 	option Option to add formatter to
	 * @param 	formatter Formatter
	 * @return This object for building
	 * @since	0.8
	 */
	public ModOptions setOptionStringFormat(ModOption option, DisplayStringFormatter formatter) {
		gui.setFormatter(option, formatter);

		return this;
	}

	/**
	 * Add a formatter to the set of formatters for the given option.
	 *
	 * @param option Option to add formatter to
	 * @param formatter Formatter to add
	 * @return This object for building
	 * @since 0.8
	 */
	public ModOptions addOptionFormatter(ModOption option, DisplayStringFormatter formatter) {
		gui.addFormatter(option, formatter);

		return this;
	}

	//=========================
	// Getters
	//=========================

	/**
	 * Return the ID for this menu.
	 *
	 * @return ID of this menu
	 */
	public String getID() {
		return id;
	}

	/**
	 * Return name for this menu.
	 *
	 * @return Name of this options menu
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get parent for this menu.
	 *
	 * @return Parent for this menu (or null if no parent)
	 */
	public ModOptions getParent() {
		return parent;
	}

	/**
	 * Set the parent for this menu.
	 *
	 * @param parent Parent menu
	 * @return This object for building
	 */
	public ModOptions setParent(ModOptions parent) {
		this.parent = parent;

		return this;
	}

	/**
	 * Return the GUI controller for this menu.
	 *
	 * @return Gui controller
	 */
	public GuiController getGuiController() {
		return gui;
	}

	//=========================
	// Setters
	//=========================

	/**
	 * Set the ID of this options.
	 *
	 * @param id ID to use
	 * @throws PatternSyntaxException When ID is invalid
	 * @since	0.8
	 */
	private void setID(String id) {
		Matcher m = illegalNamePattern.matcher(id);

		if (m.matches()) {
			throw new PatternSyntaxException("(ModOptions) Please do not use special characters for ID", "", 0);
		} else {
			this.id = id;
		}
	}

	/**
	 * Set multiplayer value.
	 *
	 * @param multi	True if visible in multiplayer
	 * @return This object for building
	 */
	public ModOptions setMultiplayerMode(boolean multi) {
		multiplayer = multi;

		return this;
	}

	/**
	 * Set singleplayer value.
	 *
	 * @param single	True if visible in singleplayer
	 * @return This object for building
	 */
	public ModOptions setSingleplayerMode(boolean single) {
		singleplayer = single;

		return this;
	}

	//=========================
	// Verification
	//=========================

	/**
	 * Checks if this mod is available in single player world menus.
	 *
	 * @return True if in single player world menus
	 */
	public boolean isSingleplayerMod() {
		return singleplayer;
	}

	/**
	 * Checks if this mod is available in multiplayer server menus.
	 *
	 * @return True if in a multiplayer world menu
	 */
	public boolean isMultiplayerMod() {
		return multiplayer;
	}

	//=========================
	// Storage
	//=========================

	/**
	 * Loads global values from disk into memory for this and all sub-menus.
	 *
	 * @return This object for building
	 */
	public ModOptions loadValues() {
		loadValues("", false);

		return this;
	}

	/**
	 * Loads values from disk into memory for this and all sub-menus.
	 *
	 * @param worldName Name of world/ server to load for
	 * @param multi True if  multiplayer word
	 * @return This object for building
	 */
	public ModOptions loadValues(String worldName, boolean multi) {
		String line;

		// Also load all children
		for (ModOptions child : this.getSubOptions()) {
			child.loadValues(worldName, multi);
		}

		File file = getFile(worldName, multi);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			HashMap map = new HashMap<String, String>();

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":", 2);
				String id = parts[0];
				String value = parts[1].replace(":", "");
				map.put(id, value);
			}

			for (ModOption o : this.getOptions()) {
				try {
					// If the option is saved to the disk
					if (map.containsKey(o.getID())) {
						String val = (String) map.get(o.getID());
						boolean global = (worldName.length() == 0);

						if (o instanceof ModSliderOption) {
							ModSliderOption s = (ModSliderOption) o;
							s.setValue(Float.parseFloat(val), global);
						} else if (o instanceof ModMultiOption) {
							ModMultiOption t = (ModMultiOption) o;
							t.setValue(val, global);
						} else if (o instanceof ModBooleanOption) {
							ModBooleanOption b = (ModBooleanOption) o;
							b.setValue(Boolean.valueOf(val), global);
						} else if (o instanceof ModMappedOption) {
							ModMappedOption t = (ModMappedOption) o;
							t.setValue(Integer.parseInt(val), global);
						} else if (o instanceof ModKeyOption) {
							ModKeyOption k = (ModKeyOption) o;
							Integer key = Integer.parseInt(val);

							if (key != ModKeyOption.defaultVal) {
								try {
									k.setValue(key, global);
								} catch (KeyAlreadyBoundException e) {
									// We have to give up attempting to assign
									// and just print a message
									System.out.println("(MOAPI) Key + " + val.charAt(0)
														+ " already bound, please rebind in options");
								}
							}
						} else {
							o.setValue(val, global);
						}

						// Turn off global default for this option
						o.setGlobal(global);
					}
				} catch (NumberFormatException e) {
					System.err.println("(ModOptionsAPI): Could not load option value");
				}
			}
		} catch (FileNotFoundException e) {
			// Ignore, this is expected
		} catch (IOException e) {
			System.out.println("(ModOptionsAPI): IOException occured: " + e.getMessage());
		}

		return this;
	}

	/**
	 * Save options to disk for a particular world.
	 *
	 * @param name			worldname
	 * @param multiplayer		Save to a multiplayer file if true
	 * @return This object for building
	 */
	public ModOptions save(String name, boolean multiplayer) {
		boolean global = (name.length() == 0);
		// Delete old file, write new
		File file = getFile(name, multiplayer);
		file.delete();

		try {
			PrintWriter printwriter = new PrintWriter(new FileWriter(file));

			for (ModOption o : this.getOptions()) {
				Object obj = o.getValue(global);

				// Only record if it's a global value or a local non-global reference value
				if ((obj != null) && (global || (!o.useGlobalValue()))) {
					// Remove all ":" in the name.
					printwriter.println(o.getID().replace(":", "") + ":" + obj.toString());
				}
			}

			printwriter.close();
		} catch (IOException e) {
			System.err.println("(ModOptionsAPI): Could not save options to " + name);
		}

		return this;
	}

	/**
	 * Saves options to disk.
	 *
	 * @return This object for building
	 */
	public ModOptions save() {
		save("", false);

		return this;
	}

	/**
	 * Get directory for this set of options.
	 *
	 * @return Directory for this set of options
	 */
	private String getDir() {
		String subDir = getID();
		ModOptions p = parent;
		String fileName;

		while (p != null) {
			subDir = p.getID() + "/" + subDir;
			p = p.getParent();
		}

		return Minecraft.getGameDirectory() + "/ModOptions/" + subDir;
	}

	/**
	 * Get file associated with this set of options.
	 *
	 * @param name world name
	 * @param multiplayer Save to a multiplayer file if true
	 */
	private File getFile(String name, boolean multiplayer) {
		String prefix = "";
		// Add a prefix for world specifics

		if (name.length() > 0) {
			if (multiplayer) {
				prefix = name + ".server.";
			} else {
				prefix = name + ".world.";
			}
		}

		String subDir = getDir();
		// Ensure dir exists
		File dir = new File(subDir);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		//System.out.println(subDir + "/" + prefix + "settings.ini");
		return new File(subDir + "/" + prefix + "settings.ini");
	}
}
