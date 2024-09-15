package jukebox.jukebox;

import java.io.*;
import java.util.*;
import gr.hua.dit.oop2.musicplayer.*;
import java.security.SecureRandom;
import java.util.logging.*;

public class Song implements Strategies {  // Η public κλάση Song που αναπαριστά ένα τραγούδι ή μια λίστα τραγουδιών αναγνωρίζει και υλοποιεί τις μεθόδους του interface Strategies

    private static final Logger logger = Logger.getLogger(Song.class.getName());

    public static int Threadcount = 0;
    private int currentSongIndex;
    private final checkArguments Arg;  // Η private final "μεταβλητή" τύπου checkArguments με όνομα Arg δηλώνεται μέσα στην κλάση ώστε να είναι ορατή σε όλες τις μεθόδους της κλάσης
    private final Parser parser;  //  Ομοίως για την private final "μεταβλητή" τύπου Parser με όνομα parser
    private final Player player;  //  Ομοίως για την private final "μεταβλητή" τύπου Player με όνομα player
    private final String name;  //  Ομοίως για την private final "μεταβλητή" τύπου String με όνομα name
    private final String absolutePath;  // Ομοίως για την private final "μεταβλητή" τύπου String με όνομα absolutePath
    private final String path;  // Ομοίως για την private final "μεταβλητή" τύπου String με όνομα path
    private final String parentPath;  // Ομοίως για την private final "μεταβλητή" τύπου String με όνομα parentPath
    private final String fileInput;  // Ομοίως για την private final "μεταβλητή" τύπου String με όνομα fileInput
    private ArrayList<String> playList = null;  // Η private Arraylist με όνομα playList που περιέχει μεταβλητές τύπου String αρχικοποιείται με null
    private InputStream stream;

    public int getCurrentThreadCount() {
        return Threadcount;
    }

