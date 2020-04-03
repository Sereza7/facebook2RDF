package facebook2RDF;

import java.io.IOException;

import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;

public class StarDogDBCreator {

	/**
	 *  Creates a connection to the DBMS itself so we
	 *  can perform some administrative actions.
	 */
	private static final String url = "http://localhost:5820";
	private static final String username = "admin";
	private static final String password = "admin";
	private static final String to = "myNewDB";
	public static void createAdminConnection() {
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
	    }
	}
	public static void main(String[] args) throws IOException {
		createAdminConnection();
	}
}
