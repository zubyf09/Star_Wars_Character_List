# CHALLENGE
Implement a small but scalable (!) app which interacts with the API. Please use the API provided to you in the resources section.


The app should contain 2 main areas:
* Character search (Home Screen)
* Character details


Below are the specific attribute requirements for the character search & details screen.

```
# [Character search]
name
birth_year

# [Character details]
name
birth_year
height (cm/feet/inches)
name (species)
language (species)
homeworld (species)
population (planets)
films (the movies the character appears in: title, release date, opening crawl)

```

### Expected user flow

On app start, the user lands on the character search screen.
The user can search for characters from the Star Wars universe. The result of the search should display a character list.
When tapping on a character, details are displayed as defined in the attribute requirements text file.


### Technical requirements

Please use Kotlin as the project language. The design of the UI, usage of 3rd party libraries and supported API level are simply your choice, with exceptions mentioned in the resources section. That said, don't forgot to approach this like a project that has to scale, so your project should include a modern architectural approach, unit tests, dependency injection and whatever else you consider important.


### Documentation

As part of the deliverable, please add a read.me file documenting your decisions (e.g. design patterns, 3rd party library choices). This will helps us understand your thought process and can fuel further discussions during the interview process. Please be aware that your documentation and explanations will be a big part of our evaluation.

