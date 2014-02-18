package gs.or.venator.schedapalestra.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FitsSystemWindowLinearLayout extends LinearLayout {

	public FitsSystemWindowLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FitsSystemWindowLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FitsSystemWindowLinearLayout(Context context) {
		super(context);
	}

	@Override
	protected boolean fitSystemWindows(Rect insets) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
			insets.top += getPaddingTop();
			insets.left += getPaddingLeft();
			insets.bottom += getPaddingBottom();
			insets.right += getPaddingRight();
		}
		return super.fitSystemWindows(insets);
	}

}
