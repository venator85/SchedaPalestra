package gs.or.venator.schedapalestra;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.TextView;

public class OneRepMaxCalculationActivity extends Activity {

	private TextView txt_weight;
	private TextView txt_reps;
	private TextView txt_1rm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_1rm_calculation);

		txt_weight = (TextView) findViewById(R.id.txt_weight);
		txt_reps = (TextView) findViewById(R.id.txt_reps);
		txt_1rm = (TextView) findViewById(R.id.txt_1rm);

		SimpleTextWatcher textWatcher = new SimpleTextWatcher() {
			@Override
			public void afterTextChanged_(Editable s) {
				calculateOneRepMax();
			}
		};
		txt_weight.addTextChangedListener(textWatcher);
		txt_reps.addTextChangedListener(textWatcher);

		txt_1rm.setText("N/A");
	}

	protected void calculateOneRepMax() {
		try {
			int reps = Integer.parseInt(txt_reps.getText().toString());
			if (reps > 20) {
				txt_reps.setError(getText(R.string.too_many_reps));
			}
			double weight = Double.parseDouble(txt_weight.getText().toString());
			double oneRepMax = repMax(weight, reps);
			txt_1rm.setText(Utils.formatWeight(oneRepMax));
		} catch (NumberFormatException e) {
			txt_1rm.setText("N/A");
		}
	}

	private double repMax(double weight, int reps) {
		double percOneRepMax;
		if (reps < 5) {
			percOneRepMax = (21.0 - reps) / 20.0;
		} else {
			percOneRepMax = (38.0 - reps) / 40.0;
		}
		return weight / percOneRepMax;
	}

}
