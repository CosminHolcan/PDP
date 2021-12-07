using System;
using System.Collections.Generic;
using System.Threading;

namespace Lab4
{
    public class CallbackParser
    {
        private List<string> urls { get; set; }

        public CallbackParser(List<string> urls)
        {
            this.urls = urls;
        }

        public void Run()
        {
            foreach (string url in this.urls)
                Start(CustomSocket.Create(url));
        }

        private void Start(CustomSocket socket)
        {
            socket.StartConnect(HandleConnected);
            do
            {
                Thread.Sleep(100);
            }
            while (socket.Connected);
        }

        private void HandleConnected(CustomSocket socket)
        {
            Console.WriteLine($"Callback parser-{socket.FullUrl}: Socket connected");
            socket.StartSend(HandleSent);
        }

        private void HandleSent(CustomSocket socket, int totalSentBytes)
        {
            Console.WriteLine($"Callback parser-{socket.FullUrl}: Sent {totalSentBytes} bytes to server.");
            socket.StartReceive(HandleReceived);
        }

        private void HandleReceived(CustomSocket socket)
        {
            Console.WriteLine($"Callback parser-{socket.FullUrl}: Received content length is: {socket.GetResponseContentLength}\nContent is : \n{socket.ResponseContent}");
            socket.Finish();
        }
    }
}
