package facebook2RDF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.complexible.stardog.db.DatabaseOptions;
import com.stardog.stark.Namespace;
import com.stardog.stark.Namespaces;

public class StarDogDBCreator {

	/**
	 *  Creates a connection to the DBMS itself so we
	 *  can perform some administrative actions.
	 */
	private static final String url = "http://localhost:5820";
	private static final String username = "admin";
	private static final String password = "admin";
	private static final String to = "myNewDB";
	@SuppressWarnings("deprecation")
	public static void resetDB() {
	    try (final AdminConnection aConn = AdminConnectionConfiguration.toServer(url)
	            .credentials(username, password)
	            .connect()) {

	        // A look at what databases are currently in Stardog
	        aConn.list().forEach(item -> System.out.println(item));

	        // Checks to see if the 'myNewDB' is in Stardog. If it is, we are
	        // going to drop it so we are starting fresh
	        if (aConn.list().contains(to)) {
	        	aConn.drop(to);
	        	System.out.println("A DB with the name '"+to+"' was already existing. It is getting deleted.");
	        	}
	        
	        Iterator<Namespace> source = Namespaces.EXTENDED.iterator();
	        Namespace sioc = new Namespace() {

				@Override
				public int compareTo(Namespace o) {
					return 0;
				}

				@Override
				public String iri() {
					return "http://rdfs.org/sioc/ns#";
				}

				@Override
				public String prefix() {
					return "sioc";
				}
	        	
	        };
	        ArrayList<Namespace> namespaces = new ArrayList<Namespace>();
	        namespaces.add(sioc);
	        System.out.println("Added sioc to the namespaces.");
	        source.forEachRemaining(namespaces::add);
	        System.out.println("Extended the namespaces (added foaf).");
	     // Convenience function for creating a persistent database
	        aConn.disk(to).set(DatabaseOptions.NAMESPACES,namespaces).create();
	        System.out.println("Created the new database.");
	    }
	}
	public static void main(String[] args) throws IOException {
		System.out.println(Namespaces.FOAF);
		resetDB();
	}
}
