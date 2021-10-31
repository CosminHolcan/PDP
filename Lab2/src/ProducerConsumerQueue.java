import java.lang.constant.Constable;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerQueue {
    private final Lock m_mtx = new ReentrantLock();
    Queue<Integer> m_queue = new ArrayDeque<Integer>();
    private final Condition m_cv = m_mtx.newCondition();
    boolean m_isEnd = false;


    public void enqueue(int val) {
        m_mtx.lock();
        m_queue.add(val);
        m_cv.signal();
        m_mtx.unlock();
    }

    public Integer dequeue() {
        int ret;
        m_mtx.lock();
        while (true) {
            if (!m_queue.isEmpty()) {
                ret = m_queue.remove();
                m_mtx.unlock();
                return ret;
            }
            if (m_isEnd) {
                m_mtx.unlock();
                return null;
            }
            try {
                m_cv.await();
            } catch(InterruptedException e) {
                System.err.println("Exception: " + e);
            }
        }
    }

    public void close() {
        m_mtx.lock();
        m_isEnd = true;
        m_cv.signalAll();
        m_mtx.unlock();
    }
};