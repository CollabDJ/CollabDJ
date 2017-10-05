# CollabDJ
**CollabDJ" is an app that can make tunes on the fly with friends

## User Stories
* After a session of making a song you can save what you made locally on the device
* Screen to choose initial options
    -Create a new song
    -Open existing song
    -Join a friend's song making session
* "Create a new song" opens the song making screen with a blank song
* "Open existing song" opens a menu to view past saved songs
    Opens the song making screen with sound samples queued up from a previous session
* "Join a friend" opens a menu where you can see sessions available for joining
    Friends can be seen either if they're local over Wifi or Bluetooth (Maybe)
    
    As a fallback, instead of implementing this screen, make it possible to just enter a person's ip address for demo purposes since the song making part is the most important
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
* "Make a song page"
    -Has a session name (This is what shows up for people in the "Join a friend" screen)
    -Has a hidden/unhidden toggle
        -When unhidden the session is visible in the "Join a friend" screen
    -Has a password optionally so if you're in a public place you can allow only friends to join (Stretch goal, optional)
    -Shows all the people who are joined
        -Option to mute someone (They can't add songs but can listen)
        -Option to kick someone
    -Each person is connected to each other peer to peer (Maybe)
    -There's a track that is constantly moving
        -There's a little bar on the screen
        ----|-----------------
        -There's a menu of various sound samples that can be queued
            -They appear on the track and as they intersect the bar, the sound plays
            -Each queued sample stores the user and time who queued it so it can be saved for later and the song can be replayed
            -Queueing a sample notifies other connected people so they also see/hear the sounds
            -Each sample can also be edited so when the song is reloaded samples can be moved a bit or deleted to polish a song (stretch goal)
    -you can save any time
        -saving allows you to either override a file or save a new file
    -you can quit any time
        -asks you if you want to save
