# facebook2RDF
Social &amp; semantic web project.

/!\ This project has no graphical interface and only consists of a list of services to connect Stardog with Java, and query the Facebook rest API./!\
The authentification token of the user has to be generated beforehand for any use.

Classes:
 - FacebookGetter makes it easier to get data from the facebook graph api.
 - StarDogDBCreator creates a DB on a stardog server. The name of this DB is stored in the constant "to".
 - StarDogDBFiller: allows to fill a stardog DB ("to" constant) with triples.
 The triples can be passed as arguments of the "tripleToDB" function or stored in an rdf file that can be added to the database.
 Use the standard prefixes for objects & properties from other ontologies.
 Examples: 
    filler.tripleToDB("FacebookDB"+":"+"Personi345", "rdfs:type","foaf"+":"+"Person");
    filler.tripleToDB("FacebookDB"+":"+"Post3jfs9", "rdfs:type","sioc"+":"+"Post");
