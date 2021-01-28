package edu.vt.ece.bench;

import edu.vt.ece.locks.Timestamp;
import edu.vt.ece.locks.TimestampSystem;

public class TimestampImp implements TimestampSystem {
    public static volatile Label Record[];
    public int RecordLn;
    public TimestampImp(int n)
    {
        Record = new Label[n];
        RecordLn = Record.length;
        for(int i = 0; i < n; i++)
        {
            Record[i] = new Label();
        }
    }

    @Override
    public Timestamp[] scan()
    {
        return Record;
    }

    public Timestamp MaxTS(Timestamp[] arg)
    {
        Label highest = new edu.vt.ece.bench.Label();
        Label buff = (Label) arg[0];
        for(int i = 0; i < arg.length; i++)
        {
            buff = (Label) arg[i];
            if(!(highest.compare(buff)))
            {
                highest = buff;
            }
        }
        return highest;
    }

    @Override
    public void label(Timestamp TS, int i)
    {
        long bit;
        Label TSr = (Label) TS;
        bit = TSr.Index.get() + 1;
        Record[i] = new Label(bit,i);
    }
}
