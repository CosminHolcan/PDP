using System.Collections.Generic;

namespace Lab4
{
    class Program
    {
        private static readonly List<string> URLS = new()
        {
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/progs/srv-begin-end.cs",
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/progs/srv-task.cs",
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/progs/srv-await.cs"
        };

        static void Main()
        {
            //CallbackParser callbackParser = new CallbackParser(URLS);
            //callbackParser.Run();
            TaskParser taskParser = new TaskParser(URLS);
            taskParser.Run();
            //AsyncAwaitParser asyncAwaitParser = new AsyncAwaitParser(URLS);
            //asyncAwaitParser.Run();
        }
    }
}
