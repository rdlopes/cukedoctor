package com.github.cukedoctor.api;

import com.github.cukedoctor.api.model.Feature;

import java.util.List;

/**
 * Created by pestano on 02/06/15.
 */
public interface CukedoctorConverter {


	DocumentAttributes getDocumentAttributes();

	List<Feature> getFeatures();

	String getDocumentation();

	/**
	 * creates a string representation of the current document in Asciidoctor format
	 */
	String renderDocumentation();

	String getDocumentationTitle();

	CukedoctorConverter renderFeatures(List<Feature> features);

	CukedoctorConverter renderFeatures();

	CukedoctorConverter renderAttributes();

	/**
	 * Document overall summary which gathers information about all features like
	 * number of steps, execution time, total passed scenarios and so on
	 */
	CukedoctorConverter renderSummary();



	/**
	 * set output file name without extension (should always be .adoc or other asciidoc compatible extensions)
	 * If no filename is set then Documentation title (first H1 section) will be used as filename (underline will be used to remove spaces)
	 * The filename is used mainly for asciidoc docinfo creation.
	 * @param filename
	 */
	CukedoctorConverter setFilename(String filename);


	String getFilename();


	CukedoctorConverter addCustomPdfTheme();

	CukedoctorConverter addCustomCss();

	/**
	 * saves the documentation into disk using {@link CukedoctorConverter#getFilename()}  as name (also path). Note that
	 * {@link CukedoctorConverter#renderDocumentation()} will be called and used as content to save the file.
	 */
	CukedoctorConverter saveDocumentation();


}
