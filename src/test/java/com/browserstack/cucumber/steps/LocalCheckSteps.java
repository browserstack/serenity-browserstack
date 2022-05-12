package com.browserstack.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import java.text.ParseException;
import com.browserstack.cucumber.pages.LocalPage;

public class LocalCheckSteps {
    LocalPage localPage;

    @Then("^I should see \"([^\"]*)\"$")
    public void matchTitle(String matchTitle) throws Throwable {
        localPage.open();
        localPage.bodyShouldMatch(matchTitle);
    }
}
