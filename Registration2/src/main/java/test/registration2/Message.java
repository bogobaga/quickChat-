package test.registration2;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Message {

    Scanner input = new Scanner(System.in);

    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;

    private static ArrayList<String> sentMessages = new ArrayList<>();
    private static ArrayList<String> disregardedMessages = new ArrayList<>();
    private static ArrayList<String> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();
    private static ArrayList<String> recipients = new ArrayList<>();
    private static ArrayList<String> status = new ArrayList<>();

    private static int totalMessages = 0;

    public Message() {
    }

    public String Message() {
        return "Welcome to QuickChat";
    }

    public String Options() {
        return """
               1. Send Messages
               2. Retrieve Message by ID
               3. Delete Message by ID
               4. Stored Messages
               5. Quit
               """;
    }

    public static boolean checkMessageID(String ID) {
        return ID.length() <= 10;
    }

    public boolean checkRecipientCell(String number) {
        return number.matches("^\\+27[0-9]{9}$");
    }

    public String createMessageHash(String messageID, int messageNumber, String message) {
        String[] words = message.split(" ");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        String firstPart;
        if (messageID.length() >= 2) {
            firstPart = messageID.substring(0, 2);
        } else {
            firstPart = messageID;
        }

        messageHash = firstPart + ":" + messageNumber + ":" + firstWord + lastWord;
        return messageHash.toUpperCase();
    }

    public void captureMessage() {
        System.out.print("Enter Message ID: ");
        messageID = input.nextLine();

        while (!checkMessageID(messageID)) {
            System.out.print("Invalid ID. Enter again: ");
            messageID = input.nextLine();
        }

        System.out.print("Enter Recipient Number: ");
        recipient = input.nextLine();

        while (!checkRecipientCell(recipient)) {
            System.out.print("Invalid number. Enter again: ");
            recipient = input.nextLine();
        }

        System.out.print("Enter Message: ");
        message = input.nextLine();

        while (message.length() > 250) {
            System.out.print("Message too long. Enter again: ");
            message = input.nextLine();
        }

        String hash = createMessageHash(messageID, totalMessages + 1, message);
        System.out.println("Message Captured");
        System.out.println("Hash: " + hash);
    }

    public String sentMessage() {
        System.out.println("1. Send");
        System.out.println("2. Disregard");
        System.out.println("3. Store");
        System.out.print("Choose option: ");

        int choice = input.nextInt();
        input.nextLine();

        String hash = createMessageHash(messageID, totalMessages + 1, message);

        if (choice == 1) {
            sentMessages.add(message);
            messageHashes.add(hash);
            messageIDs.add(messageID);
            recipients.add(recipient);
            status.add("Sent");
            totalMessages++;
            saveMessagesToJSON();
            return "Message Sent";

        } else if (choice == 2) {
            disregardedMessages.add(message);
            return "Message Disregarded";

        } else if (choice == 3) {
            storedMessages.add(message);
            messageHashes.add(hash);
            messageIDs.add(messageID);
            recipients.add(recipient);
            status.add("Stored");
            totalMessages++;
            saveMessagesToJSON();
            return "Message Stored";

        } else {
            return "Invalid Option";
        }
    }

    // FIX: also print the hash for context
    public void retrieveMessageByID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                System.out.println("Recipient: " + recipients.get(i));
                System.out.println("Hash: " + messageHashes.get(i));
                System.out.println("Status: " + status.get(i));

                if (status.get(i).equals("Sent")) {
                    System.out.println("Message: " + sentMessages.get(getSentIndex(i)));
                } else {
                    System.out.println("Message: " + storedMessages.get(getStoredIndex(i)));
                }
                return;
            }
        }
        System.out.println("Message not found");
    }

    // FIX: now also removes the hash at index i
    public void deleteMessageByID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {

                if (status.get(i).equals("Sent")) {
                    sentMessages.remove(getSentIndex(i));
                } else {
                    storedMessages.remove(getStoredIndex(i));
                }

                messageHashes.remove(i);   // FIX: was missing before
                messageIDs.remove(i);
                recipients.remove(i);
                status.remove(i);

                saveMessagesToJSON();
                System.out.println("Message Deleted");
                return;
            }
        }
        System.out.println("Message not found");
    }

    private int getSentIndex(int globalIndex) {
        int count = 0;
        for (int i = 0; i <= globalIndex; i++) {
            if (status.get(i).equals("Sent")) {
                if (i == globalIndex) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    private int getStoredIndex(int globalIndex) {
        int count = 0;
        for (int i = 0; i <= globalIndex; i++) {
            if (status.get(i).equals("Stored")) {
                if (i == globalIndex) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public void saveMessagesToJSON() {
        try {
            FileWriter writer = new FileWriter("messages.json");
            writer.write("[\n");

            for (int i = 0; i < messageIDs.size(); i++) {
                writer.write("{\n");
                writer.write("\"messageID\":\"" + messageIDs.get(i) + "\",\n");
                writer.write("\"recipient\":\"" + recipients.get(i) + "\",\n");
                writer.write("\"status\":\"" + status.get(i) + "\",\n");
                writer.write("\"hash\":\"" + messageHashes.get(i) + "\",\n");

                if (status.get(i).equals("Sent")) {
                    writer.write("\"message\":\"" + sentMessages.get(getSentIndex(i)) + "\"\n");
                } else {
                    writer.write("\"message\":\"" + storedMessages.get(getStoredIndex(i)) + "\"\n");
                }

                if (i == messageIDs.size() - 1) {
                    writer.write("}\n");
                } else {
                    writer.write("},\n");
                }
            }

            writer.write("]");
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving JSON");
        }
    }

   public void loadMessagesFromJSON() {
    try {
        File file = new File("messages.json");
        if (!file.exists()) {
            return;
        }

      
        messageIDs.clear();
        recipients.clear();
        status.clear();
        messageHashes.clear();
        sentMessages.clear();
        storedMessages.clear();

        Scanner fileScanner = new Scanner(file);
        String id = "", recipient = "", stat = "",  hash = "", message = "";

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();

            if (line.contains("\"messageID\"")) {
                id = line.split(":")[1].replace("\"", "").replace(",", "").trim();

            } else if (line.contains("\"recipient\"")) {
                recipient = line.split(":")[1].replace("\"", "").replace(",", "").trim();

            } else if (line.contains("\"status\"")) {stat = line.split(":")[1].replace("\"", "").replace(",", "").trim();

            } else if (line.contains("\"hash\"")) {hash = line.split(":")[1].replace("\"", "").replace(",", "").trim();

            } else if (line.contains("\"message\"")) {message = line.split(":")[1].replace("\"", "").replace(",", "").trim();

            } else if (line.contains("}")) {
               
                if (!id.isEmpty()) {
                    messageIDs.add(id);
                    recipients.add(recipient);
                    status.add(stat);
                    messageHashes.add(hash);

                    if (stat.equals("Sent")) {
                        sentMessages.add(message);
                    } else {
                        storedMessages.add(message);
                    }

                    // Reset for next message
                    id = ""; recipient = ""; 
                    stat = ""; hash = ""; message = "";
                }
            }
        }
        fileScanner.close();
        System.out.println("Messages loaded successfully");

    } catch (FileNotFoundException e) {
        System.out.println("JSON file not found");
    }
}

   
    public int returnTotalMessages() {
        return totalMessages;
    }


    public static ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    public static ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static ArrayList<String> getStoredMessages() {
        return storedMessages;
    }

    public static ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    public static ArrayList<String> getMessageIDs() {
        return messageIDs;
    }

    public static ArrayList<String> getRecipients() {
        return recipients;
    }

    public static ArrayList<String> getStatus() {
        return status;
    }

    public void runMenu() {
    loadMessagesFromJSON();
    while (true) {

        System.out.println(Options());
        System.out.print("Enter Choice: ");

        int choice = input.nextInt();
        input.nextLine();

        if (choice == 1) {

            System.out.print("How many messages do you want to send: ");
            int numMessages = input.nextInt();
            input.nextLine();

            for (int i = 0; i < numMessages; i++) {

                System.out.println("\nMessage " + (i + 1));

                captureMessage();
                System.out.println(sentMessage());
            }

        } else if (choice == 2) {

            System.out.print("Enter ID: ");
            String id = input.nextLine();
            retrieveMessageByID(id);

        } else if (choice == 3) {

            System.out.print("Enter ID: ");
            String id = input.nextLine();
            deleteMessageByID(id);

        } else if (choice == 4) {

            ReportMessages sm = new ReportMessages();
            sm.runStoreMessagesMenu();

        } else if (choice == 5) {

            System.out.println("Goodbye");
            System.exit(0);

        } else {

            System.out.println("Invalid Option");
        }
    }
}
}
