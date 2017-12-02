import Fusion.FusedPrimaryServer.FusedPrimaryServer;
import Fusion.communication.RMIAgent;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {



    public static void main(String[] args) {
        Main main = new Main();
        main.simulateBackup();
    }

    public void simulateChord() {
        de.uniba.wiai.lspi.chord.service.PropertiesLoader.
                loadPropertyFile();
        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
        URL localURL = null;
        try {
            localURL = new URL(protocol + "://localhost:8080/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
        try {
            chord.create(localURL);
            ((ChordImpl)chord).startBackupServer(0);
        } catch (ServiceException e) {
            throw new RuntimeException("Could not create DHT!", e);
        }
        URL bootstrapURL = null;
        try {
            bootstrapURL = new URL(protocol + "://localhost:8080/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Chord chord2 = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl
                ();
        try {
            chord2.join(localURL, bootstrapURL);
            ((ChordImpl)chord2).startBackupServer(0);
        } catch (ServiceException e) {
            throw new RuntimeException("Could not join DHT!", e);
        }
    }

    public void simulateBackup() {
        RMIAgent rmiConnectionLayer1;
        RMIAgent rmiConnectionLayer2;
        FusedPrimaryServer fusedPrimaryServer1;
        FusedPrimaryServer fusedPrimaryServer2;

        List<String> rmiServerIds = new LinkedList<>();
        rmiServerIds.add("b1");
        rmiServerIds.add("b2");
        rmiConnectionLayer1 = new RMIAgent(rmiServerIds);
        rmiConnectionLayer2 = new RMIAgent(rmiServerIds);
        fusedPrimaryServer1 = new FusedPrimaryServer(0, rmiConnectionLayer1);
        fusedPrimaryServer2 = new FusedPrimaryServer(1, rmiConnectionLayer2);
        Random random = new Random();
        LinkedList<ChordNodeSimulate> entriesX1 = new LinkedList<>();
        LinkedList<ChordNodeSimulate> entriesX2 = new LinkedList<>();
        int numOfEntries = 100;
        for(int i = 0; i < numOfEntries; i ++) {
            ChordNodeSimulate entry1 = new ChordNodeSimulate(random.nextInt(), random.nextInt());
            ChordNodeSimulate entry2 = new ChordNodeSimulate(random.nextInt(), random.nextInt());
            fusedPrimaryServer1.upsert(entry1.key, entry1.value);
            fusedPrimaryServer2.upsert(entry2.key, entry2.value);
            entriesX1.add(entry1);
            entriesX2.add(entry2);
        }

        // Simulate for update
        for (int i = 0; i < 50; i++) {
            fusedPrimaryServer1.upsert(entriesX1.get(random.nextInt(entriesX1.size())).key, random.nextInt());
            fusedPrimaryServer2.upsert(entriesX2.get(random.nextInt(entriesX2.size())).key, random.nextInt());
        }
        // Simulate for delete
        for (int i = 0; i < entriesX1.size() / 10; i++) {
            fusedPrimaryServer1.delete(entriesX1.get(random.nextInt(entriesX1.size())).key);
            fusedPrimaryServer2.delete(entriesX2.get(random.nextInt(entriesX2.size())).key);
        }

        // Simulate for retrieve
        for (int i = 0; i < entriesX1.size() / 10; i++) {
            fusedPrimaryServer1.delete(entriesX1.get(random.nextInt(entriesX1.size())).key);
            fusedPrimaryServer2.delete(entriesX2.get(random.nextInt(entriesX2.size())).key);
        }

    }
}
