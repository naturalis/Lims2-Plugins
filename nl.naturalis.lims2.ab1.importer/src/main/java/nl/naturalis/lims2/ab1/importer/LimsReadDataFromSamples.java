/**
 * 
 */
package nl.naturalis.lims2.ab1.importer;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nl.naturalis.lims2.utils.LimsFrameProgress;
import nl.naturalis.lims2.utils.LimsImporterUtil;
import nl.naturalis.lims2.utils.LimsLogger;
import nl.naturalis.lims2.utils.LimsNotes;
import nl.naturalis.lims2.utils.LimsReadGeneiousFieldsValues;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biomatters.geneious.publicapi.components.Dialogs;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.documents.DocumentUtilities;
import com.biomatters.geneious.publicapi.documents.PluginDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentAction;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.opencsv.CSVReader;

/**
 * @author Reinier.Kartowikromo
 *
 */
public class LimsReadDataFromSamples extends DocumentAction {

	private List<AnnotatedPluginDocument> docs;
	private LimsImporterUtil limsImporterUtil = new LimsImporterUtil();
	private LimsSamplesFields limsExcelFields = new LimsSamplesFields();
	private LimsNotes limsNotes = new LimsNotes();
	private LimsFileSelector fcd = new LimsFileSelector();
	private LimsReadGeneiousFieldsValues ReadGeneiousFieldsValues = new LimsReadGeneiousFieldsValues();
	private LimsDummySeq limsDummySeq = new LimsDummySeq();

	private String extractIDfileName = "";
	private List<String> msgList = new ArrayList<String>();
	private List<String> msgUitvalList = new ArrayList<String>();
	private List<String> verwerkingListCnt = new ArrayList<String>();
	private List<String> verwerkList = new ArrayList<String>();
	private static final Logger logger = LoggerFactory
			.getLogger(LimsReadDataFromSamples.class);
	private Object documentFileName = "";
	private Object result = "";

	public int importCounter;
	private int importTotal;
	private String[] record = null;
	private String ID = "";
	private String fileSelected = "";
	private final String noteCode = "DocumentNoteUtilities-Extract ID (Seq)";
	private final String fieldName = "ExtractIDCode_Seq";
	private JFrame frame = new JFrame();

	String logFileName = limsImporterUtil.getLogPath() + File.separator
			+ "Sample-method-Uitvallijst-" + limsImporterUtil.getLogFilename();

	LimsLogger limsLogger = new LimsLogger(logFileName);
	LimsFrameProgress limsFrameProgress = new LimsFrameProgress();
	private String plateNumber = "";
	private boolean isSampleDoc = false;
	private boolean isExtractIDSeqExists = false;
	private int version = 0;
	private String recordDocumentName = "";

	public LimsReadDataFromSamples() {

	}

	@Override
	public void actionPerformed(
			AnnotatedPluginDocument[] annotatedPluginDocuments) {

		performOperation(annotatedPluginDocuments);
	}

