import java.io.*;
import java.net.*;

public class OSPFRouter {
  private int port;
  private InetAddress address;

  public OSPFRouter(int port, InetAddress address) {
    this.port = port;
    this.address = address;
  }

  public void start() throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    while (true) {
      Socket socket = serverSocket.accept();
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String message = in.readLine();
      System.out.println("Received message: " + message);
      in.close();
      socket.close();
    }
  }

  public void sendMessage(String message, int destPort, InetAddress destAddress) throws IOException {
    Socket socket = new Socket(destAddress, destPort);
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    out.println(message);
    out.close();
    socket.close();
  }

  public static void main(String[] args) throws IOException {
    int port = Integer.parseInt(args[0]);
    InetAddress address = InetAddress.getByName(args[1]);
    OSPFRouter router = new OSPFRouter(port, address);
    new Thread(() -> {
      try {
        router.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
    router.sendMessage("Hello, world!", port + 1, address);
  }
}
