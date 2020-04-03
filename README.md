# facebook2RDF
Social &amp; semantic web project.

The goal of this project is to gather informations from the facebook graph api and to put it on a relational database using stardog.

Classes:
 - FacebookGetter makes it easier to get data from the facebook graph api.
 - StarDogDBCreator creates a DB on a stardog server. The name of this DB is stored in the constant "to".
 - StarDogDBFiller allows to fill a stardog DB ("to" constant) with triplets stored in a .owl file.
 
