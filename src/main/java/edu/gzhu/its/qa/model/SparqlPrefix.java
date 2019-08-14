package edu.gzhu.its.qa.model;

public class SparqlPrefix {

	/**
	 * prefix 用于语义检索的前缀
	 * @author : 丁国柱
	 */
	public static final String prefix = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ " PREFIX  owl: <http://www.w3.org/2002/07/owl#> "
			+ " PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ "PREFIX text: <http://jena.apache.org/text#>";

	public static final String rdfsPrefix = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";

	public static final String rdfPrefix = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";

}
