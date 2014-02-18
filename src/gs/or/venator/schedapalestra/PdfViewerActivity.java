package gs.or.venator.schedapalestra;

import org.apache.commons.lang3.StringUtils;

import android.os.Bundle;

import com.joanzapata.pdfview.PDFView;

public class PdfViewerActivity extends TranslucentActionBarActivity {

	public static final String PDF_FILE = "pdf_file";
	public static final String TITLE = "title";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pdf_viewer);

		String title = getIntent().getStringExtra(TITLE);
		getActionBar().setTitle(title);

		String pdfFile = getIntent().getStringExtra(PDF_FILE);
		if (StringUtils.isNotEmpty(pdfFile)) {
			PDFView pdfView = (PDFView) findViewById(R.id.pdfview);
			pdfView.fromAsset(pdfFile).load();
		}
	}

}
