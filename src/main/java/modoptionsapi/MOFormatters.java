package modoptionsapi;

import org.jetbrains.annotations.NotNull;

/**
 * Container class for formatters.
 *
 * @author 	Clinton Alexander
 * @version	1.0
 * @since	0.3
 */
public class MOFormatters {
	/**
	 * Default formatter for adding the name label.
	 */
	public static final DefaultFormat defaultFormat = new MOFormatters.DefaultFormat();
	/**
	 * Integer formatter for sliders.
	 *
	 * @since	0.6.1
	 */
	public static final IntegerSliderFormat integerSlider = new MOFormatters.IntegerSliderFormat();

	/**
	 * Default manipulation class.
	 *
	 * @author	Clinton Alexander
	 * @version	1.0
	 * @since	0.6
	 */
	private static final class DefaultFormat implements MODisplayString {
		/**
		 * Format the standard input into a readable string.
		 */
		public String manipulate(String name, String value) {
			return name + ": " + value;
		}
	}

	/**
	 * Formatting class for a given suffix with a space between.
	 *
	 * @author	Clinton Alexander
	 * @version	1.0
	 * @since	0.6
	 */
	public static final class SuffixFormat implements MODisplayString {
		/**
		 * Suffix value.
		 */
		private final String suffix;

		/**
		 * Creates a string formatter with a suffix.
		 *
		 * @param	suffix	Suffix to add to all values
		 */
		public SuffixFormat(String suffix) {
			this.suffix = suffix;
		}

		/**
		 * Format the input into a readable string.
		 */
		@NotNull
		public String manipulate(String name, String value) {
			return value + " " + suffix;
		}
	}

	/**
	 * Formatting class for turning a float numeric string to an int one.
	 *
	 * @author	Clinton Alexander
	 * @version	1.0
	 * @since	0.6.1
	 */
	public static final class IntegerSliderFormat implements MODisplayString {
		/**
		 * Creates a string formatter which formats slider values to integer values.
		 *
		 */
		public IntegerSliderFormat() {
		}

		/**
		 * Format the input into a readable string.
		 */
		@NotNull
		public String manipulate(String name, String value) {
			try {
				float f = Float.parseFloat(value);
				int i = (int) f;
				return "" + i;
			} catch (NumberFormatException e) {
				System.out.println("(MdoOptionsAPI) Could not format " + value + " into an integer");
				return name + ": " + value;
			}
		}
	}
}
