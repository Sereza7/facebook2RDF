# facebook2RDF
Social &amp; semantic web project.

The goal of this project is to gather informations from the facebook graph api and to put it on a relational database using stardog.

Classes:
 - FacebookGetter makes it easier to get data from the facebook graph api.
 - StarDogDBCreator creates a DB on a stardog server. The name of this DB is stored in the constant "to".
 - StarDogDBFiller: allows to fill a stardog DB ("to" constant) with triples.
 The triples can be passed as arguments of the "tripleToDB" function or stored in an rdf file that can be added to the database.
 Use the standard prefixes for objects & properties from other ontologies.
 Examples: 
    filler.tripleToDB("FacebookDB"+":"+"Personi345", "rdfs:type","foaf"+":"+"Person");
    filler.tripleToDB("FacebookDB"+":"+"Post3jfs9", "rdfs:type","sioc"+":"+"Post");
