# SNHU-CS-360


## Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address
The requirements for this app were to have a traditional log-in system capable of creating new accounts and logging into existing accounts. It was also necessary to implement a feature using SMS notifications and provide a dialog that requests the user for the necessary permissions at an appropriate time.

The user needs this app needed to address was the ability to log their daily weight, view a history of daily weights, have the capabilty to delete or edit certain daily weight entries as well as recieve a notification when they reach their weight goal.

## What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?
To support these user needs, a log-in screen and a weight tracking screen were introduced to the UI. I based my log-in screen on existing log-in screens such that users will be familiar with the workflow of using the log-in feature. It is in this way my UI design met the expectations of the user.

## How did you approach the process of coding your app? What techniques or strategies did you use? How could those be applied in the future?
I approached the process of coding my app by employing Design Driven Development (DDD). In doing so I designed the UI first and created layout files for each screen and all of their static components. From there I implemented the functionality of each of these components in code.

This approach can be applied to any UI components that are not contextual and are not inflated dynamically by the code base. It is for this reason that I was not able to use DDD for the table that holds daily weight entries and instead had to implement a RecyclerView and class that represents each entry first and then design said entries later.

## How did you test to ensure your code was functional? Why is this process important and what did it reveal?
I mostly tested my code was functional using informal reviews of the code base and debugging. This revealed many logical errors in my code base, such as inverted conditions for if-else statements or race condition errors with my DialogFragment objects.

This process is important because without it I would not have understood why the request permissions dialog was never being created or why my dialog for entering a user's weight goal was being bypassed. These are critical bugs that if not fixed would have precluded the inclusion of essential features of the app, or would have lead to crude workarounds.

## Considering the full app design and development process, from initial planning to finalization, where did you have to innovate to overcome a challenge?
Most of my innovation was done during the development phase, primarily when involving the list that displays daily weight entries. At first I was going to use a GridLayout that was filled dynamically from the database but that would require resizing the layout dimensions and the dimensions of the entries or implementing a scroll bar.

I was able to avoid this by implementing a RecyclerView that holds single 2 column GridLayout entries. This made it unnecessary for me to implement a scrollbar from scratch and the hitbox that triggers the onClick handler for an entry covered both columns (i.e. the entire entry) instead of a single cell as would have been the case with an unnested GridLayout.

## In what specific component from your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?
I believe that I best demonstrated my knowledge and skill by implementing the log-in screen and the Room database that is the backend for the app. The design of the log-in screen shows my understanding of creating Android Studio layouts and the implementation shows my understanding of how the relationship between XML units and Java objects works in the Android Run Time's Document Object Model (DOM). My implemenation of the Room database also shows a good understanding of how to construct SQL queries, populate database tables and how to properly use annotation tags to utilize non-traditional libraries like Room.
