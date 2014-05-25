XmlTest TeamCity plugin
=======================
XSpec scenarios in your TeamCity server
-------------------------------------------

XmlTest is a plugin for continuous integration server [TeamCity](http://www.jetbrains.com/teamcity/).

It allows you to integrate your **XSpec scenarios** to your continous integration workflow. 

Installation
------------

* Drop `/target/XmlTest.zip` to your TeamCity server plugins folder (under your TeamCity Data folder), e.g. `C:\TeamCityData\plugins\XmlTest.zip`
* Restart TeamCity server and all build agents

Build configuration with XmlTest
--------------------------------

*XmlTest* installs as a build runner. So you need to 

* Create a project in TeamCity as you would normally do (you can use SampleXSpecProject in this repo to test it)
* Create a new build step in your configuration. 
![XmlTest build step](https://raw.githubusercontent.com/j-maly/XmlTestTeamCityPlugin/master/docs/screenshots/build-step.png)
* Pick XmlTest as runner type 
* Enter paths to your xspec scenarios. Wildcards are recognized, so you might want to use something like `**/*.xspec`
* Go to general settings of your build configuration and add the following lines to the `Artifact paths` field: 
```
target\xmltest-xspec-results/**/*.xml => xspec.zip
target\xmltest-xspec-results/**/*.html => xspec.zip
```
![XmlTest artifact settings](https://raw.githubusercontent.com/j-maly/XmlTestTeamCityPlugin/master/docs/screenshots/project-artifacts.png)
* Go to your **Project settings** (not build configuration settings) and in *Report tabs*, add a new tab titled `XSpec` with Start page `xspec.zip!index.html`
![XmlTest setting up XSpec report tab](https://raw.githubusercontent.com/j-maly/XmlTestTeamCityPlugin/master/docs/screenshots/report-tabs.png)
* You are now ready to run your build! 

TeamCity integration
--------------------

Once your build is finished, you can see the examine the results in 
* Overview tab (build results)
![XmlTest - Overview tab](https://raw.githubusercontent.com/j-maly/XmlTestTeamCityPlugin/master/docs/screenshots/overview-tab.png)
* Tests tab (each scenario ~ one line)
  * clicking a failed scenario displays XSLT output when running the test (showing the failed expectation)
![XmlTest - Tests tab](https://raw.githubusercontent.com/j-maly/XmlTestTeamCityPlugin/master/docs/screenshots/tests-tab.png)
* XSpec tab shows reports produced by XSpec formatter (part of the XSpec project)
![XmlTest - XSpec tab](https://raw.githubusercontent.com/j-maly/XmlTestTeamCityPlugin/master/docs/screenshots/xspec-tab.png) 
* Artifacts tab - here you can download the reports in XML or HTM (as produced by the formatter)
* Build log - displays output of the XSLT processor running the scenarios. 

Implementation
--------------
XmlTest runs XProc pipeline for each .xspec file. The pipeline applies a series of transformation on the .xspec file and produces an XML report. This report is then again formatted by an XSLT stylesheet, which produces an HTML report. 

If you wish, you can peek at the code doing all this in `TeamCity\buildAgent\plugins\XmlTest-agent\runtime\xproc\compiler` folder (and make any modification you like). Or fork the repo and make your changes in [XmlTest-agent/src/runtime/xproc](/XmlTest-agent/src/runtime/xproc).

To compile the plugin yourself, run `mvn package` in the root, which will produce `\target\XmlTest.zip`.

XSpec
-----

XSpec is a behavior driven development framework for your XSLT stylesheets and XQuery libraries. It allows you to test your templates and queries against input data and compare the result with expected results. 

XSpec is managed by Jeni Tennison and Florent Georges in [XSpec Google Code repository](https://code.google.com/p/xspec/)


Acknowledgments
---------------

* Jeni Tennison and Florent Georges are the original authors of [XSpec](https://code.google.com/p/xspec/)
* Thanks to the authors of [FxCop](http://blog.jetbrains.com/teamcity/tag/fxcop/) for making their source code available. I mimiced large parts of it when trying to find out how to integrate with TeamCity
* XSpec scenarios are run using Norm Walsh's [XMLCalabash](http://xmlcalabash.com/)
