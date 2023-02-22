package org.example;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {

    public static Scanner input = new Scanner(System.in);
    public static List<StockManagement> stockManagement= new ArrayList<>();
    public static int id;
    public static String op;
    static boolean inp;
    static int row,go, n =1, page ,limit=3,offset=0;
    static String value;
    public static CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
    static  StockManagement stockManagements= new StockManagement();
    static DBConn con = new DBConn();
    static String productName;
    static double unitPrice;
    static int quantity;

    static LocalDateTime localeDateTime= LocalDateTime.now();
    static DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd MM YYYY");
    static String dateformat = localeDateTime.format(dtf);


    //---------------------Color------------------------//
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    //-------------------------------------------------//

    public static String validateLetterInput(String title) {
        do {
            System.out.print(title);
            value = input.nextLine();
            String PATTERN = "[a-zA-Z\sa-zA-z]+";
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(value);
            inp = matcher.matches();
            if (!inp) {
                System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                System.out.println(ANSI_RED + "You can input only letter!");
                System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            }
        } while (!(inp));
        return value;
    }
    public static String validateNumberInput(String title) {

        do {
            System.out.print(title);
            value = input.nextLine();
            String PATTERN = "\\d{1,10}";
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(value);
            inp = matcher.matches();
            if (!inp) {
                System.out.println(ANSI_CYAN +  "------------------------------------------------------------------------------------------------------------");
                System.out.println(ANSI_RED + "You can input only number!");
                System.out.println(ANSI_CYAN +  "------------------------------------------------------------------------------------------------------------");
            }
        } while (!(inp));
        return value;
    }
    public static String validateDoubleInput(String title) {

        do {
            System.out.print(title);
            value = input.nextLine();
            String PATTERN = "[0-9]{1,13}(\\.[0-9]*)?";
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(value);
            inp = matcher.matches();
            if (!inp) {
                System.out.println("You can input only number!");
            }
        } while (!(inp));
        return value;
    }
    public static void Menu(){

        System.out.println("%s---------------------------------------- %sPRODUCT MANAGEMENT %s------------------------------------------------".formatted(ANSI_PURPLE, ANSI_GREEN, ANSI_PURPLE));
        Table table_m = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.SURROUND);

        table_m.addCell("   %sW. Write          ".formatted(ANSI_CYAN),  cellStyle);
        table_m.addCell( " %s| R. Read           ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| U. Update         ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| D. Delete         ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| S. Search         ".formatted(ANSI_CYAN) ,  cellStyle);

        table_m.addCell( " %s  F. First          ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| P. Previous       ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| N. Next           ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| L. Last           ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| G. Goto           ".formatted(ANSI_CYAN) ,  cellStyle);

        table_m.addCell( " %s  Sa. Save          ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| Un. Unsaved        ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| Ba. Backup        ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| Re. Restore       ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( " %s| Se. Set           ".formatted(ANSI_CYAN) ,  cellStyle);
        table_m.addCell( "   %sE. Exit          ".formatted(ANSI_RED) ,  cellStyle);
        System.out.println(table_m.render());
        op = validateLetterInput("%sPlease choose Your Option: ".formatted(ANSI_PURPLE));
    }
    public static void Write() throws SQLException, IOException {
        Write write = new Write();
        write.Insert();
        Display();

    }
//    public static void Read(){}
//    public static void Update(){}
//    public static void Delete(){}
//    public static void First(){}
//    public static void Previous(){}
//    public static void Next(){}
//    public static void Last(){}
//    public static void Search(){}
//    public static void Goto(){}
    public static void Save() throws SQLException {
        Write write = new Write();
        write.save();
    }
    public static void Unsaved(){
        Write write = new Write();
        try {
            write.unsave();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    public static void SetRow(){}
//    public static void Write(){}
    public static void Read(){

        //  Sokun Sonamheng
        System.out.printf("%s-------------------------------------------- %sREAD PRODUCT %s--------------------------------------------------%n", ANSI_CYAN, ANSI_GREEN, ANSI_CYAN);
        String pID = validateNumberInput(ANSI_PURPLE + "Input ID to show Product: ");
        System.out.printf("%s------------------------------------------------------------------------------------------------------------%n", ANSI_CYAN);

        /* Display selected product */
        Table tProduct = new Table(1, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.SURROUND);

        String queryProduct = "SELECT * FROM products WHERE id = %s".formatted(pID);
        try(Statement statement = DBConn.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(queryProduct)
            ){
            tProduct.addCell("PRODUCT", cellStyle);
            if (rs.next()){
                do{
                    tProduct.addCell("ID: " + rs.getInt("id"));
                    tProduct.addCell("Name: " + rs.getString("name"));
                    tProduct.addCell("Unit Price: " + rs.getInt("unitprice"));
                    tProduct.addCell("Stock Quantity: " + rs.getInt("stockqty"));
                    tProduct.addCell("Date: " + rs.getDate("importdate"));
                }while (rs.next());
            }else {
                tProduct.addCell("Record Not Found!", cellStyle);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(tProduct.render());
    }
//    public static void Update(){}
//    public static void Delete(){}

    public static int DisAllProduct() throws SQLException {
        Statement statement = DBConn.getConnection().createStatement();
        String total = "SELECT COUNT(*) FROM products";
        ResultSet rss = statement.executeQuery(total);
        while (rss.next()) {
            return rss.getInt("count");
        }
        return 0;
    }
    public static void Next() throws SQLException, IOException {
        if(n == page){
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            System.out.println(ANSI_RED + "=>=>=> It's already the last page.");
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");

            Display();
        }
        else {
            offset += limit;
            n++;
            Display();
        }
    }
    public static void Previous() throws SQLException, IOException {
        offset -= limit;
        if (offset < 0) {
            offset = 0;
            n = 1;
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            System.out.println(ANSI_RED + "=>=>=> It's already the first page.");
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            Display();
        } else {
            if (n != 0) {
                n--;
                Display();
            }
        }
    }
    public static void First() throws SQLException, IOException {
        offset = 0;
        n = 1;
        Display();
    }
    public static void Last() throws SQLException, IOException {
        offset = DisAllProduct() - (DisAllProduct() % limit);
        n = page;

        if(DisAllProduct() % limit == 0 ){
            offset = DisAllProduct() - limit;
        }
        Display();
    }
//    public static void Search(){}
    public static void Goto() throws SQLException, IOException {
        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
        go = Integer.parseInt(validateNumberInput(ANSI_PURPLE + "Enter page : "));
        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");

        if(go<=page){
            if(go==1){
                n=go;
                First();
            }else if(go == page){
                n=go;
                Last();
            }else{
                n=go;
                offset = (limit*go)-limit;
                Display();
            }
        }else{
            Table t = new Table(5, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
            t.addCell("ID", cellStyle);
            t.addCell("Name", cellStyle);
            t.addCell("Unit Price", cellStyle);
            t.addCell("Stock Quantity", cellStyle);
            t.addCell("Import Date", cellStyle);
            t.addCell("  There is no such a page u wish to go to!",5);
            System.out.println(t.render());
        }
    }

    public static void Search(){

        //  Sokun Sonamheng
        System.out.printf("%s-------------------------------------------- %sSEARCH PRODUCT %s------------------------------------------------%n", ANSI_CYAN, ANSI_GREEN, ANSI_CYAN);
        String seName = validateLetterInput(ANSI_PURPLE + "Input product's name to search: ");

        /* Display selected product */
        Table sePro = new Table(5, BorderStyle.UNICODE_ROUND_BOX, ShownBorders.ALL);

        String querySearchProduct = "SELECT * FROM products WHERE name iLIKE '%"+"%s".formatted(seName)+"%'";
        try(Statement statement = DBConn.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(querySearchProduct)
        ){
            sePro.addCell("ID ", cellStyle);
            sePro.addCell("Name", cellStyle);
            sePro.addCell("Unit Price", cellStyle);
            sePro.addCell("Stock Quantity", cellStyle);
            sePro.addCell("Imported Date", cellStyle);
            if (rs.next()){
                while (rs.next()) {
                    sePro.addCell(String.valueOf(rs.getInt("id")), cellStyle);
                    sePro.addCell(rs.getString("name"));
                    sePro.addCell(String.valueOf(rs.getInt("unitprice")), cellStyle);
                    sePro.addCell(String.valueOf(rs.getInt("stockqty")), cellStyle);
                    sePro.addCell(String.valueOf(rs.getDate("importdate")), cellStyle);
                }
            } else {
                sePro.addCell("Record Not Found!", cellStyle,5);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sePro.render());
    }

    public static void SetRow() throws IOException, SQLException {
        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
        row = Integer.parseInt(validateNumberInput(ANSI_PURPLE + "Enter Row to Set: "));
        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");

        try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/set_row.txt")) {
            fileWriter.write(row);
            offset = 0;
            n = 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Display();
    }

    public static void Update() throws ClassNotFoundException, SQLException {
        id = Integer.parseInt(validateNumberInput(ANSI_PURPLE + "Please Input ID: "));
        String SElectUpdate = "SELECT * FROM products WHERE id=?";
        PreparedStatement preparedStatement = con.connDB().prepareStatement(SElectUpdate);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            productName = validateLetterInput(ANSI_PURPLE + "Please Enter Name: ");
            unitPrice= Double.parseDouble(validateDoubleInput(ANSI_PURPLE + "Please enter the Unit price: "));
            quantity= Integer.parseInt(validateNumberInput(ANSI_PURPLE + "Enter the stock Quantity: "));
            System.out.print(ANSI_PURPLE + "Date: %s".formatted(ANSI_GREEN));
            System.out.println(dateformat);
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            stockManagement.add(new StockManagement(id, productName, unitPrice, quantity));
        }else {
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            System.out.println(ANSI_RED + "The Data has no this id, Please Check id again.......!");
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            int up = Integer.parseInt(validateNumberInput("Press 1 to Update Again or Press 0 to exit: "));
            if (up == 1) {
                Update();
            }

        }
    }

    public static void UpSave() throws SQLException {
        //  System.out.println(stockManagement);

            try {
                String queryUpdate = "UPDATE products SET  name=?, unitprice=?, stockqty=? WHERE id=?";
                PreparedStatement preparedStatements = con.connDB().prepareStatement(queryUpdate);
                for (int i = 0; i < stockManagement.size(); i++) {
                    preparedStatements.setString(1, stockManagement.get(i).getName());
                    preparedStatements.setDouble(2, stockManagement.get(i).unitPrice);
                    preparedStatements.setInt(3, stockManagement.get(i).quantity);
                    preparedStatements.setInt(4, stockManagement.get(i).id);
                    preparedStatements.executeUpdate();
                    System.out.println("Update Successfully !");
                    stockManagement.remove(i);
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    public static void UpUnsaved() throws Exception {
                CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
                table.setColumnWidth(0, 15, 15);
                table.setColumnWidth(1, 15, 15);
                table.setColumnWidth(2, 15, 15);
                table.setColumnWidth(3, 15, 15);
                table.setColumnWidth(4, 15, 15);
                table.addCell("ID", numberStyle);
                table.addCell("name", numberStyle);
                table.addCell("unitPrice", numberStyle);
                table.addCell("Quantity", numberStyle);
                table.addCell("Date: ");

                for(StockManagement stock:stockManagement){
                    table.addCell(String.valueOf(stock.getId()),numberStyle);
                    table.addCell(stock.getName(),numberStyle);
                    table.addCell(String.valueOf(stock.getUnitPrice()),numberStyle);
                    table.addCell(String.valueOf(stock.getQuantity()),numberStyle);
                    table.addCell(dateformat,numberStyle);
                }
                System.out.println(table.render());
    }

    public static void Delete() throws SQLException, IOException {

        Table tProduct = new Table(1, BorderStyle.UNICODE_ROUND_BOX, ShownBorders.ALL);

        boolean b = true;
        int deleteId = 0;
        while (b) {
            System.out.printf("%s-------------------------------------------- %sDELETE PRODUCT %s------------------------------------------------%n", ANSI_CYAN, ANSI_GREEN, ANSI_CYAN);
            deleteId = Integer.parseInt(validateNumberInput(ANSI_PURPLE + "Input Id to Delete: "));
            String queryProduct = "SELECT * FROM products WHERE id = %s".formatted(deleteId);
            try (Statement statement = DBConn.getConnection().createStatement();
                 ResultSet rs = statement.executeQuery(queryProduct)
            ) {
                if (rs.next()) {
                    do {
                        tProduct.addCell("PRODUCT");
                        tProduct.addCell("ID: " + rs.getInt("id"), cellStyle);
                        tProduct.addCell("Name: " + rs.getString("name"), cellStyle);
                        tProduct.addCell("Unit Price: " + rs.getInt("unitprice"), cellStyle);
                        tProduct.addCell("Stock Quantity: " + rs.getInt("stockqty"), cellStyle);
                        tProduct.addCell("Date: " + rs.getDate("importdate"), cellStyle);
                    } while (rs.next());
                    b = false;
                } else {
                    System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                    System.out.println(ANSI_RED + "Product not found!");
                    System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                    while (true) {
                        int zxz = Integer.parseInt(validateNumberInput("Press 1 to Delete Again or Press 0 to exit: "));
                        if (zxz == 1) {
                            break;
                        } else if (zxz == 0) {
                            Menu();
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(tProduct.render());
        }
        String y = validateLetterInput("Press y to confirm or any letter to cancel:");
        if (y.equalsIgnoreCase("y")) {
            String sql = "DELETE FROM products WHERE id =" + deleteId;
            Statement statement = DBConn.getConnection().createStatement();
            statement.executeUpdate(sql);
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            System.out.println(ANSI_GREEN + "Product was deleted successfully.");
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");

        }
        Display();
    }

    static Object datetime(){
        LocalDate currentDate = LocalDate.now();
        // Define date format pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        // Format date as string
        return currentDate.format(formatter);
    }
    public static void BackUp(){
        String backupProduct = "CREATE TABLE Backup_%s AS SELECT * FROM products".formatted(datetime());
        try (Statement statement = DBConn.getConnection().createStatement();
             ResultSet rs = statement.executeQuery(backupProduct)){
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            System.out.println(ANSI_GREEN + "Backup Complete");
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
            System.out.println(ANSI_GREEN + "Backup Complete");
            System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");

        }
    }
    public static void Restore(){

    }
    public static void Help(){}
//    public static void Display() throws SQLException {


    public static void Display() throws SQLException, IOException {
        ResultSet rs = null;
        Table t = new Table(5, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
        t.addCell(ANSI_GREEN + "Lists of Products",cellStyle,5);
        t.addCell(ANSI_PURPLE + "   ID   ", cellStyle);
        t.addCell(ANSI_PURPLE + "   Name   ", cellStyle);
        t.addCell(ANSI_PURPLE + "   Unit Price   ", cellStyle);
        t.addCell(ANSI_PURPLE + "   Stock Quantity   ", cellStyle);
        t.addCell(ANSI_PURPLE + "   Import Date   ", cellStyle);

        File file = new File("src/main/java/org/example/set_row.txt");
        FileReader fileReader = new FileReader(file);
        if(file.exists()){
            int i;
            while ((i = fileReader.read()) != -1){
                limit = i;
            }
        }
        String queryDisplay = "SELECT * FROM products ORDER BY id ASC LIMIT " + limit + " OFFSET " + offset;
        Statement statement = DBConn.getConnection().createStatement();
        rs = statement.executeQuery(queryDisplay);
        while (rs.next()) {
                t.addCell(ANSI_CYAN + String.valueOf(rs.getInt("id")),cellStyle);
                t.addCell(ANSI_GREEN + rs.getString("name"),cellStyle);
                t.addCell(ANSI_GREEN + String.valueOf(rs.getInt("unitprice")),cellStyle);
                t.addCell(ANSI_GREEN + String.valueOf(rs.getInt("stockqty")),cellStyle);
                t.addCell(ANSI_GREEN + rs.getString("importdate"), cellStyle);
        }

        page = DisAllProduct() % limit == 0 ? DisAllProduct() / limit : DisAllProduct() / limit + 1;
        t.addCell(ANSI_PURPLE + "Page "+ n + " of " + page,cellStyle,2);
        t.addCell(ANSI_CYAN + "Total Record: "+ DisAllProduct() ,cellStyle,3);
        System.out.println(t.render());
    }
    public static void main(String[] args) {
       con.connDB();
        try {
            Display();

            do {
                Menu();
                switch (op.toLowerCase()) {
                    case "w" -> Write();
                    case "r" -> Read();
                    case "u" -> {
                        System.out.printf("%s-------------------------------------------- %sUPDATE PRODUCT %s------------------------------------------------%n", ANSI_CYAN, ANSI_GREEN, ANSI_CYAN);
                        Update();
                    }
                    case "d" -> Delete();
                    case "f" -> First();
                    case "p" -> Previous();
                    case "n" -> Next();
                    case "l" -> Last();
                    case "s" -> Search();
                    case "g" -> Goto();
                    case "se" -> SetRow();
                    case "sa" -> Save();
                    case "ba" -> BackUp();
                    case "re" -> Restore();
                    case "un" -> {
                        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                        System.out.print("I: for Unsave Insertion");
                        System.out.print(" | U: for Unsave Update");
                        System.out.println(" | B: for Back Menu ");
                        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                        op= validateLetterInput(ANSI_PURPLE + "Select: ");
                        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");

                        switch(op.toUpperCase()){
                                case "I"->{
                                    System.out.println("Hello");
                                    break;
                                }
                                case "U"->{
                                    Unsaved();
                                }
                            }
                        }

                    case "h" -> Help();
                    case "e" -> {
                        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                        System.out.println(ANSI_GREEN + "(^-^) Good Bye! (^-^)");
                        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                        System.exit(0);
                    }
                    default -> {
                        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                        System.out.println(ANSI_RED + "Invalid Selected!\nPlease Choose Again!");
                        System.out.println(ANSI_CYAN + "------------------------------------------------------------------------------------------------------------");
                    }
                }
            } while (true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}



