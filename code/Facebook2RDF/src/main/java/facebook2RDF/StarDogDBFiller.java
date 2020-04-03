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
	
	public static void main(String[] args) throws IOException {
		
		
		StarDogDBFiller filler = new StarDogDBFiller();
		ConnectionConfiguration connectionConfig = ConnectionConfiguration
		        .to(to)
		        .server(url)
		        .credentials(username, password);
		// creates the Stardog connection pool
		filler.connectionPool = createConnectionPool(connectionConfig);
		
	
		
		filler.addData("src/main/java/ontologies/Family_Control.owl");
		
		filler.connectionPool.shutdown();
	}
	
}
