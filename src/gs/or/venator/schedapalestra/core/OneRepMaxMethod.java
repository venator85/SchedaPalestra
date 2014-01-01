package gs.or.venator.schedapalestra.core;

import android.view.View;

public interface OneRepMaxMethod {

	String getMethodName();

	double repMax(double weight, int reps);

	boolean hasExtraContent();

	String getExtraContentButtonText();

	void onExtraContentButtonClicked(View v);

}
