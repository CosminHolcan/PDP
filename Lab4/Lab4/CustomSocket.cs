using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Lab4
{
    public class CustomSocket : Socket
    {
        private const int DEFAULT_HTTP_PORT = 80;
        private const int BUFFER_SIZE = 10000;
        private string domainUrl;
        private string pathUrl;
        private IPEndPoint endPoint;
        private readonly Regex CONTENT_LENGTH_REGEX = new Regex(@"Content-Length: \d+");

        public string FullUrl { get; }

        public StringBuilder ResponseContent { get; } = new StringBuilder();

        public int GetResponseContentLength => int.Parse(CONTENT_LENGTH_REGEX
            .Match(ResponseContent.ToString())
            .Value
            .Substring(16));

        public static CustomSocket Create(string url)
        {
            int delimiterIndex = url.IndexOf('/');
            string domainUrl = delimiterIndex < 0 ? url : url.Substring(0, delimiterIndex);
            string urlPath = delimiterIndex < 0 ? "/" : url[delimiterIndex..];

            IPHostEntry ipHostInformation = Dns.GetHostEntry(domainUrl);
            IPAddress ipAddress = ipHostInformation.AddressList[0];

            return new CustomSocket(url, domainUrl, urlPath, ipAddress);
        }

        private CustomSocket(string url, string baseUrl, string urlPath, IPAddress ipAddress) :
            base(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp)
        {
            FullUrl = url;
            this.domainUrl = baseUrl;
            this.pathUrl = urlPath;
            endPoint = new IPEndPoint(ipAddress, DEFAULT_HTTP_PORT);
        }

        public void StartConnect(Action<CustomSocket> afterConnected)
        {
            BeginConnect(endPoint, asyncResult =>
            {
                EndConnect(asyncResult);
                afterConnected(this);
            }, null);
        }

        public void StartSend(Action<CustomSocket, int> afterSent)
        {
            string stringToSend = $"GET {pathUrl} HTTP/1.1\r\n" +
                $"Host: {domainUrl}\r\n" +
                "Content-Length: 0\r\n\r\n";
            byte[] encodedString = Encoding.ASCII.GetBytes(stringToSend);

            BeginSend(encodedString, 0, encodedString.Length, SocketFlags.None, asyncResult =>
            {
                int totalSentBytes = EndSend(asyncResult);
                afterSent(this, totalSentBytes);
            }, null);
        }

        public void StartReceive(Action<CustomSocket> afterReceived)
        {
            byte[] buffer = new byte[BUFFER_SIZE];
            ResponseContent.Clear();

            BeginReceive(buffer, 0, BUFFER_SIZE, SocketFlags.None, asyncResult =>
                HandleReceiveResult(asyncResult, buffer, afterReceived), null);
        }

        public Task StartConnectAsync() => Task.Run(() =>
        {
            TaskCompletionSource taskCompletion = new TaskCompletionSource();

            StartConnect(_ => taskCompletion.TrySetResult());

            return taskCompletion.Task;
        });

        public Task<int> StartSendAsync() => Task.Run(() =>
        {
            TaskCompletionSource<int> taskCompletion = new TaskCompletionSource<int>();

            StartSend((_, numberOfSentBytes) => taskCompletion.TrySetResult(numberOfSentBytes));

            return taskCompletion.Task;
        });

        public Task StartReceiveAsync() => Task.Run(() =>
        {
            TaskCompletionSource taskCompletion = new TaskCompletionSource();

            StartReceive(_ => taskCompletion.TrySetResult());

            return taskCompletion.Task;
        });

        public void Finish()
        {
            Shutdown(SocketShutdown.Both);
            Close();
        }

        private void HandleReceiveResult(
            IAsyncResult asyncResult,
            byte[] buffer,
            Action<CustomSocket> onReceived)
        {
            int numberOfReadBytes = EndReceive(asyncResult);
            ResponseContent.Append(Encoding.ASCII.GetString(buffer, 0, numberOfReadBytes));

            if (!IsContentFullyReceived)
            {
                BeginReceive(buffer, 0, BUFFER_SIZE, SocketFlags.None, asyncResult =>
                    HandleReceiveResult(asyncResult, buffer, onReceived), null);
                return;
            }

            onReceived(this);
        }

        private bool IsContentFullyReceived => ResponseContent.ToString().Contains("\r\n\r\n");
    }
}
