package gs.or.venator.schedapalestra.core;

import gs.or.venator.schedapalestra.R;
import gs.or.venator.schedapalestra.SchedaPalestraApp;
import gs.or.venator.schedapalestra.R.string;
import gs.or.venator.schedapalestra.util.Utils;

import org.apache.commons.lang3.StringUtils;

import android.widget.TextView;

public class SetCalculation {
	private TextView oneRmTextView;
	private double oneRmPercent;
	private TextView barbellWeightTextView;
	private TextView resultTextView;
	private boolean splitWeight;

	public SetCalculation(TextView oneRmTextView, double oneRmPercent, boolean splitWeight, TextView barbellWeightTextView, TextView resultTextView) {
		this.oneRmTextView = oneRmTextView;
		this.oneRmPercent = oneRmPercent;
		this.splitWeight = splitWeight;
		this.barbellWeightTextView = barbellWeightTextView;
		this.resultTextView = resultTextView;
	}

	public void update() {
		try {
			double oneRm = Double.parseDouble(oneRmTextView.getText().toString());
			double result = oneRm * oneRmPercent;
			double resultPerSide = result;
			final String weightTotal = Utils.formatWeight(result);
			if (barbellWeightTextView != null || splitWeight) {
				if (barbellWeightTextView != null) {
					final String sBarbellWeight = barbellWeightTextView.getText().toString();
					if (StringUtils.isNumeric(sBarbellWeight)) {
						double barbellWeight = Double.parseDouble(sBarbellWeight);
						resultPerSide -= barbellWeight;
					}
				}
				resultPerSide /= 2;
				final String weightPerSide = Utils.formatWeight(resultPerSide);
				resultTextView.setText(SchedaPalestraApp.getContext().getString(R.string.weight_times_two, weightPerSide));
				resultTextView.setTag(resultPerSide);
			} else {
				resultTextView.setText(weightTotal);
				resultTextView.setTag(result);
			}
		} catch (NumberFormatException e) {
			resultTextView.setText(R.string.n_a);
		}
	}

	@Override
	public String toString() {
		return "Calculation [oneRmTextView=" + oneRmTextView + ", oneRmPercent=" + oneRmPercent + ", barbellWeightTextView=" + barbellWeightTextView
				+ ", resultTextView=" + resultTextView + "]";
	}

}