# CollabDJ
**CollabDJ** is an app that can make tunes on the fly with friends

## User Stories

<a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/HamburgerMenu.jpg">Hamburger Menu</a>

<a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/JoinSession.jpg">Join Session</a>

<a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/People.jpg">People</a>
<a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/QRCode.jpg">QRCode</a>




1. **Choose initial options** page
    * <a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/HomeScreen.jpg">Home Screen Wireframe</a>
    * Create a new song
    * Open existing song
    * Join a friend's song making session
    * Look at saved songs

2. **Create a new song** page opens the song making screen with a blank song
    * <a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/SongScreen.jpg">Song Screen Wireframe</a>
    * <a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/SampleViewDetailed.jpg">Sample View Detailed Wireframe</a>
    * Show a recycler view of playable samples
    * Samples can be played, paused, stopped
    * New samples can be added with a + button by clicking empty cells
    * <a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/SamplesScreen.jpg">Samples Screen Wireframe</a>

1. After a session of making a song you can save what you made locally on the device


4. **Open existing song** page opens a menu to view past saved songs
    * Opens the song making screen with sound samples queued up from a previous session
    * <a href="https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/OpenSong.jpg">Open Song Wireframe</a>

5. **Join a friend** page opens a menu where you can see sessions available for joining (Optional)
    * Friends can be seen either if they're local over Wifi or Bluetooth (Maybe)
    * As a fallback, instead of implementing this screen, make it possible to just scan a person's QR code for demo purposes since the song making part is the most important
    ```
    One possible implementation:
    ============================
    Session has a GUID and name
    Each person says which session ID theyre in.
    If there are multiple people in the list with the same session id, it puts them as one item in the list
    Menu looks something like this:
    List of people around you (Under the hood):
        Person 1: Session NULL
        Person 2: Session NULL
        Person 3: Session 012
        Person 4: Session 1337
        Person 5: Session 012
    What you actually see in the menu:
        Person 1: (Looking for a session)
        Person 2: (Looking for a session)
        Person 3, Person 5 Name:(Cool people making a song)
        Person 4 Name:(I'm by myself)
   ```
6. **Make a song** page
    * Has a session name (This is what shows up for people in the "Join a friend" screen)
    * Has a hidden/unhidden toggle
      * When unhidden the session is visible in the "Join a friend" screen
    * Has a password optionally so if you're in a public place you can allow only friends to join (Stretch goal, optional)
    * Shows all the people who are joined
      * Option to mute someone (They can't add songs but can listen)
      * Option to kick someone
    * Each person is connected to each other peer to peer (Maybe)
    * There's a list of sound samples and they can be played, looped or stopped
      * Each queued sample stores the user and time who queued it so it can be saved for later and the song can be replayed
      * Queueing a sample notifies other connected people so they also see/hear the sounds
      * Each sample can also be edited so when the song is reloaded samples can be moved a bit or deleted to polish a song (stretch goal)
    * There's also buttons to add new sound samples
    * you can save any time
      * saving allows you to either override a file or save a new file
    * you can quit any time
      * asks you if you want to save

[HamburgerMenu]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/HamburgerMenu.jpg "Hamburger Menu"
[HomeScreen]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/HomeScreen.jpg "Home Screen"
[JoinSession]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/JoinSession.jpg "Join Session"
[OpenSong]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/OpenSong.jpg "Open Song"
[People]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/People.jpg "People"
[QRCode]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/QRCode.jpg "QRCode"
[SamplesScreen]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/SamplesScreen.jpg "Samples Screen"
[SampleViewDetailed]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/SampleViewDetailed.jpg "Sample View Detailed"
[SongScreen]: https://github.com/CollabDJ/CollabDJ/blob/master/CollabDJWireframes/SongScreen.jpg "SongScreen"
