package es.samfc.gamebackend.utils.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

/**
 * Clase para obtener el ID de un jugador a partir de su nombre.
 */
public class PlayerUUIDFetcher {

    PlayerUUIDFetcher() {
        throw new IllegalStateException("No puede instanciarse PlayerUUIDFetcher");
    }

    /**
     * Método para obtener el ID de un jugador a partir de su nombre. Obtendremos el ID a partir de la API de PlayerDB.
     * @param nombreUsuario Nombre del jugador.
     * @return Optional<UUID> ID del jugador.
     */
    public static Optional<UUID> getUUID(String nombreUsuario) {
        if (nombreUsuario == null) return Optional.empty();
        try {
            URL url = new URL("https://playerdb.co/api/player/minecraft/" + nombreUsuario);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("User-Agent", "JavaApp");

            int responseCode = conexion.getResponseCode();
            //Si el jugador no existe, la API devuelve 400 Bad Request
            if (responseCode == 400) return generateOfflineUUID(nombreUsuario);
            if (responseCode != 200) return Optional.empty();

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = in.readLine()) != null) {
                respuesta.append(linea);
            }

            in.close();
            String json = respuesta.toString();

            // Buscamos el campo "id" dentro de "player"
            int idIndex = json.indexOf("\"id\":\"");

            int start = idIndex + 6;
            int end = json.indexOf("\"", start);
            return Optional.of(UUID.fromString(json.substring(start, end)));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<UUID> generateOfflineUUID(String username) {
        String uuidString = "OfflinePlayer:" + username;
        return Optional.of(UUID.nameUUIDFromBytes(uuidString.getBytes()));
    }

}
