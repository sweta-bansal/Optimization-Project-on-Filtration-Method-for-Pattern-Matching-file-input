
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MatchingAlgo_file {

	 public static int[] radixSort(int[] a) {
	 
		int w=32; //bit
		int d=4; //digit
        int[] b = null;
        for (int p = 0; p < w/d; p++) {
            int c[] = new int[1<<d];
            // the next three for loops implement counting-sort
            b = new int[a.length];
            for (int i = 0; i < a.length; i++)
                c[(a[i] >> d*p)&((1<<d)-1)]++;
            for (int i = 1; i < 1<<d; i++)
                c[i] += c[i-1];
            for (int i = a.length-1; i >= 0; i--)
                b[--c[(a[i] >> d*p)&((1<<d)-1)]] = a[i];
            a = b;
        }
        return b;
    }
	public static int[] calculatePr(int[] pattern, int[] sortedPattern)
	{
		int n=pattern.length;
		int Pr[]=new int[n];
		for(int i=0;i<n;i++)
		{
			int num=sortedPattern[i];
			for(int j=0;j<n;j++)
			{
				if(num==pattern[j])
					Pr[i]=j;
			}
		}
		
		return Pr;
	}
	public static int[] convertToBit(int[] a)
	{
		int n=a.length;
		int[] b=new int[n-1];
		for(int i=0;i<n-1;i++)
		{
			if(a[i]<a[i+1])
				b[i]='1';
			else
				b[i]='0';
		}
		return b;
	}
	/**
	 * Pre processes the pattern array based on proper prefixes and proper
	 * suffixes at every position of the array
	 *
	 * @param ptrn
	 *            word that is to be searched in the search string
	 * @return partial match table which indicates
	 */
	public static int[] preProcessPattern(int[] ptrn) {
	    int i = 0, j = -1;
	    int ptrnLen = ptrn.length;
	    int[] b = new int[ptrnLen + 1];
	 
	    b[i] = j;
	    while (i < ptrnLen) {            
	            while (j >= 0 && ptrn[i] != ptrn[j]) {
	            // if there is mismatch consider the next widest border
	            // The borders to be examined are obtained in decreasing order from 
	            //  the values b[i], b[b[i]] etc.
	            j = b[j];
	        }
	        i++;
	        j++;
	        b[i] = j;
	    }
	    return b;
	}
	/**
     * Based on the pre processed array, search for the pattern in the text
     *
     * @param text
     *            text over which search happens
     * @param ptrn
     *            pattern that is to be searched
     */
   
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List KMPsearchSubString(int[] text, int[] ptrn) {
        int i = 0, j = 0;
        // pattern and text lengths
        int ptrnLen = ptrn.length;
        int txtLen = text.length;
        List matchedIndex= new ArrayList();
        
        // initialize new array and preprocess the pattern
        int[] b = preProcessPattern(ptrn);
 
        while (i < txtLen) {
            while (j >= 0 && text[i] != ptrn[j]) {
                j = b[j];
            }
            i++;
            j++;
 
            // a match is found
            if (j == ptrnLen) {
                j = b[j];
                matchedIndex.add(i - ptrnLen);
            }
        }
        return matchedIndex;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List OrderPreservingMatch(int[] text, int[] pattern)
    {
    	//converting to bit array
    	int tBit[]=convertToBit(text);
		int pBit[]=convertToBit(pattern);
		//calling KMPSearch for exact bit pattern match
		List Tj=KMPsearchSubString(tBit, pBit);
		int textIntArray[]=new int[text.length];
		int patternIntArray[]=new int[pattern.length];
		for (int i=0;i<textIntArray.length;i++)
		{
			textIntArray[i]=(int)text[i];
		}
		for (int i=0;i<patternIntArray.length;i++)
		{
			patternIntArray[i]=(int)pattern[i];
		}
		int sortedPattern[]=radixSort(patternIntArray);
		//computing E bit array on pattern
		int E[]=new int[sortedPattern.length];
		
		for(int i=0;i<sortedPattern.length-1;i++)
		{
			if(sortedPattern[i]==sortedPattern[i+1])
				E[i]=1;
			else
				E[i]=0;
			
		}
		// computing Pr to store the index in sorted order of pattern
		int Pr[]=calculatePr(patternIntArray, sortedPattern);
		// verification on pre-processed pattern
		for(int j=0;j<Tj.size();j++)
		{
			int mismatch=0;
			for(int i=0;i<pattern.length-1;i++)
			{
				if(textIntArray[(int)Tj.get(j)+Pr[i]]>textIntArray[(int)Tj.get(j)+Pr[i+1]])
				{
					mismatch=1;
					break;
				}
				else if(textIntArray[(int)Tj.get(j)+Pr[i]]==textIntArray[(int)Tj.get(j)+Pr[i+1]] && E[i]==0 )
				{
					mismatch=1;
					break;
				}
				else if(textIntArray[(int)Tj.get(j)+Pr[i]]<textIntArray[(int)Tj.get(j)+Pr[i+1]] && E[i]==1 )
				{
					mismatch=1;
					break;
				}
			}
			if(mismatch==1)
			{
				Tj.set(j, -1);
			}
			
		}
		return Tj;
    }
   
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		int text[];
		int pattern[];
		for (String line : Files.readAllLines(Paths.get("C://Users/Sweta/Desktop/MS in CS GMU/CS-583 Analysis of Algorithm/project/input.txt"))) 
		{	
			String[] sections = line.split(";");
			String[] t = sections[0].split(",");
			String[] p = sections[1].split(",");
			text=new int[t.length];
			pattern=new int[p.length];
			for(int i=0;i<t.length;i++)
			{
				text[i]=Integer.valueOf(t[i]);
				
			}
			
			for(int i=0;i<p.length;i++)
			{
				pattern[i]=Integer.valueOf(p[i]);
				
			}
		
		List Tj=OrderPreservingMatch(text, pattern);

		int count=0;
		for(int i=0;i<Tj.size();i++)
		{
			if ((int)Tj.get(i)>0)
				count++;
		}
		System.out.println("Number of matches = "+count);
		for(int i=0;i<Tj.size();i++)
		{
			if ((int)Tj.get(i)>0)
			{
				int pos=(int) Tj.get(i)+1;
				System.out.print(pos+"("+text[(int) Tj.get(i)]);
				for(int j=(int)Tj.get(i)+1;j<(int)Tj.get(i)+pattern.length;j++)
				{
					System.out.print(","+(text[j]+0));
				}
				System.out.print("),");
			}
		}
		
	}
	}
}
