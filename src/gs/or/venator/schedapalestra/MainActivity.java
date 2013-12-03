package gs.or.venator.schedapalestra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String WORKOUT_JSON = "workout.json";
	private static final String WORKOUT_STATS_JSON = "workout_stats.json";

	private Map<String, List<SetCalculation>> exerciseIdToWeight;
	private Map<String, ExerciseStat> exerciseStats;

	private File workoutJsonFile;
	private File workoutJsonStatsFile;

	private ViewPager viewPager;

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
				if (barbellWeightTextView != null || splitWeight) {
					if (barbellWeightTextView != null) {
						final String sBarbellWeight = barbellWeightTextView.getText().toString();
						if (StringUtils.isNumeric(sBarbellWeight)) {
							double barbellWeight = Double.parseDouble(sBarbellWeight);
							resultPerSide -= barbellWeight;
						}
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
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		populate();
	}

	private void populate() {
		List<View> pages = new ArrayList<View>();

		exerciseIdToWeight = new HashMap<String, List<SetCalculation>>();
		exerciseStats = new LinkedHashMap<String, ExerciseStat>();

		workoutJsonFile = new File(Environment.getExternalStorageDirectory(), WORKOUT_JSON);
		workoutJsonStatsFile = new File(Environment.getExternalStorageDirectory(), WORKOUT_STATS_JSON);
		ensureWorkoutFileExistsOnSd(workoutJsonFile);
		Log.i("Using workout.json at " + workoutJsonFile.getAbsolutePath(), this);

		FileInputStream fisWorkout = null;
		FileInputStream fisStats = null;
		try {
			fisWorkout = new FileInputStream(workoutJsonFile);
			String sWorkoutJson = Utils.convertStreamToString(fisWorkout);
			JSONArray workout = new JSONArray(sWorkoutJson);

			if (workoutJsonStatsFile.exists()) {
				fisStats = new FileInputStream(workoutJsonStatsFile);
				String sStatsJson = Utils.convertStreamToString(fisStats);
				JSONArray stats = new JSONArray(sStatsJson);

				for (int i = 0; i < stats.length(); i++) {
					JSONObject stat = stats.getJSONObject(i);
					ExerciseStat exerciseStat = new ExerciseStat(stat);
					exerciseStats.put(exerciseStat.getId(), exerciseStat);
				}
			}

			for (int i = 0; i < workout.length(); i++) {
				ViewGroup sessionPage = (ViewGroup) getLayoutInflater().inflate(R.layout.session, viewPager, false);
				pages.add(sessionPage);

				ViewGroup sessionView = (ViewGroup) sessionPage.findViewById(R.id.session);

				JSONObject session = workout.getJSONObject(i).getJSONObject("session");

				String sessionName = session.getString("name");
				TextView session_name = findView(sessionView, R.id.session_name);
				session_name.setText(sessionName);

				JSONArray exercises = session.getJSONArray("exercises");
				for (int j = 0; j < exercises.length(); j++) {
					JSONObject exercise = exercises.getJSONObject(j);

					final String exerciseId = exercise.getString("id");
					List<SetCalculation> weights = new ArrayList<SetCalculation>();
					exerciseIdToWeight.put(exerciseId, weights);

					ViewGroup exerciseView = (ViewGroup) getLayoutInflater().inflate(R.layout.exercise, sessionView, false);
					sessionView.addView(exerciseView);

					ViewGroup sets_container = findView(exerciseView, R.id.sets_container);

					String exerciseName = exercise.getString("name");
					TextView exercise_name = findView(exerciseView, R.id.exercise_name);
					exercise_name.setText(exerciseName);

					boolean plainWeight = exercise.optBoolean("plain_weight");
					if (plainWeight) {
						TextView label_massimale = findView(exerciseView, R.id.label_massimale);
						label_massimale.setText("Carico:");
					}

					boolean withBarbell = exercise.optBoolean("with_barbell");
					boolean splitWeight = exercise.optBoolean("split_weight");
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
						txt_set_reps.setText(String.valueOf(reps));

						double oneRmPc = set.optDouble("1rmpc");
						if (Double.isNaN(oneRmPc)) {
							oneRmPc = 1.0;
							findView(setView, R.id.txt_set_at).setVisibility(View.GONE);
							txt_set_1rmpc.setVisibility(View.GONE);
						} else {
							txt_set_1rmpc.setText(Utils.formatPercentage(oneRmPc));
						}

						weights.add(new SetCalculation(txt_1rm, oneRmPc, splitWeight, txt_barbell_weight, txt_set_weight));
					}

					ExerciseStat stat = exerciseStats.get(exerciseId);
					if (stat != null) {
						txt_1rm.setText(Utils.doubleToShortString(stat.getSavedOneRm()));
						if (txt_barbell_weight != null) {
							txt_barbell_weight.setText(Utils.doubleToShortString(stat.getSavedBarbellWeight()));
						}
						stat.setOneRmTextView(txt_1rm);
						stat.setBarbellWeightTextView(txt_barbell_weight);
					} else {
						stat = new ExerciseStat(exerciseId, exerciseName);
						stat.setOneRmTextView(txt_1rm);
						stat.setBarbellWeightTextView(txt_barbell_weight);
						stat.setPlainWeight(plainWeight);
						stat.setSplitWeight(splitWeight);
						exerciseStats.put(exerciseId, stat);
					}

					Log.i("Weights by exercise: " + exerciseId + " --> " + weights, this);
				}
			}

			for (Entry<String, List<SetCalculation>> e : exerciseIdToWeight.entrySet()) {
				for (SetCalculation c : e.getValue()) {
					c.update();
				}
			}

			viewPager.setAdapter(new MyPagerAdapter(pages));

		} catch (Exception e) {
			Toast.makeText(this, "Error on opening/reading workout.json file:\n" + e.toString(), Toast.LENGTH_LONG).show();
			throw new RuntimeException(e.getMessage(), e);

		} finally {
			IOUtils.closeQuietly(fisWorkout);
			IOUtils.closeQuietly(fisStats);
		}
	}

	private void updateCalculations(String exerciseId) {
		List<SetCalculation> list = exerciseIdToWeight.get(exerciseId);
		if (list != null) {
			for (SetCalculation c : list) {
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
			Toast.makeText(this, R.string.inizializzo_scheda_da_assets, Toast.LENGTH_LONG).show();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_save) {
			saveStats();
			return true;
		} else if (item.getItemId() == R.id.menu_1rm) {
			Intent intent = new Intent(this, OneRepMaxCalculationActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveStats() {
		FileWriter fosStats = null;
		try {
			JSONArray array = new JSONArray();
			for (Entry<String, ExerciseStat> e : exerciseStats.entrySet()) {
				JSONObject o = e.getValue().toJson();
				array.put(o);
			}
			String sStats = array.toString();
			Log.d("Saving stats:\n" + array.toString(2), this);

			fosStats = new FileWriter(workoutJsonStatsFile);
			fosStats.write(sStats);

			Toast.makeText(this, R.string.massimali_e_carichi_salvati, Toast.LENGTH_SHORT).show();
			Utils.hideKeyboard(this);
		} catch (Exception e) {
			Toast.makeText(this, "Unable to save stats:\n" + e.toString(), Toast.LENGTH_LONG).show();
			Log.e("Unable to save stats", e, this);
		} finally {
			IOUtils.closeQuietly(fosStats);
		}
	}
}
