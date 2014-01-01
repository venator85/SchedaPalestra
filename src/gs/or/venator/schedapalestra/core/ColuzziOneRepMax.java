package gs.or.venator.schedapalestra.core;

import gs.or.venator.schedapalestra.PdfViewerActivity;
import gs.or.venator.schedapalestra.R;
import gs.or.venator.schedapalestra.SchedaPalestraApp;
import android.content.Intent;
import android.view.View;

/**
 * Formula interpolated from data in the table assets/scheda_di_carico.pdf
 */
public class ColuzziOneRepMax implements OneRepMaxMethod {

	@Override
	public String getMethodName() {
		return "Scheda di carico S. Coluzzi";
	}

	@Override
	public double repMax(double weight, int reps) {
		double percOneRepMax;
		if (reps < 5) {
			percOneRepMax = (21.0 - reps) / 20.0;
		} else {
			percOneRepMax = (38.0 - reps) / 40.0;
		}
		return weight / percOneRepMax;
	}

	@Override
	public boolean hasExtraContent() {
		return true;
	}

	@Override
	public String getExtraContentButtonText() {
		return SchedaPalestraApp.getContext().getString(R.string.scheda_di_carico);
	}

	@Override
	public void onExtraContentButtonClicked(View v) {
		Intent intent = new Intent(v.getContext(), PdfViewerActivity.class);
		intent.putExtra(PdfViewerActivity.TITLE, getExtraContentButtonText());
		intent.putExtra(PdfViewerActivity.PDF_FILE, "scheda_di_carico.pdf");
		v.getContext().startActivity(intent);
	}

}
