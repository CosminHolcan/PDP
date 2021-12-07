using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Lab4
{
    public class AsyncAwaitParser
    {
        private List<string> urls { get; set; }

        public AsyncAwaitParser(List<string> urls)
        {
            this.urls = urls;
        }

        public void Run()
        {
            List<Task> tasks = new List<Task>();

            foreach (string url in urls)
            {
                tasks.Add(Task.Run(() => Start(CustomSocket.Create(url))));
            }

            Task.WhenAll(tasks).Wait();
        }

        private async Task Start(CustomSocket socket)
        {
            await socket.StartConnectAsync();
            Console.WriteLine($"Async/Await parser-{socket.FullUrl}: Socket connected");

            int totalSentBytes = await socket.StartSendAsync();
            Console.WriteLine($"Async/Await parser-{socket.FullUrl}: Sent {totalSentBytes} bytes to server.");

            await socket.StartReceiveAsync();
            Console.WriteLine($"Async/Await parser-{socket.FullUrl}: Received content length is: {socket.GetResponseContentLength}\nContent is : \n{socket.ResponseContent}");

            socket.Finish();
        }
    }
}
