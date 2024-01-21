package SomNetworkVelocity;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Updater {

    private static final String path = SomCore.getDataDirectory() + ".jar";

    public static void UpdatePlugin() {
        try {
            SomCore.Log("§eSNV Downloading...");
            URL url = new URL("http://192.168.0.16:25515/SomNetworkVelocity/target/SomNetworkVelocity.jar");
            SomCore.Log("§eURL: " + url);
            SomCore.Log("§ePath: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.connect();

            int httpStatusCode = conn.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP Status " + httpStatusCode);
            }

            DataInputStream dataInStream = new DataInputStream(conn.getInputStream());
            DataOutputStream dataOutStream = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(Paths.get(path))));
            byte[] b = new byte[4096];
            int readByte;
            while (-1 != (readByte = dataInStream.read(b))) {
                dataOutStream.write(b, 0, readByte);
            }
            dataInStream.close();
            dataOutStream.close();
            SomCore.Log("§bSNV Downloaded!");
        } catch (Exception e) {
            SomCore.Log("§cSNV Download Error");
            e.printStackTrace();
        }
    }
}
