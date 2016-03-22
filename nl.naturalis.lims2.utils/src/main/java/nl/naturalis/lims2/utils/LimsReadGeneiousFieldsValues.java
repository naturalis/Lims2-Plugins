/**
 * 
 */
package nl.naturalis.lims2.utils;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biomatters.geneious.publicapi.components.Dialogs;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.documents.DocumentNote;
import com.biomatters.geneious.publicapi.documents.DocumentNoteType;
import com.biomatters.geneious.publicapi.documents.DocumentNoteUtilities;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceDocument;
import com.biomatters.geneious.publicapi.implementations.sequence.DefaultNucleotideSequence;

/**
 * @author Reinier.Kartowikromo
 *
 */
public class LimsReadGeneiousFieldsValues {
	private String url = "";
	private String user = "";
	private String password = "";
	private static final Logger logger = LoggerFactory
			.getLogger(LimsReadGeneiousFieldsValues.class);
	private LimsImporterUtil limsImporterUtil = new LimsImporterUtil();
	private String logFileName = "";
	private LimsLogList limsLogList = new LimsLogList();
	private String dummyPcrPlateIdSeqValue = "";
	private String dummyMarkerSeqValue = "";
	private String dummyRegistrNmbrSamplesValue = "";
	private String dummyScientificNameSamplesValue = "";
	private String dummySamplePlateIdSamplesValue = "";
	private String dummyPositionSamplesValue = "";
	private String dummyExtractIDSamplesValue = "";
	private String dummySeqStaffSamplesValue = "";
	private List<String> listDummyValues = new ArrayList<String>();

	public LimsReadGeneiousFieldsValues() {

	}

	public Object readValueFromAnnotatedPluginDocument(
			AnnotatedPluginDocument annotatedPluginDocuments, String noteCode,
			Object fieldName) {

		/** noteCode = "DocumentNoteUtilities-Registration number"; */
		DocumentNoteType noteType = DocumentNoteUtilities.getNoteType(noteCode);
		Object fieldValue = null;

		if (noteType != null) {
			AnnotatedPluginDocument.DocumentNotes documentNotes = annotatedPluginDocuments
					.getDocumentNotes(true);

			DocumentNote bos = documentNotes.getNote(noteCode);
			/** example: FieldName = "BasisOfRecordCode" */
			if (fieldName != null) {
				fieldValue = bos.getFieldValue((String) fieldName);
			} else {
				fieldValue = null;
			}
		}

		return fieldValue;
	}

	public Object object(AnnotatedPluginDocument annotatedPluginDocument) {

		/** noteCode = "DocumentNoteUtilities-Version number"; */
		DocumentNoteType noteType = DocumentNoteUtilities
				.getNoteType("DocumentNoteUtilities-VersieCode");
		Object fieldValue = null;

		if (noteType != null) {
			AnnotatedPluginDocument.DocumentNotes documentNotes = annotatedPluginDocument
					.getDocumentNotes(true);

			DocumentNote bos = documentNotes
					.getNote("DocumentNoteUtilities-VersieCode");
			/** example: FieldName = "Version number" */
			fieldValue = bos.getFieldValue("Version number");
		}

		return fieldValue;
	}

