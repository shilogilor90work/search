import java.util.Hashtable;

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
     * Raw input from file
     * @param algo         which algo to use
     * @param time_info    boolean to calculate time
     * @param open_info    boolean if to print to screen
     * @param move_cost    move cost matrix
     * @param black_info   tiles that can not be moved.
     * @param matrix       the matrix of the board
     * @param n_size       matrix height size
     * @param m_size       matrix length size
     * @param blank_row    location of blank row
     * @param blank_column location of blank column
     */
    Raw_input(String algo, String time_info, String open_info, Hashtable<String, String> move_cost,String[] black_info, String[][] matrix, int n_size, int m_size, int blank_row, int blank_column)
    {
      this.algo = algo;
      if (time_info.equals("with time"))
      {
        this.time_info = true;
      } else
      {
        this.time_info = false;
      }
      if (open_info.equals("with open"))
      {
        this.open_info = true;
      } else
      {
        this.open_info = false;
      }
      this.move_cost = move_cost;
      this.m_size = m_size;
      this.n_size = n_size;
      this.black_info = black_info;
      this.matrix = matrix;
      this.blank_row = blank_row;
      this.blank_column = blank_column;
    }
  }
