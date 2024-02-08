##  Foundation  -  Mobile  developer  technical  assessment

###  Introduction

Using the application framework provided for you, **create  a  movie  browsing  application** that displays a list of most popular movies, as well as some basic details about them.

###  Implementation  details

- Use  the  application  framework  provided  for  you.
- Write  your  application  using  Kotlin.
- Host  your  project  on  GitHub or send us via email including the `.git` folder. Use  git  as  you  see  fit.
- Use  the  latest  version  of  [TMDB  api](https://developers.themoviedb.org/3/getting-started/introduction)  to  fetch  data  about  movies.  An  API  key  will  be  provided  for  you.
- Create  a  screen  to  show  a  list  of  trending  movies.  Display  some  additional  info  including  the  movie  poster,  title,  its  genres, rating etc. (UI is provided)
- Selecting  a  movie  from  the  list,  the  user  should  navigate  to  the  detail  page  of  that  movie,  that  displays  some  additional  info. (UI + navigation provided)
- On the details screen the user can mark the movie and add it to their own list (You don't have to persist this data). This status is also visible on the movie list screen. (shared data/state)
- Please  note  that  TMDB  uses  unique  genre  IDs  that  need  to  be  resolved  using  a  separate  API  call.  Do  not  hardcode  genres  into  your  application.

###  Notes
- The  application  framework  that  is  provided  for  you  uses  an  MVVM  approach.  We  advise  to  separate  your  code  into  layers  that  you  see  fit  in  this  architecture.
- Instead of proof of concept style solutions, we would be interested how you would implement the task as a scalable solution
