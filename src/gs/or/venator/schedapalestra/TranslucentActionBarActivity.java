package gs.or.venator.schedapalestra;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class TranslucentActionBarActivity extends Activity {

	@Override
	public void setContentView(int layoutResID) {
		ViewGroup content = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
		View layoutView = LayoutInflater.from(this).inflate(layoutResID, content, false);
		layoutView.setFitsSystemWindows(true);
		content.addView(layoutView);
	}

	@Override
	public void setContentView(View view) {
		view.setFitsSystemWindows(true);
		super.setContentView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		view.setFitsSystemWindows(true);
		super.setContentView(view, params);
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		int mStatusBarHeight = getInternalDimensionSize(getResources(), "status_bar_height");
//		Toast.makeText(this, "status bar height " + mStatusBarHeight, Toast.LENGTH_SHORT).show();
//	}
//
//	private int getInternalDimensionSize(Resources res, String key) {
//		int result = 0;
//		int resourceId = res.getIdentifier(key, "dimen", "android");
//		if (resourceId > 0) {
//			result = res.getDimensionPixelSize(resourceId);
//		}
//		return result;
//	}

}
