package modoptionsapi;

/**
 * Used to manipulate a display string for a Mod Option.
 * Intended to be used as an anonymous class.
 *
 * @author	Clinton Alexander
 * @version	1.0
 * @since	0.6
 */
public interface MODisplayString {
	/**
	 * Manipulates the display string according.
	 *
	 * @param	name	Name of the button
	 * @param	value	Current value of button
	 * @return	Display string, such as with a suffix, prefix, etc
	 */
	String manipulate(String name, String value);
}
