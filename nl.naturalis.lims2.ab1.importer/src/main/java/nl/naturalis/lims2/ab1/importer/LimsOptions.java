/**
 * 
 */
package nl.naturalis.lims2.ab1.importer;

import jebl.util.ProgressListener;

import com.biomatters.geneious.publicapi.components.Dialogs.DialogOptions;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;
import com.biomatters.geneious.publicapi.plugin.Icons;
import com.biomatters.geneious.publicapi.plugin.Options;

/**
 * @author Reinier.Kartowikromo
 *
 */
public class LimsOptions extends Options {

	private FileSelectionOption fileSelectionOption;
	private LabelOption labelOption;
	private DialogOptions dialogOptions;
	private Icons icon;

	public LimsOptions(String filename) {
		// dialogOptions.getCustomIcon();
		// dialogOptions.getDefaultButton();
		// dialogOptions = getDialogOptions();

		labelOption = (LabelOption) addLabel(filename);
		labelOption.setCenterText(true);
		labelOption.isVisible();

		/*
		 * fileSelectionOption = addFileSelectionOption("Excel",
		 * "Select a Excel file", "");
		 * fileSelectionOption.setAlwaysUsesDefaultPreferenceLocation(true);
		 * fileSelectionOption.setAllowMultipleSelection(true);
		 */

	}

	public String getSelectedFile() {
		return fileSelectionOption.getValue();
	}

	public String getLabelFile() {
		return fileSelectionOption.getLabel();
	}

	public Options getOptions(final AnnotatedPluginDocument[] documents)
			throws DocumentOperationException {
		return new LimsOptions(null);
	}

	public void performOperation(AnnotatedPluginDocument[] documentList,
			ProgressListener progressListener, Options options)
			throws DocumentOperationException {
		LimsOptions myOptions = (LimsOptions) options;
		String sampleOptionSelected = myOptions.fileSelectionOption.getValue();

	}

}
