###### LiquidAutomation Currently Outdated (Closed in 2014) But if you are interested in Making Bots for Games , You will find useful techniques.

# What is LiquidAutomation?

##### Users
It's a third-party software that utilizes reverse
engineering of the game's obfuscated code and automates user input .used for user
convenience by having user's in-game avatar perform routine activities without user
interference.

##### Developers
It provides developers with a complete API to gain real time information about what is happening in-game and all the actions and functions they need to control their characters.

# Brief history behind LiquidAutomation

First version used to be called 'ShadowBot' ,It was made to test the concept and written in an unprofessional way ,in around 2014 Kenneth joined to rewrite the whole project in a more proper and user-friendly way following conventions. 

# How it works

We made a loader to download the gamepack's jar file and load in an applet in UI and in order to gain information for the in-game character ,we need to find out the values for the game's variables but we faced a lot technical Problems ,I am going to mention few of them below.

### Problem 1: 
The source code of game is [obfuscated](https://en.wikipedia.org/wiki/Obfuscation_(software))   , and the game used to change it's obfuscation with each update which happend almost once a week.

That made the project struggle with two challenges  ,The first one was trying to understand the meaning of each variable to find out which variable is needed and the second one was finding their new names after each update.

##### Solution:

There is a community called rs-hacking.com in which people share information found about Runescape's jar file and  data for all previous versions of the game.

By observing data of the game ,we found out there is a pattern/structure that almost never changes even with updates  so we made something called "Updater" which identify each class/field in aaa unique way ,there is a lot of ways to identify information ,ie:to find out classes by looking at the number of variables and their type and parent class. 

So by using "Updater" we provide the LiquidAutomation a html file called "Hooks.html" that contains all variables names and their locations in classes.


### Problem 2:
All variables in game source code are private.

##### Solution:

We used several Reverse-Engineering techniques to gain access to them ,and they are:

- ##### Injection:
	We used ObjectWeb ASM API to modify game bytecode by injecting getters functions / callback during the loading of the jar file.

- ##### Reflection:
	There is a very unique package in Java called reflection which simply gives you the ability to examine or modify the runtime behavior of applications running in the Java virtual machine.
	

### Problem 3:

We needed to draw on the game's canvas to give a fancy view for users of what the bot is doing.

#### Solution:
- Make a transparent panel over the game's applet and draw on it.
- Inject a callback for your draw method in canvas class located in the game's jar file.
- Use reflection to set the game's canvas variable to your own canvas. 


# Top fancy features

### FPS controller 
Since the bot doesn't require to visualize the game in order to interact ,we made an option for users to limit FPS to lower CPU-Usage.


### Tools for developers
We provided developers with all kinds of tools to gain information by drawing them on game canvas such as in-game characters / Objects ids .

![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/tools_1.png)
![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/tools_2.png)

### Script delivery network
In order to provide a very cool user-experience ,we provided a network where developers upload their scripts and users can add them  by simply clicking "Add" from UI in the bot ,instead of making users download source code of scripts and put them in local directory which require to re-download files each time the developer update ,also it's  safer that way ,since we guarantee developers uploads don't contain any malicious code.

![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/sdn.png)

### Screenshot
Running 6 bots together in same computer.
![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/running_bots.png)


# Authors

- Mohammad Abu Aboud (Leader Developer) - Github user: [hiasat](https://github.com/hiasat) (used to be Magorium)
- Kenneth LaCombe  - Github user: [kennehisftw](https://github.com/kennehisftw)

Thank for [Lem0ns](https://github.com/Lem0ns) , [CallMeSherlock](https://github.com/CallMeSherlock) for their amazing contribution to the project.

