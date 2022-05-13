package com.browserstack.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import com.browserstack.cucumber.pages.BStackDemoPage;

public class BStackDemoSteps {
    BStackDemoPage demoPage;

    @Given("^I am on the website '(.+)'$")
    public void I_am_on_the_website(String url) throws Throwable {
        demoPage.open();
        Thread.sleep(2000);
    }

    @When("^I select a product and click on 'Add to cart' button")
    public void I_select_a_product_and_add_to_cart() throws Throwable {
        demoPage.selectFirstProductName();
        demoPage.clickAddToCartButton();
        Thread.sleep(2000);
    }

    @Then("the product should be added to cart")
    public void product_should_be_added_to_cart() {
        demoPage.waitForCartToOpen();
        assertEquals(demoPage.getSelectedProductName(), demoPage.getProductCartText());
    }
}
