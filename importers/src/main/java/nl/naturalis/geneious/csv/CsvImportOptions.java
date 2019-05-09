package nl.naturalis.geneious.csv;

import static com.biomatters.geneious.publicapi.components.Dialogs.showMessageDialog;
import static nl.naturalis.geneious.ErrorCode.CSV_NO_FILE_PROVIDED;
import static nl.naturalis.geneious.ErrorCode.CSV_UNSUPPORTED_FILE_TYPE;
import static org.apache.commons.io.FilenameUtils.getExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.biomatters.geneious.publicapi.components.Dialogs.DialogIcon;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.plugin.Options;
import com.biomatters.geneious.publicapi.utilities.GuiUtilities;
import com.google.common.collect.ImmutableSet;

import nl.naturalis.geneious.ErrorCode;
import nl.naturalis.geneious.MessageProvider;
import nl.naturalis.geneious.MessageProvider.Message;
import nl.naturalis.geneious.util.SharedPreconditionValidator;

public abstract class CsvImportOptions<T extends Enum<T>, U extends CsvImportConfig<T>> extends Options {

  private static final String FILE = "nl.naturalis.geneious.%s.file";
  private static final String LINES_TO_SKIP = "nl.naturalis.geneious.%s.skip";
  private static final String DELIMITER = "nl.naturalis.geneious.%s.delim";
  private static final String SHEET_NAME = "nl.naturalis.geneious.%s.sheet";

  private static final OptionValue OPT_NOT_APPLICABLE = new OptionValue("0", "  n/a  ");

  private static final OptionValue DELIM_INIT = new OptionValue("0", "  --- csv/tsv/txt ---  ");
  private static final OptionValue SHEET_INIT = new OptionValue("0", "  --- spreadsheet ---  ");

  private static final List<OptionValue> DELIM_OPTIONS = Arrays.asList(
      new OptionValue("\t", "  tab  "),
      new OptionValue(",", "  comma  "),
      new OptionValue(";", "  semi-colon  "),
      new OptionValue("|", "  pipe  "));

  private static final Set<String> ALLOWED_FILE_TYPES = ImmutableSet.of("csv", "tsv", "txt", "xls");

  protected final String identifier;
  protected final List<AnnotatedPluginDocument> documents;
  protected final FileSelectionOption file;
  protected final IntegerOption linesToSkip;
  protected final ComboBoxOption<OptionValue> delimiter;
  protected final ComboBoxOption<OptionValue> sheet; // Name of sheet (tab) within the spreadsheet

  public CsvImportOptions(List<AnnotatedPluginDocument> documents, String identifier) {
    this.documents = documents;
    this.identifier = identifier;
    this.file = addFileSelectionOption();
    this.linesToSkip = addLinesToSkipOption();
    this.delimiter = addDelimiterOption();
    this.sheet = addSheetNameOption();
    file.addChangeListener(this::fileChanged);
  }

  @Override
  public String verifyOptionsAreValid() {
    String msg = super.verifyOptionsAreValid();
    if (msg != null) {
      return msg;
    }
    SharedPreconditionValidator validator = new SharedPreconditionValidator(documents);
    Message message = validator.validate();
    if (message.getCode() != ErrorCode.OK) {
      return message.getMessage();
    }
    if (StringUtils.isBlank(file.getValue())) {
      return MessageProvider.get(CSV_NO_FILE_PROVIDED);
    }
    String ext = getExtension(file.getValue());
    if (!ALLOWED_FILE_TYPES.contains(ext.toLowerCase())) {
      return MessageProvider.get(CSV_UNSUPPORTED_FILE_TYPE, ext);
    }
    return null; // Signals to Geneious it can continue
  }

  public abstract U createImportConfig();

  protected final U initializeStandardOptions(U cfg) {
    cfg.setSelectedDocuments(documents);
    cfg.setFile(new File(file.getValue()));
    cfg.setSkipLines(linesToSkip.getValue());
    cfg.setDelimiter(delimiter.getValue().getName());
    cfg.setSheetNumber(Integer.parseInt(sheet.getValue().getName()));
    return cfg;
  }

  protected String getDefaultFileSelectionLabel() {
    return "File";
  }

  protected int getDefaultNumLinesToSkip() {
    return 1;
  }

