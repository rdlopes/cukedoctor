package com.github.cukedoctor.multipage.model;

import com.github.cukedoctor.api.DocumentAttributes;
import com.github.cukedoctor.api.model.Feature;

import java.util.List;

/**
 * This class groups multiple features into an adoc page.
 *
 * It is used by the {@link com.github.cukedoctor.multipage.spi.MultipagePersister} to generate adoc files per page.
 *
 * @see com.github.cukedoctor.multipage.api.MultipageConverter for the main entry point
 * @see com.github.cukedoctor.multipage.spi.MultipagePersister for how pages are saved into files
 * @see com.github.cukedoctor.multipage.spi.MultipagePager for how features are grouped into pages
 */
public class Page {

    private String pageTitle;
    private List<Feature> features;
    private DocumentAttributes documentAttributes;
    private String render;

    public Page(String pageTitle, List<Feature> features, DocumentAttributes documentAttributes) {
        this.pageTitle = pageTitle;
        this.features = features;
        this.documentAttributes = documentAttributes;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public DocumentAttributes getDocumentAttributes() {
        return documentAttributes;
    }

    public void setDocumentAttributes(DocumentAttributes documentAttributes) {
        this.documentAttributes = documentAttributes;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getRender() {
        return render;
    }

    /**
     * @param render A page's render is its asciidoc form generated by the classic {@link com.github.cukedoctor.api.CukedoctorConverter}
     */
    public void setRender(String render) {
        this.render = render;
    }
}