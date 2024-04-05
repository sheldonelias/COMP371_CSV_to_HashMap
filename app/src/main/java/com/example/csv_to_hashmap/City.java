package com.example.csv_to_hashmap;

public class City
{
    private Integer idNumber;
    private String name;
    private String country;
    private String continent;
    private Integer population;
    private double latitude;
    private double longitude;
    private Integer yearFounded;
    private Integer elevation;

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitutde) {
        this.latitude = latitutde;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getYearFounded() {
        return yearFounded;
    }

    public void setYearFounded(int yearFounded) {
        this.yearFounded = yearFounded;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    @Override
    public String toString() {

        return "City{" +
                "idNumber=" + idNumber +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", continent='" + continent + '\'' +
                ", population=" + population +
                ", latitutde=" + latitude +
                ", longitude=" + longitude +
                ", yearFounded=" + yearFounded +
                ", elevation=" + elevation +
                '}';
    }
}