    public void setCurrentSongIndex(int value) {
        currentSongIndex = value;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public String getName() {
        return name;  // Ο getter public String getName() επιστρέφει την μεταβλητή name (το όνομα του αρχείου)
    }

    public String getAbsolutePath() {
        return absolutePath;  // Ο getter public String getAbsolutePath() επιστρέφει την μεταβλητή absolutePath (το απόλυτο μονοπάτι του αρχείου)
    }

    public String getPath() {
        return path;  // Ο getter public String getPath() επιστρέφει την μεταβλητή path (το μονοπάτι του αρχείου)
    }

    public String getParentPath() {
        return parentPath;   // Ο getter public String getParentPath() επιστρέφει την μεταβλητή parentPath (το μονοπάτι του καταλόγου του πατέρα του αρχείου)
    }

    public ArrayList<String> getPlayList() {
        return playList;
    }

    public Song(String fileInput) {

        currentSongIndex = 0;
        this.fileInput = fileInput;  // αντιγράφει την τιμή της παραμέτρου fileInput στο πεδίο (this)fileinput (στην περίπτωσή μας περνάει το 1ο argument που του δίνει ο χρήστης στην μεταβλητή fileinput)
        Arg = new checkArguments();  // Δημιουργεί νέo αντικείμενο Arg που είναι στιγμιότυπο της κλάσης checkArguments (επειδή δεν θα χρησιμοποιήσουμε τη μέθοδο checkArgNumb δεν έχει σημασία τι όρισμα θα περάσουμε στη συνάρτηση δημιουργίας)
        parser = new Parser(fileInput, Arg);  // Δημιουργεί νέo αντικείμενο parser που είναι στιγμιότυπο της κλάσης Parser και θέτει filename = fileInput
        player = PlayerFactory.getPlayer();  // Παίρνει από το PlayerFactory έναν player
        //addListeners();
        File f = new File(fileInput);  // Δημιουργεί νέo αντικείμενο f που είναι στιγμιότυπο της κλάσης File με παράμετρο fileInput
        name = f.getName();  // Η μεταβλητή name παίρνει ως τιμή το όνομα του αρχείου που δημιουργήθηκε
        absolutePath = f.getAbsolutePath();  // Η μεταβλητή absolutePath παίρνει ως τιμή το απόλυτο μονοπάτι του αρχείου που δημιουργήθηκε
        path = f.getPath();  // Η μεταβλητή path παίρνει ως τιμή το μονοπάτι του αρχείου που δημιουργήθηκε
        parentPath = f.getParent();  // Η μεταβλητή parentPath παίρνει ως τιμή το μονοπάτι του τελευταίου καταλόγου που βρίσκεται το αρχείο που δημιουργήθηκε
        if (Arg.checkListType(fileInput) || Arg.checkDirectory(fileInput)) {  // Αν (είναι true ότι) το fileInput πρόκειται για m3u λίστα
            playList = parser.getPlayList();  // Η λίστα playlist παίρνει ως τιμή την τιμή που επιστρέφεται από την μέθοδο getPlaylist() της κλάσης Parser (δηλαδή κάνει την parse στο playList)
        }
    }

    @Override
    public void play(String Name) {  // παίζει το τραγούδι
        try {  // Με την try μπορούμε ορίζουμε ένα κομμάτι κώδικα που θέλουμε να ελεγχθεί για τυχόν σφάλματα κατά την εκτέλεσή του
            File file = new File(Name);  // Δημιουργεί νέo αντικείμενο file που είναι στιγμιότυπο της κλάσης File με παράμετρο Name
            if (file.exists()) { // Αν υπάρχει το αρχείο
                System.out.println(">>> CURRENT SONG: " + file.getName());  // Κάνει print στο τερματικό την φράση ">>> PLAYING: " και την τιμή που επιστρέφει η μέθοδος getName() της κλάσης File (το όνομα του τραγουδιού που παίζει)
            }
            player.play((InputStream) new FileInputStream(file.getAbsoluteFile()));  // Δημιουργεί νέο stream και το τρέχει με την μέθοδο play (ανοίγει ο player)
        }
        catch (FileNotFoundException e) {  // Με την catch ορίζουμε ένα κομμάτι κώδικα που εκτελείται αν εντοπιστεί σφάλμα FileNotFoundException κατά την εκτέλεση του κώδικα που βρίσκεται στο try
            System.err.println(">>> Something's wrong with the file: " + e.getMessage());  // Κάνει print στο τερματικό με ένδειξη σφάλματος την φράση ">>> Something's wrong with the file: " και την τιμή που επιστρέφεται από τη μέθοδο getMessage() δηλαδή το όνομα του exception που στην περίπτωσή μας είναι FileNotFoundException
        }
        catch (PlayerException e) {  // Με την catch ορίζουμε ένα κομμάτι κώδικα που εκτελείται αν εντοπιστεί σφάλμα PlayerException κατά την εκτέλεση του κώδικα που βρίσκεται στο try
            System.err.println(">>> Something's wrong with the player: " + e.getMessage());  // Κάνει print στο τερματικό με ένδειξη σφάλματος την φράση ">>> Something's wrong with the player: " και την τιμή που επιστρέφεται από τη μέθοδο getMessage() δηλαδή το όνομα του exception που στην περίπτωσή μας είναι PlayerException
        }
    }

    public void startPlaying(String Name) {

        try {
            File file = new File(Name);
            if (file.exists()) {
                System.out.println(">>> CURRENT SONG: " + file.getName());
                String text = file.getName();
                if (text.length() > 15) {
                    text = text.substring(0, 12);
                    text = String.format("%s...", text);
                }
                jukebox.jukebox.Window.jLabel4.setText(text);

            }
            stream = (InputStream) new FileInputStream(file.getAbsoluteFile());
            player.startPlaying(stream);
        }
        catch (PlayerException | FileNotFoundException e) {
            logger.severe(e.getMessage());
        }
    }

    public String getStatus() {
        return player.getStatus().name();
    }

    public void stop() {
        player.stop();
    }

    public void pause() {
        player.pause();
    }

    public void resume() {
        player.resume();
    }

    // the ideal thing is to make startPlaying wait for a while before returning
    // but the rest of the program is usable
    @Override
    public void loop() {  // παίζει τη λίστα ή το τραγούδι κατ' επανάληψη
        if (Arg.checkFileType(fileInput)) {  // Αν (είναι true ότι) το fileinput πρόκειται για mp3 αρχείο
            while (true) {  // Όσο η συνθήκη είναι αληθές (στην περίπτωσή μας είναι πάντα αληθές και πρόκειται για ατέρμων βρόχο)
                play(absolutePath);  // Καλεί την μέθοδο play με παράμετρο absolutePath (παίζει το τραγούδι)
            }
        }
        else if (Arg.checkListType(fileInput) || Arg.checkDirectory(fileInput)) {  // Αλλιώς αν (είναι true ότι) το fileInput πρόκειται για m3u λίστα
            while (true) {  // Όσο η συνθήκη είναι αληθές (στην περίπτωσή μας είναι πάντα αληθές και πρόκειται για ατέρμων βρόχο)
                order();  // Καλεί την μέθοδο order (παίζει τη λίστα με σειρά)
            }
        }
    }

    @Override
    public void order() {  // παίζει όλα τα τραγούδια με τη σειρά που γράφονται στη λίστα
        for (String song : playList) {  // Διατρέχει το playlist χωρίς counter για κάθε τραγούδι song στη λίστα playList
            play(song);  // Καλεί την μέθοδο play με παράμετρο song (παίζει το τραγούδι)
        }
    }

    // παίζει όλα τα τραγούδια της λίστας με τυχαία σειρά
    @Override
    public void random() {
        SecureRandom rand = new SecureRandom();
        ArrayList<String> newPlayList = new ArrayList<>();  // νέα λίστα τραγουδίων
        int max = playList.size();

        // όσο η λίστα playList δεν είναι άδεια
        for (int i = 0; i < max; i++) {
            // πάρε ένα τυχαίο τραγούδι
            String song = playList.get(rand.nextInt(playList.size()));
            playList.remove(song);  // διέγραψε το από την playList
            newPlayList.add(song);  // και πρόσθεσέ το στην νέα λίστα
        }

        // για κάθε τραγούδι song στην νέα λίστα newPlayList
        for (String song : newPlayList) {
            play(song);  // παίξε το τραγούδι
        }

        // αποθήκευσε στην playList την αρχική λίστα
        playList = parser.getPlayList();
    }

    @Override
    public void closePlayer() {
        player.close();  // Κλείνει ο player
    }

    public void playGUI(String Name) {  // παίζει όλα τα τραγούδια με τη σειρά που γράφονται στη λίστα
        new Thread(new Runnable() {
            @Override
            public void run() {
                Threadcount++;
                startPlaying(Name);  // Καλεί την μέθοδο play με παράμετρο song (παίζει το τραγούδι)
                jukebox.jukebox.Window.jTextArea1.setText("Player Status : Playing");
                while (!"IDLE".equals(player.getStatus().name())) {
                    try {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (Threadcount > 1) {
                        //System.out.println(Threadcount);    //Πάντα 2, υπάρχουν 2 threads οπότε σταματάει ο προηγούμενος
                        Threadcount--;
                        return;
                    }
                }
                Threadcount--;
                //System.out.println(Threadcount); //Πάντα 0, δεν υπάρχουν threads
                currentSongIndex = 0;
                jukebox.jukebox.Window.jTextArea1.setText("Player Status : Stream Ended");
            }
        }).start();
    }

    public void orderGUI() {  // παίζει όλα τα τραγούδια με τη σειρά που γράφονται στη λίστα
        new Thread(new Runnable() {
            @Override
            public void run() {
                Threadcount++;
                for (; currentSongIndex < playList.size(); currentSongIndex++) {  // Διατρέχει το playlist χωρίς counter για κάθε τραγούδι song στη λίστα playList
                    startPlaying(playList.get(currentSongIndex));  // Καλεί την μέθοδο play με παράμετρο song (παίζει το τραγούδι)
                    jukebox.jukebox.Window.jTextArea1.setText("Player Status : Playing");
                    while (!"IDLE".equals(player.getStatus().name())) {
                        try {
                            Thread.sleep(1);
                        }
                        catch (InterruptedException ex) {
                            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (Threadcount > 1) {
                            Threadcount--;
                            return;
                        }
                    }
                }
                Threadcount--;
                jukebox.jukebox.Window.jTextArea1.setText("Player Status : Stream Ended");
                currentSongIndex = 0;
            }
        }).start();
    }

    public void loopGUI() {  // παίζει όλα τα τραγούδια με τη σειρά που γράφονται στη λίστα
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Threadcount++;
                    checkArguments Arg = new checkArguments();
                    if (Arg.checkFileType(path)) {
                        startPlaying(absolutePath);
                        jukebox.jukebox.Window.jTextArea1.setText("Player Status : Playing");
                        while (!"IDLE".equals(player.getStatus().name())) {
                            try {
                                Thread.sleep(1);
                            }
                            catch (InterruptedException ex) {
                                Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (Threadcount > 1) {
                                Threadcount--;
                                return;
                            }
                        }
                    }
                    else if (Arg.checkListType(path) || Arg.checkDirectory(path)) {
                        for (; currentSongIndex < playList.size(); currentSongIndex++) {  // Διατρέχει το playlist χωρίς counter για κάθε τραγούδι song στη λίστα playList
                            startPlaying(playList.get(currentSongIndex));  // Καλεί την μέθοδο play με παράμετρο song (παίζει το τραγούδι)
                            jukebox.jukebox.Window.jTextArea1.setText("Player Status : Playing");
                            while (!"IDLE".equals(player.getStatus().name())) {
                                try {
                                    Thread.sleep(1);
                                }
                                catch (InterruptedException ex) {
                                    Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                if (Threadcount > 1) {
                                    Threadcount--;
                                    return;
                                }
                            }
                        }
                    }
                    Threadcount--;
                    jukebox.jukebox.Window.jTextArea1.setText("Player Status : Stream Ended");
                    currentSongIndex = 0;
                }
            }
        }).start();
    }

    public void randomGUI() {  // παίζει όλα τα τραγούδια με τη σειρά που γράφονται στη λίστα
        new Thread(new Runnable() {
            @Override
            public void run() {
                Threadcount++;
                SecureRandom rand = new SecureRandom();
                ArrayList<String> newPlayList = new ArrayList<>();  // νέα λίστα τραγουδίων
                ArrayList<String> testPlayList = new ArrayList<>();

                // όσο η λίστα playList δεν είναι άδεια
                for (currentSongIndex = 0; currentSongIndex < playList.size(); currentSongIndex++) {
                    // πάρε ένα τυχαίο τραγούδι
                    String song = playList.get(currentSongIndex);
                    //playList.remove(song);  // διέγραψε το από την playList
                    testPlayList.add(song);  // και πρόσθεσέ το στην νέα λίστα
                }

                // όσο η λίστα playList δεν είναι άδεια
                for (currentSongIndex = 0; currentSongIndex < playList.size(); currentSongIndex++) {
                    // πάρε ένα τυχαίο τραγούδι
                    String song = testPlayList.get(rand.nextInt(testPlayList.size()));
                    testPlayList.remove(song);  // διέγραψε το από την playList
                    newPlayList.add(song);  // και πρόσθεσέ το στην νέα λίστα
                }

                // για κάθε τραγούδι song στην νέα λίστα newPlayList
                for (String song : newPlayList) {
                    startPlaying(song);  // παίξε το τραγούδι
                    jukebox.jukebox.Window.jTextArea1.setText("Player Status : Playing");
                    while (!"IDLE".equals(player.getStatus().name())) {
                        try {
                            Thread.sleep(1);
                        }
                        catch (InterruptedException ex) {
                            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (Threadcount > 1) {
                            // αποθήκευσε στην playList την αρχική λίστα
                            //playList = parser.getPlayList();
                            Threadcount--;
                            return;
                        }
                    }
                }
                // αποθήκευσε στην playList την αρχική λίστα
                //playList = parser.getPlayList();
                Threadcount--;
                jukebox.jukebox.Window.jTextArea1.setText("Player Status : Stream Ended");
                currentSongIndex = 0;
            }
        }).start();
    }

    public void loggers() {
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.INFO);  // FINE για να φανει και το progress (δευτερόλεπτα), αλλιως INFO
        logger.addHandler(handlerObj);
        logger.setLevel(Level.INFO);  // FINE για να φανει και το progress (δευτερόλεπτα), αλλιως INFO
        logger.setUseParentHandlers(false);

        player.addPlayerListener(new PlayerListener() {
            @Override
            public void statusUpdated(PlayerEvent e) {
                logger.info("Status changed to " + e.getStatus());

            }
        });
        player.addProgressListener(new ProgressListener() {
            @Override
            public void progress(ProgressEvent e) {
                float seconds = e.getMicroseconds() * 1.0f / 1000000;
                logger.fine("Seconds: " + seconds);

            }
        });
    }
}
