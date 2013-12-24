package gs.or.venator.schedapalestra;

import gs.or.venator.schedapalestra.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public class OneRepMaxCalculationDialog extends DialogFragment {

	public interface AlertDialogListener {

		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);

	}

	private AlertDialogListener mListener;
	private String title;

	private OneRepMaxCalculator oneRepMaxCalculator;

	public static final OneRepMaxCalculationDialog newInstance(String title, CharSequence reps, CharSequence weight, boolean splitWeight,
			CharSequence barbellWeight) {
		OneRepMaxCalculationDialog f = new OneRepMaxCalculationDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putCharSequence("reps", reps);
		b.putCharSequence("weight", weight);
		b.putCharSequence("barbellWeight", barbellWeight);
		b.putBoolean("splitWeight", splitWeight);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (AlertDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + AlertDialogListener.class.getSimpleName());
		}
		oneRepMaxCalculator = new OneRepMaxCalculator(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		title = getArguments().getString("title");
		CharSequence reps = getArguments().getCharSequence("reps");
		CharSequence weight = getArguments().getCharSequence("weight");
		CharSequence barbellWeight = getArguments().getCharSequence("barbellWeight");
		boolean splitWeight = getArguments().getBoolean("splitWeight");

		oneRepMaxCalculator.setWeight(weight, splitWeight);
		oneRepMaxCalculator.setReps(reps);
		oneRepMaxCalculator.setBarbellWeight(barbellWeight);
		Log.e("title: " + title, OneRepMaxCalculationDialog.class);
		Log.e("reps: " + reps, OneRepMaxCalculationDialog.class);
		Log.e("weight: " + weight, OneRepMaxCalculationDialog.class);
		Log.e("barbellWeight: " + barbellWeight, OneRepMaxCalculationDialog.class);
		Log.e("splitWeight: " + splitWeight, OneRepMaxCalculationDialog.class);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = oneRepMaxCalculator.getView();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(v);
		builder.setTitle(title);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogPositiveClick(OneRepMaxCalculationDialog.this);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogNegativeClick(OneRepMaxCalculationDialog.this);
			}
		});
		return builder.create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		mListener.onDialogNegativeClick(OneRepMaxCalculationDialog.this);
	}
}