	public String getRegistrationNumberFromTableAnnotatedDocument(
			Object filename, String xmlNotesName, String xmlNotesName1)
			throws IOException {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		url = limsImporterUtil.getPropValues("url");
		user = limsImporterUtil.getPropValues("user");
		password = limsImporterUtil.getPropValues("password");

		String result = "";

		try {

			final String SQL = " SELECT a.registrationnumber, a.name"
					+ " FROM " + " ( "
					+ " SELECT TRIM(EXTRACTVALUE(document_xml,  ' "
					+ xmlNotesName + " ')) AS registrationnumber, "
					+ " TRIM(EXTRACTVALUE(document_xml,  ' " + xmlNotesName1
					+ " ')) AS name " + " FROM annotated_document" + " ) AS a "
					+ " WHERE a.name =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, (String) filename);
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				logger.debug("Annotated document id : " + result);
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
		return result;
	}

	public String getFileNameFromGeneiousDatabase(String filename) {

		boolean truefalse = false;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			url = limsImporterUtil.getPropValues("url");
			user = limsImporterUtil.getPropValues("user");
			password = limsImporterUtil.getPropValues("password");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = "";

		try {

			final String SQL = " SELECT a.name"
					+ " FROM "
					+ " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(plugin_document_xml, '//ABIDocument/name')) AS name "
					+ " FROM annotated_document" + " ) AS a "
					+ " WHERE a.name =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, filename);
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				if (rs.wasNull())
					truefalse = false;
				else
					truefalse = true;

				logger.debug("Filename: " + result
						+ " already exists in the geneious database.");
				limsLogList.UitvalList.add("Filename: " + result
						+ " already exists in the geneious database." + "\n");
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
		return result;
	}

	public boolean checkOfFastaOrAB1Exists(String fileName, String fieldName,
			String xmlnotes) {
		boolean truefalse = false;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			url = limsImporterUtil.getPropValues("url");
			user = limsImporterUtil.getPropValues("user");
			password = limsImporterUtil.getPropValues("password");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = "";

		try {

			final String SQL = " SELECT a.name" + " FROM " + " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(" + fieldName + ", ' "
					+ xmlnotes + " ')) AS name " + " FROM annotated_document"
					+ " ) AS a " + " WHERE a.name =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, fileName);
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				if (rs.wasNull())
					truefalse = false;
				else
					truefalse = true;

				logger.debug("Filename: " + result
						+ " already exists in the geneious database.");
				limsLogList.UitvalList.add("Filename: " + result
						+ " already exists in the geneious database." + "\n");
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
		return truefalse;
	}

	public boolean fileNameExistsInGeneiousDatabase(String filename) {

		boolean truefalse = false;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			url = limsImporterUtil.getPropValues("url");
			user = limsImporterUtil.getPropValues("user");
			password = limsImporterUtil.getPropValues("password");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = "";

		try {

			final String SQL = " SELECT a.name"
					+ " FROM "
					+ " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(plugin_document_xml, '//ABIDocument/name')) AS name "
					+ " FROM annotated_document" + " ) AS a "
					+ " WHERE a.name =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, filename);
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				if (rs.wasNull())
					truefalse = false;
				else
					truefalse = true;

				logger.debug("Filename: " + result
						+ " already exists in the geneious database.");
				limsLogList.UitvalList.add("Filename: " + result
						+ " already exists in the geneious database." + "\n");
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
		return truefalse;
	}

	// this utility method returns a SequenceDocument based on the given name
	// , if they match the search parameters
	// or null if there is no match
	private SequenceDocument match(ArrayList<String> namesToMatch, String name,
			boolean matchBoth) {
		boolean nameMatch = false;
		if (namesToMatch.size() > 0) {
			for (String nameToMatch : namesToMatch) {
				if (name.toUpperCase().contains(nameToMatch)) {
					nameMatch = true;
				}
			}
		}

		boolean match;
		if (matchBoth) {
			match = nameMatch;
		} else {
			match = nameMatch;
		}
		if (match) {
			return new DefaultNucleotideSequence(name.substring(0,
					name.indexOf(" ")), name, "NNNNNNNNNN", new Date());
		}
		return null;
	}

	private void createLogFile(String fileName, List<String> list) {
		logFileName = limsImporterUtil.getLogPath() + File.separator + fileName
				+ limsImporterUtil.getLogFilename();
		File logfile = new File(logFileName);
		if (!logfile.exists()) {
			LimsLogger limsLogger = new LimsLogger(logFileName);
			limsLogger.logToFile(logFileName, list.toString());
		}
	}

	public boolean getExtractIDFromSamples_GeneiousDB(String extractid)
			throws IOException {

		boolean truefalse = false;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		url = limsImporterUtil.getPropValues("url");
		user = limsImporterUtil.getPropValues("user");
		password = limsImporterUtil.getPropValues("password");

		String result = "";

		try {

			final String SQL = " SELECT a.name"
					+ " FROM "
					+ " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(plugin_document_xml, '//ABIDocument/name')) AS name "
					+ " FROM annotated_document" + " ) AS a "
					+ " WHERE a.name like ?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			pst = con.prepareStatement(SQL);
			pst.setString(1, "%" + extractid + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				logger.info("Extract ID: " + result
						+ " add to the geneious database.");

				if (rs.wasNull())
					truefalse = false;
				else
					truefalse = true;
			}

		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}

		return truefalse;
	}

	public String getFastaIDForSamples_GeneiousDB(String extractid)
			throws IOException {

		boolean truefalse = false;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		url = limsImporterUtil.getPropValues("url");
		user = limsImporterUtil.getPropValues("user");
		password = limsImporterUtil.getPropValues("password");

		String result = "";

		try {

			final String SQL = " SELECT a.name"
					+ " FROM "
					+ " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(plugin_document_xml, '//XMLSerialisableRootElement/name')) AS name "
					+ " FROM annotated_document" + " ) AS a "
					+ " WHERE a.name like ?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			pst = con.prepareStatement(SQL);
			pst.setString(1, "%" + extractid + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				logger.info("Extract ID: " + result
						+ " already exsist in the geneious database.");

				if (rs.wasNull())
					truefalse = false;
				else
					truefalse = true;
			}

		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}

		return result;
	}

	/**
	 * Cachename opvragen uit xml veld document_xml tabel annotated_document
	 * //document/hiddenFields/override_cache_name
	 * */
	public String getCacheNameFromGeneiousDatabase(Object filename,
			String xmlNotesName) throws IOException {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		url = limsImporterUtil.getPropValues("url");
		user = limsImporterUtil.getPropValues("user");
		password = limsImporterUtil.getPropValues("password");

		String result = "";

		try {

			final String SQL = " SELECT a.name" + " FROM " + " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(document_xml,  ' "
					+ xmlNotesName + " ')) AS name "
					+ " FROM annotated_document" + " ) AS a "
					+ " WHERE a.name =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, (String) filename);
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				logger.debug("Filename: " + result
						+ " already exists in the geneious database.");
				limsLogList.UitvalList.add("Filename: " + result
						+ " already exists in the geneious database." + "\n");
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
		return result;
	}

	public String getIDFromTableAnnotatedDocument(Object filename,
			String xmlNotesName) throws IOException {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		url = limsImporterUtil.getPropValues("url");
		user = limsImporterUtil.getPropValues("user");
		password = limsImporterUtil.getPropValues("password");

		String result = "";

		try {

			final String SQL = " SELECT a.id, a.name" + " FROM " + " ( "
					+ " SELECT id as ID, TRIM(EXTRACTVALUE(document_xml,  ' "
					+ xmlNotesName + " ')) AS name "
					+ " FROM annotated_document" + " ) AS a "
					+ " WHERE a.name =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, (String) filename);
			rs = pst.executeQuery();
			while (rs.next()) {
				result = rs.getObject(1).toString();
				logger.debug("Annotated document id : " + result);
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
		return result;
	}

	public void DeleteDummyRecordFromTableAnnotatedtDocument(Object ID)
			throws IOException {

		Connection con = null;
		PreparedStatement pst = null;

		url = limsImporterUtil.getPropValues("url");
		user = limsImporterUtil.getPropValues("user");
		password = limsImporterUtil.getPropValues("password");

		try {

			final String SQL = " DELETE FROM annotated_document"
					+ " WHERE id =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, (String) ID);
			pst.executeUpdate();
			logger.debug("Delete Dummy Annotated document id : " + ID
					+ "From tabel annotated_document ");
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
	}

	public int getLastVersionFromDocument(String fileName) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			url = limsImporterUtil.getPropValues("url");
			user = limsImporterUtil.getPropValues("user");
			password = limsImporterUtil.getPropValues("password");
		} catch (IOException e) {
			e.printStackTrace();
		}

		int result = 0;

		try {

			final String SQL = " SELECT MAX(a.version) as version, a.name "
					+ " FROM "
					+ " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/DocumentVersionCode_Seq')) AS version, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/hiddenFields/cache_name')) AS name "
					+ " FROM annotated_document) AS a " + " WHERE a.name =?";
			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, (String) fileName);
			rs = pst.executeQuery();
			if (rs.next()) {
				do {
					result = rs.getInt(1);
					logger.debug("Versionnumber : " + result);
				} while (rs.next());
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}

		return result;

	}

	public int getDocumentVersion(String fileName) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			url = limsImporterUtil.getPropValues("url");
			user = limsImporterUtil.getPropValues("user");
			password = limsImporterUtil.getPropValues("password");
		} catch (IOException e) {
			e.printStackTrace();
		}

		int result = 0;

		try {

			final String SQL = " SELECT a.version, a.name "
					+ " FROM "
					+ " ( "
					+ " SELECT	TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/DocumentVersionCode_Seq')) AS version, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/hiddenFields/cache_name')) AS name "
					+ " FROM annotated_document) AS a " + " WHERE a.name =?";
			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, (String) fileName);
			rs = pst.executeQuery();
			if (rs.next()) {
				do {
					result = rs.getInt(1);
					logger.debug("Versionnumber : " + result);
				} while (rs.next());
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}

		return result;

	}

	public String getDummyPcrPlateIdSeqValue() {
		return dummyPcrPlateIdSeqValue;
	}

	public void setDummyPcrPlateIdSeqValue(String dummyPcrPlateIdSeqValue) {
		this.dummyPcrPlateIdSeqValue = dummyPcrPlateIdSeqValue;
	}

	public String getDummyMarkerSeqValue() {
		return dummyMarkerSeqValue;
	}

	public void setDummyMarkerSeqValue(String dummyMarkerSeqValue) {
		this.dummyMarkerSeqValue = dummyMarkerSeqValue;
	}

	public String getDummyRegistrNmbrSamplesValue() {
		return dummyRegistrNmbrSamplesValue;
	}

	public void setDummyRegistrNmbrSamplesValue(
			String dummyRegistrNmbrSamplesValue) {
		this.dummyRegistrNmbrSamplesValue = dummyRegistrNmbrSamplesValue;
	}

	public String getDummyScientificNameSamplesValue() {
		return dummyScientificNameSamplesValue;
	}

	public void setDummyScientificNameSamplesValue(
			String dummyScientificNameSamplesValue) {
		this.dummyScientificNameSamplesValue = dummyScientificNameSamplesValue;
	}

	public String getDummySamplePlateIdSamplesValue() {
		return dummySamplePlateIdSamplesValue;
	}

	public void setDummySamplePlateIdSamplesValue(
			String dummySamplePlateIdSamplesValue) {
		this.dummySamplePlateIdSamplesValue = dummySamplePlateIdSamplesValue;
	}

	public String getDummyPositionSamplesValue() {
		return dummyPositionSamplesValue;
	}

	public void setDummyPositionSamplesValue(String dummyPositionSamplesValue) {
		this.dummyPositionSamplesValue = dummyPositionSamplesValue;
	}

	public String getDummyExtractIDSamplesValue() {
		return dummyExtractIDSamplesValue;
	}

	public void setDummyExtractIDSamplesValue(String dummyExtractIDSamplesValue) {
		this.dummyExtractIDSamplesValue = dummyExtractIDSamplesValue;
	}

	public String getDummySeqStaffSamplesValue() {
		return dummySeqStaffSamplesValue;
	}

	public void setDummySeqStaffSamplesValue(String dummySeqStaffSamplesValue) {
		this.dummySeqStaffSamplesValue = dummySeqStaffSamplesValue;
	}

	public List<String> getDummySamplesValues(Object filename) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			url = limsImporterUtil.getPropValues("url");
			user = limsImporterUtil.getPropValues("user");
			password = limsImporterUtil.getPropValues("password");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = "";

		try {

			final String SQL = " SELECT a.name, a.pcrplateid, a.marker, a.Registrationnumber, a.ScientificName, "
					+ " a.SamplePlateId, a.Position, a.ExtractID, a.Seqstaff "
					+ " FROM( "
					+ " SELECT "
					+ " TRIM(EXTRACTVALUE(document_xml,  '//document/hiddenFields/cache_name')) AS name, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/PCRplateIDCode_Seq')) AS pcrplateid, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/MarkerCode_Seq')) AS marker, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/RegistrationNumberCode_Samples')) AS registrationnumber, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/TaxonName2Code_Samples')) AS scientificName, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/ProjectPlateNumberCode_Samples')) AS samplePlateId, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/PlatePositionCode_Samples')) AS position, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/ExtractIDCode_Samples')) AS extractID, "
					+ " TRIM(EXTRACTVALUE(document_xml, '//document/notes/note/SequencingStaffCode_FixedValue_Samples')) AS seqStaff "
					+ " FROM annotated_document) AS a " + " WHERE a.name =?";

			con = DriverManager.getConnection(url, user, password);
			con.clearWarnings();
			logger.debug("User:" + user);
			logger.debug("Password:" + password);
			pst = con.prepareStatement(SQL);
			pst.setString(1, (String) filename);
			rs = pst.executeQuery();
			ResultSetMetaData metadata = rs.getMetaData();
			int numberOfColumns = metadata.getColumnCount();
			while (rs.next()) {
				int i = 1;
				while (i <= numberOfColumns) {
					listDummyValues.add(rs.getString(i++));
					logger.debug("Annotated document id : " + result);
				}
			}
		} catch (SQLException ex) {
			logger.info(ex.getMessage(), ex);

			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					Dialogs.showMessageDialog("Username or Password is not correct: "
							+ "User: " + user + " Password: " + password);
				}
			});

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.warn(ex.getMessage(), ex);
			}
		}
		return listDummyValues;
	}

}
