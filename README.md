###### LiquidAutomation Currently Outdated (Closed in 2014) But if you are interested in Making Bots for Games , You will find useful techniques.

# What is LiquidAutomation?

##### Users
It's a third-party software that utilizes reverse
engineering of the game's obfuscated code and automates user input. used for user
convenience by having user's in-game avatar perform routine activities without user
interference.

##### Developers
It's provides developers an complete API to gain real time information about what is happening in game and all actions functions they need to control the Game Character.

# Brief history behind LiquidAutomation

First version used to be called ShadowBot , It was made to test the idea and written in unprofessional way , in Around 2014 Kenneth Joined to rewrite whole project in more proper/friendly user following conventions. 

# How it works

We made loader to download gamepack jar file and load in applet in UI , and in order to gain information for In-Game character we need to find out the values of game variables but we faced a lot technical Problems I am going to mention few of them below.

### Problem 1: 
The Source code of game is [obfuscated](https://en.wikipedia.org/wiki/Obfuscation_(software))   , and the game used to change it's obfuscation each game update which was around once a week.

That gave the project two challenging Problems , The first one was finding meaning of variables to find out which one is needed and the second one was finding their new names after update.

##### Solution:

There is a community called rs-hacking.com which people share information found about Runescape jar and keeps data for all previous game versions.

By observing those data of the game we found out there is pattern/structure that almost never changes between game updates , So we made something called Updater which identify each Class/Field in unique way, There is a lot of ways to identify information one simple example to find out classes by looking at the number of variables and their type and parent class. 

So using that updater we provide the LiquidAutomation a html file called "Hooks.html" that contain all variables names and their location in classes


### Problem 2:
the problem is all variables in game source code is private.

##### Solution:

So we used several Reverse-Engineering techniques to gain access to them:

- ##### Injection:
	We used ObjectWeb ASM API to modify game bytecode by injecting getters functions / callback during loading jar file.

- ##### Reflection:
	There is very unique package in Java called reflection which simply "gives you ability to examine or modify the runtime behavior of applications running in the Java virtual machine".
	

### Problem 3:

We needed to draw on game canvas to give fancy view for user whats the bot is doing.

#### Solution:
There is a lot of fancy solutions 
- Make transparent panel over game applet and draw on it
- Inject a callback for your Draw method in canvas class located in game jar
- Use reflection to set Game Canvas Variable to your own Canvas 


# Top fancy features

### FPS controller 
Since the bot doesn't require to visualize the game in order to interact, So we made that option for user to limit FPS to lower CPU-Usage a lot


### Tools for developers
We provided Developer all kind of tools to gain information by drawing them on game canvas such as In-Game characters / Objects ids 

![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/tools_1.png)
![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/tools_2.png)

### Script delivery network
In order to provide very cool user-experience instead of letting the user download source code of scripts and put them in local directory which require to Re-Download files each time the developer update

We provided a network where developers upload their scripts and user can add them  by simply clicking "Add" from UI in Bot , also it's more safe that way , since we guarantee developers upload doesn't contain any malicious code.

![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/sdn.png)

### Screenshot
Running 6 bots together in same computer.
![alt text](https://raw.githubusercontent.com/Hiasat/liquid-automation/master/running_bots.png)


# Authors

- Mohammad Abu Aboud (Leader Developer) - Github user: [hiasat](https://github.com/hiasat) (used to be Magorium)
- Kenneth LaCombe  - Github user: [kennehisftw](https://github.com/kennehisftw)

Thanks [Lem0ns](https://github.com/Lem0ns) , [CallMeSherlock](https://github.com/CallMeSherlock) for their amazing contribution to the project.

