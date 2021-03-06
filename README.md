# Transit App
### An open-source project in Java and Kotlin

## Deployment Status : v1.0 Official Release on Google Play 
## CircleCI Status: [![CircleCI](https://circleci.com/gh/rjsuzuki/transit-app.svg?style=svg)](https://circleci.com/gh/rjsuzuki/transit-app)
--------------------------------------------------------------------------------

## Screenshots
![home](https://i.imgur.com/CqBJupym.png)
![nav](https://i.imgur.com/4f0wkpfm.png)
![trip](https://i.imgur.com/p8Lefx9m.png)
![results](https://i.imgur.com/JA1IHqzm.png)
![favorites](https://i.imgur.com/bspIbBsm.png)
![gmap](https://i.imgur.com/DvNsHQGm.png)
![bartmap](https://i.imgur.com/0c0D4WKm.png)

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
There is a simple test and build pipeline with CircleCI and you can review the current status up above.

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
3. Do not use protected, only public and privat

### XML Layout

1. snake_case
2. Variable Names => [fragment] + [purpose] + [componentType]

## Material Design Guidlines

1. Use the appropriate XML files for Strings, Colors, and Styles
2. Elevation = 1dp
3. Use containers
4. Use the Material Pallete colors implemented in the colors.xml
5. Please follow conventional [Material Design](https://material.io/design) guidelines for all future changes.


## Other

Thank you!

## Copyright
Copyright 2018 ryanjsuzuki.com, Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


## Markddown Reference
>Indentation

```
block of code lines
```

``single line code

# LARGE
## Medium
###### smallest

** bold **

