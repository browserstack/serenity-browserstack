# serenity-browserstack

[Serenity](http://www.thucydides.info/docs/serenity/) Integration with BrowserStack.

Master branch contains **Selenium 3** samples, for **Selenium 4 - W3C protocol** please checkout [selenium-4](https://github.com/browserstack/serenity-browserstack/tree/selenium-4) branch

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780)

<img src="http://www.thucydides.info/docs/serenity/images/serenity-logo.png" height = "100">

## Setup
* Clone the repo
* Install dependencies `mvn install`
* You can setup environment variables for all sample repos (see Notes) or update `browserstack.yml` file with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings)

## Running your tests
- To run a single test, run `mvn verify -P single`
- To run local tests, update your `browserstack.yml` file with `browserstackLocal: true` and run `mvn verify -P local`
- To run parallel tests, run `mvn verify -P parallel`

 Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

## Notes
* You can view your test results on the [BrowserStack Automate dashboard](https://www.browserstack.com/automate)
* To test on a different set of browsers, check out our [platform configurator](https://www.browserstack.com/automate/java#setting-os-and-browser)
* You can export the environment variables for the Username and Access Key of your BrowserStack account
  
  ```
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```
