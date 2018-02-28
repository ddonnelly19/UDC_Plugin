package com.hpe.ucmdb.udc.model;

public class ReturnValue {
    private String templateName;
    private String setTemplateName;
    private String adapterName;
    private String JobName;
    private String scriptsName;
    private String description;

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public String getAdapterName() {
        return this.adapterName;
    }

    public void setAdapterName(final String adapterName) {
        this.adapterName = adapterName;
    }

    public String getJobName() {
        return this.JobName;
    }

    public void setJobName(final String jobName) {
        this.JobName = jobName;
    }

    public String getScriptsName() {
        return this.scriptsName;
    }

    public void setScriptsName(final String scriptsName) {
        this.scriptsName = scriptsName;
    }

    public String getSetTemplateName() {
        return this.setTemplateName;
    }

    public void setSetTemplateName(final String setTemplateName) {
        this.setTemplateName = setTemplateName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}