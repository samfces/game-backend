package es.samfc.gamebackend.utils.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerUUIDFetcherTest {

    @Test
    @DisplayName("Check class constructor")
    void testConstructor() {
        assertThrows(IllegalStateException.class, PlayerUUIDFetcher::new);
    }

    @Test
    @DisplayName("Try with null username")
    void testWithNullUsername() {
        assertFalse(PlayerUUIDFetcher.getUUID(null).isPresent());
    }

    @Test
    @DisplayName("Try with cracked username")
    void testWithCrackedUsername() {
        Optional<UUID> fetchedUUID = PlayerUUIDFetcher.getUUID("Cr4cK3dU53r__123");
        assertTrue(fetchedUUID.isPresent());
        assertEquals(3, fetchedUUID.get().version());
    }

    @Test
    @DisplayName("Try with premium username")
    void testWithPremiumUsername() {
        Optional<UUID> fetchedUUID = PlayerUUIDFetcher.getUUID("Notch");
        assertTrue(fetchedUUID.isPresent());
        assertEquals(4, fetchedUUID.get().version());
    }

    @Test
    @DisplayName("Try with malformed username")
    void testWithMalformedUsername() {
        Optional<UUID> fetchedUUID = PlayerUUIDFetcher.getUUID("Cr4cK3dU53raaaaaaaaaaaaaaaaaaaaaaaa");
        assertFalse(fetchedUUID.isPresent());
    }

}