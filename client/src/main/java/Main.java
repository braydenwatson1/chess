import chess.*;
import client.ServerFacade;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client: ");

        var serverUrl = "http://localhost:8080";
        ServerFacade server = new ServerFacade(serverUrl);

        new Repl(server).run();
    }
}