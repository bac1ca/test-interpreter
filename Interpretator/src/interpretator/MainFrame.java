package interpretator;

import interpretator.editor.DocumentContext;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author alex
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        this.editorPane.setEditorKit(new MyEditorKit());
        Output.getInstance().setOutputPane(outputPane);
        ErrorHighlighter.getInstance().setOutputPane(editorPane);
        StatusLine.getInstance().setStatusLine(messageLabel);
        editorPane.getDocument().addDocumentListener(new DocumentListenerImpl(editorPane));
        editorPane.addCaretListener(new CaretListenerImpl(editorPane));
        initActionsMap();
    }
    
    private void initActionsMap(){
        undoManager = new UndoManager();
        Document doc = editorPane.getDocument();
        doc.addUndoableEditListener(new UndoHandler());
        
        undoAction = new UndoAction();
        editorPane.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "undoKeystroke");
        editorPane.getActionMap().put("undoKeystroke", undoAction);

        redoAction = new RedoAction();
        editorPane.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "redoKeystroke");
        editorPane.getActionMap().put("redoKeystroke", redoAction);
        
        editorPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopUp(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopUp(e);
                }
            }
            
            private void showPopUp(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu();
                menu.add(undoAction);
                menu.add(redoAction);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        editorPane = new MyJTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputPane = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        positionLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadItem = new javax.swing.JMenuItem();
        saveItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(600, 400));

        editorPane.setFont(new java.awt.Font("Monospaced", 0, 15)); // NOI18N
        jScrollPane1.setViewportView(editorPane);

        jSplitPane1.setTopComponent(jScrollPane1);

        outputPane.setEditable(false);
        outputPane.setFont(new java.awt.Font("Monospaced", 0, 15)); // NOI18N
        jScrollPane2.setViewportView(outputPane);

        jSplitPane1.setRightComponent(jScrollPane2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        positionLabel.setPreferredSize(new java.awt.Dimension(60, 15));
        jPanel1.add(positionLabel, java.awt.BorderLayout.EAST);
        jPanel1.add(messageLabel, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        fileMenu.setText("File");

        loadItem.setText("Open...");
        loadItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadItem);

        saveItem.setText("Save As...");
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveItem);

        mainMenuBar.add(fileMenu);

        jMenu1.setText("Sample");

        jMenuItem1.setText("Pi");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("e");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("sqrt(2)");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        mainMenuBar.add(jMenu1);

        setJMenuBar(mainMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Document doc = editorPane.getDocument();
        try {
            doc.remove(0, doc.getLength());
            doc.insertString(0, 
                    "var n = 500\n" +
                    "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                    "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                    "print \"pi = \"\n" +
                    "out pi", null);
        } catch (BadLocationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void loadItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadItemActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                StringBuilder buf = new StringBuilder();
                int c;
                while((c = reader.read()) != -1) {
                    buf.append((char)c);
                }
                Document doc = editorPane.getDocument();
                try {
                    doc.remove(0, doc.getLength());
                    doc.insertString(0,buf.toString(), null);
                } catch (BadLocationException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_loadItemActionPerformed

    private void saveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveItemActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save");
        chooser.setApproveButtonText("Save");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                Document doc = editorPane.getDocument();
                try {
                    writer.write(doc.getText(0, doc.getLength()));
                    writer.flush();
                } catch (BadLocationException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_saveItemActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Document doc = editorPane.getDocument();
        try {
            doc.remove(0, doc.getLength());
            doc.insertString(0, 
                    "var n = 20\n" +
                    "var e = reduce(map({1, n},\n" +
                    "               x -> {1, x}), 1,\n" +
                    "               x y -> x + 1 / reduce(y, 1.0,\n" +
                    "               x y -> x * y))\n" +
                    "print \"e = \"\n" +
                    "out e", null);
        } catch (BadLocationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Document doc = editorPane.getDocument();
        try {
            doc.remove(0, doc.getLength());
            doc.insertString(0, 
                    "var n = 5\n" +
                    "var sqrt = reduce(map({1, n}, \n" +
                    "           x -> 2.0), 1, \n" +
                    "           x y -> (x + y/x)/2)\n" +
                    "print \"sqrt(2) = \"\n" +
                    "out sqrt", null);
        } catch (BadLocationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane editorPane;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem loadItem;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextPane outputPane;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JMenuItem saveItem;
    // End of variables declaration//GEN-END:variables

    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;
    
    private class CaretListenerImpl implements CaretListener {
        private final JTextPane editor;

        private CaretListenerImpl(JTextPane editor) {
            this.editor = editor;
        }

        @Override
        public void caretUpdate(CaretEvent e) {
            int dot = e.getDot();
            Document doc = editor.getDocument();
            doc.render(() -> {
                try {
                    String text = doc.getText(0, doc.getLength());
                    DocumentContext context = new DocumentContext(text);
                    final int[] position = context.getRowCol(dot);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            positionLabel.setText(""+position[0]+":"+position[1]);
                        }
                    });
                } catch (BadLocationException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
    
    private static final class MyJTextPane extends JTextPane {

        @Override
        public boolean getScrollableTracksViewportWidth() {
            Component parent = getParent();
            ComponentUI ui = getUI();
            return parent != null ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
        }
    }
    
    private class UndoHandler implements UndoableEditListener {

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            undoManager.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }
    }

    private class UndoAction extends AbstractAction {

        private UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            update();
            redoAction.update();
        }

        protected void update() {
            setEnabled(undoManager.canUndo());
        }
    }

    private class RedoAction extends AbstractAction {

        private RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            update();
            undoAction.update();
        }

        protected void update() {
            setEnabled(undoManager.canRedo());
        }
    }
}