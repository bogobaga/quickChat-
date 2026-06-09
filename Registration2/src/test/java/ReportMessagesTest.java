package test.registration2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class ReportMessagesTest {

    private ReportMessages storeMessages;

    @BeforeEach
    public void setUp() {
        // Clear all static lists before each test to avoid state bleed
        Message.getSentMessages().clear();
        Message.getStoredMessages().clear();
        Message.getDisregardedMessages().clear();
        Message.getMessageHashes().clear();
        Message.getMessageIDs().clear();
        Message.getRecipients().clear();
        Message.getStatus().clear();

        // Message 1 - Sent
        Message.getSentMessages().add("Did you get the cake?");
        Message.getMessageIDs().add("MSG001");
        Message.getRecipients().add("+27834557896");
        Message.getMessageHashes().add("MS:1:DIDcake?");
        Message.getStatus().add("Sent");

        // Message 2 - Stored
        Message.getStoredMessages().add("Where are you? You are late! I have asked you to be on time.");
        Message.getMessageIDs().add("MSG002");
        Message.getRecipients().add("+27838884567");
        Message.getMessageHashes().add("MS:2:WHEREtime.");
        Message.getStatus().add("Stored");

        // Message 3 - Disregarded (not added to main arrays)
        Message.getDisregardedMessages().add("Yohoooo, I am at your gate.");

        // Message 4 - Sent (developer entry, bypasses +27 validation in test context)
        Message.getSentMessages().add("It is dinner time !");
        Message.getMessageIDs().add("MSG004");
        Message.getRecipients().add("0838884567");
        Message.getMessageHashes().add("MS:4:ITtime");
        Message.getStatus().add("Sent");

        // Message 5 - Stored
        Message.getStoredMessages().add("Ok, I am leaving without you.");
        Message.getMessageIDs().add("MSG005");
        Message.getRecipients().add("+27838884567");
        Message.getMessageHashes().add("MS:5:OKyou.");
        Message.getStatus().add("Stored");

        storeMessages = new ReportMessages();
    }

    // Test 1: Sent Messages array correctly populated
    // Expected: contains "Did you get the cake?" and "It is dinner time !"
    @Test
    public void testSentMessagesArrayPopulated() {
        ArrayList<String> sent = Message.getSentMessages();
        assertEquals(2, sent.size());
        assertTrue(sent.contains("Did you get the cake?"));
        assertTrue(sent.contains("It is dinner time !"));
    }

    // Test 2: Display the longest message
    // Expected: "Where are you? You are late! I have asked you to be on time."
    @Test
    public void testLongestMessage() {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        String result = storeMessages.longestMessage();
        assertEquals(expected, result);
    }

    // Test 3: Search for message by ID (Message 4)
    // Expected: "It is dinner time !"
    @Test
    public void testSearchByID() {
        String result = storeMessages.searchByID("MSG004");
        assertEquals("It is dinner time !", result);
    }

    // Test 4: Search all messages for recipient +27838884567
    // Expected: both message 2 and message 5
    @Test
    public void testSearchByRecipient() {
        String result = storeMessages.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    // Test 5: Delete message 2 by its hash
    // Expected: "Message deleted" and message no longer in stored list
    @Test
    public void testDeleteByHash() {
        String result = storeMessages.deleteByHash("MS:2:WHEREtime.");
        assertEquals("Message deleted", result);
        assertFalse(Message.getStoredMessages()
                .contains("Where are you? You are late! I have asked you to be on time."));
    }

    // Test 6: Display report contains hash, recipient, and message fields
    @Test
    public void testReport() {
        String result = storeMessages.report();
        assertTrue(result.contains("Hash") || result.contains("MS:1:DIDcake?"));
        assertTrue(result.contains("Recipient") || result.contains("+27834557896"));
        assertTrue(result.contains("Did you get the cake?"));
    }
}