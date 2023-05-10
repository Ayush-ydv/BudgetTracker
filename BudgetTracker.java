import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class BudgetTracker extends JFrame implements ActionListener {

    private JLabel lblDate, lblCategory, lblAmount;
    private JTextField txtDate, txtCategory, txtAmount;
    private JButton btnSave, btnView;
    
    
    private Connection conn;
    
    public BudgetTracker() throws ClassNotFoundException {
       
        lblDate = new JLabel("Date (YYYY-MM-DD):");
        lblCategory = new JLabel("Category:");
        lblAmount = new JLabel("Amount:");
        txtDate = new JTextField(10);
        txtCategory = new JTextField(10);
        txtAmount = new JTextField(10);
        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        btnView = new JButton("View");
        btnView.addActionListener(this);
        
      
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(lblDate);
        inputPanel.add(txtDate);
        inputPanel.add(lblCategory);
        inputPanel.add(txtCategory);
        inputPanel.add(lblAmount);
        inputPanel.add(txtAmount);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSave);
        buttonPanel.add(btnView);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setTitle("Budget Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	String con__string = "jdbc:mysql://localhost/ayush?" + "user=root&password=123Manshu@25";
			
			conn = DriverManager.getConnection(con__string);
			
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == btnSave) {
            
            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO budget (date, category, amount) VALUES (?, ?, ?)");
                stmt.setString(1, txtDate.getText());
                stmt.setString(2, txtCategory.getText());
                stmt.setDouble(3, Double.parseDouble(txtAmount.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Budget data saved.");
                txtDate.setText("");
                txtCategory.setText("");
                txtAmount.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnView) {
           
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM budget");
                JTable table = new JTable(buildTableModel(rs));
                JOptionPane.showMessageDialog(this, new JScrollPane(table));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
       
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        DefaultTableModel tableModel = new DefaultTableModel();
        for (int i = 1; i <= columnCount; i++) {
        	tableModel.addColumn(metaData.getColumnName(i));
        	}
        	
        	while (rs.next()) {
        	Object[] row = new Object[columnCount];
        	for (int i = 1; i <= columnCount; i++) {
        	row[i - 1] = rs.getObject(i);
        	}
        	tableModel.addRow(row);
        	}
        	return tableModel;
        	}
    
    public static void main(String[] args) throws ClassNotFoundException {
        BudgetTracker budgetTracker = new BudgetTracker();
        budgetTracker.setVisible(true);
    }
}

