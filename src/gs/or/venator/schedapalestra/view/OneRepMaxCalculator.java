package gs.or.venator.schedapalestra.view;

import gs.or.venator.schedapalestra.R;
import gs.or.venator.schedapalestra.SchedaPalestraApp;
import gs.or.venator.schedapalestra.core.OneRepMaxMethod;
import gs.or.venator.schedapalestra.util.SimpleTextWatcher;
import gs.or.venator.schedapalestra.util.Utils;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class OneRepMaxCalculator {

	private View view;

	private Spinner one_rep_max_method;
	private TextView txt_weight;
	private TextView txt_reps;
	private TextView txt_1rm;
	private TextView txt_barbell_weight;
	private CheckBox chk_times_two;

	private OneRepMaxMethod method;

	private static class OneRepMaxMethodAdapter extends ArrayAdapter<OneRepMaxMethod> {

		public OneRepMaxMethodAdapter(Context context) {
			super(context, android.R.layout.simple_spinner_item, SchedaPalestraApp.getInstance().getOneRepMaxMethods());
			setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView v = (TextView) super.getView(position, convertView, parent);
			OneRepMaxMethod item = getItem(position);
			v.setText(item.getMethodName());
			return v;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			TextView v = (TextView) super.getDropDownView(position, convertView, parent);
			OneRepMaxMethod item = getItem(position);
			v.setText(item.getMethodName());
			return v;
		}
	}

	public OneRepMaxCalculator(Context context) {

		view = LayoutInflater.from(context).inflate(R.layout.one_repmax_calculation, null, false);

		method = SchedaPalestraApp.getInstance().getOneRepMaxMethods().get(0);

		final TextView one_rep_max_method_extra_content = (TextView) view.findViewById(R.id.one_rep_max_method_extra_content);

		one_rep_max_method = (Spinner) view.findViewById(R.id.one_rep_max_method);
		one_rep_max_method.setAdapter(new OneRepMaxMethodAdapter(context));
		one_rep_max_method.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				method = (OneRepMaxMethod) parent.getItemAtPosition(pos);
				calculateOneRepMax();

				if (method.hasExtraContent()) {
					one_rep_max_method_extra_content.setVisibility(View.VISIBLE);

					SpannableString contentUnderline = new SpannableString(method.getExtraContentButtonText());
					contentUnderline.setSpan(new UnderlineSpan(), 0, contentUnderline.length(), 0);
					one_rep_max_method_extra_content.setText(contentUnderline);

					one_rep_max_method_extra_content.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							method.onExtraContentButtonClicked(v);
						}
					});
				} else {
					one_rep_max_method_extra_content.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		txt_weight = (TextView) view.findViewById(R.id.txt_weight);
		txt_barbell_weight = (TextView) view.findViewById(R.id.txt_barbell_weight);
		txt_reps = (TextView) view.findViewById(R.id.txt_reps);
		txt_1rm = (TextView) view.findViewById(R.id.txt_1rm);
		chk_times_two = (CheckBox) view.findViewById(R.id.chk_times_two);

		SimpleTextWatcher textWatcher = new SimpleTextWatcher() {
			@Override
			public void afterTextChanged_(String s) {
				calculateOneRepMax();
			}
		};
		txt_weight.addTextChangedListener(textWatcher);
		txt_reps.addTextChangedListener(textWatcher);
		txt_barbell_weight.addTextChangedListener(textWatcher);
		chk_times_two.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				calculateOneRepMax();
			}
		});

		txt_1rm.setText(R.string.n_a);
	}

	public View getView() {
		return view;
	}

	public void setWeight(CharSequence weight, boolean splitWeight) {
		txt_weight.setText(weight);
		chk_times_two.setChecked(splitWeight);
	}

	public void setReps(CharSequence reps) {
		txt_reps.setText(reps);
	}

	public void setBarbellWeight(CharSequence barbellWeight) {
		txt_barbell_weight.setText(barbellWeight);
	}

	protected void calculateOneRepMax() {
		try {
			int reps = Integer.parseInt(txt_reps.getText().toString());
			if (reps > 20) {
				txt_reps.setError(SchedaPalestraApp.getContext().getText(R.string.too_many_reps));
			}
			double weight = Double.parseDouble(txt_weight.getText().toString());
			if (chk_times_two.isChecked()) {
				weight *= 2;
			}

			double barbellWeight;
			try {
				barbellWeight = Double.parseDouble(txt_barbell_weight.getText().toString());
			} catch (NumberFormatException e) {
				barbellWeight = 0.0;
			}

			weight += barbellWeight;

			double oneRepMax = method.repMax(weight, reps);
			txt_1rm.setText(Utils.formatWeight(oneRepMax));
			txt_1rm.setTag(oneRepMax);
		} catch (NumberFormatException e) {
			txt_1rm.setText(R.string.n_a);
		}
	}

	public Double getOneRepMax() {
		try {
			double oneRepMax = (Double) txt_1rm.getTag();
			return oneRepMax;
		} catch (Exception e) {
			return null;
		}
	}

}
