/*
 * FilteredJTable.java
 *
 * Created on 31. Juli 2007, 12:22
 */

package net.java.nboglpack.glcapabilities;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;

/**
 * Sorted JTable with filter capability.
 * @author Michael Bien
 */
public class FilteredTable extends javax.swing.JPanel {
    
    /** Creates new form FilteredJTable */
    public FilteredTable() {
        initComponents();
        createFilter(table, filterField);
    }
    
    
    public JTable getTable() {
        return table;
    }
    
    private void createFilter(JTable table, JTextField searchField) {
        
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateSearch(e.getDocument());
            }

            public void removeUpdate(DocumentEvent e) {
                updateSearch(e.getDocument());
            }
            
            private void updateSearch(Document doc) {
                
                try {
                    final String[] search = doc.getText(0, doc.getLength()).toLowerCase().trim().split(" ");
                    
                    if(search.length == 0) {
                        sorter.setRowFilter(null);
                        return;
                    }
                    
                    sorter.setRowFilter(new RowFilter<Object, Object>() {
                        
                        public boolean include(Entry<? extends Object, ? extends Object> entry) {
                            
                            for (int i = 0; i < entry.getValueCount(); i++) {
                                boolean contains = true;
                                for(int n = 0; n < search.length; n++){
                                    if (!entry.getStringValue(i).toLowerCase().contains(search[n])){
                                        contains = false;
                                        break;
                                    }
                                }
                                if(contains)
                                    return true;
                            }
                            
                            return false;
                        }
                                                
                    });
                } catch (BadLocationException ex) {
                    sorter.setRowFilter(null);
                    Exceptions.printStackTrace(ex);
                }
            }

            public void changedUpdate(DocumentEvent e) {}
            
        });
    }    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterField = new javax.swing.JTextField();
        javax.swing.JLabel filterLabel = new javax.swing.JLabel();
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        filterLabel.setText(org.openide.util.NbBundle.getMessage(FilteredTable.class, "FilteredTable.filterLabel.text")); // NOI18N

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        scrollPane.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filterLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterField, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterLabel)
                    .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField filterField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    
}
