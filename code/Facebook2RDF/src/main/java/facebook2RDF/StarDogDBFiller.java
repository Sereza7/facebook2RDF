package facebook2RDF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.semanticweb.elk.owl.iris.ElkPrefix;
import org.semanticweb.elk.owl.parsing.Owl2ParseException;
import org.semanticweb.elk.owl.parsing.Owl2Parser;
import org.semanticweb.elk.owl.parsing.Owl2ParserAxiomProcessor;
import org.semanticweb.elk.owl.util.OwlObjectNameVisitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLAPIPreconditions;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.ConnectionPool;
import com.complexible.stardog.api.ConnectionPoolConfig;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.complexible.stardog.db.DatabaseOptions;
import com.complexible.stardog.metadata.Metadata;
import com.stardog.stark.Resource;
import com.stardog.stark.Values;
import com.stardog.stark.io.RDFFormats;
import com.stardog.stark.vocabs.FOAF;
import com.stardog.stark.vocabs.RDF;

import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

public class StarDogDBFiller {
	
	private static final String url = "http://localhost:5820";
	private static final String username = "admin";
	private static final String password = "admin";
	private static final String to = "myNewDB";
	private static final int minPool = 0;
	private static final int maxPool = 1000;
	private static final long expirationTime = 1000;
	private static final TimeUnit expirationTimeUnit = TimeUnit.HOURS;
	private static final long blockCapacityTime = 1;
	private static final TimeUnit blockCapacityTimeUnit = TimeUnit.SECONDS;
	private static final boolean reasoningType = false;
	
	private ConnectionPool connectionPool;
	private Connection connection;
	
	private OWLOntologyManager owlManager;
	private String ontologyName = "FacebookDB";
	private static String  nameSpace = "FacebookDB/";//must have a 


	public StarDogDBFiller() {
		
	}
	

	/**
	 * Now we want to create the configuration for our pool.
	 * @param connectionConfig the configuration for the connection pool
	 * @return the newly created pool which we will use to get our Connections
	 */
	private static ConnectionPool createConnectionPool
	                              (ConnectionConfiguration connectionConfig) {
	    ConnectionPoolConfig poolConfig = ConnectionPoolConfig
	            .using(connectionConfig)
	            .minPool(minPool)
	            .maxPool(maxPool)
	            .expiration(expirationTime, expirationTimeUnit)
	            .blockAtCapacity(blockCapacityTime, blockCapacityTimeUnit);
	    System.out.println("Creating Connection Pool.");
	    return poolConfig.create();
	}
	
	public void addData(String dataLocation) throws StardogException, FileNotFoundException { 
		Connection connection = connectionPool.obtain();
		System.out.println("Connection is initialized");
		connection.begin();
		
		//describe the transaction
		connection.add()
						.io()
						.format(RDFFormats.RDFXML)
						.stream(new FileInputStream(dataLocation));
		
		// and commit the change
		connection.commit();
		System.out.println("Transaction Commited");
		//at least, release the connection
		connectionPool.release(connection);
		System.out.println("Connection released at the end of transaction.");
		
		return;
	}
	
	/**
	 * TODO complete this function to use the OWLAPI to write RDF file from triples. 
	 * was having a hard time splitting external namespaces from the main one.
	 * @param subject
	 * @param relation
	 * @param object
	 * @throws OWLOntologyCreationException
	 * @throws IOException
	 * @throws OWLOntologyStorageException
	 */
	public void tripleToRDF(String subject, String relation, String object) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		 owlManager = OWLManager.createConcurrentOWLOntologyManager();
		 OWLOntology ontology = owlManager.createOntology(IRI.create(ontologyName));
		 		 
		 OWLDataFactory df = OWLManager.getOWLDataFactory();
		 
		 OWLObjectPropertyExpression relationO = df.getOWLObjectProperty(IRI.create(relation));
		 OWLIndividual subjectO = df.getOWLNamedIndividual(IRI.create(subject));
		 OWLIndividual objectO = df.getOWLNamedIndividual(IRI.create(object));
		 
		 owlManager.addAxiom(ontology, df.getOWLObjectPropertyAssertionAxiom(relationO, subjectO, objectO));
		 
		 File output = File.createTempFile("addTripleToStarDog","owl");
		 owlManager.saveOntology(ontology, IRI.create(output));
		 owlManager.removeOntology(ontology);
		 
		 //saves to the stardog db
		 this.addData(output.getAbsolutePath());
	}
	
	private void tripleToDB(String subject, String relation, String object) {
		Connection connection = connectionPool.obtain();
		System.out.println("Connection is initialized");
		connection.begin();
		connection.add()
			.statement(Values.iri(subject),  Values.iri(relation), Values.iri(object));
		connection.commit();
		System.out.println("Transaction Commited");
		//at least, release the connection
		connectionPool.release(connection);
		System.out.println("Connection released at the end of transaction.");
	}
	
	
	public static void main(String[] args) throws IOException {
		
		
		StarDogDBFiller filler = new StarDogDBFiller();
		ConnectionConfiguration connectionConfig = ConnectionConfiguration
		        .to(to)
		        .server(url)
		        .credentials(username, password);
		// creates the Stardog connection pool
		filler.connectionPool = createConnectionPool(connectionConfig);
		
	
		
		//filler.addData("src/main/java/ontologies/Family_Control.owl");
		
		filler.tripleToDB("FacebookDB"+":"+"Personi345", "rdfs:type","foaf"+":"+ "Person");
		
		filler.connectionPool.shutdown();
	}


	
	
}
