package com.hpe.ucmdb.udc.model;

import com.hpe.ucmdb.udc.MyVirtualFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

class RemoteFile extends VirtualFile {
    private final byte[] content;
    private final String fileName;

    public RemoteFile(final byte[] content, final String fileName) {
        this.content = content;
        this.fileName = fileName;
    }

    public static VirtualFile createVirtualFile(final RemoteFile r) {
        return r;
    }

    @NotNull
    public String getName() {
        final String fileName = this.fileName;
        if (fileName == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/model/RemoteFile", "getName"));
        }
        return fileName;
    }

    public String getPresentableName() {
        return "Remote: " + this.fileName;
    }

    @NotNull
    public VirtualFileSystem getFileSystem() {
        return new MyVirtualFileSystem();
    }

    @NotNull
    public String getPath() {
        final String fileName = this.fileName;
        if (fileName == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/model/RemoteFile", "getPath"));
        }
        return fileName;
    }

    public boolean isWritable() {
        return true;
    }

    public boolean isDirectory() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    public VirtualFile getParent() {
        return null;
    }

    public VirtualFile[] getChildren() {
        return new VirtualFile[0];
    }

    @NotNull
    public OutputStream getOutputStream(final Object o, final long l, final long l2) throws IOException {
        return new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
            }
        };
    }

    @NotNull
    public byte[] contentsToByteArray() throws IOException {
        final byte[] content = this.content;
        if (content == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/model/RemoteFile", "contentsToByteArray"));
        }
        return content;
    }

    public long getTimeStamp() {
        return 0L;
    }

    public long getLength() {
        try {
            return this.contentsToByteArray().length;
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public void refresh(final boolean b, final boolean b2, @Nullable final Runnable runnable) {
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.contentsToByteArray());
    }

    public long getModificationStamp() {
        return 0L;
    }
}