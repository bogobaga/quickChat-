
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.registration2.Message;

/**
 *
 * @author username
 */
public class MessageTest {
    


    Message test = new Message();


    @Test
    public void testCheckMessageIDSuccess() {

        assertTrue(Message.checkMessageID("1234567890"));
    }

    @Test
    public void testCheckMessageIDFail() {

        assertFalse(Message.checkMessageID("12345678901"));
    }

   
    @Test
    public void testCheckRecipientCellSuccess() {

        assertTrue(test.checkRecipientCell("+27838968900"));
    }

    @Test
    public void testCheckRecipientCellFail() {
        assertFalse(test.checkRecipientCell("0838968900"));
    }

 
    @Test
    public void testCreateMessageHash() {

    String result = test.createMessageHash( "00",1, "Hi Mike can you join us tonight");

    assertEquals("00:1:HITONIGHT",result);
    }

    
    @Test
    public void testMessageLengthSuccess() {

        String shortMessage =
                "Hello this message is under 250 characters";

        assertTrue(shortMessage.length() <= 250);
    }

    
    @Test
    public void testMessageLengthFail() {

        String longMessage =
              "Bafana Bafana played an incredible match last night "
          + "against Morocco at the FNB Stadium in Johannesburg. "
          + "The crowd was electric as Percy Tau scored a stunning "
          + "free kick in the final minutes of the game to secure "
          + "a dramatic victory for South Africa in the qualifiers.";

        assertTrue(longMessage.length() > 250);
    }

    
    @Test
    public void testReturnTotalMessages() {

        int expected = 0;

        int actual = test.returnTotalMessages();

        assertEquals(expected, actual);
    }
}