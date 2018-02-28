package com.hpe.ucmdb.udc;

import com.hpe.ucmdb.udc.model.ReturnValue;
import com.hpe.ucmdb.udc.model.TemplateEntry;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UiTemplate extends JDialog {
    private String selectedTemplate;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<String> templateList;
    private JTextField textField1;
    private JTextField textField2;
    private JBTextField textField3;
    private JTextField textField5;
    private Tree tree1;
    private JTextArea textArea1;
    private ReturnValue returnValue;
    private String description;
    private byte[] byte2;

    public UiTemplate(final Project project, final List<TemplateEntry> templates, final Map<String, String> descriptions) {
        this.$$$setupUI$$$();
        final DefaultListModel<String> templateListModel = new DefaultListModel<>();
        for (final TemplateEntry template : templates) {
            if (!templateListModel.contains(template.getTemplateName())) {
                templateListModel.addElement(template.getTemplateName());
            }
        }
        this.templateList.setModel(templateListModel);
        this.setContentPane(this.contentPane);
        this.setModal(true);
        this.setTitle("Template List");
        this.setBounds(0, 0, 800, 660);
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(this.buttonOK);
        this.templateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.templateList.setSelectedIndex(0);
        this.selectedTemplate = this.templateList.getSelectedValue();
        this.textField1.setText(this.selectedTemplate);
        final DefaultTreeModel model = (DefaultTreeModel) this.tree1.getModel();
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.setUserObject(this.selectedTemplate);
        root.remove(root.getNextNode());
        root.remove(root.getNextNode());
        root.remove(root.getNextNode());
        final List<TemplateEntry> jTreeNodes = new ArrayList<>();
        for (final TemplateEntry temp : templates) {
            if (temp.getTemplateName().equals(this.selectedTemplate)) {
                jTreeNodes.add(temp);
            }
        }
        for (final TemplateEntry t : jTreeNodes) {
            final List<String> ts = t.getFolder();
            DefaultMutableTreeNode n = null;
            if (ts != null) {
                for (int i = 0; i < ts.size(); ++i) {
                    if (i == 0) {
                        if (this.findChild(ts.get(i), root) == null) {
                            n = new DefaultMutableTreeNode(ts.get(i));
                            model.insertNodeInto(n, root, root.getChildCount());
                        } else {
                            n = this.findChild(ts.get(i), root);
                        }
                    } else if (this.findChild(ts.get(i), root) == null) {
                        final DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(ts.get(i));
                        assert n != null;
                        model.insertNodeInto(n2, n, n.getChildCount());
                        n = n2;
                    } else {
                        n = this.findChild(ts.get(i), root);
                    }
                }
                final DefaultMutableTreeNode file = new DefaultMutableTreeNode(t.getFile());
                assert n != null;
                model.insertNodeInto(file, n, n.getChildCount());
            } else {
                final DefaultMutableTreeNode file = new DefaultMutableTreeNode(t.getFile());
                model.insertNodeInto(file, root, 0);
            }
        }
        this.description = "";
        for (final Map.Entry<String, String> entry : descriptions.entrySet()) {
            if (entry.getKey().equals(this.selectedTemplate + ".zip")) {
                this.description = entry.getValue();
            }
        }
        this.textArea1.setText(this.description);
        this.tree1.updateUI();
        this.buttonOK.addActionListener(e -> {
            if (UiTemplate.this.getDataRequirement()) {
                UiTemplate.this.returnValue = new ReturnValue();
                UiTemplate.this.returnValue.setTemplateName(UiTemplate.this.selectedTemplate);
                UiTemplate.this.returnValue.setSetTemplateName(UiTemplate.this.textField1.getText().trim());
                UiTemplate.this.returnValue.setAdapterName(UiTemplate.this.textField2.getText().trim());
                UiTemplate.this.returnValue.setJobName(UiTemplate.this.textField3.getText().trim());
                UiTemplate.this.returnValue.setScriptsName(UiTemplate.this.textField5.getText().trim());
                UiTemplate.this.returnValue.setDescription(UiTemplate.this.description);
                UiTemplate.this.dispose();
            }
        });
        this.buttonCancel.addActionListener(e -> {
            try {
                UiTemplate.this.quit();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                UiTemplate.this.onCancel();
            }
        });
        this.contentPane.registerKeyboardAction(e -> UiTemplate.this.onCancel(), KeyStroke.getKeyStroke(27, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.templateList.addListSelectionListener(e -> {
            final String selected = UiTemplate.this.templateList.getSelectedValue();
            UiTemplate.this.selectedTemplate = selected;
            UiTemplate.this.textField1.setText(selected);
            final DefaultTreeModel model1 = (DefaultTreeModel) UiTemplate.this.tree1.getModel();
            final DefaultMutableTreeNode root1 = (DefaultMutableTreeNode) model1.getRoot();
            root1.removeAllChildren();
            final List<TemplateEntry> jTreeNodes1 = new ArrayList<>();
            for (final TemplateEntry temp : templates) {
                if (temp.getTemplateName().equals(selected)) {
                    jTreeNodes1.add(temp);
                }
            }
            for (final TemplateEntry t : jTreeNodes1) {
                final List<String> ts = t.getFolder();
                DefaultMutableTreeNode n = null;
                if (ts != null) {
                    for (int i = 0; i < ts.size(); ++i) {
                        if (i == 0) {
                            if (UiTemplate.this.findChild(ts.get(i), root1) == null) {
                                n = new DefaultMutableTreeNode(ts.get(i));
                                model1.insertNodeInto(n, root1, root1.getChildCount());
                            } else {
                                n = UiTemplate.this.findChild(ts.get(i), root1);
                            }
                        } else {
                            final List<String> parentFolders = new ArrayList<>();
                            for (int j = i - 1; j >= 0; --j) {
                                parentFolders.add(ts.get(j));
                            }
                            if (UiTemplate.this.findChild2(ts.get(i), root1, parentFolders, t.getTemplateName()) == null || !UiTemplate.this.findChildByParent(parentFolders, UiTemplate.this.findChild2(ts.get(i), root1, parentFolders, t.getTemplateName()))) {
                                final DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(ts.get(i));
                                assert n != null;
                                model1.insertNodeInto(n2, n, n.getChildCount());
                                n = n2;
                            } else {
                                n = UiTemplate.this.findChild2(ts.get(i), root1, parentFolders, t.getTemplateName());
                            }
                        }
                    }
                    final DefaultMutableTreeNode file = new DefaultMutableTreeNode(t.getFile());
                    assert n != null;
                    model1.insertNodeInto(file, n, n.getChildCount());
                } else {
                    final DefaultMutableTreeNode file = new DefaultMutableTreeNode(t.getFile());
                    model1.insertNodeInto(file, root1, 0);
                }
            }
            UiTemplate.this.description = "";
            for (final Map.Entry<String, String> entry : descriptions.entrySet()) {
                if (entry.getKey().equals(selected + ".zip")) {
                    UiTemplate.this.description = entry.getValue();
                }
            }
            UiTemplate.this.textArea1.setText(UiTemplate.this.description);
            UiTemplate.this.tree1.updateUI();
        });
        this.textField1.getDocument().addDocumentListener(new DocumentListener() {
            final DefaultTreeModel model = (DefaultTreeModel) UiTemplate.this.tree1.getModel();
            final DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.model.getRoot();

            @Override
            public void insertUpdate(final DocumentEvent e) {
                this.root.setUserObject(UiTemplate.this.textField1.getText());
                UiTemplate.this.tree1.updateUI();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                this.root.setUserObject(UiTemplate.this.textField1.getText());
                UiTemplate.this.tree1.updateUI();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                this.root.setUserObject(UiTemplate.this.textField1.getText());
                UiTemplate.this.tree1.updateUI();
            }
        });
        this.textArea1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                UiTemplate.this.description = UiTemplate.this.textArea1.getText();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                UiTemplate.this.description = UiTemplate.this.textArea1.getText();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                UiTemplate.this.description = UiTemplate.this.textArea1.getText();
            }
        });
        this.tree1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final TreePath path = UiTemplate.this.tree1.getPathForLocation(e.getX(), e.getY());
                    String text = "";
                    if (path != null && path.getLastPathComponent() != null) {
                        final TreeNode node = (TreeNode) path.getLastPathComponent();
                        if (node.isLeaf()) {
                            final List<String> nodes = new ArrayList<>();
                            nodes.add(node.toString());
                            nodes.add(UiTemplate.this.selectedTemplate);
                            final String zipPath = UiTemplate.this.getZIPsPath(nodes);
                            final File model = new File(zipPath);
                            try {
                                UiTemplate.this.byte2 = UiTemplate.input2byte(UiTemplate.getFile(model, nodes));
                                final InputStream fr = UiTemplate.getFile(model, nodes);
                                text = UiTemplate.this.inputStream2String(fr);
                                text = text.replace("\r\n", "\n");
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            final Collection<Language> registeredLanguages = Language.getRegisteredLanguages();
                            System.out.println(registeredLanguages);
                            Language xml = Language.findLanguageByID("TEXT");
                            if (node.toString().endsWith(".py")) {
                                xml = Language.findLanguageByID("Python");
                            } else if (node.toString().endsWith(".xml")) {
                                xml = Language.findLanguageByID("XML");
                            }
                            text = text.replace("\r\n", "\n");
                            final FileUI fu = new FileUI(xml, project, text);
                            fu.setTitle(node.toString());
                            fu.setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private static InputStream getFile(final File file, final List<String> nodes) throws Exception {
        InputStream is = null;
        final ZipFile zipFile = new ZipFile(file.getCanonicalPath());
        final InputStream in = new BufferedInputStream(new FileInputStream(file));
        final ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.getName().endsWith(nodes.get(0))) {
                is = zipFile.getInputStream(ze);
            }
            zin.closeEntry();
        }
        return is;
    }

    private static byte[] input2byte(final InputStream inStream) throws IOException {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        final byte[] buff = new byte[100];
        int rc;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        final byte[] in2b = swapStream.toByteArray();
        inStream.close();
        return in2b;
    }

    public static void main(final String[] args) {
        final UiTemplate dialog = new UiTemplate(null, null, null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private String inputStream2String(InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        in = new BufferedInputStream(in);
        final byte[] b = new byte[4096];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        final byte[] bytes = out.toByteArray();
        return CharsetToolkit.bytesToString(bytes, CharsetToolkit.UTF8_CHARSET);
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private void diff(final Project project, final VirtualFile file, final DiffContent local) {
//        final Task.Backgroundable task = new Task.Backgroundable(project, "Diffing @" + ServerConfigManager.getInstance().getDefaultServerConfig(project).getServer(), false, new PerformInBackgroundOption() {
//            public boolean shouldStartInBackground() {
//                return true;
//            }
//
//            public void processSentToBackground() {
//            }
//        }) {
//            final byte[] content = UiTemplate.this.byte2;
//
//            public void onSuccess() {
//                if (this.content != null) {
//                    @SuppressWarnings("deprecation") final DiffContent server = (DiffContent)new FileContent(project, file);
//                    local.getDocument().setReadOnly(false);
//                    server.getDocument().setReadOnly(true);
//                    @SuppressWarnings("deprecation") final DiffRequest dr = new DiffRequest(project) {
//                        @NotNull
//                        public DiffContent[] getContents() {
//                            @SuppressWarnings("deprecation") final DiffContent[] array = { local, server };
//                            if (array == null) {
//                                throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/UiTemplate$10$1", "getContents"));
//                            }
//                            return array;
//                        }
//
//                        public String[] getContentTitles() {
//                            return new String[] { file.getName() + "1", file.getName() + "2" };
//                        }
//
//                        public String getWindowTitle() {
//                            return file.getName();
//                        }
//                    };
//                    dr.setOnOkRunnable((Runnable)new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
//                    //noinspection deprecation
//                    DiffManager.getInstance().getDiffTool().show(dr);
//                }
//                else {
//                    PopupUtil.showBalloonForActiveComponent("Can't get content ", MessageType.ERROR);
//                }
//            }
//
//            public void run(@NotNull final ProgressIndicator progressIndicator) {
//                if (progressIndicator == null) {
//                    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "progressIndicator", "com/hpe/ucmdb/udc/UiTemplate$10", "run"));
//                }
//                progressIndicator.setIndeterminate(true);
//            }
//        };
//        ProgressManager.getInstance().run((Task)task);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private void onOK() {
//        this.dispose();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private String getZIPsPath(final List<String> nodes) {
        String zipsPath = "";
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
            if (f.getAbsolutePath().endsWith(nodes.get(nodes.size() - 1) + ".zip")) {
                zipsPath = f.getAbsolutePath();
            }
        }
        return zipsPath;
    }

    private void onCancel() {
        this.dispose();
    }

    private void quit() {
        this.dispatchEvent(new WindowEvent(this, 201));
    }

    private DefaultMutableTreeNode findChild(final String chlidName, final DefaultMutableTreeNode root) {
        DefaultMutableTreeNode node;
        @SuppressWarnings("unchecked") final Enumeration<TreeNode> e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            node = (DefaultMutableTreeNode) e.nextElement();
            if (chlidName.equals(node.getUserObject().toString())) {
                return node;
            }
        }
        return null;
    }

    private DefaultMutableTreeNode findChild2(final String chlidName, final DefaultMutableTreeNode root, final List<String> folders, final String templateName) {
        DefaultMutableTreeNode node;
        final DefaultMutableTreeNode n = null;
        final Enumeration e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            node = (DefaultMutableTreeNode) e.nextElement();
            if (chlidName.equals(node.getUserObject().toString())) {
                boolean a = true;
                final List<String> xixi = new ArrayList<>();
                for (final TreeNode tr : node.getPath()) {
                    if (!tr.toString().equals(root.getUserObject()) && !tr.toString().equals(chlidName)) {
                        xixi.add(tr.toString());
                    }
                }
                if (xixi.size() != folders.size()) {
                    a = false;
                }
                for (int i = 0; i < xixi.size(); ++i) {
                    if (!xixi.get(xixi.size() - 1 - i).equals(folders.get(i))) {
                        a = false;
                    }
                }
                if (a) {
                    return node;
                }
            }
        }
        return null;
    }

    private boolean findChildByParent(final List<String> folders, final DefaultMutableTreeNode node) {
        if (node == null) {
            return false;
        }
        DefaultMutableTreeNode n = null;
        boolean a = true;
        for (int i = 0; i < folders.size(); ++i) {
            if (i == 0) {
                n = (DefaultMutableTreeNode) node.getParent();
                if (!n.getUserObject().equals(folders.get(i))) {
                    a = false;
                }
            } else {
                n = (DefaultMutableTreeNode) n.getParent();
                if (!n.getUserObject().equals(folders.get(i))) {
                    a = false;
                }
            }
        }
        return a;
    }

    public ReturnValue getReturnValue() {
        return this.returnValue;
    }

    private boolean getDataRequirement() {
        final String templateName = this.textField1.getText().trim();
        final String adapterName = this.textField2.getText().trim();
        final String jobName = this.textField3.getText().trim();
        final String scriptsName = this.textField5.getText().trim();
        if (templateName.trim().length() == 0) {
            this.setFocus(this.textField1);
            PopupUtil.showBalloonForActiveComponent("Package Name must be supplied.", MessageType.ERROR);
            return false;
        }
        if (adapterName.trim().length() == 0) {
            this.setFocus(this.textField2);
            PopupUtil.showBalloonForActiveComponent("Adapter Name must be supplied.", MessageType.ERROR);
            return false;
        }
        if (jobName.trim().length() == 0) {
            this.setFocus(this.textField3);
            PopupUtil.showBalloonForActiveComponent("Job Name must be supplied.", MessageType.ERROR);
            return false;
        }
        if (scriptsName.trim().length() == 0) {
            this.setFocus(this.textField5);
            PopupUtil.showBalloonForActiveComponent("Scripts Name must be supplied.", MessageType.ERROR);
            return false;
        }
        final String regEx = "^+[A-Za-z0-9\\s?_-]{1,50}$";
        final Pattern pattern = Pattern.compile(regEx);
        final Matcher matcher = pattern.matcher(templateName);
        final boolean rsTemplateName = matcher.matches();
        final Matcher matcher2 = pattern.matcher(adapterName);
        final boolean rsAdapterName = matcher2.matches();
        final Matcher matcher3 = pattern.matcher(jobName);
        final boolean rsJobName = matcher3.matches();
        final Matcher matcher4 = pattern.matcher(scriptsName);
        final boolean rsScriptsName = matcher4.matches();
        if (!rsTemplateName) {
            this.setFocus(this.textField1);
            PopupUtil.showBalloonForActiveComponent("Package name should only contain English alphabets, numbers, blanks, '-' and '_'. And its length shouldn't be greater than 50.", MessageType.ERROR);
            return false;
        }
        if (!rsAdapterName) {
            this.setFocus(this.textField2);
            PopupUtil.showBalloonForActiveComponent("Adapter name should only contain English alphabets, numbers, '-' and '_'. And its length shouldn't be greater than 50.", MessageType.ERROR);
            return false;
        }
        if (!rsJobName) {
            this.setFocus(this.textField3);
            PopupUtil.showBalloonForActiveComponent("Job name should only contain English alphabets, numbers, '-' and '_'. And its length shouldn't be greater than 50.", MessageType.ERROR);
            return false;
        }
        if (!rsScriptsName) {
            this.setFocus(this.textField5);
            PopupUtil.showBalloonForActiveComponent("Script name should only contain English alphabets, numbers, '-' and '_'. And its length shouldn't be greater than 50.", MessageType.ERROR);
            return false;
        }
        return true;
    }

    private void setFocus(final JComponent component) {
        ApplicationManager.getApplication().invokeLater(component::requestFocus);
    }

    private void $$$setupUI$$$() {
        final JPanel contentPane = new JPanel();
        (this.contentPane = contentPane).setLayout(new GridLayoutManager(2, 2, JBUI.insets(10), -1, -1, false, false));
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 2, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(panel, new GridConstraints(1, 1, 1, 1, 0, 3, 7, 1, null, null, null));
        panel.add(new Spacer(), new GridConstraints(0, 0, 1, 1, 0, 1, 6, 1, null, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, JBUI.emptyInsets(), -1, -1, true, false));
        panel.add(panel2, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 3, null, null, null));
        final JButton buttonOK = new JButton();
        (this.buttonOK = buttonOK).setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null));
        final JButton buttonCancel = new JButton();
        (this.buttonCancel = buttonCancel).setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, null, null, null));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(panel3, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 7, null, null, null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 2, JBUI.insets(2), -1, -1, false, false));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 0, null, null, null));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(JBColor.BLACK), null, 0, 0, null, null));
        final JLabel label = new JLabel();
        label.setText("Package Name :");
        panel4.add(label, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, null, null));
        panel4.add(this.textField1 = new JTextField(), new GridConstraints(0, 1, 1, 1, 8, 1, 6, 0, null, new Dimension(100, -1), null));
        final JLabel label2 = new JLabel();
        label2.setText("$PATTERN_NAME :");
        panel4.add(label2, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, null, null, null));
        final JLabel label3 = new JLabel();
        label3.setText("$JOB_NAME :");
        panel4.add(label3, new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, null, null, null));
        final JLabel label4 = new JLabel();
        label4.setText("$SCRIPTS_NAME :");
        panel4.add(label4, new GridConstraints(3, 0, 1, 1, 8, 0, 0, 4, null, null, null));
        panel4.add(this.textField2 = new JTextField(), new GridConstraints(1, 1, 1, 1, 8, 1, 6, 0, null, new Dimension(100, -1), null));
        panel4.add(this.textField3 = new JBTextField(), new GridConstraints(2, 1, 1, 1, 8, 1, 6, 0, null, new Dimension(100, -1), null));
        final JTextField textField5 = new JTextField();
        (this.textField5 = textField5).setText("");
        panel4.add(textField5, new GridConstraints(3, 1, 1, 1, 8, 1, 6, 0, null, new Dimension(100, -1), null));
        panel4.add(this.textArea1 = new JTextArea(), new GridConstraints(4, 1, 1, 1, 0, 3, 6, 6, null, new Dimension(100, 50), null));
        final JLabel label5 = new JLabel();
        label5.setText("Description :");
        panel4.add(label5, new GridConstraints(4, 0, 1, 1, 8, 0, 0, 0, null, null, null));
        final JPanel pane3 = new JPanel();
        pane3.setLayout(new GridLayoutManager(1, 1, JBUI.insets(2, 0), -1, -1, false, false));
        panel3.add(pane3, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 7, null, null, null));
        final JBScrollPane scrollPane = new JBScrollPane();
        pane3.add(scrollPane, new GridConstraints(0, 0, 1, 1, 0, 3, 7, 7, null, null, null));
        this.tree1 = new Tree();
        this.tree1.putClientProperty("JTree.lineStyle", "");
        scrollPane.setViewportView(this.tree1);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(panel5, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, null, null));
        final JBScrollPane scrollPane2 = new JBScrollPane();
        panel5.add(scrollPane2, new GridConstraints(0, 0, 1, 1, 0, 3, 7, 7, null, new Dimension(256, 619), null));
        this.templateList = new JBList<>();
        this.templateList.setModel(new DefaultListModel<>());
        this.templateList.putClientProperty("List.isFileList", Boolean.FALSE);
        scrollPane2.setViewportView(this.templateList);
        panel5.add(new Spacer(), new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, null, null, null));
    }
}