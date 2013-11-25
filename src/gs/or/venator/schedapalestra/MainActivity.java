package gs.or.venator.schedapalestra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String WORKOUT_JSON = "workout.json";

	private Map<String, List<Calculation>> exerciseIdToWeight = new HashMap<String, List<Calculation>>();

	private LinearLayout sessionsLayout;

	public class Calculation {
		private TextView oneRmTextView;
		private double oneRmPercent;
		private TextView barbellWeightTextView;
		private TextView resultTextView;

		public Calculation(TextView oneRmTextView, double oneRmPercent, TextView barbellWeightTextView, TextView resultTextView) {
			this.oneRmTextView = oneRmTextView;
			this.oneRmPercent = oneRmPercent;
			this.barbellWeightTextView = barbellWeightTextView;
			this.resultTextView = resultTextView;
		}

		public void update() {
			try {
				double oneRm = Double.parseDouble(oneRmTextView.getText().toString());
				double result = oneRm * oneRmPercent;
				double resultPerSide = result;
				if (barbellWeightTextView != null) {
					final String sBarbellWeight = barbellWeightTextView.getText().toString();
					if (StringUtils.isNumeric(sBarbellWeight)) {
						double barbellWeight = Double.parseDouble(sBarbellWeight);
						resultPerSide -= barbellWeight;
					}
					resultPerSide /= 2;
					final String weightPerSide = Utils.formatWeight(resultPerSide, false);
					resultTextView.setText(String.format("%s (%s + %s)", Utils.formatWeight(result), weightPerSide, weightPerSide));
				} else {
					resultTextView.setText(Utils.formatWeight(result));
				}
			} catch (NumberFormatException e) {
				resultTextView.setText("N/A");
			}
		}

		@Override
		public String toString() {
			return "Calculation [oneRmTextView=" + oneRmTextView + ", oneRmPercent=" + oneRmPercent + ", barbellWeightTextView=" + barbellWeightTextView
					+ ", resultTextView=" + resultTextView + "]";
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sessionsLayout = (LinearLayout) findViewById(R.id.sessions);
		populate();
	}
	
	private void populate() {
		sessionsLayout.removeAllViews();

		File workoutJsonFile = new File(Environment.getExternalStorageDirectory(), WORKOUT_JSON);
		ensureWorkoutFileExistsOnSd(workoutJsonFile);
		Log.i("Using workout.json at " + workoutJsonFile.getAbsolutePath(), this);

		try {
			FileInputStream fis = new FileInputStream(workoutJsonFile);
			String sWorkoutJson = Utils.convertStreamToString(fis);

			JSONArray workout = new JSONArray(sWorkoutJson);

			for (int i = 0; i < workout.length(); i++) {
				ViewGroup sessionView = (ViewGroup) getLayoutInflater().inflate(R.layout.session, sessionsLayout, false);
				sessionsLayout.addView(sessionView);

				JSONObject session = workout.getJSONObject(i).getJSONObject("session");

				String sessionName = session.getString("name");
				TextView session_name = findView(sessionView, R.id.session_name);
				session_name.setText(sessionName);

				JSONArray exercises = session.getJSONArray("exercises");
				for (int j = 0; j < exercises.length(); j++) {
					JSONObject exercise = exercises.getJSONObject(j);

					final String exerciseId = exercise.getString("id");
					List<Calculation> weights = new ArrayList<Calculation>();
					exerciseIdToWeight.put(exerciseId, weights);

					ViewGroup exerciseView = (ViewGroup) getLayoutInflater().inflate(R.layout.exercise, sessionView, false);
					sessionView.addView(exerciseView);

					ViewGroup sets_container = findView(exerciseView, R.id.sets_container);

					String exerciseName = exercise.getString("name");
					TextView exercise_name = findView(exerciseView, R.id.exercise_name);
					exercise_name.setText(exerciseName);

					boolean withBarbell = exercise.getBoolean("with_barbell");
					final View with_barbell_container = findView(exerciseView, R.id.with_barbell_container);
					TextView txt_barbell_weight;
					if (withBarbell) {
						with_barbell_container.setVisibility(View.VISIBLE);
						txt_barbell_weight = findView(with_barbell_container, R.id.txt_barbell_weight);
						txt_barbell_weight.addTextChangedListener(new SimpleTextWatcher() {
							@Override
							public void afterTextChanged_(Editable s) {
								updateCalculations(exerciseId);
							}
						});
					} else {
						with_barbell_container.setVisibility(View.GONE);
						txt_barbell_weight = null;
					}

					TextView txt_1rm = findView(exerciseView, R.id.txt_1rm);
					txt_1rm.addTextChangedListener(new SimpleTextWatcher() {
						@Override
						public void afterTextChanged_(Editable s) {
							updateCalculations(exerciseId);
						}
					});

					JSONArray sets = exercise.getJSONArray("sets");

					for (int k = 0; k < sets.length(); k++) {
						ViewGroup setView = (ViewGroup) getLayoutInflater().inflate(R.layout.set, sets_container, false);
						sets_container.addView(setView);

						TextView txt_set_reps = findView(setView, R.id.txt_set_reps);
						TextView txt_set_1rmpc = findView(setView, R.id.txt_set_1rmpc);
						TextView txt_set_weight = findView(setView, R.id.txt_set_weight);

						JSONObject set = sets.getJSONObject(k);

						int reps = set.getInt("reps");
						double oneRmPc = set.getDouble("1rmpc");

						txt_set_reps.setText(String.valueOf(reps));
						txt_set_1rmpc.setText(Utils.formatPercentage(oneRmPc));

						weights.add(new Calculation(txt_1rm, oneRmPc, txt_barbell_weight, txt_set_weight)); //FIXME barbell
					}

					Log.i("Weights by exercise: " + exerciseId + " --> " + weights, this);

				}
			}

			for (Entry<String, List<Calculation>> e : exerciseIdToWeight.entrySet()) {
				for (Calculation c : e.getValue()) {
					c.update();
				}
			}

		} catch (Exception e) {
			Log.e("Error on opening/reading workout.json file", e, this);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void updateCalculations(String exerciseId) {
		List<Calculation> list = exerciseIdToWeight.get(exerciseId);
		if (list != null) {
			for (Calculation c : list) {
				c.update();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends View> T findView(View root, int id) {
		return (T) root.findViewById(id);
	}

	private void ensureWorkoutFileExistsOnSd(File workoutJsonFile) {
		if (!workoutJsonFile.exists()) {
			Toast.makeText(this, "Inizializzo scheda da assets", Toast.LENGTH_LONG).show();
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				AssetManager assetManager = getAssets();
				is = assetManager.open(WORKOUT_JSON);
				fos = new FileOutputStream(workoutJsonFile);
				IOUtils.copy(is, fos);
			} catch (IOException e) {
				Log.e("Unable to copy workout.json from assets to SD", e, this);
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(fos);
			}
		}
	}
}
