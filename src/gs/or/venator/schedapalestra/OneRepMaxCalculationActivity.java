package gs.or.venator.schedapalestra;

import android.app.Activity;
import android.os.Bundle;

public class OneRepMaxCalculationActivity extends Activity {

	private OneRepMaxCalculator oneRepMaxCalculator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		oneRepMaxCalculator = new OneRepMaxCalculator(this);
		setContentView(oneRepMaxCalculator.getView());
	}

}
