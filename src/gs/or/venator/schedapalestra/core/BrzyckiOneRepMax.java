package gs.or.venator.schedapalestra.core;

import android.view.View;

public class BrzyckiOneRepMax implements OneRepMaxMethod {

	@Override
	public String getMethodName() {
		return "Brzycki";
	}

	@Override
	public double repMax(double weight, int reps) {
		return 36.0 * weight / (37.0 - reps);
	}

	@Override
	public boolean hasExtraContent() {
		return false;
	}

	@Override
	public String getExtraContentButtonText() {
		return null;
	}

	@Override
	public void onExtraContentButtonClicked(View v) {

	}

}
