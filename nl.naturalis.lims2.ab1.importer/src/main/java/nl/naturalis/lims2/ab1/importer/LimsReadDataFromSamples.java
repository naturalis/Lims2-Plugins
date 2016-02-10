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
import com.biomatters.geneious.publicapi.documents.sequence.SequenceDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentAction;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.biomatters.geneious.publicapi.plugin.GeneiousService;
import com.biomatters.geneious.publicapi.plugin.PluginUtilities;
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
	private SequenceDocument seq;
	private List<String> msgList = new ArrayList<String>();
	private List<String> msgUitvalList = new ArrayList<String>();
	private List<String> verwerkingListCnt = new ArrayList<String>();
	private List<String> verwerkList = new ArrayList<String>();
	private static final Logger logger = LoggerFactory
			.getLogger(LimsReadDataFromSamples.class);

	public int importCounter;
	private int importTotal;
	private String[] record = null;
	private String ID = "";

	String logFileName = limsImporterUtil.getLogPath() + File.separator
			+ "Sample-method-Uitvallijst-" + limsImporterUtil.getLogFilename();

	LimsLogger limsLogger = new LimsLogger(logFileName);

	@Override
	public void actionPerformed(
			AnnotatedPluginDocument[] annotatedPluginDocuments) {

		List<GeneiousService> list = new ArrayList<GeneiousService>();
		list = PluginUtilities.getGeneiousServices();
		System.out.println(list.toString());
		logger.info(list.toString());

		if (DocumentUtilities.getSelectedDocuments().isEmpty()) {
			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Select all documents");
					return;
				}
			});
		}

		if (!DocumentUtilities.getSelectedDocuments().isEmpty()) {

			logger.info("Start updating selected document(s).");

			String fileSelected = fcd.loadSelectedFile();
			System.out.println("OK");

			// Options option = new Options(getClass());
			// option.addDocumentSelectionOption("Test", "Selected", null,
			// documentType.NUCLEOTIDE_SEQUENCE_TYPE, displayFields, true,
			// docs);

			// if (DocumentUtilities.getSelectedDocuments().isEmpty()) {
			try {
				/** Add selected documents to a list. */

				docs = DocumentUtilities.getSelectedDocuments();
				if (fileSelected == null) {
					return;
				}

				msgUitvalList.add("Filename: " + fileSelected + "\n");

				for (int cnt = 0; cnt < docs.size(); cnt++) {

					logger.info("-------------------------- S T A R T --------------------------");
					logger.info("Start Reading data from a samples file.");

					seq = (SequenceDocument) docs.get(cnt).getDocument();
					extractIDfileName = getExtractIDFromAB1FileName(seq
							.getName());

					msgList.add(seq.getName());

					readDataFromExcel(fileSelected);

					/** set note for Registration number */
					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"RegistrationNumberCode_Samples",
							"Registration number (Samples)",
							"Registration number (Samples)",
							limsExcelFields.getRegistrationNumber(), cnt);

					/** set note for Taxonname */
					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"TaxonName2Code_Samples",
							"Scientific name 2 (Samples)",
							"Scientific name 2 (Samples)",
							limsExcelFields.getTaxonNaam(), cnt);

					/** set note for Project Plate number */
					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"ProjectPlateNumberCode_Samples",
							"Project plate number (Samples)",
							"Project plate number (Samples)",
							limsExcelFields.getProjectPlaatNummer(), cnt);

					/** Set note for Extract plate number */
					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"ExtractPlateNumberCode_Samples",
							"Extract plate number (Samples)",
							"Extract plate number (Samples)",
							limsExcelFields.getExtractPlaatNummer(), cnt);

					/** set note for Plate position */
					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"PlatePositionCode_Samples",
							"Plate position (Samples)",
							"Plate position (Samples)",
							limsExcelFields.getPlaatPositie(), cnt);

					/** set note for Extract-ID */
					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"ExtractIDCode_Samples", "Extract ID (Samples)",
							"Extract ID (Samples)",
							limsExcelFields.getExtractID(), cnt);

					/** set note for Sample method */
					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"SampleMethodCode_Samples",
							"Sample method (Samples)",
							"Sample method (Samples)",
							limsExcelFields.getSubSample(), cnt);

					limsNotes.setNoteToAB1FileName(annotatedPluginDocuments,
							"DocumentVersionCode", "Document version",
							"Document version",
							limsExcelFields.getVersieNummer(), cnt);

					logger.info("Done with adding notes to the document");
					importCounter = msgList.size();
				}
			} catch (DocumentOperationException e) {
				e.printStackTrace();
			}
			logger.info("--------------------------------------------------------");
			logger.info("Total of document(s) updated: " + docs.size());

			/* Set for creating dummy files */
			setExtractIDFromSamplesSheet(fileSelected);

			// }

			logger.info("-------------------------- E N D --------------------------");
			logger.info("Done with updating the selected document(s). ");

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Sample-method: "
							+ Integer.toString(docs.size()) + " out of "
							+ Integer.toString(importTotal)
							+ " documents are imported." + "\n"
							+ msgList.toString());
					logger.info("Sample-method: Total imported document(s): "
							+ msgList.toString());

					limsLogger.logToFile(logFileName, msgUitvalList.toString());

					msgList.clear();
					msgUitvalList.clear();
					verwerkingListCnt.clear();
					verwerkList.clear();
				}
			});

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
			logger.info("Read samples file: " + fileName);
			CSVReader csvReader = new CSVReader(new FileReader(fileName), '\t',
					'\'', 0);
			csvReader.readNext();

			try {
				while ((record = csvReader.readNext()) != null) {
					if (record.length == 0) {
						continue;
					}

					ID = "e" + record[3];
					if (!ReadGeneiousFieldsValues
							.getExtractIDFromSamples_GeneiousDB(ID)) {

						limsDummySeq.createDummySampleSequence(ID, ID,
								record[0], record[2], record[5], record[4],
								record[1]);
					}

				} // end While
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

	private void readDataFromExcel(String fileName) {

		int counter = 0;
		int cntVerwerkt = 0;

		logger.info("CSV file: " + fileName);

		logger.info("Start with adding notes to the document");
		try {
			CSVReader csvReader = new CSVReader(new FileReader(fileName), '\t',
					'\'', 0);
			csvReader.readNext();

			try {
				msgUitvalList
						.add("-----------------------------------------------"
								+ "\n");
				msgUitvalList.add("Ab1 filename: " + seq.getName() + "\n");

				while ((record = csvReader.readNext()) != null) {
					if (record.length == 0) {
						continue;
					}

					ID = "e" + record[3];

					if (ID.equals(extractIDfileName)) {
						limsExcelFields.setProjectPlaatNummer(record[0]);
						limsExcelFields.setPlaatPositie(record[1]);
						limsExcelFields.setExtractPlaatNummer(record[2]);
						if (record[3] != null) {
							limsExcelFields.setExtractID(ID);
						}
						limsExcelFields.setRegistrationNumber(record[4]);
						limsExcelFields.setTaxonNaam(record[5]);
						// limsExcelFields.setSubSample(record[0]);

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
						counter--;
						cntVerwerkt++;
						verwerkingListCnt.add(Integer.toString(cntVerwerkt));
						verwerkList.add(ID);

					} // end IF

					if (!verwerkList.contains(ID)) {
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
		logger.info("Document Filename: " + fileName);
		String[] underscore = StringUtils.split(fileName, "_");
		return underscore[0];
	}
}