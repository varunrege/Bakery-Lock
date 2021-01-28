package edu.vt.ece.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import edu.vt.ece.bench.*;
import edu.vt.ece.bench.ThreadId;

public class Bakery implements Lock {

    static AtomicBoolean[] stat;
    TimestampImp TSfunc;

    public Bakery() {
        this(2);
    }

    public Bakery(int n) {
        stat = new AtomicBoolean[n];
        TSfunc = new TimestampImp(n);
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

    @Override
    public void lock() {
        int thrid = ((ThreadId)Thread.currentThread()).getThreadId();
        stat[thrid].set(true);
        Timestamp[] scan = TSfunc.scan();
        Timestamp tstamp = TSfunc.MaxTS(scan);
        TSfunc.label(tstamp,thrid);
        while(stop(thrid))
        {
            Thread.yield();
        }
    }

    @Override
    public void unlock() {
        int rem = ((ThreadId)Thread.currentThread()).getThreadId();
        stat[rem].set(false);

    }
}
