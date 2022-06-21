package modoptionsapi;

/**
 * Is thrown when a key is already bound.
 *
 * @author	Clinton Alexander
 * @version	1.0
 * @since	0.7
 */
public class KeyAlreadyBoundException extends IllegalStateException {
	public KeyAlreadyBoundException(Integer key) {
		super("Key " + ModKeyOption.getKeyName(key) + " is already bound");
	}
}
