package gs.or.venator.schedapalestra.core;

import gs.or.venator.schedapalestra.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;

public class ExerciseStat {

	private String id;
	private String name;
	private TextView oneRmTextView;
	private TextView barbellWeightTextView;
	private boolean plainWeight;
	private boolean splitWeight;

	private Double savedOneRm;
	private Double savedBarbellWeight;

	public ExerciseStat(JSONObject o) throws JSONException {
		id = o.getString("id");
		name = o.getString("name");

		savedOneRm = o.optDouble("weight");
		if (Double.isNaN(savedOneRm)) {
			savedOneRm = null;
		}

		savedBarbellWeight = o.optDouble("barbell_weight");
		if (Double.isNaN(savedBarbellWeight)) {
			savedBarbellWeight = null;
		}

		plainWeight = o.optBoolean("plain_weight");
		splitWeight = o.optBoolean("split_weight");
	}

	public ExerciseStat(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public void setOneRmTextView(TextView oneRmTextView) {
		this.oneRmTextView = oneRmTextView;
	}

	public void setBarbellWeightTextView(TextView barbellWeightTextView) {
		this.barbellWeightTextView = barbellWeightTextView;
	}

	public void setPlainWeight(boolean plainWeight) {
		this.plainWeight = plainWeight;
	}

	public void setSplitWeight(boolean splitWeight) {
		this.splitWeight = splitWeight;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TextView getOneRmTextView() {
		return oneRmTextView;
	}

	public TextView getBarbellWeightTextView() {
		return barbellWeightTextView;
	}

	public boolean isPlainWeight() {
		return plainWeight;
	}

	public boolean isSplitWeight() {
		return splitWeight;
	}

	public Double getSavedOneRm() {
		return savedOneRm;
	}

	public Double getSavedBarbellWeight() {
		return savedBarbellWeight;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("id", id);
		o.put("name", name);

		try {
			double d = Double.parseDouble(oneRmTextView.getText().toString());
			o.put("weight", d);
		} catch (Exception e) {
			Log.e("oneRmTextView.getText() [" + oneRmTextView.getText() + "]", this);
		}

		if (barbellWeightTextView != null) {
			try {
				double d = Double.parseDouble(barbellWeightTextView.getText().toString());
				o.put("barbell_weight", d);
			} catch (Exception e) {
				Log.e("oneRmTextView.getText() [" + barbellWeightTextView.getText() + "]", this);
			}
		}
		o.put("plain_weight", plainWeight);
		o.put("split_weight", splitWeight);

		Log.i("toJson " + this + " --> " + o.toString(), this);
		return o;
	}

	@Override
	public String toString() {
		return "ExerciseStat [" + (id != null ? "id=" + id + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (oneRmTextView != null ? "oneRmTextView=" + oneRmTextView + ", " : "")
				+ (barbellWeightTextView != null ? "barbellWeightTextView=" + barbellWeightTextView + ", " : "") + "plainWeight=" + plainWeight
				+ ", splitWeight=" + splitWeight + ", savedOneRm=" + savedOneRm + ", savedBarbellWeight=" + savedBarbellWeight + "]";
	}

}
