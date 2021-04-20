package com.example.covid19tracker.model;

public class CountryModel {
    private String country;
    private String confirmed;
    private String newConfirmed;
    private String active;
    private String deaths;
    private String newDeaths;
    private String recovered;
    private String newRecovered;
    private String critical;
    private String tests;
    private String flag;
    private String oneCasePerPeople;
    private String oneDeathPerPeople;
    private String oneTestPerPeople;
    private String population;

    public CountryModel(String country, String confirmed, String newConfirmed, String active,
                        String deaths, String newDeaths, String recovered, String newRecovered,
                        String critical, String tests, String flag, String oneCasePerPeople,
                        String oneDeathPerPeople, String oneTestPerPeople,String population) {
        this.country = country;
        this.confirmed = confirmed;
        this.newConfirmed = newConfirmed;
        this.active = active;
        this.deaths = deaths;
        this.newDeaths = newDeaths;
        this.recovered = recovered;
        this.newRecovered = newRecovered;
        this.critical = critical;
        this.tests = tests;
        this.flag = flag;
        this.oneCasePerPeople = oneCasePerPeople;
        this.oneDeathPerPeople = oneDeathPerPeople;
        this.oneTestPerPeople = oneTestPerPeople;
        this.population = population;
    }

    public String getCountry() {
        return country;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getNewConfirmed() {
        return newConfirmed;
    }

    public String getActive() {
        return active;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getNewDeaths() {
        return newDeaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getNewRecovered() {
        return newRecovered;
    }

    public String getCritical() {
        return critical;
    }

    public String getTests() {
        return tests;
    }

    public String getFlag() {
        return flag;
    }

    public String getOneCasePerPeople() {
        return oneCasePerPeople;
    }

    public String getOneDeathPerPeople() {
        return oneDeathPerPeople;
    }

    public String getOneTestPerPeople() {
        return oneTestPerPeople;
    }

    public String getPopulation() {
        return population;
    }
}
