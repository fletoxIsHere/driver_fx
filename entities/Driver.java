package ma.fletox.javafx_drivers.entities;

import java.util.Date;
import java.util.Objects;

public class Driver {

    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String cin;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private  Boolean status;

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Driver(){}
    public Driver(int id, String cin, String nom, String prenom, Date dateNaissance, Boolean status) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return id == driver.id && Objects.equals(cin, driver.cin) && Objects.equals(nom, driver.nom) && Objects.equals(prenom, driver.prenom) && Objects.equals(dateNaissance, driver.dateNaissance) && Objects.equals(status, driver.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cin, nom, prenom, dateNaissance, status);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", cin='" + cin + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", status=" + status +
                '}';
    }
}


