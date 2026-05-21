package com;

import java.sql.*;
import java.util.ArrayList;

public class ExpenseManager {

    private ArrayList<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense e) {
        expenses.add(e);
        saveToDatabase(e);
    }

    public void deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
        }
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public double getTotalExpense() {
        double total = 0;
        for (Expense e : expenses) total += e.getAmount();
        return total;
    }

    public int getExpenseCount() {
        return expenses.size();
    }

    public double getCurrentMonthTotal() {
        double total = 0;

        java.time.LocalDate now = java.time.LocalDate.now();
        String currentMonth = String.format("%02d/%d",
                now.getMonthValue(),
                now.getYear());

        for (Expense e : expenses) {
            String[] parts = e.getDate().split("/");
            String monthYear = parts[1] + "/" + parts[2];

            if (monthYear.equals(currentMonth)) {
                total += e.getAmount();
            }
        }
        return total;
    }

    public ArrayList<Expense> search(String keyword) {
        ArrayList<Expense> result = new ArrayList<>();

        for (Expense e : expenses) {
            if (e.getCategory().toLowerCase().contains(keyword.toLowerCase()) ||
                e.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(e);
            }
        }
        return result;
    }

    public double getMonthlyTotal(String monthYear) {
        double total = 0;

        for (Expense e : expenses) {
            String[] parts = e.getDate().split("/");
            String monthYearData = parts[1] + "/" + parts[2];

            if (monthYearData.equals(monthYear)) {
                total += e.getAmount();
            }
        }
        return total;
    }

    // ================= DATABASE =================

    private void saveToDatabase(Expense e) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO expenses(category, amount, date, description) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, e.getCategory());
            ps.setDouble(2, e.getAmount());
            ps.setString(3, e.getDate());
            ps.setString(4, e.getDescription());

            ps.executeUpdate();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadFromDatabase() {
        try {
            Connection conn = DBConnection.getConnection();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM expenses");

            expenses.clear();

            while (rs.next()) {
                Expense e = new Expense(
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("date"),
                        rs.getString("description")
                );
                expenses.add(e);
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

