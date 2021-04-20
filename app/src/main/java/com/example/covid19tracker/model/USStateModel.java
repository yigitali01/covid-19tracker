package com.example.covid19tracker.model;

public class USStateModel {
    private String state;
    private String confirmed;
    private String newConfirmed;
    private String active;
    private String deaths;
    private String newDeaths;
    private String recovered;
    private String tests;
    private String testsPerOneMillion;
    private String deathsPerOneMillion;
    private String casesPerOneMillion;
    private String population;

    public USStateModel(String state, String confirmed, String newConfirmed, String active, String deaths, String newDeaths, String recovered, String tests, String testsPerOneMillion, String deathsPerOneMillion, String casesPerOneMillion, String population) {
        this.state = state;
        this.confirmed = confirmed;
        this.newConfirmed = newConfirmed;
        this.active = active;
        this.deaths = deaths;
        this.newDeaths = newDeaths;
        this.recovered = recovered;
        this.tests = tests;
        this.testsPerOneMillion = testsPerOneMillion;
        this.deathsPerOneMillion = deathsPerOneMillion;
        this.casesPerOneMillion = casesPerOneMillion;
        this.population = population;
    }

    public String getState() {
        return state;
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

    public String getTests() {
        return tests;
    }

    public String getTestsPerOneMillion() {
        return testsPerOneMillion;
    }

    public String getDeathsPerOneMillion() {
        return deathsPerOneMillion;
    }

    public String getCasesPerOneMillion() {
        return casesPerOneMillion;
    }

    public String getPopulation() {
        return population;
    }
}
