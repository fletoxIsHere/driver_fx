package ma.fletox.javafx_drivers.dao;

import ma.fletox.javafx_drivers.entities.Driver;

import java.util.List;

public interface DriverDao {
    void insert(Driver driver);
    void update(Driver driver, Integer id);
    void delete(Integer id);
    Driver findById(Integer id);

    List<Driver> findAll();

    public void exportVersExcel(String path);
    public void importDepuisExcel(String path);
    public void exportVersJson(String path);
    public void importDepuisJson(String path);
    public void exportVersTxt(String path);
    public void importDepuisTxt(String path);



}
