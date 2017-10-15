package Server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by dinhvan5481 on 8/29/17.
 */
public class BookKeeper {
    private ConcurrentHashMap<Integer, String> reservations;
    private int availableSeats;

    private BookKeeper() {}

    public BookKeeper(int availableSeats) {
        reservations = new ConcurrentHashMap<>();
        this.availableSeats = availableSeats;
    }

    public String reserve(String name) {
        int resFound = getReservedSeat(name);
        if (resFound >= 0) {
            // Is this right?
            return "Name already has a reservation\n";
        }
        for (int seat = 1; seat <= availableSeats; seat++) {
            String prev = reservations.putIfAbsent(new Integer(seat), name);
            if(prev == null) {
                return String.format("Seat assigned to you is: %d\n", seat);
            }
        }
        return "Sold out - No seat available";
    }

    public String bookSeat(String name, int seatNum) {
        int resFound = getReservedSeat(name);
        if (resFound >= 0) {
            // Is this right?
            return "Name already has a reservation\n";
        }
        String prev = reservations.putIfAbsent(new Integer(seatNum), name);
        if(prev == null) {
            return String.format("Seat assigned to you is: %d\n", seatNum);
        } else {
            return String.format("%d is not available.\n", seatNum);
        }
    }

    public String search(String name) {
        int resFound = getReservedSeat(name);
        if (resFound >= 0) {
            return String.format("%d\n", resFound);
        }

        return String.format("No reservation found for %s\n", name);
    }

    private int getReservedSeat(String name) {
        Collection<Integer> reservationsFound = reservations.entrySet().parallelStream()
                .filter((entry) -> entry.getValue().equals(name))
                .map((entry) -> entry.getKey())
                .collect(Collectors.toList());
        if (reservationsFound.iterator().hasNext()) {
            int reservationFound = reservationsFound.iterator().next();
            if (reservationsFound.iterator().hasNext()) {
                //System.err.println("A user should not be allowed to have more than 1 reservation.");
            }
            return reservationFound;
        }

        return -1;
    }


    public String delete(String name) {
        int resFound = getReservedSeat(name);
        String prev = reservations.remove(new Integer(resFound));
        if (resFound < 0 || prev == null) {
            return String.format("No reservation found for %s\n", name);
        }
        return String.format("%d\n", resFound);
    }

    public String getCurrentReservations() {
        StringBuilder sb = new StringBuilder();
        reservations.entrySet().stream()
                .forEach(entry -> {
                    sb.append(String.format("(%d,%s)", entry.getKey(), entry.getValue()));
                });
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        reservations.entrySet().stream()
                .forEach(entry -> sb.append(String.format("Seat %d reserved for %s\n",
                        entry.getKey(), entry.getValue())));

        return sb.toString();
    }

}
