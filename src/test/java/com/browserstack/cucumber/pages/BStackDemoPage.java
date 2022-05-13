package com.browserstack.cucumber.pages;

import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;

@DefaultUrl("https://www.bstackdemo.com")
public class BStackDemoPage extends PageObject {

    private String selectedProductName;

    public BStackDemoPage() {
        this.selectedProductName = "";
    }

    @FindBy(xpath = "//*[@id=\"1\"]/p")
    WebElementFacade firstProductName;

    @FindBy(xpath = "//*[@id=\"1\"]/div[4]")
    WebElementFacade firstProductAddToCartButton;

    @FindBy(css = ".float-cart__content")
    WebElementFacade cartPane;

    @FindBy(xpath = "//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")
    WebElementFacade productCartText;

    public void setSelectedProductName(String selectedProductName) {
        this.selectedProductName = selectedProductName;
    }

    public String getSelectedProductName() {
        return selectedProductName;
    }

    public void selectFirstProductName() {
        String firstProduct = firstProductName.getText();
        setSelectedProductName(firstProduct);
    }

    public void clickAddToCartButton() {
        firstProductAddToCartButton.click();
    }

    public void waitForCartToOpen() {
        element(cartPane).waitUntilVisible();
    }

    public String getProductCartText() {
        return productCartText.getText();
    }
}
