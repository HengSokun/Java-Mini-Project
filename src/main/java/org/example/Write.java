package org.example;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.example.Main.*;


public class Write {
    public Scanner scanner = new Scanner(System.in);
    public  String  name;
    public  int price;
    int qty;
    public String formattedDate;
    public static ArrayList<Prodact> unsave = new ArrayList<>();
//    public String importdate;
    public void Insert(){
        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
        System.out.print(ANSI_PURPLE + "Name : ");
        name = scanner.nextLine();
        price = Integer.parseInt(Main.validateNumberInput(ANSI_PURPLE + "Price : "));
        qty = Integer.parseInt(Main.validateNumberInput(ANSI_PURPLE + "Qty : "));
        System.out.print(ANSI_PURPLE + "Date : %s".formatted(ANSI_GREEN));
        datetime();
        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");

        Prodact prodact = new Prodact(name,price,qty,formattedDate);
        unsave.add(prodact);
//        System.out.println(unsave);
    }
    public void unsave() throws SQLException {
        if (unsave.isEmpty()){
            System.out.println("not data un-save!");
        }
    else {
            Table t = new Table(4, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
            t.addCell("Name", cellStyle);
            t.addCell("Unit Price", cellStyle);
            t.addCell("Stock Quantity", cellStyle);
            t.addCell("Import Date", cellStyle);

            for (Prodact prodact : unsave) {
                t.addCell(prodact.getName());
                t.addCell(String.valueOf(prodact.getPrice()));
                t.addCell(String.valueOf(prodact.getQty()));
                t.addCell(prodact.getDate());
            }
            System.out.println(t.render());
        }
    }
    public void save() throws SQLException {

//        Statement statement = DBConn.getConnection().createStatement();
            String add = "INSERT INTO products(name,unitprice,stockqty,importdate) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = DBConn.getConnection().prepareStatement(add);
        for (Prodact prodact : unsave) {
            preparedStatement.setString(1, prodact.getName());
            preparedStatement.setInt(2, prodact.getPrice());
            preparedStatement.setInt(3, prodact.getQty());
            preparedStatement.setString(4, prodact.getDate());

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("Insert successfully");
            }

        }

            unsave.clear();

//        Table t = new Table(4, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
//        t.addCell("Name", cellStyle);
//        t.addCell("Unit Price", cellStyle);
//        t.addCell("Stock Quantity", cellStyle);
//        t.addCell("Import Date", cellStyle);
//        for (int i = 0; i<unsave.size();i++){
//            t.addCell(unsave.get(i).getName());
//            t.addCell(String.valueOf(unsave.get(i).getPrice()));
//            t.addCell(String.valueOf(unsave.get(i).getQty()));
//            t.addCell(unsave.get(i).getDate());
//        }
//        System.out.println(t.render());
    }
 void datetime(){
     LocalDate currentDate = LocalDate.now();

     // Define date format pattern
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

     // Format date as string
     formattedDate = currentDate.format(formatter);

     // Print formatted date
     System.out.println(formattedDate);
    }
}
