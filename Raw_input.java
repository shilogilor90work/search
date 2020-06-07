import java.util.Hashtable;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
/**
 * Raw input from file.
 */
class Raw_input {
    String algo;
    boolean time_info;
    boolean open_info;
    String[] black_info;
    Hashtable<String, String> move_cost = new Hashtable<String, String>();
    int n_size;
    int m_size;
    String[][] matrix;
    int blank_row;
    int blank_column;

    /**
     * Raw input from the input.txt file converted to an object.
     */
    Raw_input()
    {
      try {
        // read file
          Scanner myReader = new Scanner(new File("input.txt"));
          // algorithm type
          this.algo = myReader.nextLine();
          // with time bool
          if (myReader.nextLine().equals("with time"))
          {
            this.time_info = true;
          } else
          {
            this.time_info = false;
          }
          // with open bool
          if (myReader.nextLine().equals("with open"))
          {
            this.open_info = true;
          } else
          {
            this.open_info = false;
          }
          String size_info = myReader.nextLine();
          // n_size data
          this.n_size =  Integer.parseInt(size_info.substring(0 , size_info.indexOf("x")));
          // m_size data
          this.m_size =  Integer.parseInt(size_info.substring(size_info.indexOf("x") + 1));
          String black_raw_info = myReader.nextLine();
          // black as array info
          this.black_info = black_raw_info.substring(black_raw_info.indexOf(":") + 1).replaceAll("\\s","").split(",");
          String red_raw_info = myReader.nextLine();
          // red as array info
          String [] red_info = red_raw_info.substring(red_raw_info.indexOf(":") + 1).replaceAll("\\s","").split(",");
          // initialize  the matrix size
          this.matrix = new String[this.n_size][this.m_size];
          // initialize the move_cost data
          for(int i=0;i<this.n_size;i++)
          {
            String[] result = myReader.nextLine().split(",");
            for(int j=0;j<result.length;j++)
            {
              this.matrix[i][j] = result[j];
              if (Arrays.asList(red_info).contains(this.matrix[i][j]))
              {
                this.move_cost.put(this.matrix[i][j], "30");
              } else if (Arrays.asList(this.black_info).contains(this.matrix[i][j]))
              {
                this.move_cost.put(this.matrix[i][j], "false");
              } else
              {
                this.move_cost.put(this.matrix[i][j], "1");
              }
              if (this.matrix[i][j].equals("_"))
              {
                this.blank_row = i;
                this.blank_column = j;
              }
            }
          }
          myReader.close();
        } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
    }
  }
