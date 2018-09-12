import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;

public class ClientMessages implements Runnable
{

    AsynchronousSocketChannel socket;

    public ClientMessages(AsynchronousSocketChannel socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        Scanner stdin = new Scanner(System.in);
        String msg = "";
        try
        {
            while (true) {
                System.out.print("Sending message to server: ");
                if (stdin.hasNextLine())
                    msg = stdin.nextLine();
                else
                    msg = "bye";
                byte[] bytes = msg.getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                socket.write(buffer);
                buffer.clear();
                if (new String(buffer.array()).trim().equals("bye"))
                    break;
            }
            socket.close();
        }
        catch (Exception ex){   }
    }
}