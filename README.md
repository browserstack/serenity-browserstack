# serenity-browserstack

[Serenity](https://serenity-bdd.info/) Integration with BrowserStack SDK.

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780)

<img src="https://serenity-bdd.info/wp-content/uploads/elementor/thumbs/serenity-bdd-pac9onzlqv9ebi90cpg4zsqnp28x4trd1adftgkwbq.png" height = "100">

## Using Maven
### Setup
* Clone the repo
* Replace YOUR_USERNAME and YOUR_ACCESS_KEY with your BrowserStack access credentials in browserstack.yml.
* Install dependencies `mvn install`
* You can setup environment variables for all sample repos (see Notes) or update `serenity.conf` file with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings)

### Running your tests
- To run a sample test, run `mvn verify -P sample-test`
- To run local tests, run `mvn verify -P sample-local-test`

 Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

### Integrate your test suite

This repository uses the BrowserStack SDK to run tests on BrowserStack. Follow the steps below to install the SDK in your test suite and run tests on BrowserStack:

* Create sample browserstack.yml file with the browserstack related capabilities with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings) and place it in your root folder.
* Add maven dependency of browserstack-java-sdk in your pom.xml file
```sh
<dependency>
    <groupId>com.browserstack</groupId>
    <artifactId>browserstack-java-sdk</artifactId>
    <version>LATEST</version>
    <scope>compile</scope>
</dependency>
```
* Modify your build plugin to run tests by adding argLine `-javaagent:${com.browserstack:browserstack-java-sdk:jar}` and `maven-dependency-plugin` for resolving dependencies in the profiles `sample-test` and `sample-local-test`.
```
            <plugin>
               <artifactId>maven-dependency-plugin</artifactId>
                 <executions>
                   <execution>
                     <id>getClasspathFilenames</id>
                       <goals>
                         <goal>properties</goal>
                       </goals>
                   </execution>
                 </executions>
            </plugin>
            <plugin>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven.failsafe-plugin.version}</version>
                <configuration>
                    <includes>
                        <include>your_test_filename.java</include>
                    </includes>
                    <reuseForks>true</reuseForks>
                    <argLine>
                        -javaagent:${com.browserstack:browserstack-java-sdk:jar}
                    </argLine>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>integration-test</goal>
                            <goal>verify</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```
* Install dependencies `mvn compile`

## Using Gradle

### Prerequisites
- If using Gradle, Java v9+ is required.

### Setup
* Clone the repo
* Replace YOUR_USERNAME and YOUR_ACCESS_KEY with your BrowserStack access credentials in browserstack.yml.

### Running your tests

- Clone the repository
- To run the test suite having cross-platform with parallelization, run `gradle sampleTest`
- To run local tests, run `gradle sampleLocalTest`

Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

### Integrate your test suite

This repository uses the BrowserStack SDK to run tests on BrowserStack. Follow the steps below to install the SDK in your test suite and run tests on BrowserStack:

* Following are the changes required in `build.gradle` -
  * Add `id 'com.browserstack.gradle-sdk' version "1.1.2"` in plugins
  * Add `implementation 'com.browserstack:browserstack-java-sdk:latest.release'` in dependencies
  * Fetch Artifact Information and add `jvmArgs` property in tasks *SampleTest* and *SampleLocalTest* :
  ```
  def browserstackSDKArtifact = configurations.compileClasspath.resolvedConfiguration.resolvedArtifacts.find { it.name == 'browserstack-java-sdk' }
  
  task sampleTest(type: Test) {
    dependsOn cleanTest
    include '**/SampleTest.**'
    jvmArgs "-javaagent:${browserstackSDKArtifact.file}"
    useJUnitPlatform()
  }
  ```
* Following are the changes required in `settings.gradle` -
  * Add `resolutionStrategy` inside `pluginManagement`
  * Inside `resolutionStrategy` add the plugin `id` as `com.browserstack.gradle-sdk` along with module `version`
  ```
  eachPlugin {
            if (requested.id.id == "com.browserstack.gradle-sdk") {
                useModule("com.browserstack:gradle-sdk:1.1.2")
            }
        }
  ```

* Install dependencies and run test `gradle sampleTest`

## Notes
* You can view your test results on the [BrowserStack Automate dashboard](https://www.browserstack.com/automate)
* You can export the environment variables for the Username and Access Key of your BrowserStack account
  
  ```
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```
