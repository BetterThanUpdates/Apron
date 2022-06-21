package moapi;

/**
 * Mirrors {@link modoptionsapi.KeyAlreadyBoundException}.
 *
 * @version 1.0
 * @since 0.8
 */
public class KeyAlreadyBoundException extends modoptionsapi.KeyAlreadyBoundException {
	public KeyAlreadyBoundException(Integer key) {
		super(key);
	}
}
