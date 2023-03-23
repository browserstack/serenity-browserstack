# serenity-browserstack

[Serenity](http://www.thucydides.info/docs/serenity/) Integration with BrowserStack.

Master branch contains **Selenium 4** samples, for **Selenium 3 - JSON Wire protocol** please checkout [selenium-3](https://github.com/browserstack/serenity-browserstack/tree/selenium-3) branch

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780)

<img src="http://www.thucydides.info/docs/serenity/images/serenity-logo.png" height = "100">

## Setup
* Clone the repo
* Install dependencies `mvn install`
* You can setup environment variables for all sample repos (see Notes) or update `serenity.properties` file with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings)

## Running your tests
- To run tests, run `mvn verify -P parallel`
- To run local tests, run `mvn verify -P local`

 Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

## Notes
* You can view your test results on the [BrowserStack Automate dashboard](https://www.browserstack.com/automate)
* To test on a different set of browsers, check out our [platform configurator](https://www.browserstack.com/automate/java#setting-os-and-browser)
* You can export the environment variables for the Username and Access Key of your BrowserStack account
  
  ```
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```
  
## Additional Resources
* [Documentation for writing Automate test scripts in Java](https://www.browserstack.com/automate/java)
* [Customizing your tests on BrowserStack](https://www.browserstack.com/automate/capabilities)
* [Browsers & mobile devices for selenium testing on BrowserStack](https://www.browserstack.com/list-of-browsers-and-platforms?product=automate)
* [Using REST API to access information about your tests via the command-line interface](https://www.browserstack.com/automate/rest-api)
