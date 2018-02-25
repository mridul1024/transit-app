# Transit App
### An open-source project in Java and Kotlin
--------------------------------------------------------------------------------
ADD LOGO and DEVICE PICTURES HERE


## Table of Contents

- [About](#about)
- [Installation](#installation)
- [Setup](#setup)
- [How to Contribute](#how-to-contribute)
- [Syntax and Coding Guidelines](#syntax-and-coding-guidelines)
	-[Java and Kotlin](#java-and-kotlin)
	-[XML Layout](#xml-layout)
- [Material Design Guidelines](#material-design-guidelines)
- [How to Report an Issue](#how-to-report-an-issue)
- [Other](#other)

## About

This is an open-source android application that was originally built with Android Studio 3.0 and GenYMotion Emulator.
I chose Android Studio Canary edition because it provides Kotlin support from the get go.
There is a simple test and build pipeline with Jenkins hosted on a Raspberry Pi 3 headless server that I created at home. 
Considering to dockerize the app as well.

I hope you all enjoy using and contributing to this application.

## Installation

Download repo to your desired location.

Open Android Studio > New > Open an existing project or Import Project > Choose the folder's location

Start coding!

## Setup

The gradle file should handle all dependencies, but make sure your IDE is up-to-date with the latest build-tools.

To check SDK Tools in Android Studio > Go to Tools > SDK Manager > SDK Tools > Download the latest versions SDK Tools AND Google Play Services > (Optional) you can click the checkbox for 'Show Package Details' at the bottom to view more tools

## How to Contribute

I will begin posting instructions for the next module soon, but in the mean time, if you notice any bugs, please let me know and feel free to make a Pull Request!

## Syntax and Coding Guidelines
	


### Java and Kotlin 

1. camelCase
2. Global variables begin with 'm' => mExampleVariable
3. Do not use protected, only public and private
4. Don't repeat yourself
5. Abstraction when appropriate
6. Delegation over inheritance

### XML Layout

1. snake_case
2. Variable Names => [fragment] + [purpose] + [componentType]

## Material Design Guidlines

1. Use the appropriate XML files for Strings, Colors, and Styles
2. Elevation = 1dp
3. Use containers
4. Use the Material Pallete colors implemented in the colors.xml 


## Other

Thank you!


>Indentation

```
block of code lines
```

``single line code

# LARGE
## Medium
###### smallest

** bold **

