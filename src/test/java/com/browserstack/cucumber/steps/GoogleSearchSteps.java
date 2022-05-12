package com.browserstack.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import java.text.ParseException;
import com.browserstack.cucumber.pages.GooglePage;

public class GoogleSearchSteps {
    GooglePage googlePage;

    @When("^I type query as \"([^\"]*)\"$")
    public void search_google_for(String searchWord) throws Throwable {
        googlePage.open();
        googlePage.searchForString(searchWord);
    }

    @Then("^I submit$")
    public void thenSubmit() throws Throwable {
        googlePage.submitForm();
    }

    @Then("^I should see title \"([^\"]*)\"$")
    public void matchTitle(String matchTitle) throws Throwable {
        googlePage.titleShouldMatch(matchTitle);
    }
}
