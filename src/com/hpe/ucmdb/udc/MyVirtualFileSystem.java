package com.hpe.ucmdb.udc;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class MyVirtualFileSystem extends VirtualFileSystem {
    @NotNull
    public String getProtocol() {
        return "ucmdb";
    }

    @Nullable
    public VirtualFile findFileByPath(@NotNull @NonNls final String s) {
        return null;
    }

    public void refresh(final boolean b) {
    }

    @Nullable
    public VirtualFile refreshAndFindFileByPath(@NotNull final String s) {
        return null;
    }

    public void addVirtualFileListener(@NotNull final VirtualFileListener virtualFileListener) {
    }

    public void removeVirtualFileListener(@NotNull final VirtualFileListener virtualFileListener) {
    }

    protected void deleteFile(final Object o, @NotNull final VirtualFile virtualFile) throws IOException {
    }

    protected void moveFile(final Object o, @NotNull final VirtualFile virtualFile, @NotNull final VirtualFile virtualFile2) throws IOException {
    }

    protected void renameFile(final Object o, @NotNull final VirtualFile virtualFile, @NotNull final String s) throws IOException {
    }

    @NotNull
    protected VirtualFile createChildFile(final Object o, @NotNull final VirtualFile virtualFile, @NotNull final String s) throws IOException {
        return null;
    }

    @NotNull
    protected VirtualFile createChildDirectory(final Object o, @NotNull final VirtualFile virtualFile, @NotNull final String s) throws IOException {
        final VirtualFile virtualFile2 = null;
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/MyVirtualFileSystem", "createChildDirectory"));
    }

    @NotNull
    protected VirtualFile copyFile(final Object o, @NotNull final VirtualFile virtualFile, @NotNull final VirtualFile virtualFile2, @NotNull final String s) throws IOException {
        return null;
    }

    public boolean isReadOnly() {
        return true;
    }
}