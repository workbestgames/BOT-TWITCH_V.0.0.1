import java.io.*;
import java.net.Socket;

public class main {

    private static Socket conector;

    private static BufferedWriter  enviar;
    private static BufferedReader receber;

    private static String token = "oauth://";
    private static String nick = "eubyt";

    public static void main(String[] args) {
        try {
            IRC();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void IRC() throws IOException {

        /*
           WebSocket Clients
           SSL: wss://irc-ws.chat.twitch.tv:443
           Otherwise: ws://irc-ws.chat.twitch.tv:80

           IRC Clients
           SSL: irc://irc.chat.twitch.tv:6697
           Otherwise: irc://irc.chat.twitch.tv:6667
         */

        System.out.println("Conectando");

        conector = new Socket("irc.chat.twitch.tv", 6667);

        enviar = new BufferedWriter (new OutputStreamWriter(conector.getOutputStream()));
        receber = new BufferedReader(new InputStreamReader(conector.getInputStream()));

        /*
           Autenticar
           PASS oauth:<Twitch OAuth token>
           NICK <Twitch user>
         */

        Enviar("PASS " + token);
        Enviar("NICK "+ nick);

        /*
           CAP REQ :twitch.tv/membership
           CAP REQ :twitch.tv/tags
           CAP REQ :twitch.tv/commands
         */

        Enviar("CAP REQ :twitch.tv/membership");
        Enviar("CAP REQ :twitch.tv/tags");
        Enviar("CAP REQ :twitch.tv/commands");

        EntrarCanal("eubyt");

        String chat;
        while ((chat = receber.readLine()) != null)
        {
            System.out.println(chat);

            //A cada 5m o servidor envia PING :tmi.twitch.tv
            //Deve responder como PONG :tmi.twitch.tv

            if (chat.equals("PING :tmi.twitch.tv")) Enviar("PONG :tmi.twitch.tv");
        }



    }

    private static void EntrarCanal(String canal) {
        Enviar("JOIN #" + canal);
    }


    private static void Enviar(String texto) {
        try {
            enviar.write(texto + "\n");
            enviar.flush();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
