package modoptionsapi;

import java.util.Hashtable;

import org.lwjgl.input.Keyboard;

public class ModKeyOption extends ModOption<Integer> {
	private static final Hashtable<Integer, ModOption<?>> BINDINGS = new Hashtable<>();
	public static final Integer defaultVal = 0;

	public ModKeyOption(String name) {
		this.name = name;
		this.setValue(defaultVal, true);
		this.setValue(defaultVal, false);
	}

	public void setValue(int value) {
		this.setValue(new Integer(value), this.global);
	}

	public void setValue(Integer value) {
		this.setValue(value, this.global);
	}

	public void setValue(int value, boolean scope) {
		this.setValue(new Integer(value), scope);
	}

	public void setValue(Integer value, boolean scope) {
		Integer curVal = this.getValue(scope);

		if (value.equals(defaultVal)) {
			BINDINGS.remove(value);
			super.setValue(value, this.global);
		} else {
			if ((!this.getLocalValue().equals(value) || this.global) && (!this.getGlobalValue().equals(value) || !this.global) && isKeyBound(value)) {
				throw new KeyAlreadyBoundException(value);
			}

			if (curVal != null) {
				BINDINGS.remove(curVal);
			}

			super.setValue(value, scope);
			BINDINGS.put(value, this);
		}
	}

	public static boolean isKeyBound(Integer c) {
		return !c.equals(defaultVal) && BINDINGS.containsKey(c);
	}

	public static String getKeyName(Integer key) {
		String val = Keyboard.getKeyName(key);
		return val == null ? "INVALID" : val;
	}
}
