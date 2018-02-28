package com.hpe.ucmdb.udc.action;

import com.hpe.ucmdb.udc.UiTemplate;
import com.hpe.ucmdb.udc.model.ReturnValue;
import com.hpe.ucmdb.udc.model.TemplateEntry;
import com.intellij.ide.FileSelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class CreatePackage extends AnAction {
    // --Commented out by Inspection (3/1/2017 3:04 PM):private static final String LIB_ROOT = "lib";

    private static List<TemplateEntry> readZipFile(final List<File> files) throws Exception {
        final List<TemplateEntry> templateList = new ArrayList<>();
        for (final File file : files) {
            final InputStream in = new BufferedInputStream(new FileInputStream(file));
            final ZipInputStream zin = new ZipInputStream(in);
            final String tempalteName = file.getName().split("\\.")[0];
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    continue;
                }
                if (ze.getName().split("/").length > 1) {
                    final List<String> folderList = new ArrayList<>();
                    for (int i = 0; i < ze.getName().split("/").length - 1; ++i) {
                        folderList.add(ze.getName().split("/")[i]);
                    }
                    final TemplateEntry template = new TemplateEntry(tempalteName, folderList, ze.getName().split("/")[ze.getName().split("/").length - 1]);
                    templateList.add(template);
                } else {
                    final TemplateEntry template2 = new TemplateEntry(tempalteName, null, ze.getName());
                    templateList.add(template2);
                }
            }
            zin.closeEntry();
        }
        return templateList;
    }

    private static Map<String, InputStream> getDescriptionFiles(final List<File> files) throws Exception {
        final Map<String, InputStream> descriptions = new HashMap<>();
        for (final File file : files) {
            final ZipFile zipFile = new ZipFile(file.getCanonicalPath());
            final InputStream in = new BufferedInputStream(new FileInputStream(file));
            final ZipInputStream zin = new ZipInputStream(in);
            final String tempalteName = file.getName().split("\\.")[0];
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().endsWith("descriptor.xml")) {
                    final InputStream is = zipFile.getInputStream(ze);
                    descriptions.put(file.getName(), is);
                }
                zin.closeEntry();
            }
        }
        return descriptions;
    }

    private static VirtualFile findOrCreateChildDirectory(final VirtualFile parent, final String childName) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                final VirtualFile childDirectory = parent.findChild(childName);
                if (childDirectory == null || !childDirectory.exists()) {
                    parent.createChildDirectory(null, childName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("create folder to file:" + childName);
        });
        return parent.findChild(childName);
    }

    private static void writeSingleResource(final VirtualFile resTypeFolder, final String resourceName, final byte[] content, final String adapterName, final String jobName, final String scriptsName, final boolean a) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                final VirtualFile child = resTypeFolder.findOrCreateChildData(null, resourceName);
                if (a) {
                    String text = new String(content, "UTF-8");
                    final Map<String, String> matchers = new HashMap<>();
                    matchers.put("$JOB_NAME", jobName);
                    matchers.put("$PATTERN_NAME", adapterName);
                    matchers.put("$SCRIPTS_NAME", scriptsName);
                    text = replaceMacro(text, matchers);
                    final byte[] bytes = text.getBytes("UTF-8");
                    child.setBinaryContent(bytes);
                } else {
                    child.setBinaryContent(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Write to file:" + resourceName);
        });
    }

    private static String replaceMacro(String text, final Map<String, String> matchers) {
        for (final String key : matchers.keySet()) {
            final String value = matchers.get(key);
            text = text.replace(key, value);
        }
        return text;
    }

    private static boolean isRoot(@NotNull final Module module, @NotNull final VirtualFile dir) {
        return ProjectFileIndex.SERVICE.getInstance(module.getProject()).getContentRootForFile(dir).equals(dir);
    }

    public void actionPerformed(final AnActionEvent e) {
        List<TemplateEntry> templateList = new ArrayList<>();
        Map<String, String> descriptions = new HashMap<>();
        final Project project = e.getProject();
        final VirtualFile root = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        final List<File> files = new ArrayList<>();
        final List<String> zips = this.getZIPsPath();
        for (final String zip : zips) {
            files.add(this.getZIPContent(zip));
        }
        try {
            final Map<String, InputStream> descriptionFiles = getDescriptionFiles(files);
            descriptions = this.getDescritions(descriptionFiles);
            templateList = readZipFile(files);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        final UiTemplate uiTemplate = new UiTemplate(project, templateList, descriptions);
        uiTemplate.setVisible(true);
        final ReturnValue rt = uiTemplate.getReturnValue();
        if (rt == null) {
            return;
        }
        final String setName = rt.getSetTemplateName();
        final String adapterName = rt.getAdapterName();
        final String jobName = rt.getJobName();
        final String scriptName = rt.getScriptsName();
        int i1 = 0;
        assert root != null;
        if (root.findChild(setName) != null) {
            final String s = "This destination already contains a folder named '" + setName + "'. If any files have the same names, you will be asked if you want to replace those files. Do you still want to merge this folder ? ";
            i1 = Messages.showOkCancelDialog(project, s, "Confirmation", "Yes", "No", Messages.getInformationIcon());
        }
        if (i1 == 0) {
            try {
                final VirtualFile packageFolder = findOrCreateChildDirectory(root, setName);
                final ZipInputStream Zin = new ZipInputStream(new FileInputStream(this.getContent(rt.getTemplateName())));
                final String Parent = packageFolder.getCanonicalPath();
                File fout;
                try {
                    ZipEntry entry;
                    while ((entry = Zin.getNextEntry()) != null) {
                        if (!entry.isDirectory()) {
                            fout = new File(Parent, entry.getName());
                            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            final byte[] b1 = new byte[1024];
                            int b2;
                            while ((b2 = Zin.read(b1)) != -1) {
                                bos.write(b1, 0, b2);
                            }
                            final byte[] zipContent = bos.toByteArray();
                            if (!fout.exists()) {
                                VirtualFile packageFolderi = null;
                                final int l = entry.getName().split("/").length;
                                final List<VirtualFile> list = new ArrayList<>();
                                if (l == 1) {
                                    writeSingleResource(packageFolder, entry.getName().split("/")[l - 1], zipContent, adapterName, jobName, scriptName, false);
                                } else {
                                    boolean a = false;
                                    for (int j = 0; j <= l - 2; ++j) {
                                        if (j == 0) {
                                            packageFolderi = findOrCreateChildDirectory(packageFolder, entry.getName().split("/")[j]);
                                            if (packageFolderi.getName().equals("discoveryJobs") || packageFolderi.getName().equals("discoveryPatterns") || packageFolderi.getName().equals("discoveryScripts")) {
                                                a = true;
                                            }
                                            list.add(packageFolderi);
                                        } else {
                                            packageFolderi = findOrCreateChildDirectory(list.get(j - 1), entry.getName().split("/")[j]);
                                            if (packageFolderi.getName().equals("discoveryJobs") || packageFolderi.getName().equals("discoveryPatterns") || packageFolderi.getName().equals("discoveryScripts")) {
                                                a = true;
                                            }
                                            list.add(packageFolderi);
                                        }
                                    }
                                    if (entry.getName().split("/")[l - 1].contains("$PATTERN_NAME")) {
                                        writeSingleResource(packageFolderi, entry.getName().split("/")[l - 1].replace("$PATTERN_NAME", adapterName), zipContent, adapterName, jobName, scriptName, a);
                                    } else if (entry.getName().split("/")[l - 1].contains("$JOB_NAME")) {
                                        writeSingleResource(packageFolderi, entry.getName().split("/")[l - 1].replace("$JOB_NAME", jobName), zipContent, adapterName, jobName, scriptName, a);
                                    } else if (entry.getName().split("/")[l - 1].contains("$SCRIPTS_NAME")) {
                                        writeSingleResource(packageFolderi, entry.getName().split("/")[l - 1].replace("$SCRIPTS_NAME", scriptName), zipContent, adapterName, jobName, scriptName, a);
                                    } else {
                                        writeSingleResource(packageFolderi, entry.getName().split("/")[l - 1], zipContent, adapterName, jobName, scriptName, a);
                                    }
                                }
                            }
                            final FileWriter newFile = new FileWriter(new File(packageFolder.getCanonicalPath(), "descriptor.xml"));
                            final XMLWriter newWriter = new XMLWriter(newFile);
                            try {
                                newWriter.write(this.writeDescription(rt, getDescriptionFiles(files)));
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                            newWriter.close();
                            bos.close();
                            System.out.println(fout + "success");
                        }
                    }
                    Zin.close();
                    assert project != null;
                    ProjectView.getInstance(project).getSelectInTargets().toArray(new SelectInTarget[0])[0].selectIn(new FileSelectInContext(e.getProject(), packageFolder), true);
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
    }

    private Map<String, String> getDescritions(final Map<String, InputStream> descriptionFiles) throws Exception {
        final Map<String, String> descriptions = new HashMap<>();
        final SAXReader reader = new SAXReader();
        for (final Map.Entry<String, InputStream> entry : descriptionFiles.entrySet()) {
            final Document doc = reader.read(entry.getValue());
            final Element root = doc.getRootElement();
            final String content = root.element("description").getText().trim();
            descriptions.put(entry.getKey(), content);
            entry.getValue().close();
        }
        return descriptions;
    }

    private File getZIPContent(final String dir) {
        final PluginId pyhelper1 = PluginId.getId("UDC");
        final IdeaPluginDescriptor plugin = PluginManager.getPlugin(pyhelper1);
        assert plugin != null;
        final File pyhelper2 = plugin.getPath();
        return new File(dir);
    }

    private Document writeDescription(final ReturnValue rt, final Map<String, InputStream> descriptionFiles) throws Exception {
        final SAXReader reader = new SAXReader();
        Document doc = null;
        for (final Map.Entry<String, InputStream> entry : descriptionFiles.entrySet()) {
            if (entry.getKey().equals(rt.getTemplateName() + ".zip")) {
                doc = reader.read(entry.getValue());
                final Element root = doc.getRootElement();
                root.element("description").setText(rt.getDescription());
                entry.getValue().close();
            }
        }
        return doc;
    }

    private List<String> getZIPsPath() {
        final List<String> zipsPath = new ArrayList<>();
        final PluginId pyhelper1 = PluginId.getId("UDC");
        final IdeaPluginDescriptor plugin = PluginManager.getPlugin(pyhelper1);
        assert plugin != null;
        final File pyhelper2 = plugin.getPath();
        System.out.println(pyhelper2.getAbsolutePath());
        final File root = new File(pyhelper2, "lib");
        System.out.println(root.exists());
        final File[] fs = root.listFiles();
        assert fs != null;
        for (File f : fs) {
            if (f.getAbsolutePath().endsWith(".zip")) {
                zipsPath.add(f.getAbsolutePath());
            }
        }
        return zipsPath;
    }

    private File getContent(final String s) {
        final String ss = "lib/" + s + ".zip";
        final PluginId pyhelper1 = PluginId.getId("UDC");
        final IdeaPluginDescriptor plugin = PluginManager.getPlugin(pyhelper1);
        assert plugin != null;
        final File pyhelper2 = plugin.getPath();
        System.out.println(pyhelper2.getAbsolutePath());
        final File model = new File(pyhelper2, ss);
        System.out.println(model.exists());
        return model;
    }

    public void update(final AnActionEvent e) {
        final VirtualFile data = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (data == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        final Project project = e.getProject();
        if (project == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        final Module moduleForFile = ModuleUtil.findModuleForFile(data, project);
        if (moduleForFile == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        final boolean x = isRoot(ModuleUtil.findModuleForFile(data, e.getProject()), data);
        e.getPresentation().setEnabled(x);
    }
}