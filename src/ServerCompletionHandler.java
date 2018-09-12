import java.nio.channels.*;


public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object>
{
    private AsynchronousServerSocketChannel server;

    public ServerCompletionHandler(AsynchronousServerSocketChannel server)
    {
        this.server = server;
    }

    public void completed(AsynchronousSocketChannel ch, Object att)
    {
       Server.handleConnection(ch);
       server.accept(att, this);
    }

    public void failed(Throwable e, Object att)
    {
        System.out.println(att + " - handler failed");
        e.printStackTrace();
    }
}