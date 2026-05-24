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
    private static ArrayList<String> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();
    private static ArrayList<String> recipients = new ArrayList<>();
    private static ArrayList<String> status = new ArrayList<>();

    private static int totalMessages = 0;

    public Message() {
        loadMessagesFromJSON();
    }

    public String Message() {
        return "Welcome to QuickChat\n";
    }

    public String Options() {
        return """
               Choose Any Option below
               1. Send Messages
               2. Retrieve Message by ID
               3. Delete Message by ID
               4. Quit
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

    messageHash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;

    return messageHash.toUpperCase();
    }

    public void captureMessage() {

    System.out.print("Enter Message ID (max 10 chars): ");
    messageID = input.nextLine();

    while (!checkMessageID(messageID)) {

    System.out.print("Invalid ID. Enter again: ");
    messageID = input.nextLine();
    }

    System.out.print("Enter recipient number (+27...): ");
    recipient = input.nextLine();

    while (!checkRecipientCell(recipient)) {

    System.out.print("Invalid number. Enter again: ");
    recipient = input.nextLine();
    }

    System.out.print("Enter message (max 250 chars): ");
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

    System.out.println("\n1. Send Message");
    System.out.println("2. Disregard Message");
    System.out.println("3. Store Message");

    System.out.print("Choose option: ");

    int choice = input.nextInt();
    input.nextLine();

    if (choice == 1) {

    sentMessages.add(message);

    messageIDs.add(messageID);
    recipients.add(recipient);
    status.add("Sent");

    saveMessagesToJSON();

    totalMessages++;

    return "Message sent successfully.";

    } else if (choice == 2) {

    return "Message disregarded.";

    } else if (choice == 3) {

    storedMessages.add(message);

    messageIDs.add(messageID);
    recipients.add(recipient);
    status.add("Stored");
  
    saveMessagesToJSON();

    totalMessages++;

    return "Message stored successfully.";

    } else {

    return "Invalid option.";
        }
    }

    public void retrieveMessageByID(String id) {

    for (int i = 0; i < messageIDs.size(); i++) {

    if (messageIDs.get(i).equals(id)) {

    System.out.println("\nMessage Found");
    System.out.println("Recipient: " + recipients.get(i));
    System.out.println("Status: " + status.get(i));

    if (status.get(i).equals("Sent")) {

    System.out.println("Message: "+ sentMessages.get(getSentIndex(i)));

    } else {

    System.out.println("Message: "+ storedMessages.get(getStoredIndex(i)));
    }

    return;
    }
    }

    System.out.println("Message not found.");
    }

    public void deleteMessageByID(String id) {

    for (int i = 0; i < messageIDs.size(); i++) {

    if (messageIDs.get(i).equals(id)) {

    if (status.get(i).equals("Sent")) {

        sentMessages.remove(getSentIndex(i));

    } else {

         storedMessages.remove(getStoredIndex(i));
    }

    messageIDs.remove(i);
    recipients.remove(i);
    status.remove(i);
    
    saveMessagesToJSON();
    
    System.out.println("Message deleted.");
    
    return;
    }
    }

    System.out.println("Message not found.");
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

    writer.write("\"status\":\""  + status.get(i) + "\",\n");

    if (status.get(i).equals("Sent")) {

    writer.write("\"message\":\""  + sentMessages.get(getSentIndex(i)) + "\"\n");

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

    System.out.println("Messages saved to JSON file.");

    } catch (IOException e) {

    System.out.println("Error saving JSON file.");
    }
    }

    public void loadMessagesFromJSON() {

    try {
        
        File file = new File("messages.json");
        if (!file.exists()) return;

        Scanner fileScanner = new Scanner(file);
        String currentID = null, currentRecipient = null, 
               currentStatus = null, currentMessage = null;

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();

    if (line.startsWith("\"messageID\""))
        currentID = line.split(":")[1].replace("\"","").replace(",","").trim();
    else if (line.startsWith("\"recipient\""))
        currentRecipient = line.split(":")[1].replace("\"","").replace(",","").trim();
    else if (line.startsWith("\"status\""))
        currentStatus = line.split(":")[1].replace("\"","").replace(",","").trim();
    else if (line.startsWith("\"message\""))
        currentMessage = line.split(":")[1].replace("\"","").replace(",","").trim();
    else if (line.startsWith("}")) {
         if (currentID != null) {
            messageIDs.add(currentID);
            recipients.add(currentRecipient);
            status.add(currentStatus);
         if ("Sent".equals(currentStatus)) sentMessages.add(currentMessage);
    else storedMessages.add(currentMessage);
            totalMessages++;
    }
            currentID = currentRecipient = currentStatus = currentMessage = null;
            }
        }
        fileScanner.close();
        for (int i = 0; i < messageIDs.size(); i++) {
    System.out.println("ID: " + messageIDs.get(i) + 
                       " | Recipient: " + recipients.get(i) + 
                       " | Status: " + status.get(i));
}
        
    } catch (FileNotFoundException e) {
        
        System.out.println("No JSON file found.");
    }

    }

    public String printMessage() {

        return "Sent messages: " + sentMessages.toString();
    }

    public int returnTotalMessages() {

        return totalMessages;
    }

    public void runMenu() {

    while (true) {

    System.out.println("\n" + Options());

    System.out.print("Enter choice: ");

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

    System.out.print("Enter Message ID to retrieve: ");

    String retrieveID = input.nextLine();

    retrieveMessageByID(retrieveID);

    } else if (choice == 3) {

    System.out.print("Enter Message ID to delete: ");

    String deleteID = input.nextLine();

    deleteMessageByID(deleteID);

    } else if (choice == 4) {

    System.out.println("Goodbye!");

    System.exit(0);

    } else {

    System.out.println("Invalid option.");
    }
}
     
    }
}
