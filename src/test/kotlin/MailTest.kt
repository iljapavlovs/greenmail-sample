import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetupTest
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Arrays
import java.util.concurrent.TimeUnit
import javax.mail.Folder
import javax.mail.Session


class MailTest {

    private val bindAddress = System.getProperty("greenmail.host.address", "127.0.0.1")

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun test() {
        // Ugly workaround : GreenMail in docker starts with open TCP connections,
        //                   but TLS sockets might not be ready yet.

        TimeUnit.SECONDS.sleep(1)

        // SMTP - for send emails

        // Send messages via SMTP and secure SMTPS
        GreenMailUtil.sendTextEmail(
            "foo@localhost", "bar@localhost",
            "test1", "Test GreenMail Docker service",
            ServerSetupTest.SMTP.createCopy(bindAddress)
        )
        GreenMailUtil.sendTextEmail(
            "foo@localhost", "bar@localhost",
            "test2", "Test GreenMail Docker service",
            ServerSetupTest.SMTPS.createCopy(bindAddress)
        )


//        IMAP and POP3 - for receiving emails

        // IMAP
        for (setup in Arrays.asList(
            ServerSetupTest.IMAP.createCopy(bindAddress),
            ServerSetupTest.IMAPS.createCopy(bindAddress),
            ServerSetupTest.POP3.createCopy(bindAddress),
            ServerSetupTest.POP3S.createCopy(bindAddress)
        )) {
            val store = Session.getInstance(setup.configureJavaMailSessionProperties(null, false)).getStore()
            store.connect("foo@localhost", "foo@localhost")
            try {
                val folder: Folder = store.getFolder("INBOX")
                folder.open(Folder.READ_ONLY)
                assertEquals("Can not check mails using " + store.getURLName(), 2, folder.getMessageCount())
            } finally {
                store.close()
            }
        }
    }

}