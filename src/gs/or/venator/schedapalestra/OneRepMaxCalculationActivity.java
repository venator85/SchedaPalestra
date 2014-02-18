package gs.or.venator.schedapalestra;

import gs.or.venator.schedapalestra.view.OneRepMaxCalculator;
import android.os.Bundle;

public class OneRepMaxCalculationActivity extends TranslucentActionBarActivity {

	private OneRepMaxCalculator oneRepMaxCalculator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		oneRepMaxCalculator = new OneRepMaxCalculator(this);
		setContentView(oneRepMaxCalculator.getView());
	}

}
