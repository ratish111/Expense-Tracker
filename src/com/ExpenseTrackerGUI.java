package com;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

public class ExpenseTrackerGUI extends JFrame {

    private ExpenseManager manager = new ExpenseManager();
    private DefaultTableModel tableModel;

    private JLabel totalLabel;
    private JLabel monthLabel;
    private JLabel countLabel;

    public ExpenseTrackerGUI() {

        manager.loadFromDatabase();

        setTitle("💰 Expense Tracker");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // ================= DASHBOARD =================
        JPanel dashboard = new JPanel(new GridLayout(1,3,10,10));

        totalLabel = new JLabel("₹0", SwingConstants.CENTER);
        monthLabel = new JLabel("₹0", SwingConstants.CENTER);
        countLabel = new JLabel("0", SwingConstants.CENTER);

        dashboard.add(createCard("Total Expense", totalLabel));
        dashboard.add(createCard("This Month", monthLabel));
        dashboard.add(createCard("Total Entries", countLabel));

        add(dashboard, BorderLayout.NORTH);

        // ================= MODERN FORM =================
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Add Expense"));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 13);

        JComboBox<String> category = new JComboBox<>(new String[]{
                "Food","Travel","Bills","Shopping","Other"
        });

        JTextField amount = new JTextField();
        JTextField desc = new JTextField();

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(new java.util.Date());

        category.setFont(inputFont);
        amount.setFont(inputFont);
        desc.setFont(inputFont);

        // Sizes
        Dimension fieldSize = new Dimension(220, 30);
        Dimension amountSize = new Dimension(260, 28);
        Dimension dateSize = new Dimension(140, 22);
        Dimension descSize = new Dimension(220, 40);

        category.setPreferredSize(fieldSize);
        amount.setPreferredSize(amountSize);
        dateChooser.setPreferredSize(dateSize);
        desc.setPreferredSize(descSize);

        // Styling borders
        amount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5,10,5,10)
        ));

        desc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5,10,5,10)
        ));

        // ===== ROW 1 =====
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel l1 = new JLabel("Category");
        l1.setFont(labelFont);
        form.add(l1, gbc);

        gbc.gridx = 1;
        form.add(category, gbc);

        // ===== ROW 2 =====
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel l2 = new JLabel("Amount");
        l2.setFont(labelFont);
        form.add(l2, gbc);

        gbc.gridx = 1;
        form.add(amount, gbc);

        // ===== ROW 3 =====
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel l3 = new JLabel("Date");
        l3.setFont(labelFont);
        form.add(l3, gbc);

        gbc.gridx = 1;
        form.add(dateChooser, gbc);

        // ===== ROW 4 =====
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel l4 = new JLabel("Description");
        l4.setFont(labelFont);
        form.add(l4, gbc);

        gbc.gridx = 1;
        form.add(desc, gbc);

        add(form, BorderLayout.WEST);

        // ================= TABLE =================
        tableModel = new DefaultTableModel(
                new String[]{"Category","Amount","Date","Description"},0);

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= BOTTOM =================
        JPanel bottom = new JPanel();

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton total = new JButton("Total");
        JButton report = new JButton("Report");

        JTextField searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        JButton showAll = new JButton("Show All");

        bottom.add(add);
        bottom.add(delete);
        bottom.add(total);
        bottom.add(report);
        bottom.add(searchField);
        bottom.add(searchBtn);
        bottom.add(showAll);

        add(bottom, BorderLayout.SOUTH);

        loadTable(manager.getExpenses());
        updateDashboard();

        // ================= ADD =================
        add.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String date = sdf.format(dateChooser.getDate());

                Expense ex = new Expense(
                        (String)category.getSelectedItem(),
                        Double.parseDouble(amount.getText()),
                        date,
                        desc.getText()
                );

                manager.addExpense(ex);
                loadTable(manager.getExpenses());
                updateDashboard();

                amount.setText("");
                desc.setText("");
                dateChooser.setDate(new java.util.Date());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,"Invalid input!");
            }
        });

        // DELETE
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                manager.deleteExpense(row);
                loadTable(manager.getExpenses());
                updateDashboard();
            }
        });

        // TOTAL
        total.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Total: ₹"+manager.getTotalExpense());
        });

        // SEARCH
        searchBtn.addActionListener(e -> {
            loadTable(manager.search(searchField.getText()));
        });

        // SHOW ALL
        showAll.addActionListener(e -> {
            loadTable(manager.getExpenses());
        });

        // REPORT
        report.addActionListener(e -> {
            String m = JOptionPane.showInputDialog("Enter month MM/YYYY");
            JOptionPane.showMessageDialog(this,
                    "Total: ₹"+manager.getMonthlyTotal(m));
        });
    }

    private JPanel createCard(String title, JLabel value){
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(title));
        value.setFont(new Font("Segoe UI",Font.BOLD,20));
        p.add(value,BorderLayout.CENTER);
        return p;
    }

    private void updateDashboard(){
        totalLabel.setText("₹"+manager.getTotalExpense());
        monthLabel.setText("₹"+manager.getCurrentMonthTotal());
        countLabel.setText(""+manager.getExpenseCount());
    }

    private void loadTable(java.util.ArrayList<Expense> list){
        tableModel.setRowCount(0);
        for(Expense e:list){
            tableModel.addRow(new Object[]{
                    e.getCategory(),
                    e.getAmount(),
                    e.getDate(),
                    e.getDescription()
            });
        }
    }
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new WelcomeScreen().setVisible(true);
    });
}
}
