Original App Design Project - README
===

# Meet Plan

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app is designed to easily create and organize meeting among friends from start to finish - scheduling a time, deciding on an activity, place or event, transportation, and common expense settlement. It makes it a one-stop shop for all the components required to have a smooth and easy time with a group of friends without having to visit multiple applications or websites.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social Networking, Lifestyle, Entertainment
- **Mobile:** This is a mobile application with a listview to easily see the meeting you are invited to or create your own.
- **Story:** Figurig out logistics for a group meetup or a day out can be difficult and overwhelming. This will simplify the process for users and be a resource to access in order to coordinate and keeping everyone in sync.
- **Market:** Groups that are planning a meetup.
- **Habit:** When individuals decide that they want to meet or hang out, they can use this application to make the process easier. It allows for easy and visual communication for the important parts of a meetup, as well as browsing places and activities to do.
- **Scope:** Initially, the following features of the meetup will be implemented: creating a meetup having a fixed day/time, inviting friends to join, deciding on a place/event/activity, and expense splitting. Once completed, find the best time to meet, having a photo gallery for the meetup and finding tranportation are features that will follow.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- User account
  - [x] Be able to sign up for an account
  - [x] Launch and use camera to add a profile picture
  - [x] Login into an existing account
  - [x] Logout of an account
- Create Meetup
  - [x] Create a new meetup
  - [x] Pick a date for the meetup
  - [x] Pick a time for the meetup
  - [x] Invite other memebers to the meetup
- Pick a meetup activity - Restaurant
  - [x] Propose a restaurant to meet at by browsing different restaurants (pulled through an API)
  - [x] Include a double tap gesture to select restaurant
- View Meetings
  - [x] View existing meetings that user is part of
  - [x] View meetings the user is invited to attend
  - [x] Accept/decline meetings that user is invited to attend
- [x] Implement the material design library to add visual polish
- [x] Animate views for transitioning between different fragments 

**Optional Nice-to-have Stories**
- Expense sharing
  - [x] Allow members to enter their expenses
  - [x] Tag all meetup members to an expense
  - [x] Tag specific meetup member(s) to an expense
  - [x] Allow members to pay on behalf of others for uneven splitting
  - [x] Compute and display optimal transactions between members to settle expenses
- [x] Implement unit testing for some fragments and activities
- Additional meetup activities
  - [x] Browse and select from an additional activity category (events, movies, parks etc.)
  - [x] Filter activities displayed to user via search by city
- User profile
  - [x] Users can edit account information
  - [X] Users can upload a profile picture from gallery
- [x] View meetings that have completed
- [x] Include a details page for each activity to see more information (such as address, rating, phone etc.)
- [x] Use Google Maps to see location of activity on map
- [x] Photo gallery where members can upload photos shared with the rest of the group
- [x] Receive notification when scheduled time is nearing
- [ ] Have a poll to determine best time to meet
- [ ] Browse transportation options (i.e. bus and train schedules)

### 2. Screen Archetypes

* Login
   * Login into an existing account
* Signup
   * Be able to sign up for an account
* List of meetings
   * View meetings that user is part of
   * Accept meetup that user is invited to attend
   * View meetings that have finished
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
* List of meetings

**Flow Navigation** (Screen to Screen)

* Login
   * -> Signup
   * -> Home (List of meetings)
* Signup
   * -> Home (List of meetings)
* Home (List of meetings)
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
   *  -> Home (List of meetings)
   *  -> Login

## Wireframes

### Digital Wireframes & Mockups
![](https://i.imgur.com/2vRtETc.png)



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
|email|String|Email address of the user|
|createdAt|Date|Date of user creation|
|updatedAt|Date|Date of last update to the user|

**Meetup**
| Property | Type | Description |
|----------|------|-------------|
|objectId|String|Unique identifier of the meetup that this group represents|
|name|String|Title of the meetup|
|description|String|Brief overview about the meetup|
|invites|Array|List of invitees that have not yet responded|
|time|Date|Scheduled date and time for the meetup|
|location|String|Place where meetup will occur|
|members|Array|List of members that have accepted the meetup|
|invites|Array|List of invitees that have not yet responded|
|createdAt|Date|Date of meetup creation|
|updatedAt|Date|Date of last update to the meetup|

### Networking
* Login
   * (Read/GET) Query user to log into the account
* Signup
   * (Create/POST) Publish new user to database
* Home (List of meetups)
   * (Read/GET) Query all meetups that the user has been invited to
   * (Read/GET) Query all meetups that the user has accepted
* Meetup details
   * (Read/GET) Query the scheduled time for the meetup (if set)
   * (Read/GET) Query the location for the meetup (if set)
   * (Read/GET) Query all members in the meetup
   * (Create/POST) Publish members to the meetup
* Browse activity
   *  (Read/GET) Query restarants to display
   *  (Read/GET) Query local events to display
#### Parse Network Requests
Signup

    user.signUpInBackground(new SignUpCallback
        @Override
        public void done(ParseUser user, ParseException e) { 
        if (e != null) {
            return;
        }
    });

Save

              
    ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
    query.findInBackground(new FindCallBack<ParseUser> {
        @Override
        public void done(List<ParseUser> users, ParseException e) { 
        if (e != null) {
            return;
        }
    });

#### API Endpoints
* Ticket Master
    * discovery/v2/events: finds events that are filtered based on parameters given (such as location, date, etc.)
* Yelp
    * /businesses/search: finds businesses that are filtered based on parameters given (such as location, name, etc.)
