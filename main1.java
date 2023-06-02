package ma.fletox.javafx_drivers;


import ma.fletox.javafx_drivers.services.DriverService;

public class main1 {
    public static void main(String[] args) {
        DriverService driver = new DriverService();
        //driver.exporterVersJson("fileJson.json");
        //driver.importerDepuisJson("fileJson.json");
        driver.importerDepuisTxt("txt.txt");
        //driver.exporterVersTxt("txt.txt");


    }
}