package modoptionsapi;

import java.util.MissingResourceException;

/**
 * Signifies that a mod was missing when thrown.
 *
 * @version 0.7
 * @since 0.6.5
 */
public class MOMissingModException extends MissingResourceException {
	public MOMissingModException(String msg) {
		super(msg, "", "");
	}
}
