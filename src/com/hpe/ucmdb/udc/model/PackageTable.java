package com.hpe.ucmdb.udc.model;

public class PackageTable {
    private final String packageName;
    private boolean selected;

    public PackageTable(final String packageName, final boolean selected) {
        this.packageName = packageName;
        this.selected = selected;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setPackageName(final String packageName) {
//        this.packageName = packageName;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public String getPackageName() {
        return this.packageName;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected() {
        this.selected = true;
    }
}