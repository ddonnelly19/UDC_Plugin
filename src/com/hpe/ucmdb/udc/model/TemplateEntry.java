package com.hpe.ucmdb.udc.model;

import java.util.List;

public class TemplateEntry {
    private final String templateName;
    private final List<String> folder;
    private final String file;

    public TemplateEntry(final String templateName, final List<String> folder, final String file) {
        this.templateName = templateName;
        this.folder = folder;
        this.file = file;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public List<String> getFolder() {
        return this.folder;
    }

    public String getFile() {
        return this.file;
    }
}