	public void performOperation(AnnotatedPluginDocument[] documents) {

		/* Get Databasename */
		ReadGeneiousFieldsValues.resultDB = ReadGeneiousFieldsValues
				.getServerDatabaseServiceName();

		Object[] options = { "Ok", "No", "Cancel" };
		int n = JOptionPane.showOptionDialog(frame,
				"Choose one option to start Samples import", "Samples",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);
		if (n == 0) {
			if (DocumentUtilities.getSelectedDocuments().isEmpty()) {
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {

						Dialogs.showMessageDialog("Select at least one document");
						return;
					}
				});
			}
			if (!DocumentUtilities.getSelectedDocuments().isEmpty()) {
				docs = DocumentUtilities.getSelectedDocuments();

				isExtractIDSeqExists = DocumentUtilities.getSelectedDocuments()
						.iterator().next().toString()
						.contains("MarkerCode_Seq");

				if (!isExtractIDSeqExists) {
					Dialogs.showMessageDialog("At least one selected document lacks Extract ID (Seq).");
					return;
				}

				logger.info("Start updating selected document(s).");
				fileSelected = fcd.loadSelectedFile();
				/** Add selected documents to a list. */

				if (fileSelected == null) {
					return;
				}

				msgUitvalList.add("Filename: " + fileSelected + "\n");

				for (int cnt = 0; cnt < docs.size(); cnt++) {

					documentFileName = docs.get(cnt)
							.getFieldValue("cache_name");

					recordDocumentName = docs.get(cnt).getName();

					/*
					 * if (docs.toString().contains("consensus sequence") ||
					 * docs.toString().contains("Contig")) {
					 * System.out.println("DocumentName: " +
					 * recordDocumentName); }
					 */
					System.out.println("DocumentName: " + recordDocumentName);

					if (documentFileName.equals(recordDocumentName)) {

						if (!docs.toString().contains("consensus sequence")
								|| !docs.toString().contains("Contig")) {
							version = Integer
									.parseInt((String) ReadGeneiousFieldsValues
											.getVersionValueFromAnnotatedPluginDocument(
													documents,
													"DocumentNoteUtilities-Document version",
													"DocumentVersionCode_Seq",
													cnt));
						}
					}

					result = "";

					/* Get file name from the document(s) */
					if (documentFileName.toString().contains("ab1")
							|| docs.get(cnt).getName().contains("fas")
							|| docs.get(cnt).getName().contains("dum")) {
						result = ReadGeneiousFieldsValues
								.getFileNameFromGeneiousDatabase(
										(String) documentFileName,
										"//XMLSerialisableRootElement/name");
						// .readValueFromAnnotatedPluginDocument(
						// documents[cnt], "importedFrom",
						// "filename");

					}

					/* Check of the filename contain "FAS" extension */
					if (result.toString().contains("fas") && result != null) {
						extractIDfileName = (String) ReadGeneiousFieldsValues
								.readValueFromAnnotatedPluginDocument(
										documents[cnt],
										"DocumentNoteUtilities-Extract ID (Seq)",
										"ExtractIDCode_Seq");

					} else if (result.toString().contains("ab1")) {
						/* get AB1 filename */
						extractIDfileName = getExtractIDFromAB1FileName(docs
								.get(cnt).getName());
					} else if (docs.get(cnt).getName()
							.contains("consensus sequence")
							|| docs.get(cnt).getName().contains("Contig")) {
						extractIDfileName = docs.get(cnt).getName();
					} else if (result.toString().contains("dum")) {
						/* get AB1 filename */
						extractIDfileName = getExtractIDFromAB1FileName(docs
								.get(cnt).getName());
					}

					msgList.add(extractIDfileName);

					isSampleDoc = DocumentUtilities.getSelectedDocuments()
							.iterator().next().toString()
							.contains("ExtractIDCode_Seq");

					/** Create progress bar */
					limsFrameProgress.createProgressBar();

					/** Show the progress bar */
					limsFrameProgress.showProgress(extractIDfileName);

					readDataFromExcel(fileSelected, extractIDfileName,
							documents, cnt);

					importCounter = msgList.size();
				}

				logger.info("--------------------------------------------------------");
				logger.info("Total of document(s) updated: " + docs.size());
				limsFrameProgress.hideFrame();

				/* Set for creating dummy files */
				if (isSampleDoc) {
					limsFrameProgress.createProgressBar();
					setExtractIDFromSamplesSheet(fileSelected);
					limsFrameProgress.hideFrame();
				}

				logger.info("-------------------------- E N D --------------------------");
				logger.info("Done with updating the selected document(s). ");

				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						Dialogs.showMessageDialog("Sample-method: "
								+ Integer.toString(importTotal) + " out of "
								+ Integer.toString(docs.size())
								+ " documents are imported." + "\n"
								+ msgList.toString());
						logger.info("Sample-method: Total imported document(s): "
								+ msgList.toString());

						limsLogger.logToFile(logFileName,
								msgUitvalList.toString());

						msgList.clear();
						msgUitvalList.clear();
						verwerkingListCnt.clear();
						verwerkList.clear();
					}
				});

			}
		} else if (n == 1) {
			limsFrameProgress.createProgressBar();
			fileSelected = fcd.loadSelectedFile();
			setExtractIDFromSamplesSheet(fileSelected);
			limsFrameProgress.hideFrame();
		} else if (n == 2) {
			System.out.println("Optie 2");
			return;
		}
	}

	private void setSamplesNotes(AnnotatedPluginDocument[] documents, int cnt) {

		/** set note for Registration number */
		limsNotes.setNoteToAB1FileName(documents,
				"RegistrationNumberCode_Samples", "Registr-nmbr (Samples)",
				"Registr-nmbr (Samples)",
				limsExcelFields.getRegistrationNumber(), cnt);

		/** set note for Taxonname */
		limsNotes.setNoteToAB1FileName(documents, "TaxonName2Code_Samples",
				"[Scientific name] (Samples)", "[Scientific name] (Samples)",
				limsExcelFields.getTaxonNaam(), cnt);

		/** set note for Project Plate number */
		limsNotes.setNoteToAB1FileName(documents,
				"ProjectPlateNumberCode_Samples", "Sample plate ID (Samples)",
				"Sample plate ID (Samples)",
				limsExcelFields.getProjectPlaatNummer(), cnt);

		/** Set note for Extract plate number */
		limsNotes.setNoteToAB1FileName(documents,
				"ExtractPlateNumberCode_Samples", "Extract plate ID (Samples)",
				"Extract plate ID (Samples)",
				limsExcelFields.getExtractPlaatNummer(), cnt);

		/** set note for Plate position */
		limsNotes.setNoteToAB1FileName(documents, "PlatePositionCode_Samples",
				"Position (Samples)", "Position (Samples)",
				limsExcelFields.getPlaatPositie(), cnt);

		/** set note for Extract-ID */
		limsNotes.setNoteToAB1FileName(documents, "ExtractIDCode_Samples",
				"Extract ID (Samples)", "Extract ID (Samples)",
				limsExcelFields.getExtractID(), cnt);

		/** set note for Sample method */
		limsNotes.setNoteToAB1FileName(documents, "SampleMethodCode_Samples",
				"Extraction method (Samples)", "Extraction method (Samples)",
				limsExcelFields.getSubSample(), cnt);

		limsNotes.setNoteToAB1FileName(documents, "DocumentVersionCode_Seq",
				"Document version", "Document version",
				String.valueOf(limsExcelFields.getVersieNummer()), cnt);

		/** SequencingStaffCode_FixedValue */
		try {
			limsNotes
					.setNoteToAB1FileName(documents,
							"SequencingStaffCode_FixedValue_Samples",
							"Seq-staff (Samples)", "Seq-staff (Samples)",
							limsImporterUtil
									.getPropValues("samplessequencestaff"), cnt);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public GeneiousActionOptions getActionOptions() {
		return new GeneiousActionOptions("1 of 2 Samples").setInPopupMenu(true)
				.setMainMenuLocation(GeneiousActionOptions.MainMenu.Tools, 1.0)
				.setInMainToolbar(true).setInPopupMenu(true)
				.setAvailableToWorkflows(true);
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public DocumentSelectionSignature[] getSelectionSignatures() {
		return new DocumentSelectionSignature[] { new DocumentSelectionSignature(
				PluginDocument.class, 0, Integer.MAX_VALUE) };
	}

	/*
	 * Create dummy files for samples when there is no match with records in the
	 * database
	 */
	private void setExtractIDFromSamplesSheet(String fileName) {
		try {
			if (fileName != null) {
				logger.info("Read samples file: " + fileName);
				CSVReader csvReader = new CSVReader(new FileReader(fileName),
						'\t', '\'', 0);
				csvReader.readNext();

				int cnt = 0;
				try {
					while ((record = csvReader.readNext()) != null) {
						if (record.length == 0) {
							continue;
						}

						String dummyFile = ReadGeneiousFieldsValues
								.getFastaIDForSamples_GeneiousDB(ID);

						limsFrameProgress.showProgress(dummyFile);

						ID = "e" + record[3];

						if (record[2].length() > 0 && record[2].contains("-")) {
							plateNumber = record[2].substring(0,
									record[2].indexOf("-"));
						}

						if (dummyFile.trim() != "") {
							dummyFile = getExtractIDFromAB1FileName(dummyFile);
						}

						if (!dummyFile.equals(ID)) {
							limsDummySeq.createDummySampleSequence(ID, ID,
									record[0], plateNumber, record[5],
									record[4], record[1]);
							cnt++;
						}

					} // end While
					if (cnt > 0) {
						Dialogs.showMessageDialog("Done creating:" + cnt
								+ " Dummy Samples");
					} else {
						Dialogs.showMessageDialog(cnt
								+ "(zero). No dummy Samples record(s) has been added.");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					csvReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readDataFromExcel(String fileName, String extractID,
			AnnotatedPluginDocument[] documents, int cnt) {

		int counter = 0;
		int cntVerwerkt = 0;

		logger.info("CSV file: " + fileName);
		/** Start reading data from the file selected */
		logger.info("-------------------------- S T A R T --------------------------");
		logger.info("Start Reading data from a samples file.");

		try {
			CSVReader csvReader = new CSVReader(new FileReader(fileName), '\t',
					'\'', 0);
			csvReader.readNext();

			try {
				msgUitvalList
						.add("-----------------------------------------------"
								+ "\n");
				msgUitvalList.add("Ab1 filename: " + docs.get(cnt).getName()
						+ "\n");

				limsFrameProgress.showProgress(docs.get(cnt).getName());

				while ((record = csvReader.readNext()) != null) {
					if (record.length == 0) {
						continue;
					}

					ID = "e" + record[3];

					if (record[2].length() > 0 && record[2].contains("-")) {
						plateNumber = record[2].substring(0,
								record[2].indexOf("-"));
					}

					if (ID.equals(extractID)) {

						limsExcelFields.setProjectPlaatNummer(record[0]);
						limsExcelFields.setPlaatPositie(record[1]);
						limsExcelFields.setExtractPlaatNummer(plateNumber);
						if (record[3] != null) {
							limsExcelFields.setExtractID(ID);
						}
						limsExcelFields.setRegistrationNumber(record[4]);
						limsExcelFields.setTaxonNaam(record[5]);

						logger.info("Extract-ID: "
								+ limsExcelFields.getExtractID());
						logger.info("Project plaatnummer: "
								+ limsExcelFields.getProjectPlaatNummer());
						logger.info("Extract plaatnummer: "
								+ limsExcelFields.getExtractPlaatNummer());
						logger.info("Taxon naam: "
								+ limsExcelFields.getTaxonNaam());
						logger.info("Registrationnumber: "
								+ limsExcelFields.getRegistrationNumber());
						logger.info("Plaat positie: "
								+ limsExcelFields.getPlaatPositie());
						logger.info("Sample method: "
								+ limsExcelFields.getSubSample());

						limsExcelFields.setVersieNummer(version);

						logger.info("Start with adding notes to the document");
						setSamplesNotes(documents, cnt);
						logger.info("Done with adding notes to the document");
						counter--;
						cntVerwerkt++;
						verwerkingListCnt.add(Integer.toString(cntVerwerkt));
						verwerkList.add(ID);

					} // end IF

					if (!verwerkList.contains(ID)) {
						limsExcelFields.setProjectPlaatNummer("");
						limsExcelFields.setPlaatPositie("");
						limsExcelFields.setExtractPlaatNummer("");
						if (record[3] != null) {
							limsExcelFields.setExtractID("");
						}
						limsExcelFields.setRegistrationNumber("");
						limsExcelFields.setTaxonNaam("");
						msgUitvalList.add("Record ExtractID: " + record[3]
								+ "\n");
					}
					counter++;
				} // end While
				importTotal = counter;
				counter = counter - verwerkingListCnt.size();
				msgUitvalList.add("Total records: " + Integer.toString(counter)
						+ "\n");

			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extract the ID from the filename
	 * 
	 * @param annotatedPluginDocuments
	 *            set the param
	 * @return
	 */
	private String getExtractIDFromAB1FileName(String fileName) {
		/* for example: e4010125015_Sil_tri_MJ243_COI-A01_M13F_A01_008.ab1 */
		String[] underscore = null;
		logger.info("Document Filename: " + fileName);
		if (fileName.contains("_") && fileName.contains("ab1")) {
			underscore = StringUtils.split(fileName, "_");
		} else if (fileName.contains("_") && !fileName.contains(".")) {
			underscore = StringUtils.split(fileName, "_");
		} else if (fileName.contains(".") && fileName.contains("dum")) {
			underscore = StringUtils.split(fileName, ".");
		} else {
			throw new IllegalArgumentException("String " + fileName
					+ " cannot be split. ");
		}
		return underscore[0];
	}

	@SuppressWarnings("unused")
	private boolean matchExtractId(
			AnnotatedPluginDocument annotatedPluginDocument, String extractID) {

		Object fieldValue = ReadGeneiousFieldsValues
				.readValueFromAnnotatedPluginDocument(annotatedPluginDocument,
						noteCode, fieldName);
		if (extractID.equals(fieldValue)) {
			return true;
		}
		return false;
	}
}
