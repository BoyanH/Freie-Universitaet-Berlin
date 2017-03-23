import java.io.*;

/** liest ganze Zahlen von einem Eingabestrom, der unter anderem
    durch einen Dateinamen (String) gegeben sein kann oder die
    Standardeingabe (System.in) ist.
    Bei Dateiende wird EOFException ausgelöst.
    (Beim Lesen von der Standardeingabe wird das Dateiende als ^D (auf
    Unix-Systemen) oder ^Z und Return (auf Windows-Systemen) eingegeben.
*/

public class IntReader
    // zum Lesen einer Folge ganzer Zahlen
{
    StreamTokenizer input;

    IntReader() throws IOException
       { initIntReader(System.in); }
    IntReader(InputStream in) throws IOException
       { initIntReader(in); }
    IntReader(String file) throws IOException
       { initIntReader(new FileInputStream(file)); }
    IntReader(String [] args) throws IOException
    {
        if (args.length==0)
            initIntReader(System.in);
        else
            initIntReader(new FileInputStream(args[0]));
    }

    private void initIntReader(InputStream in)
    {
        Reader r = new BufferedReader(new InputStreamReader(in));
        input = new StreamTokenizer(r);

        input.resetSyntax();
        input.whitespaceChars('9'+1,255);
        input.whitespaceChars(0,'-'-1);
        input.whitespaceChars('-'+1,'0'-1);
        input.wordChars('0','9');
        input.wordChars('-','-');
    }


    public int readInt() throws IOException
    {
        if (input.nextToken() == StreamTokenizer.TT_EOF)
            throw new EOFException();
        else
            return Integer.parseInt(input.sval);
    }

    public static void main (String[] args) throws IOException
    {        // zum Testen
        IntReader in = new IntReader(args);
        int x;
        try
            { for (;;) 
                System.out.println(in.readInt());
            }
        catch (EOFException e) {};
    }
}
