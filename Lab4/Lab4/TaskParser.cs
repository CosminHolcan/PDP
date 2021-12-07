using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Lab4
{
    public class TaskParser
    {
        private List<string> urls { get; set; }

        public TaskParser(List<string> urls)
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

        private Task Start(CustomSocket socket)
        {
            socket.StartConnectAsync().Wait();
            Console.WriteLine($"Task parser-{socket.FullUrl}: Socket connected");

            int totalSentBytes = socket.StartSendAsync().Result;
            Console.WriteLine($"Task parser-{socket.FullUrl}: Sent {totalSentBytes} bytes to server.");

            socket.StartReceiveAsync().Wait();
            Console.WriteLine($"Task parser-{socket.FullUrl}: Received content length is: {socket.GetResponseContentLength}\nContent is : \n{socket.ResponseContent}");

            socket.Finish();
            return Task.CompletedTask;
        }
    }
}
