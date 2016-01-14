package nl.naturalis.lims2.oaipmh.specimens;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import nl.naturalis.lims2.oaipmh.Lims2OAIRepository;
import nl.naturalis.oaipmh.api.OAIPMHException;
import nl.naturalis.oaipmh.api.RepositoryException;

import org.openarchives.oai._2.OAIPMHtype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OAI repository for specimens.
 * 
 * @author Ayco Holleman
 *
 */
public class SpecimenOAIRepository extends Lims2OAIRepository {

	private static final Logger logger = LoggerFactory.getLogger(SpecimenOAIRepository.class);

	public SpecimenOAIRepository()
	{
		super();
	}

	@Override
	public void listRecords(OutputStream out) throws OAIPMHException, RepositoryException
	{
		logger.debug("Instantiating handler for ListRecords request");
		ListRecordsHandler handler = new ListRecordsHandler(request);
		OAIPMHtype oaipmh = handler.handleRequest();
		// if (logger.isDebugEnabled()) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		stream(oaipmh, baos);
		logger.debug(new String(baos.toByteArray()));
		// }
		stream(oaipmh, out);
	}
}
