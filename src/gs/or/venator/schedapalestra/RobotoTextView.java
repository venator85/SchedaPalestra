package gs.or.venator.schedapalestra;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoTextView extends TextView {

	public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		onInit(attrs);
	}

	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		onInit(attrs);
	}

	public RobotoTextView(Context context) {
		super(context);
		onInit(null);
	}

	private void onInit(AttributeSet attrs) {
		if (!isInEditMode()) {
			if (attrs != null) {
				TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RobotoTextView);
				int typeface = a.getInt(R.styleable.RobotoTextView_typeface, 0);
				setTypeface(FontManager.getInstance().getTypeface(typeface));
				a.recycle();
			}
		}
	}

}
