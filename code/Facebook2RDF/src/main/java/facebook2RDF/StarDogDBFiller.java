package facebook2RDF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.ConnectionPool;
import com.complexible.stardog.api.ConnectionPoolConfig;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.stardog.stark.io.RDFFormats;

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

	    return poolConfig.create();
	}
	
	public void addData(String dataLocation) throws StardogException, FileNotFoundException { 
		// first start a transaction. This will generate the contents of
		// the database from the N3 file.
		connection.begin();
		// declare the transaction
		/*connection.add().io()
		                .format(RDFFormat.RDFXML)
		                .stream(new FileInputStream(dataLocation));*/
	
		// and commit the change
		connection.commit();
		connectionPool.release(connection);
		return;
	}
	
	public static void main(String[] args) throws IOException {
		
		try (final AdminConnection aConn = AdminConnectionConfiguration.toServer(url)
	            .credentials(username, password)
	            .connect()) {

	        // A look at what databases are currently in Stardog
	        aConn.list().forEach(item -> System.out.println(item));

	        // Checks to see if the 'myNewDB' is in Stardog. If it is, we are
	        // going to drop it so we are starting fresh
	        if (aConn.list().contains(to)) {aConn.drop(to);}
	        // Convenience function for creating a persistent
	        // database with all the default settings.
	        aConn.disk(to).create();
	        aConn.close();
	    }
		System.out.println("The adminConnexion is closed here...");
		
		StarDogDBFiller filler = new StarDogDBFiller();
		ConnectionConfiguration connectionConfig = ConnectionConfiguration
		        .to(to)
		        .server(url)
		        .reasoning(reasoningType)
		        .credentials(username, password);
		// creates the Stardog connection pool
		ConnectionPool connectionPool = createConnectionPool(connectionConfig);
	
		//filler.connection = connectionPool.obtain();
		System.out.println("connection pool initialized.");
		//filler.addData("src/main/java/ontologies/Family_Control.owl");
		Connection connection = connectionPool.obtain();
		System.out.println("Connection is initialized");
	
		
		connection.begin();
		
		connection.add().io().format(RDFFormats.RDFXML).stream(new FileInputStream("src/main/java/ontologies/Family_Control.owl"));
	
		// and commit the change
		connection.commit();
		
		//at least, release the connection
		connectionPool.release(connection);
		connectionPool.shutdown();
	}
	
}
