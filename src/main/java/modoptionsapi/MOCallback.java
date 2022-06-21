package modoptionsapi;

/**
 * The callback interface for modoptions API.
 *
 * @author 	Clinton Alexander
 * @since	0.6
 */
public abstract class MOCallback {
	/**
	 * What to do upon clicking a button.
	 *
	 * @param	option		The option being clicked
	 * @return	True if to accept the click or to cancel
	 */
	public abstract boolean onClick(ModOption<?> option);
	/**
	 * What to do upon setting a global value.
	 *
	 * @param	newValue	New value of global setting
	 * @param	option		Option being set
	 * @return	True if to accept the change
	 */
	public boolean onGlobalChange(boolean newValue, ModOption<?> option) {
		return true;
	}
}
