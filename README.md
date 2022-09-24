# JENKINS SHARED LIBRARY

A shared library is a collection of independent Groovy scripts which you pull into your Jenkinsfile at runtime.

Let’s say we are supporting five micro services in the project typically, all five microservices need their own Jenkinsfile, but the content of the Jenkinsfiles is going to be mostly the same except for some inputs. Jenkins Shared Library avoids this repetition of pipeline code by creating a shared library.

## Steps to create Jenkins shared library:

#### Step 1: Create vars folder

Create a Git repository and create a directory called vars, which will host the shared library’s source code (file extension .groovy)

#### Step 2: Create Groovy file

Create a file <name.groovy> inside the vars folder (camel casing is mandatory for file names). The filename will be used later by Jenkinsfile to access this Jenkins pipeline library.

#### Step 3: Create call() function inside Groovy file

When a shared library is referred from the Jenkins job, Jenkins, by default, will invoke the
call() function within our Groovy file. Consider the call() function like the main() method in Java. We can also specify parameters for the call() function if we want to.

## Steps to configure Jenkins pipeline library:

##### In manage jenkins, click on configure system and search for Global Pipeline Libraries section.

Under the Library section, configure values as below:

- Name (remember, we will refer to this shared library name from Jenkinsfile).
-  Default version (branch name of our Shared Library git repo).
-  Under the Retrieval method, choose Modern SCM.
-  Under Source Code Management, choose Git.
-  Enter your Pipeline Shared Libraries repo URL under Project Repository
-  Configure credentials if your shared library is stored in private repo

## Referring Jenkins Shared Library from Pipeline
Call the shared library under the pipeline section

```
@Library('testlibrary')_
kubernetesShell('dockerhub', 'artisantek/useraccount', \
'ver', 'https://github.com/artisantek/nodejs-useraccount.git', 'main', 'github', \
'webapp-deployment', 'nodejs')
```


>@Library will import the shared library configured under manage jenkins to our Jenkins job.

>_ (underscore) is a must after the @Library annotation.

>kubernetesShell will invoke the call() function in kubernetesShell.groovy created under vars folder. 

>The ‘dockerhub’ string and others will be sent as a parameter to the call() function

---

This repo consists of groovy scripts, used to continously integrate and continously deliver the projects in both docker and kubernetes/eks environmenrts.

> Shell --> Uses only shell commands with piplenine declarative synatx
> Script --> Uses script block to perform various operations
