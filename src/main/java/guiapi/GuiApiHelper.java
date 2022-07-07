package guiapi;

import java.util.AbstractMap;
import java.util.ArrayList;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleButtonModel;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.textarea.TextAreaModel;

public class GuiApiHelper {
	public static final ModAction backModAction = new ModAction(GuiModScreen.class, "back");
	public static final ModAction clickModAction = new ModAction(GuiModScreen.class, "clicksound");
	private ArrayList<AbstractMap.SimpleEntry<String, ModAction>> buttonInfo_;
	private String displayText_;

	public static GuiApiHelper createChoiceMenu(String displayText) {
		return new GuiApiHelper(displayText);
	}

	public static Widget createChoiceMenu(String displayText, Boolean showBackButton, Boolean autoBack, Object... args) {
		if (args.length % 2 == 1) {
			throw new IllegalArgumentException(
					"Arguments not in correct format. You need to have an even number of arguments, in the form of String, ModAction for each button."
			);
		} else {
			GuiApiHelper helper = new GuiApiHelper(displayText);

			try {
				for (int i = 0; i < args.length; i += 2) {
					helper.addButton((String) args[i], (ModAction) args[i + 1], autoBack);
				}
			} catch (Throwable var6) {
				throw new IllegalArgumentException(
						"Arguments not in correct format. You need to have an even number of arguments, in the form of String, ModAction for each button.", var6
				);
			}

			return helper.genWidget(showBackButton);
		}
	}

	public static Widget createChoiceMenu(String displayText, Boolean showBackButton, Boolean autoBack, String[] buttonTexts, ModAction[] buttonActions) {
		if (buttonTexts.length != buttonActions.length) {
			throw new IllegalArgumentException("Arguments not in correct format. buttonTexts needs to be the same size as buttonActions.");
		} else {
			GuiApiHelper helper = new GuiApiHelper(displayText);

			for (int i = 0; i < buttonTexts.length; i += 2) {
				helper.addButton(buttonTexts[i], buttonActions[i], autoBack);
			}

			return helper.genWidget(showBackButton);
		}
	}

	public static Button makeButton(String displayText, ModAction action, Boolean addClick) {
		SimpleButtonModel simplebuttonmodel = new SimpleButtonModel();

		if (addClick) {
			action = action.mergeAction(clickModAction);
		}

		simplebuttonmodel.addActionCallback(action);
		Button button = new Button(simplebuttonmodel);
		button.setText(displayText);
		return button;
	}

	public static Button makeButton(String displayText, String methodName, Object me, Boolean addClick) {
		return makeButton(displayText, new ModAction(me, methodName), addClick);
	}

	public static Button makeButton(String displayText, String methodName, Object me, Boolean addClick, Class<?>[] classes, Object... arguments) {
		return makeButton(displayText, new ModAction(me, methodName, classes).setDefaultArguments(arguments), addClick);
	}

	public static TextArea makeTextArea(String text, Boolean htmlMode) {
		if (!htmlMode) {
			SimpleTextAreaModel model = new SimpleTextAreaModel();
			model.setText(text, false);
			return new TextArea(model);
		} else {
			HTMLTextAreaModel model = new HTMLTextAreaModel();
			model.setHtml(text);
			return new TextArea(model);
		}
	}

	public static void setTextAreaText(TextArea textArea, String text) {
		TextAreaModel model = textArea.getModel();

		if (model instanceof SimpleTextAreaModel) {
			((SimpleTextAreaModel) model).setText(text, false);
		} else {
			((HTMLTextAreaModel) model).setHtml(text);
		}
	}

	public static Widget makeTextDisplayAndGoBack(String titleText, String displayText, String buttonText, Boolean htmlMode) {
		WidgetSinglecolumn widget = new WidgetSinglecolumn();
		widget.add(makeTextArea(displayText, htmlMode));
		widget.overrideHeight = false;
		WidgetSimplewindow window = new WidgetSimplewindow(widget, titleText);
		window.backButton.setText(buttonText);
		return window;
	}

	private GuiApiHelper(String displayText) {
		this.displayText_ = displayText;
		this.buttonInfo_ = new ArrayList<>();
	}

	public void addButton(String text, ModAction action, Boolean mergeBack) {
		ModAction buttonAction = action;

		if (mergeBack) {
			buttonAction = action.mergeAction(backModAction);
			buttonAction.nameRef = "Button '" + text + "' with back.";
		}

		this.buttonInfo_.add(new AbstractMap.SimpleEntry<>(text, buttonAction));
	}

	public void addButton(String text, String methodName, Object me, Boolean mergeBack) {
		this.addButton(text, new ModAction(me, methodName), mergeBack);
	}

	public void addButton(String text, String methodName, Object me, Class<?>[] types, Boolean mergeBack, Object... arguments) {
		this.addButton(text, new ModAction(me, methodName, types).setDefaultArguments(arguments), mergeBack);
	}

	public Widget genWidget(Boolean showBackButton) {
		WidgetSinglecolumn widget = new WidgetSinglecolumn();
		TextArea textarea = makeTextArea(this.displayText_, false);
		widget.add(textarea);
		widget.heightOverrideExceptions.put(textarea, 0);

		for (AbstractMap.SimpleEntry<String, ModAction> entry : this.buttonInfo_) {
			widget.add(makeButton(entry.getKey(), entry.getValue(), true));
		}

		return new WidgetSimplewindow(widget, null, showBackButton);
	}

	static {
		backModAction.nameRef = "Helper Back ModAction";
		clickModAction.nameRef = "Helper ClickSound ModAction";
	}
}