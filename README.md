# Meet Plan

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app is designed to easily create and organize meetups among friends from start to finish - scheduling a time, deciding on an activity, place or event, transportation, and common expense settlement. It makes it a one-stop shop for all the components required to have a smooth and easy time with a group of friends without having to visit multiple applications or websites.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social Networking, Lifestyle, Entertainment
- **Mobile:** This is a mobile application with a listview to easily see the meetups you are invited to or create your own.
- **Story:** Figurig out logistics for a group meetup or a day out can be difficult and overwhelming. This will simplify the process for users and be a resource to access in order to coordinate and keeping everyone in sync.
- **Market:** Groups that are planning a meetup.
- **Habit:** When individuals decide that they want to meet or hang out, they can use this application to make the process easier. It allows for easy and visual communication for the important parts of a meetup, as well as browsing places and activities to do.
- **Scope:** Initially, the following features of the meetup will be implemented: creating a meetup having a fixed day/time, inviting friends to join, deciding on a place/event/activity, and expense splitting. Once completed, find the best time to meet, having a photo gallery for the meetup and finding tranportation are features that will follow.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Be able to sign up for an account
* Login into an existing account
* Logout of an account
* Users can add a picture to their profile
* Create a new meetup
* Invite other memebers to the meetup
* Pick a time for the meetup
* Propose an activity by browsing and selecting from different categories - restaurants, parks, events, and movies.
* Include a pinch gesture for exploring places on a map
* View meetups that user is part of
* Accept meetup that user is invited to attend
* Allow members to enter shared expenses

**Optional Nice-to-have Stories**

* Have a poll to determine best time to meet
* Browse different transportation options (i.e. bus and train schedules)
* Picture gallery where members can upload/take pictures and shared with the rest of the group
* Receive notification when scheduled time is nearing
* Users can change settings of their profile

### 2. Screen Archetypes

* Login
   * Login into an existing account
* Signup
   * Be able to sign up for an account
* List of meetups
   * View meetups that user is part of
   * Accept meetup that user is invited to attend
   * Create a new meetup
* Meetup details
   * Invite other memebers to the meetup
   * Pick a time for the meetup
* Browse places
    * Browse and select an activity from different categories - restaurants, parks, events, and movies
   * Include a pinch gesture for exploring places on a map
* Expense spliting
    * Enter shared expenses to be split with others
* Profile
    * User can log out
    * User can upload profile picture

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Profile
* List of meetups

**Flow Navigation** (Screen to Screen)

* Login
   * -> Signup
   * -> Home (List of meetups)
* Signup
   * -> Home (List of meetups)
* Home (List of meetups)
   * -> Meetup details
   * -> Profile
* Meetup details
   * -> Browse activity
   * -> Expense splitting
* Browse activity
   *  -> Meetup details
* Expense splitting
   *  -> Meetup details
* Profile
   *  -> Home (List of meetups)
   *  -> Login

## Wireframes

### Digital Wireframes & Mockups
![](https://i.imgur.com/YFZuV17.png)


### Interactive Prototype
https://www.figma.com/file/wbtXcnmbOgcEc8YFaXuypk/FBU-App-Design?node-id=0%3A1

## Schema 

### Models
**User**
| Property | Type | Description |
|----------|------|-------------|
|objectId|String|Unique identifier of the user|
|username |String|Username used to log into an account|
|password|String|Password used to log into an account|
|name|String|Name of the individual to be displayed to other memebers in the group|
|invites|Array|List of meetups that the user has been invited to the meetup but have not responded|
|meetups|Array|List of meetups that the user has accepted|
|createdAt|Date|Date of user creation|
|updatedAt|Date|Date of last update to the user|

**Meetup**
| Property | Type | Description |
|----------|------|-------------|
|objectId|String|Unique identifier of the meetup that this group represents|
|time|Date|Scheduled time for the meetup|
|location|String|Place where meetup will occur|
|members|Array|List of members that have accepted the meetup|
|Password|String|Password used to log into an account|
|createdAt|Date|Date of meetup creation|
|updatedAt|Date|Date of last update to the meetup|
