package courseman2.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import courseman2.DomainConstraint;
import courseman2.DomainConstraint.Type;

/**
 * @overview
 *  A sub-type of <tt>JTable</tt> that helps ease the task of manipulating the table data. 
 * 
 * @attributes
 *  model   EasyTable.EasyTableModel
 *  
 * @abstract_properties
 * <pre>
 *  optional(model) = false
 * </pre>
 *  
 * @usage 
 *  The following code example demonstrates how to use this class.
 *  
 *  <p>First, there are two methods of creating an <tt>EasyTable</tt> object. Both 
 *  methods require a <tt>String[]</tt> array as input for the table headers. 
 *  One method require an extra input of type <tt>List&lt;List&gt;</tt> (i.e. list of lists) 
 *  for the table data. Each element of this list is a <tt>List</tt> object, containing
 *  data for a table row. All these <tt>List</tt> objects must have the same size, which must 
 *  also be equal to the number of table headers.
 *  
 *  <p>Suppose you have the table headers as <tt>String[] headers</tt> and the table data
 *  as <tt>List&lt;List&gt; data</tt>. The first method works as follows:
 *  <br><pre>EasyTable table = new EasyTable(headers);</pre>
 *  
 *  <br>While the second method: 
 *  <br><pre>EasyTable table = new EasyTable(data, headers);</pre>
 *  
 *  <p>After creating <tt>table</tt>, you can add it to a <tt>JPanel</tt> or a <tt>JFrame</tt> window as you would 
 *  with other GUI components.
 *  
 *  <p>Now, one basic operation that you can perform with <tt>table</tt> is to 
 *  add a data row to it. Suppose <tt>table</tt> has 5 columns and 
 *  you want to add a data row whose values are represented by 
 *  the variables <tt>v1, v2, v3, v4, v5</tt> (these variables
 *  are initialised somewhere in your application). Use this code: 
 *   
 *  <p>
 *  <br><pre>
 *    int newRowIndex = table.addRow();
 *    table.setValueAt(v1,newRowIndex,0); // 1st column
 *    table.setValueAt(v2,newRowIndex,1); // 2nd column
 *    table.setValueAt(v3,newRowIndex,2); // 3rd column
 *    table.setValueAt(v4,newRowIndex,3); // 4th column
 *    table.setValueAt(v5,newRowIndex,4); // 5th column
 *  </pre>
 *    
 * @author dmle
 */
public class EasyTable extends JTable {
  @DomainConstraint(type=Type.UserDefined,optional=false)
  private EasyTableModel model;
  
  /**
   * @requires
   *  headers != null /\ length(headers) > 0
   *  
   * @effects
   *  initialise this as <tt>EasyTable<m></tt>, 
   *    where <tt>m = EasyTableModel<data,headers></tt>
   */
  public EasyTable(List<List> data, String[] headers) {
    super();
    model = new EasyTableModel(data, headers);
    setModel(model);
  }
  
  /**
   * @requires
   *  headers != null /\ length(headers) > 0
   *  
   * @effects
   *  initialise this as <tt>EasyTable<m></tt>, 
   *    where <tt>m = EasyTableModel<null,headers></tt>
   */  
  public EasyTable(String[] headers) {
    this(null, headers);
  }
  
  /**
   * Add an empty row to this table
   * 
   * @effects
   *  add an empty data row to <tt>this.model</tt>
   *  and update <tt>this</tt>'s GUI view 
   */
  public int addRow() {
    return model.addRow();
  }
  
  /**
   * @effects
   *  clear all data rows of <tt>this.model</tt>
   *  and update <tt>this</tt>'s GUI view 
   */
  public void clear() {
    model.clearData();
  }
  
  /**
   * @overview
   *  A helper sub-type of <tt>AbstractTableModel</tt> that represents the data model 
   *  of <tt>EasyTable</tt>
   *  
   * @attributes
   *  data      List<List>
   *  headers   String[]
   * 
   * @abstract_properties
   * <pre>
   *  P_AbstractTableModel /\ 
   *  optional(data) = true /\ optional(headers) = false /\
   * (A): 
   *  size(data) > 0 -> 
   *    (for each r in data: 
   *      List(r) /\ size(r) = length(headers))
   * </pre>
   * <p>i.e. 
   * <br>(A):
   * elements of <tt>data</tt> are <tt>List</tt> objects, the size of each of which is equal to 
   * the number of table headers
   *  
   * @author dmle
   */
  private static class EasyTableModel extends AbstractTableModel {
    private List<List> data;
    private List<String> headers;
    
    /**
     *  @requires
     *    headers != null /\ length(headers) > 0
     */
    public EasyTableModel(List<List> data, String[] headers) {
      this.data = new LinkedList<List>();
      if (data != null)
        this.data.addAll(data);
      
      this.headers = new ArrayList(headers.length);
      Collections.addAll(this.headers, headers);
    }
    
    @Override
    public int getRowCount() {
      return data.size();
    }

    @Override
    public int getColumnCount() {
      return headers.size();
    }
    
    @Override 
    public String getColumnName(int column) {
      return headers.get(column);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      List row = (List) data.get(rowIndex);
      
      row.set(columnIndex, aValue);
      
      fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      return data.get(rowIndex).get(columnIndex);
    }

    /**
     * @effects
     *  add a new row to <tt>this</tt> as a <tt>List</tt> containing <tt>null</tt> objects
     *  <br>fire data change event to update the table's GUI view
     */
    public int addRow() {
      int numCols = getColumnCount();
      List newRow = new ArrayList(numCols);
      
      for (int i = 0; i < numCols; i++) {
        newRow.add(null);
      }
      
      data.add(newRow);
      
      fireTableDataChanged();
      
      return data.size()-1;
    }
    
    /**
     * @effects
     *  remove all elements of <tt>data</tt>
     *  <br>fire data change event to update the table's GUI view
     */
    void clearData() {
      data.clear();
      
      fireTableDataChanged();
    }
  } // end EasyTableModel
} // end EasyTable
