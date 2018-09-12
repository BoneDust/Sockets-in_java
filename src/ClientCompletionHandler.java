import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ClientCompletionHandler implements CompletionHandler<Void, AsynchronousSocketChannel>
{
    public void completed(Void att, AsynchronousSocketChannel ch)
    {
        Client.startListeners();
    }

    public void failed(Throwable e, AsynchronousSocketChannel ch)
    {
        System.out.println("connection failed");
        e.printStackTrace();
    }
}