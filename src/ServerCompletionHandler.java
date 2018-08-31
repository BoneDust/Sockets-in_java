import java.nio.channels.*;

public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object>
{
    private AsynchronousChannelGroup groupChannel;
    private AsynchronousServerSocketChannel server;
    private Thread currentThread;

    public ServerCompletionHandler(AsynchronousServerSocketChannel server, AsynchronousChannelGroup group, Thread thread)
    {
        this.server = server;
        groupChannel = group;
        currentThread  = thread;
    }

    public void completed(AsynchronousSocketChannel ch, Object att)
    {
        while(Server.handleConnection(ch, groupChannel, currentThread))
            ;
        server.accept(att, this);
    }

    public void failed(Throwable e, Object att)
    {
        System.out.println(att + " - handler failed");
        e.printStackTrace();
        currentThread.interrupt();
    }
}