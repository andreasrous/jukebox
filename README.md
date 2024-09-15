# JukeBox

JukeBox is a simple audio player that supports MP3 files and playlists (M3U or directories). You can use it via the command line or a graphical interface to play audio files, with options for strategies like looping and shuffling.

## Compiling the Application

1. **Navigate to the JukeBox directory:**

   ```
   cd <JukeBox_Path>
   ```

2. **Compile the app:**

   ```
   mvn package
   ```

3. **A target directory will be created with the executable JAR file.**

## Command Line Usage

1. **Run the Application:**

   ```
   java -jar <jar_file_path>
   ```

2. **Play Audio:**

   ```
   java -jar <jar_file_path> <audio_file_path> [strategy]
   ```

   - <audio_file_path>: Path to an audio file or playlist.
   - [strategy]: Optional playback strategy (see Strategies). Defaults to simple playback.

3. **Notes:**

   - You can provide 1-2 arguments: audio file and/or strategy.
   - Playback will continue until the file finishes. To stop, you’ll need to terminate the app.

## Graphical Interface Usage

1. **Run the Application:**

   ```
   java -jar <jar_file_path>
   ```

2. **Add Files:**
   Use the File Explorer icon to add audio files, playlists, or folders (up to 20 files).

3. **Playback Controls:**

   - Shuffle (random order)
   - Stop
   - Pause
   - Resume
   - Skip to next
   - Repeat

**Note:** Shuffle and Repeat require re-selecting the song to take effect.

## Strategies

- **loop:** Repeats the audio file or playlist.
- **order:** Plays the playlist in order (default for playlists).
- **random:** Plays the playlist in random order.

## Supported Formats

- **Audio:** MP3
- **Playlists:** M3U, Directories
