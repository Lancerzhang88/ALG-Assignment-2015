import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.File;
import java.util.*;


public class WordMatch
{
   /**
    * This class use for storing word and how many time was counted in the
    * articles.
    * 
    */
   public static class WordCount implements Comparable<WordCount>
   {
      String word;
      int count;

      /**
       * Consturctor function
       *
       * @param word    initializ attribute and set count as 1
       */
      WordCount(String word) { this.word = word; count = 1;}

      /**
       * Increase word times
       */
      public void Add() { count++;}

      /**
       * Reconstruct comparing function for class
       *
       * @param tmpWC   use for comapring with var word in class
       * @return        get the compare result between strings
       */
      public int compareTo(WordCount tmpWC)
      {
         return this.word.compareTo(tmpWC.word);
      }
   }

   public static ArrayList<WordCount> wordsCount;
   public static ArrayList<ArrayList<String>> neighbors;

   /**
    * read from specific file to var wordsCount
    *
    * @param filesName  
    */
   public static void ReadFile(String filesName)
   {
      try {
         BufferedReader inFile = new BufferedReader(new FileReader(filesName));

         String line = null;
         //read a whole line from file       
         while((line = inFile.readLine()) != null) {

            line = line.trim().toLowerCase();

            String[] words = line.split("[^a-z]");

            for (String word : words) {
               //Filter non-letter word
               if(!word.matches(".*[a-z].*")) continue;
               boolean markFind = false;
               for(WordCount wordCount : wordsCount) {
                  if(wordCount.word.compareTo(word) == 0) {
                     wordCount.Add();
                     markFind = true;
                     break;
                  }
               }
               //Add new word into the list 
               if(!markFind) {
                  wordsCount.add(new WordCount(word));
                  markFind = false;
               }
            }
         }
         inFile.close();
      } catch (FileNotFoundException e) {
         System.out.println("File " + filesName + " not found");
      } catch (Exception e) {
         System.out.println("Existing problem!");
      }
   }

  public static void ReadWordFiles(String fileName) 
  {
    try {
      BufferedReader inFile = new BufferedReader(new FileReader(fileName));

          String line = null;
         //read a whole line from file       
      while((line = inFile.readLine()) != null) {
        ReadFile(line.trim());
      }
    } catch (FileNotFoundException e) {
      System.out.println("File " + fileName + " not found");
    } catch (Exception e) {
      System.out.println("File: " + fileName + " exist problem");
    }
  }

  public static void ReadPatternsFile(String fileName, String patternResultFile)
  {
      try {
      BufferedReader inFile = new BufferedReader(new FileReader(fileName));

          String line = null;
          String findPatternResult = "";
         //read a whole line from file       
         while((line = inFile.readLine()) != null) {
            findPatternResult += line + "\n" + MatchingWords(line) + "\n";
         }

         WriteToFile(findPatternResult, patternResultFile);

    } catch (FileNotFoundException e) {
      System.out.println("File " + fileName + " not found");
    } catch (Exception e) {
      System.out.println("File: " + fileName + " exist problem");
    }
  }

   /**
    * Find similar word in the list and save results in the var neighbors with
    * same order as words list 
    */
   public static void FindNeighbors()
   {
      neighbors = new ArrayList<ArrayList<String>>();
      //browse all words in the list
      for(WordCount wordCount : wordsCount) {
         ArrayList<String> neighbor = new ArrayList<String>();
         for(WordCount wordCount2 : wordsCount) {
            int diff = 0;
            //If the word has the same length and just one different letter, put
            //it in the neighbor list.
            if(wordCount.word.length() == wordCount2.word.length() &&
                  wordCount.compareTo(wordCount2) != 0) {
               for(int i = 0; i < wordCount.word.length();i++) {
                  if(wordCount.word.charAt(i) != wordCount2.word.charAt(i)) {
                     diff++;
                  }
                  if(diff == 2) break;
               }

               if(diff < 2) neighbor.add(wordCount2.word);

               diff = 0;
            }
         }
         //add neighbor list with words list order 
         neighbors.add(neighbor);
      }
   }
   /**
    * Quick sort algorithm
    *
    * @param list    prepare for sorting string list
    * @param left    start number
    * @param right   end number
    */
   public static int count = 0;
   
   public static void quickSort(ArrayList<WordCount> list, int left, int right)
   {
	  System.out.print("\rProcessing percentage: %" + (count++/(double)totalRecureTimes));
      if(left >= right)
         return;

//      WordCount pivot = list.get(right);
 //     int partition = partition(list, left, right, pivot);
      int partition = partition(list, left, right);
      quickSort(list, 0, partition - 1);
      quickSort(list, partition + 1, right);
   }

   /**
    * Calculate the position of middle word in the list, meanwhile reordered as
    * alphabetical order
    *
    * @param list    store unsorted words
    * @param left    starting point
    * @param right   ending point
    * @param pivot   middle word
    * @return        the position number of pivot
    */
   private static int partition(ArrayList<WordCount> list, int left, int right)
   {
	  /*
      int leftCursor = left - 1;
      int rightCursor = right;
      WordCount pivot = list.get(right);
      
      while(leftCursor < rightCursor) {
         //Find smaller string
         while(list.get(++leftCursor).compareTo(pivot) < 0);
         //Find bigger string
         while(rightCursor > 0 && list.get(--rightCursor).compareTo(pivot) > 0);
         if(leftCursor >= rightCursor) {
            break;
         } else {
            swap(list, leftCursor, rightCursor);
         }
      }
      swap(list, leftCursor, right);
      return leftCursor;
      */
	   WordCount pivot = list.get(right);
	   while(left < right) {
		   while(left < right && list.get(right).compareTo(pivot) >= 0) right--;
		   
	   }
   }

