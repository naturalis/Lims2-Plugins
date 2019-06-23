package nl.naturalis.geneious.smpl;

import java.util.List;

import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;

import nl.naturalis.geneious.csv.CsvImportOptions;

/**
 * Configures a Geneious dialog requesting user input for the {@link SampleSheetDocumentOperation Sample Sheet Import}
 * operation. Once the user click OK, this class produces a {@link SampleSheetImportConfig} object, which is then passed
 * on to the {@link SampleSheetSwingWorker}.
 * 
 * @author Ayco Holleman
 *
 */
class SampleSheetImportOptions extends CsvImportOptions<SampleSheetColumn, SampleSheetImportConfig> {

  private final BooleanOption dummies;

  public SampleSheetImportOptions(List<AnnotatedPluginDocument> documents) {
    super(documents, "smpl");
    this.dummies = addDummiesOption();
  }

  /**
   * Produces a object containing all the user input for the Sample Sheet Import operation.
   */
  @Override
  public SampleSheetImportConfig createImportConfig() {
    SampleSheetImportConfig cfg = initializeStandardOptions(new SampleSheetImportConfig());
    cfg.setCreateDummies(dummies.getValue());
    return cfg;
  }

  /**
   * Returns the text in front of the file selection field: "Sample sheet".
   */
  @Override
  protected String getDefaultFileSelectionLabel() {
    return "Sample sheet";
  }

  private BooleanOption addDummiesOption() {
    String name = "nl.naturalis.geneious.smpl.dummies";
    String descr = "Create dummy sequences for rows containing new extract IDs";
    BooleanOption opt = addBooleanOption(name, descr, Boolean.TRUE);
    opt.setHelp("You can choose to create dummy sequences for sample sheet rows that refer to yet-to-be imported "
        + "AB1 or fasta sequences. The dummy sequence acquires the annotations present in the sample sheet. Once you "
        + "import the real sequence, the annotations will be copied from the dummy sequence to the real sequence, "
        + "and the dummy sequence will be deleted. If you select this option, you may or may not select documents "
        + "to be annotated with sample sheet data. If you do not select this option, you MUST select at least one "
        + "document to be annotated with sample sheet data");
    return opt;
  }

}
