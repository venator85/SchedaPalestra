package gs.or.venator.schedapalestra;

import gs.or.venator.schedapalestra.view.OneRepMaxCalculator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class OneRepMaxCalculationDialog {

	public interface OnNewOneRepMaxSet {
		void onNewOneRepMaxSet(double newOneRepMax);
	}

	public static final AlertDialog newInstance(Context context, String title, CharSequence reps, CharSequence weight, boolean splitWeight,
			CharSequence barbellWeight, final OnNewOneRepMaxSet onNewOneRepMaxSetListener) {

		final OneRepMaxCalculator oneRepMaxCalculator = new OneRepMaxCalculator(context);
		oneRepMaxCalculator.setWeight(weight, splitWeight);
		oneRepMaxCalculator.setReps(reps);
		oneRepMaxCalculator.setBarbellWeight(barbellWeight);
		View v = oneRepMaxCalculator.getView();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(v);
		builder.setTitle(title);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Double newOneRepMax = oneRepMaxCalculator.getOneRepMax();
				if (onNewOneRepMaxSetListener != null && newOneRepMax != null) {
					onNewOneRepMaxSetListener.onNewOneRepMaxSet(newOneRepMax);
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		return builder.create();
	}

}
