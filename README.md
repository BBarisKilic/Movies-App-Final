
# Movies App
Movies App using TMDB API that allows users to view top-rated and most popular movies. The app displays movie poster, rating, trailers, reviews and even has an option to add/remove movies as favorite. And most important feature: App is able to display movies' details when it is offline.

## Screenshots from the Latest Version of The Application

### _Sidebar Navigation_
![ScreenShot](/images/Sidebar_Navigation_Drawer.jpg)
### _Movies' Details Screen_
![ScreenShot](/images/Movies_Details.jpg)

## Project Summary

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing.


You’ll build the complete functionality of this app in two stages which you will submit separately.

### **Stage 1:  Main Discovery Screen, A Details View, and Settings**
### _User Experience_
In this stage you’ll build the core experience of your movies app.


Your app will:

* Upon launch, present the user with an grid arrangement of movie posters.
* Allow your user to change sort order via a setting:
  - The sort order can be by most popular, or by top rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
  - original title
  - movie poster image thumbnail
  - A plot synopsis (called overview in the api)
  - user rating (called vote_average in the api)
  - release date

### **Stage 2: Trailers, Reviews, and Favorites**
### _User Experience_
In this stage you’ll add additional functionality to the app you built in Stage 1.


You’ll add more information to your movie details view:

* You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
* You’ll allow users to read reviews of a selected movie.
* You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
* You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.


# Rubric

### Common Project Requirements

* App is written solely in the Java Programming Language.
* App conforms to common standards found in the Android Nanodegree General Project Guidelines NOTE: It is okay if the app does not handle rotation properly or does not restore the data using onSaveInstanceState/onRestoreInstanceState).
* App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### User Interface - Layout

* UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
* Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
* UI contains a screen for displaying the details for a selected movie.
* Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
* Movie Details layout contains a section for displaying trailer videos and user reviews.

### User Interface - Function

* When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
* When a movie poster thumbnail is selected, the movie details screen is launched.
* When a trailer is selected, app uses an Intent to launch the trailer.
* In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.

### Network API Implementation

* In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
* App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
* App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

### Data Persistence

* The titles and IDs of the user’s favorite movies are stored in a native SQLite database and exposed via a ContentProvider
OR
stored using Room.
* Data is updated whenever the user favorites or unfavorites a movie. No other persistence libraries are used.
* When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.

### Android Architecture Components

* If Room is used, database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.
* If Room is used, database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.
