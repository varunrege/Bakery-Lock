package edu.vt.ece.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import edu.vt.ece.bench.*;
import edu.vt.ece.bench.ThreadId;
public class LBakery implements Lock {



    static AtomicBoolean[] stat;
    TimestampImp TSfunc;
    static int Limit;
    private static AtomicInteger Counter;


    public LBakery(int l, int n) {

        stat = new AtomicBoolean[n];
        TSfunc = new TimestampImp(n);
        Limit = l;
        Counter = new AtomicInteger(0);
        for(int i = 0; i < n; i++)
        {
            stat[i] = new AtomicBoolean();
        }
    }

    public boolean stop(int a)
    {
        boolean stopfunc = false;
        for(int k = 0; k < stat.length; k++)
        {
            if(k != a)
            {
                if(stat[k].get())
                {
                    if(TSfunc.Record[a].compare(TSfunc.Record[k]) == true)
                    {
                        stopfunc = true;
                        break;
                    }
                }
            }
        }
        return stopfunc;
    }
    public int getNumberofThreadsInCS()
    {
        return Counter.get();
    }


    @Override
    public void lock() {
        int threadID = ((ThreadId)Thread.currentThread()).getThreadId();
        stat[threadID].set(true);
        Timestamp[] scan = TSfunc.scan();
        Timestamp tstamp = TSfunc.MaxTS(scan);
        TSfunc.label(tstamp,threadID);
        while((Counter.get() >= Limit) && stop(threadID))
        {
            Thread.yield();
        }
        Counter.set(Counter.get()+1);
    }

    @Override
    public void unlock() {
        int myself = ((ThreadId)Thread.currentThread()).getThreadId();
        stat[myself].set(false);
        Counter.set(Counter.get()-1);
    }
}
