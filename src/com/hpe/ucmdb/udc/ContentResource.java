package com.hpe.ucmdb.udc;

class ContentResource {
    private final String name;
    private final String ext;
    private long lastModifiedTime;
    private byte[] content;
    private String fullName;

    ContentResource() {
        name = null;
        ext = null;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public ContentResource() {
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public ContentResource(final ContentResource cr) {
//        this.name = cr.name;
//        this.ext = cr.ext;
//        this.lastModifiedTime = cr.lastModifiedTime;
//        this.content = cr.content;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public ContentResource(final String name, final long lastModifiedTime) {
//        this.name = name;
//        this.lastModifiedTime = lastModifiedTime;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public ContentResource(final String name, final String ext, final long lastModifiedTime) {
//        this.name = name;
//        this.ext = ext;
//        this.lastModifiedTime = lastModifiedTime;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public ContentResource(final String fullName, final byte[] content) {
//        this.fullName = fullName;
//        this.content = content;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public ContentResource(final String fullName) {
//        final int i = fullName.lastIndexOf(46);
//        if (i == -1) {
//            this.name = fullName;
//        }
//        else {
//            this.name = fullName.substring(0, i);
//            this.ext = fullName.substring(i + 1);
//        }
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public String getName() {
//        return this.name;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setName(final String name) {
//        this.name = name;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public long getLastModifiedTime() {
//        return this.lastModifiedTime;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setLastModifiedTime(final long lastModifiedTime) {
//        this.lastModifiedTime = lastModifiedTime;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public String getExt() {
//        return this.ext;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setExt(final String ext) {
//        this.ext = ext;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private String getFullName() {
        if (this.fullName == null) {
            this.fullName = this.name + "." + this.ext;
        }
        return this.fullName;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public byte[] getContent() {
//        return this.content;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setContent(final byte[] content) {
//        this.content = content;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public boolean isInDirectory() {
//        return this.name.contains("/");
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ContentResource pyScript = (ContentResource) o;
        assert this.name != null;
        assert this.ext != null;
        return this.ext.equals(pyScript.ext) && this.name.equals(pyScript.name);
    }

    @Override
    public int hashCode() {
        assert this.name != null;
        int result = this.name.hashCode();
        assert this.ext != null;
        result = 31 * result + this.ext.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.getFullName();
    }
}