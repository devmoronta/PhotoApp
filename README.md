# Photos App

An Android app that allows users to search and browse photos using the Flickr API. 

## Videos
- [App Walkthrough](https://github.com/user-attachments/assets/234af415-9a59-4607-933a-fc693c4b93c7
)

## Architecture
This app is built using MVVM architecture with the following tech stack:
- **Jetpack Compose** — UI
- **Hilt** — Dependency Injection
- **Retrofit** — Networking
- **Coil** — Image Loading
- **Kotlin Coroutines** — Async operations and state management
- **Jetpack Navigation** — Screen navigation

## Features
- Search photos using the Flickr API
- Display recent photos when no search query is entered
- Photo detail screen with metadata

## Future Improvements
Given more time I would add the following:
- Extract photo grid item into a reusable `PhotoGridItem` component
- Add accessibility support
- Add pull to refresh functionality
- Add image loading placeholder
- Add retry button on error instead of just a Snackbar
