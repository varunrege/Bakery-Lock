package edu.vt.ece.bench;

import edu.vt.ece.locks.Timestamp;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Label implements Timestamp {
    AtomicLong Index;
    AtomicInteger Sign;

    public Label()
    {
        this(0,0);
    }

    public Label(long Sign2, int Index2)
    {
        this.Index = new AtomicLong(Sign2);
        this.Sign = new AtomicInteger(Index2);
    }

    @Override
    public boolean compare(Timestamp other)    // false - earlier timestamp == other is larger
    {
        Label Lbl1 = (Label) other;
        if(this.Index.get() >= Lbl1.Index.get())
        {
            if((this.Index.get() == Lbl1.Index.get()) && (this.Sign.get() < Lbl1.Sign.get()))
            {
                return false;
            }
            return true;
        }
        else
        {
            return false;
        }
    }
}
