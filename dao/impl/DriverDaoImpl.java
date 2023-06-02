package ma.fletox.javafx_drivers.dao.impl;
import ma.fletox.javafx_drivers.dao.DriverDao;
import ma.fletox.javafx_drivers.entities.Driver;

import java.io.*;
import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.*;
public class DriverDaoImpl implements DriverDao{


    private Connection conn = DB.getConnection();


    @Override
    public void insert(Driver driver) {
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement("INSERT INTO drivers (cin,nom,prenom,dateNaissance,status) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,driver.getCin());
            ps.setString(2,driver.getNom());
            ps.setString(3,driver.getPrenom());
            ps.setDate(4, (java.sql.Date) driver.getDateNaissance());
            ps.setBoolean(5,driver.getStatus());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected>0){
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    driver.setCin(String.valueOf(id));
                }
                DB.closeResultSet(rs);
            } else {
                System.out.println("Aucune ligne renvoyée");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }

    }

    @Override
    public void update(Driver driver, Integer id) {
        PreparedStatement ps = null;
        try{
            ps= conn.prepareStatement("UPDATE drivers Set cin = ?, nom =? , prenom = ? , dateNaissance = ? , status= ? where id = ?");
            ps.setString(1,driver.getCin());
            ps.setString(2,driver.getNom());
            ps.setString(3,driver.getPrenom());
            ps.setDate(4, (java.sql.Date) driver.getDateNaissance());
            ps.setBoolean(5,driver.getStatus());
            ps.setInt(6,id);

            ps.executeUpdate();

        }  catch (SQLException e){
            System.err.println("probleme de mise a jour d'un driver");
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void delete(Integer id) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM drivers WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("problème de suppression");;
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Driver findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement("Select * from drivers where id = ?");
            ps.setInt(1,id);
            rs = ps.executeQuery();

            if(rs.next()){
                Driver  driver  = new Driver();
                driver.setCin(rs.getString("cin"));
                return driver;
            }
            return null;
        }
         catch (SQLException e) {
            System.err.println("problème de trouve par id");
            return null;

        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);        }
    }

    @Override
    public List<Driver> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select * FROM drivers");
            rs = ps.executeQuery();

            List<Driver> listDrivers = new ArrayList<>();

            while(rs.next()){
                Driver driver = new Driver();

                driver.setId(rs.getInt("id"));
                driver.setCin(rs.getString("cin"));
                driver.setNom(rs.getString("nom"));
                driver.setPrenom(rs.getString("prenom"));
                driver.setDateNaissance(rs.getDate("dateNaissance"));
                driver.setStatus(rs.getBoolean("status"));

                listDrivers.add(driver);
            }
            return listDrivers;

        } catch (SQLException e) {
            System.err.println("prbleme pour trouve tous les drivers");
            return  null;
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    @Override
    public void exportVersExcel(String path) {
            try{
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("Drivers");
                    List<Driver> drivers = findAll();
                    Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Title");
                headerRow.createCell(0).setCellValue("Cin");
                headerRow.createCell(0).setCellValue("Nom");
                headerRow.createCell(0).setCellValue("Prenom");
                headerRow.createCell(0).setCellValue("DateNaissance");
                headerRow.createCell(0).setCellValue("Status");

                int rowNum = 1;
                for(Driver driver : drivers){
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(driver.getCin());
                    row.createCell(1).setCellValue(driver.getNom());
                    row.createCell(2).setCellValue(driver.getPrenom());
                    row.createCell(3).setCellValue(driver.getDateNaissance());
                    row.createCell(4).setCellValue(driver.getStatus());
                }
                try (FileOutputStream outputStream = new FileOutputStream(path)) {
                    workbook.write(outputStream);
                }
                System.out.println("Exportation vers le fichier Excel réussie !");

                }catch (IOException e){
                    System.err.println("erreur lors de l'exportation vers le fichier excel:" + e.getMessage());
                }

    }

    @Override
    public void importDepuisExcel(String path) {
        try{
            Workbook workbook = new XSSFWorkbook(path);
            Sheet sheet = workbook.getSheetAt(0);
            int numRow= 1;

           Row row = sheet.getRow(numRow++);
            while(row != null){
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Driver driver = new Driver();
                driver.setCin(row.getCell(1).getStringCellValue());
                driver.setNom(row.getCell(2).getStringCellValue());
                driver.setPrenom(row.getCell(3).getStringCellValue());
                driver.setDateNaissance(formatter.parse(row.getCell(4).getStringCellValue()));
                driver.setStatus(row.getCell(5).getBooleanCellValue());

                insert(driver);
                row = sheet.getRow(numRow++);
            }
            System.out.println("Importation depuis le fichier Excel réussie !");

        }catch(IOException e){
            System.err.println("Erreur lors de l'importation depuis le fichier Excel : " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportVersJson(String path) {

        String url = "jdbc:mysql://localhost:3306/Driver";
        String username = "root";
        String password = "";

        try{
            Connection conn = DriverManager.getConnection(url,username,password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from drivers");

            List<Driver> drivers = new ArrayList<>();
            while(rs.next()){
                Driver driver = new Driver();
                driver.setId(rs.getInt("id"));
                driver.setCin(rs.getString("cin"));
                driver.setNom(rs.getString("nom"));
                driver.setPrenom(rs.getString("prenom"));
                driver.setDateNaissance(rs.getDate("dateNaissance"));
                driver.setStatus(rs.getBoolean("status"));

                drivers.add(driver);

            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try{
                FileWriter writer = new FileWriter(path);
                gson.toJson(drivers,writer);
                System.out.println("exportation to json file est reussie !");
            } catch (IOException e) {
                System.out.println("Erreur lors de l'exportation vers le fichier JSON : " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void importDepuisJson(String path) {
        String url = "jdbc:mysql://localhost:3306/Driver";
        String username = "root";
        String password = "";


        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement()) {

            Gson gson = new GsonBuilder().create();
            try  {
                FileReader reader = new FileReader(path);
                Driver[] drivers = gson.fromJson(reader,Driver[].class);
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                for(Driver driver :drivers){
                    String insertQuery = "insert into drivers (id,cin,nom,prenom,dateNaissance,status) values(?,?,?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(insertQuery);
                    ps.setInt(1,driver.getId());
                    ps.setString(2, driver.getCin());
                    ps.setString(3, driver.getNom());
                    ps.setString(4, driver.getPrenom());
                    ps.setDate(5, (java.sql.Date) driver.getDateNaissance());
                    ps.setBoolean(6, driver.getStatus());

                    ps.executeUpdate();
                }
                System.out.println("Importation depuis le fichier JSON réussie !");

            } catch (FileNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());

                throw new RuntimeException(e);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void exportVersTxt(String path) {
        String url = "jdbc:mysql://localhost:3306/Driver";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM drivers")) {

            List<String> lines = new ArrayList<>();

            while (resultSet.next()) {
                StringBuilder line = new StringBuilder();
                line.append(resultSet.getString("id")).append("\t");
                line.append(resultSet.getString("cin")).append("\t");
                line.append(resultSet.getString("nom")).append("\t");
                line.append(resultSet.getString("prenom")).append("\t");
                line.append(resultSet.getString("dateNaissance")).append("\t");
                line.append(resultSet.getString("status")).append("\t");
                lines.add(line.toString());
            }

            try (FileWriter writer = new FileWriter(path)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
                System.out.println("Exportation vers le fichier texte réussie !");
            } catch (IOException e) {
                System.out.println("Erreur lors de l'exportation vers le fichier texte : " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    @Override
    public void importDepuisTxt(String path) {
        String url = "jdbc:mysql://localhost:3306/Driver";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture du fichier texte : " + e.getMessage());
                return;
            }

            String insertQuery = "INSERT INTO games (id, title, genre, releaseYear, price, rating, company) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

            for (String line : lines) {
                String[] values = line.split("\t");
                preparedStatement.setInt(1, Integer.parseInt(values[0]));
                preparedStatement.setString(2, values[1]);
                preparedStatement.setString(3, values[2]);
                preparedStatement.setString(4,values[3]);


                preparedStatement.setDate(5,  (java.sql.Date) formatter.parse(values[4]));
                preparedStatement.setBoolean(6, Boolean.parseBoolean(values[5]));
                preparedStatement.executeUpdate();
            }

            System.out.println("Importation depuis le fichier texte réussie !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