  private FileSelectionOption addFileSelectionOption() {
    FileSelectionOption opt = addFileSelectionOption(
        name(FILE),
        getDefaultFileSelectionLabel(),
        "",
        new String[0],
        "Select",
        (x, y) -> ALLOWED_FILE_TYPES.contains(getExtension(y)));
    opt.setAllowMultipleSelection(false);
    opt.setFillHorizontalSpace(true);
    opt.setSelectionType(JFileChooser.FILES_ONLY);
    opt.setValue("");
    opt.setDescription("Select a sample sheet to import. Supported formats: *.csv *.tsv *.txt *.xls");
    return opt;
  }

  private IntegerOption addLinesToSkipOption() {
    String descr = "The number of lines to skip within the selected file";
    IntegerOption opt = addIntegerOption(name(LINES_TO_SKIP), "Lines to skip", getDefaultNumLinesToSkip(), 0, Integer.MAX_VALUE);
    opt.setDescription(descr);
    return opt;
  }

  private ComboBoxOption<OptionValue> addDelimiterOption() {
    ComboBoxOption<OptionValue> opt = addComboBoxOption(name(DELIMITER), "Field separator", Arrays.asList(DELIM_INIT), DELIM_INIT);
    opt.setDescription("The character used to separate values within a row");
    opt.setEnabled(false);
    return opt;
  }

  private ComboBoxOption<OptionValue> addSheetNameOption() {
    ComboBoxOption<OptionValue> opt = addComboBoxOption(name(SHEET_NAME), "Sheet name", Arrays.asList(SHEET_INIT), SHEET_INIT);
    opt.setFillHorizontalSpace(true);
    opt.setDescription("The name of the sheet (a.k.a. tab) within the spreadsheet.");
    opt.setEnabled(false);
    return opt;
  }

  private void fileChanged() {
    if (StringUtils.isBlank(file.getValue())) {
      /*
       * When a file has already been selected, and then you select another file, the change listener apparently fires twice. The first time the
       * file is empty again (useful if you want to do a System.exit in between or so). The second time you get the new file.
       */
      return;
    }
    sheet.setEnabled(false);
    delimiter.setEnabled(false);
    if (CsvImportUtil.isSpreadsheet(file.getValue())) {
      loadSheetNames();
      sheet.setEnabled(true);
      delimiter.setPossibleValues(Arrays.asList(OPT_NOT_APPLICABLE));
      delimiter.setDefaultValue(OPT_NOT_APPLICABLE);
    } else if (CsvImportUtil.isCsvFile(file.getValue())) {
      delimiter.setPossibleValues(DELIM_OPTIONS);
      delimiter.setDefaultValue(DELIM_OPTIONS.get(0));
      delimiter.setEnabled(true);
      sheet.setPossibleValues(Arrays.asList(OPT_NOT_APPLICABLE));
      sheet.setDefaultValue(OPT_NOT_APPLICABLE);
    } else {
      sheet.setPossibleValues(Arrays.asList(SHEET_INIT));
      sheet.setDefaultValue(SHEET_INIT);
      delimiter.setPossibleValues(Arrays.asList(DELIM_INIT));
      delimiter.setDefaultValue(DELIM_INIT);
      String title = "Unsupported file type";
      StringBuilder sb = new StringBuilder(32);
      sb.append(title);
      String ext = getExtension(file.getValue());
      if (StringUtils.isNotBlank(ext)) {
        sb.append(": *.").append(ext);
      }
      showMessageDialog(sb.toString(), title, GuiUtilities.getMainFrame(), DialogIcon.ERROR);
    }
  }

  private void loadSheetNames() {
    try {
      Workbook workbook = WorkbookFactory.create(new File(file.getValue()));
      List<OptionValue> names = new ArrayList<>(workbook.getNumberOfSheets());
      for (int i = 0; i < workbook.getNumberOfSheets(); ++i) {
        names.add(new OptionValue(String.valueOf(i), "  " + workbook.getSheetAt(i).getSheetName() + "  "));
      }
      sheet.setPossibleValues(names);
      sheet.setDefaultValue(names.get(0));
    } catch (Exception e) {
      String title = "Error reading spreadsheet";
      String msg = title + ": " + e;
      showMessageDialog(msg, title, GuiUtilities.getMainFrame(), DialogIcon.ERROR);
    }
  }

  private String name(String format) {
    return String.format(format, identifier);
  }

}
