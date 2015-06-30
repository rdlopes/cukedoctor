package com.github.cukedoctor.mojo;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.cukedoctor.Cukedoctor;
import com.github.cukedoctor.api.CukedoctorReporter;
import com.github.cukedoctor.api.DocumentAttributes;
import com.github.cukedoctor.api.model.Feature;
import com.github.cukedoctor.mojo.model.Format;
import com.github.cukedoctor.mojo.model.Toc;
import com.github.cukedoctor.parser.FeatureParser;
import com.github.cukedoctor.util.FileUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import java.io.File;
import java.util.List;

@Mojo(name = "execute",
		defaultPhase = LifecyclePhase.INSTALL)
public class CukedoctorMojo extends AbstractMojo {

	@Parameter(defaultValue = "documentation", required = false)
	String outputFileName;

	@Parameter(defaultValue = "cukedoctor", required = false)
	String outputDir;

	@Parameter(required = false)
	String documentTitle;

	@Parameter(defaultValue = "html5", required = true)
	Format format;

	@Parameter(defaultValue = "right", required = false)
	Toc toc;

	@Parameter(defaultValue = "false", required = false)
	boolean numbered;

	@Component
	MavenProject project;

	private String generatedFile = null;//only for tests


	public void execute() throws MojoExecutionException, MojoFailureException {
		List<Feature> features = FeatureParser.findAndParse(project.getBuild().getDirectory());
		if (features == null || features.isEmpty()) {
			getLog().warn("No cucumber json files found in " + project.getBuild().getDirectory());
			return;
		}else{
			getLog().info("Generating living documentation for " + features.size() + " features...");
		}

		DocumentAttributes documentAttributes = new DocumentAttributes().
				backend(format.name().toLowerCase()).
				toc(toc.name().toLowerCase()).numbered(numbered);
		if (format.equals(Format.pdf)) {
			documentAttributes.pdfTheme(true).docInfo(false);
		} else {
			documentAttributes.docInfo(true).pdfTheme(false);
		}

		if (documentTitle == null) {
			documentTitle = "Living Documentation";
		}
		CukedoctorReporter reporter = Cukedoctor.instance(features, documentTitle, documentAttributes);
		String targetFile = "";
		if (outputFileName.contains(".")) {
			targetFile = outputFileName.substring(0, outputFileName.lastIndexOf(".")) + ".adoc";
		} else {
			targetFile = outputFileName + ".adoc";
		}

		String targetDir = getDocumentationDir();
		String pathToSave = targetDir + targetFile;
		reporter.setFilename(pathToSave);//needed by docinfo, pdf-theme
		generatedFile = reporter.renderDocumentation();
		File adocFile = FileUtil.saveFile(pathToSave, generatedFile);
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		asciidoctor.convertFile(adocFile, OptionsBuilder.options().backend(documentAttributes.getBackend()).safe(SafeMode.UNSAFE).asMap());
		//remove auxiliary files
		if (format.equals(Format.pdf)) {
			FileUtil.removeFile(adocFile.getParent() + "/"+outputFileName + "-theme.yml");
		}

		getLog().info("Generated documentation at: " + adocFile.getParent());
	}

	/**
	 * documentation is saved under ${buildDir}/cukedoctor folder
	 */
	String getDocumentationDir() {
		String baseDir = project.getBuild().getDirectory();
		if (baseDir == null) {
			baseDir = "target/";
		}
		if (!baseDir.endsWith("/")) {
			baseDir = baseDir + "/";
		}

		if(outputDir == null){
			outputDir = "cukedoctor/";
		}

		if(!outputDir.endsWith("/")){
			outputDir = outputDir +"/";
		}

		return baseDir + outputDir;

	}

	public String getGeneratedFile() {
		return generatedFile;
	}
}