   /**
    * Swap positions between left and right.
    *
    * @param list    sort list
    * @param left    left word would be swapped
    * @param right   right word would be swapped
    */
   private static void swap(ArrayList<WordCount> list, int left, int right) {
      list.set(left, list.set(right, list.get(left)));
   }


   /**
    * Write words list and neighbors into file
    *
    * @param fileName
    */
   public static void WriteFile(String fileName)
   {
      try{
         PrintWriter writer = new PrintWriter(new File(fileName));
         //Browse all words
         for(int i = 0; i < wordsCount.size(); i++) {
            writer.print(wordsCount.get(i).word + " " + wordsCount.get(i).count + " [");
            for(int j = 0; j < neighbors.get(i).size(); j++) {
               writer.print(neighbors.get(i).get(j));
               //If the neighbor is not last in the neighbors list, would
               //follow with a comma
               if(j != (neighbors.get(i).size() - 1))
                  writer.print(", ");
            }
            writer.print("]\n");
         }
         writer.close();
      } catch (Exception e) {
         System.out.println("Could not operate " + fileName + "\nPlease check it");
      }

   }

   public static void WriteToFile(String line, String fileName)
   {
      try{
         PrintWriter writer = new PrintWriter(new File(fileName));
         writer.println(line);
         writer.close();
      }catch(Exception e){
         System.out.println("Could not operate writing file. \nPlease check it");
      }
   }


  /**
    * By using symbol * ? to find some target words
    * Star(*) means zero or multi chars 
    * Question mark(?) means single char
    *
    * @param format  purpose pattern
    * @return        get results of words by using pattern from words list
    */
  public static String  MatchingWords(String format)
   {
      ArrayList<WordCount> results = new ArrayList<WordCount>();

      char[] chars =  format.toCharArray();
      for(WordCount wordCount : wordsCount) {
         char wordChars[] = wordCount.word.toCharArray();
         //Pattern pointer and word pointer that is sorted in the WORDSCOUNT are used to check difference between them 
         int charsPointer = 0;
         int wordsPointer = 0;

         //If the * symbol is find the variable STAR set true 
         boolean STAR = false;

         while(charsPointer < format.length() && wordsPointer < wordChars.length) {
            //If find * in the pattern word, move the pattern pointer to next
            //position
            if(chars[charsPointer] == '*') {
               charsPointer++;
               STAR = true;
               continue;
            }

            //If pattern char is ? would continue to compare
            if(chars[charsPointer] == '?') {
               charsPointer++;
               wordsPointer++;
               continue;
            }

            if(chars[charsPointer] == wordChars[wordsPointer]) {
               if(wordsPointer < wordChars.length - 1)
                  if(STAR && chars[charsPointer] == wordChars[wordsPointer + 1]) {//MID REPEAT 
                     wordsPointer++;
                     continue;
                  }

               STAR = false;
               charsPointer++;
               wordsPointer++;

               //The pattern word with '*' when the word is short than pattern  
               if(charsPointer == chars.length - 1)
                  if(chars[charsPointer] == '*') {
                     charsPointer++;
                     STAR = true;
                  }

            } else if(STAR) {
               wordsPointer++;
            } else {
               break;
            }
         }
         //If the pattern word end with a * OR processing both of two words
         //completed comparison with reaching the end of words
         if((STAR && charsPointer == chars.length) || (charsPointer == chars.length && wordsPointer == wordChars.length)) {
            results.add(wordCount);
         }
      }

      String returnStr = "";
      //write result as a string
      if(results.size() != 0) {
         for(WordCount wc : results) {
            returnStr += wc.word + " " + wc.count + "\n";
         }
      } else {
         returnStr = "No words in the lexicon match the pattern\n";
      }

      return returnStr;
  }
  
  public static int totalRecureTimes = 0;

  public static void main(String[] args)
  {
  	long startTime = System.currentTimeMillis();

      if(args.length != 4) {
        System.out.println("Error Command: Please Check It Again! ");
        System.out.println("java WordMatch inputFiles.txt wordList.txt patterns.txt result.txt\n");
      } else {
        //initialize list
        wordsCount = new ArrayList<WordCount>();
        wordsCount.clear();

        long readStartTime = System.currentTimeMillis();
        ReadWordFiles(args[0]);
        long readEndTime = System.currentTimeMillis();
        System.out.println("Read Time: " + (readEndTime - readStartTime) + "ms");
        
        // sort words in the list as alphabet order
        long sortStartTime = System.currentTimeMillis();
        totalRecureTimes = (int)(Math.log(wordsCount.size())/Math.log(2));
        WordMatch.quickSort(wordsCount, 0, wordsCount.size()-1);
        long sortEndTime = System.currentTimeMillis();
        System.out.println("Sort Time: " + (sortEndTime - sortStartTime) + "ms");
        System.out.println("Sort times: " + count);
        System.out.println("List Length: " + wordsCount.size());
        FindNeighbors();
        // Write word list file
        WriteFile(args[1]);

        ReadPatternsFile(args[2], args[3]);
      }
    long endTime = System.currentTimeMillis();
    System.out.println("Total Time: " + (endTime - startTime) + "ms");
  }
}                                                           