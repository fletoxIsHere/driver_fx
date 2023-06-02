package ma.fletox.javafx_drivers.services;

import ma.fletox.javafx_drivers.dao.DriverDao;
import ma.fletox.javafx_drivers.dao.impl.DB;
import ma.fletox.javafx_drivers.dao.impl.DriverDaoImpl;
import ma.fletox.javafx_drivers.entities.Driver;
import ma.fletox.javafx_drivers.dao.DriverDao;

import java.sql.*;
import java.util.List;

public class DriverService {

    private DriverDao driverDao = new DriverDaoImpl();

    public List<Driver> findAll() {
        return driverDao.findAll();
    }

    public void save(Driver driver) {
        driverDao.insert(driver);
    }

    public void update(Driver driver, int driverID) {
        driverDao.update(driver, driverID);
    }

    public void delete(Driver driver) {
        driverDao.delete(Integer.valueOf(driver.getId()));
    }

    public void exporterVersExcel(String path) {
        driverDao.exportVersExcel(path);
    }

    public void importerDepuisExcel(String path) {
        driverDao.importDepuisExcel(path);
    }

    public void exporterVersTxt(String path) {
        driverDao.exportVersTxt(path);
    }

    public void importerDepuisTxt(String path) {
        driverDao.importDepuisTxt(path);
    }

    public void exporterVersJson(String path) {
        driverDao.exportVersJson(path);
    }

    public void importerDepuisJson(String path) {
        driverDao.importDepuisJson(path);
    }

    // Method to get the number of drivers
    public int getNumberOfDrivers() {
        int count = 0;
        try {
            Connection conn = DB.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM drivers");

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
}
