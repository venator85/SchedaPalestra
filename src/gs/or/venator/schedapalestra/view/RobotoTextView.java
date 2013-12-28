package gs.or.venator.schedapalestra.view;

import gs.or.venator.schedapalestra.R;
import gs.or.venator.schedapalestra.util.FontManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoTextView extends TextView {

	private static final String EM = "m";

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
				Typeface tf = FontManager.getInstance().getTypeface(typeface);
				setTypeface(tf);

				int minWidthEms = a.getInt(R.styleable.RobotoTextView_min_width_ems, -1);
				if (minWidthEms != -1) {
					float minEms = getPaint().measureText(EM) * minWidthEms;
					setMinimumWidth((int) Math.ceil(getPaddingLeft() + minEms + getPaddingRight()));
				}

				a.recycle();
			}
		}
	}

}
