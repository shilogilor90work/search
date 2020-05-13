import java.util.Hashtable;

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
    public String toString()
    {
      //overriding the toString() method
      return "algo : " + algo + " time_info: " + time_info + " open_info: " + open_info + " n_size: " + n_size + " m_size: " + m_size + " blank_row: " + blank_row + " blank_column: " + blank_column + " matrix:\n to be done later\n" + move_cost.toString();
    }

  }
