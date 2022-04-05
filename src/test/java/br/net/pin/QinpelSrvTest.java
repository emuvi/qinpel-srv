package br.net.pin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QinpelSrvTest {
    @Test
    void constructed() {
        var constructed = new QinpelSrv();
        assertNotNull(constructed, "command should be constructed");
    }
}
