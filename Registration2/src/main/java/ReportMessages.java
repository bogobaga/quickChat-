package test.registration2;

import java.util.ArrayList;
import java.util.Scanner;

public class ReportMessages {

    Scanner input = new Scanner(System.in);

    public String menu() {
        return """
               a. Display Stored Messages
               b. Longest Message
               c. Search By ID
               d. Search By Recipient
               e. Delete By Hash
               f. Display Report
               q. Quit
               """;
    }

    public String displayStoredMessages() {
        ArrayList<String> stored = Message.getStoredMessages();

        if (stored.isEmpty()) {
            return "No Stored Messages";
        }

        String result = "";
        for (String msg : stored) {
            result += msg + "\n";
        }
        return result;
    }

    public String longestMessage() {
        ArrayList<String> stored = Message.getStoredMessages();

        if (stored.isEmpty()) {
            return "No Messages";
        }

        String longest = stored.get(0);
        for (int i = 1; i < stored.size(); i++) {
            if (stored.get(i).length() > longest.length()) {
                longest = stored.get(i);
            }
        }
        return longest;
    }

   
    public String searchByID(String id) {
        ArrayList<String> ids = Message.getMessageIDs();
        ArrayList<String> status = Message.getStatus();
        ArrayList<String> stored = Message.getStoredMessages();
        ArrayList<String> sent = Message.getSentMessages();

        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals(id)) {
                if (status.get(i).equals("Stored")) {
                    int storedIndex = getStoredIndex(i, status);
                    return storedIndex >= 0 ? stored.get(storedIndex) : "Message not found";
                } else if (status.get(i).equals("Sent")) {
                    int sentIndex = getSentIndex(i, status);
                    return sentIndex >= 0 ? sent.get(sentIndex) : "Message not found";
                }
            }
        }
        return "Message not found";
    }

    
    public String searchByRecipient(String number) {
        ArrayList<String> recipients = Message.getRecipients();
        ArrayList<String> status = Message.getStatus();
        ArrayList<String> stored = Message.getStoredMessages();
        ArrayList<String> sent = Message.getSentMessages();

        String result = "";

        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equals(number)) {
                if (status.get(i).equals("Stored")) {
                    int storedIndex = getStoredIndex(i, status);
                    if (storedIndex >= 0) result += stored.get(storedIndex) + "\n";
                } else if (status.get(i).equals("Sent")) {
                    int sentIndex = getSentIndex(i, status);
                    if (sentIndex >= 0) result += sent.get(sentIndex) + "\n";
                }
            }
        }

        return result.isEmpty() ? "No messages found for recipient" : result;
    }

   
    public String deleteByHash(String hash) {
        ArrayList<String> hashes = Message.getMessageHashes();
        ArrayList<String> ids = Message.getMessageIDs();
        ArrayList<String> recipients = Message.getRecipients();
        ArrayList<String> status = Message.getStatus();
        ArrayList<String> stored = Message.getStoredMessages();
        ArrayList<String> sent = Message.getSentMessages();

        for (int i = 0; i < hashes.size(); i++) {
            if (hashes.get(i).equalsIgnoreCase(hash)) {

                if (status.get(i).equals("Sent")) {
                    int sentIndex = getSentIndex(i, status);
                    if (sentIndex >= 0) sent.remove(sentIndex);
                } else if (status.get(i).equals("Stored")) {
                    int storedIndex = getStoredIndex(i, status);
                    if (storedIndex >= 0) stored.remove(storedIndex);
                }

                hashes.remove(i);
                ids.remove(i);
                recipients.remove(i);
                status.remove(i);

                return "Message deleted";
            }
        }
        return "Hash not found";
    }

    public String report() {
        ArrayList<String> ids = Message.getMessageIDs();
        ArrayList<String> recipientList = Message.getRecipients();
        ArrayList<String> statusList = Message.getStatus();
        ArrayList<String> stored = Message.getStoredMessages();
        ArrayList<String> sent = Message.getSentMessages();

        if (ids.isEmpty()) {
            return "No messages to report";
        }

        String result = "===== Message Report =====\n";

        for (int i = 0; i < ids.size(); i++) {
            result += "ID: " + ids.get(i) + "\n";
            result += "Recipient: " + recipientList.get(i) + "\n";
            result += "Status: " + statusList.get(i) + "\n";

            if (statusList.get(i).equals("Sent")) {
                int sentIndex = getSentIndex(i, statusList);
                result += "Message: " + (sentIndex >= 0 ? sent.get(sentIndex) : "N/A") + "\n";
            } else {
                int storedIndex = getStoredIndex(i, statusList);
                result += "Message: " + (storedIndex >= 0 ? stored.get(storedIndex) : "N/A") + "\n";
            }

            result += "--------------------------\n";
        }

        return result;
    }

    
    private int getSentIndex(int globalIndex, ArrayList<String> status) {
        int count = 0;
        for (int i = 0; i <= globalIndex; i++) {
            if (status.get(i).equals("Sent")) {
                if (i == globalIndex) return count;
                count++;
            }
        }
        return -1;
    }

    
    private int getStoredIndex(int globalIndex, ArrayList<String> status) {
        int count = 0;
        for (int i = 0; i <= globalIndex; i++) {
            if (status.get(i).equals("Stored")) {
                if (i == globalIndex) return count;
                count++;
            }
        }
        return -1;
    }

    public void runStoreMessagesMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println(menu());
            System.out.print("Enter choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "a":
                    System.out.println(displayStoredMessages());
                    break;

                case "b":
                    System.out.println(longestMessage());
                    break;

                case "c":
                    System.out.print("Enter ID: ");
                    String id = input.nextLine();
                    System.out.println(searchByID(id));
                    break;

                case "d":
                    System.out.print("Enter Recipient: ");
                    String number = input.nextLine();
                    System.out.println(searchByRecipient(number));
                    break;

                case "e":
                    System.out.print("Enter Hash: ");
                    String hash = input.nextLine();
                    System.out.println(deleteByHash(hash));
                    break;

                case "f":
                    System.out.println(report());
                    break;

                case "q":
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid Option");
            }
        }
    }
}