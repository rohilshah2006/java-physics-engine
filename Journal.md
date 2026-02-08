# Christopher Hart Game Project Period 3 Team 10 Journal #

## 5/2, 1 hour ##

Rohil and I discussed what we would do for the game project and decided on a 2d motorcycle time trial game fit with obstacles, a time limit, etc. We later instead chose to do the same concept but with a ball instead of a motorcycle as it would be easier to code within the time frame. We worked on the readme.md and talked about game elements.

## 5/2 - 5/9, a couple hours ##

Added gravity ball (which was based on the basketball class from a greenfoot game I made), fixed ball, and circle classes along with a test world and test app to test everything. The circle class is the base for both ball classes. The gravity ball loosely mimics real world physics and bounces off of fixed balls in a manner that makes sense. I did not have gravity ball collision with multiple fixed balls at this time as I couldn't figure out how to, and I also had the issue of the gravity ball going inside of fixed balls due to not having a method to prevent clipping as I also couldn't figure that out by this point.

## 5/9 - 5/10, a couple hours ##

Figured out how to prevent the gravity ball from going inside of fixed balls, but it still only worked for 1 on 1 collision, and the method wasn't very clean and was implemented weirdly. Eventually after getting some help from Mr. Mcleod I was able to add collisions between multiple objects and clean up the code, which didn't take too long as I already had the code, I just had to change around some stuff and use a fixed ball list instead of an individual fixed ball object. I also added the PlayerBall subclass and added some movement code.

## 5/10 - 5/13, a couple hours ##

Cleaned up some of the code and added a TestPlatform class which just has a static method that creates a row of fixed circles. Rohil and I got some help from Mr. Mcleod today, and realized we could map fixed circles to objects like platforms using lists, and in those lists we could interact with those circles. For example on platforms, we could reduce the bounciness of the gravity ball so the ball can land on them without being ejected off at weird angles due to circle collision.
