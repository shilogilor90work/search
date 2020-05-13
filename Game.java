import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Hashtable;

class Game {
    Hashtable<String, String> move_cost = new Hashtable<String, String>();
    int n_size;
    int m_size;
    String[][] matrix;
    int blank_row;
    int blank_column;
    int heuristic;
    int cost;
    List<String> path_to_here = new ArrayList<String>();
    boolean do_heuristic;
    char last_direction;
    Game(Hashtable<String, String> move_cost, String[][] matrix, int n_size, int m_size, int blank_row, int blank_column, boolean do_heuristic)
    {
      this.move_cost = move_cost;
      this.n_size = n_size;
      this.m_size = m_size;
      this.matrix = matrix;
      this.blank_row = blank_row;
      this.blank_column = blank_column;
      this.move_cost = move_cost;
      this.cost = 0;
      this.do_heuristic = do_heuristic;
      this.heuristic = 0;
      if (do_heuristic) {
        this.heuristic = calculate_heuristic();
      }
      this.last_direction = '_';
    }
    Game(Game game, char direction)
    {
      this.move_cost = game.move_cost;
      this.n_size = game.n_size;
      this.m_size = game.m_size;
      this.matrix = new String[this.n_size][this.m_size];
      // copy contents of matrix to new matrix
      for(int i = 0; i<this.n_size; i++)
      {
          for(int j = 0; j<this.m_size; j++)
          {
            this.matrix[i][j] = game.matrix[i][j];
          }
      }
      this.blank_row = game.blank_row;
      this.blank_column = game.blank_column;
      this.path_to_here = game.path_to_here;
      this.path_to_here = new ArrayList<String>(game.path_to_here);
      this.do_heuristic = game.do_heuristic;
      switch (direction) {
        case 'U':
          this.matrix[this.blank_row][this.blank_column] = this.matrix[this.blank_row + 1][this.blank_column];
          this.matrix[this.blank_row + 1][this.blank_column] = "_";
          this.cost = game.cost + Integer.parseInt(move_cost.get(this.matrix[this.blank_row][this.blank_column]));
          this.blank_row = game.blank_row + 1;
          this.blank_column = game.blank_column;
          break;
        case 'D':
          this.matrix[this.blank_row][this.blank_column] = this.matrix[this.blank_row - 1][this.blank_column];
          this.matrix[this.blank_row - 1][this.blank_column] = "_";
          this.cost = game.cost + Integer.parseInt(move_cost.get(this.matrix[this.blank_row][this.blank_column]));
          this.blank_row = game.blank_row - 1;
          this.blank_column = game.blank_column;
          break;
        case 'L':
          this.matrix[this.blank_row][this.blank_column] = this.matrix[this.blank_row][this.blank_column + 1];
          this.matrix[this.blank_row][this.blank_column + 1] = "_";
          this.cost = game.cost + Integer.parseInt(move_cost.get(this.matrix[this.blank_row][this.blank_column]));
          this.blank_row = game.blank_row;
          this.blank_column = game.blank_column + 1;
          break;
        case 'R':
          this.matrix[this.blank_row][this.blank_column] = this.matrix[this.blank_row][this.blank_column - 1];
          this.matrix[this.blank_row][this.blank_column - 1] = "_";
          this.cost = game.cost + Integer.parseInt(move_cost.get(this.matrix[this.blank_row][this.blank_column]));
          this.blank_row = game.blank_row;
          this.blank_column = game.blank_column - 1;
          break;
        }
        if (this.do_heuristic) {
          this.heuristic = calculate_heuristic();
        }
        this.path_to_here.add(this.matrix[game.blank_row][game.blank_column] + direction);
        this.last_direction = direction;
    }
    private int calculate_heuristic()
    {
      // manhaten * cost of movement
      int heuristic_value = 0;
      for(int i = 0; i<this.n_size; i++)
      {
          for(int j = 0; j<this.m_size; j++)
          {
            if (!this.matrix[i][j].equals("_"))
            {
              int loc = Integer.parseInt(this.matrix[i][j]) - 1;
              heuristic_value += Integer.parseInt(move_cost.get(this.matrix[i][j])) * (Math.abs(loc/m_size - i)  + Math.abs((loc%m_size - j)%m_size));
            }
          }
      }
      return heuristic_value;
    }
    public List<Game> get_successors()
    {
      List<Game> successors = new ArrayList<>();
      if (this.blank_column != this.m_size - 1) {
        if (!move_cost.get(this.matrix[this.blank_row][this.blank_column + 1]).equals("false") && last_direction != 'R')
        {
          successors.add(new Game(this, 'L'));
        }
      }
      if (this.blank_row != this.n_size - 1) {
        if (!move_cost.get(this.matrix[this.blank_row + 1][this.blank_column]).equals("false") && last_direction != 'D')
        {
          successors.add(new Game(this, 'U'));
        }
      }
      if (this.blank_column != 0) {
        if (!move_cost.get(this.matrix[this.blank_row][this.blank_column - 1]).equals("false") && last_direction != 'L')
        {
          successors.add(new Game(this, 'R'));
        }
      }
      if (this.blank_row != 0) {
        if (!move_cost.get(this.matrix[this.blank_row - 1][this.blank_column]).equals("false") && last_direction != 'U')
        {
          successors.add(new Game(this, 'D'));
        }
      }

      return successors;
    }
    public boolean is_goal(){
      for(int i = 0; i<this.n_size; i++)
      {
          for(int j = 0; j<this.m_size; j++)
          {
            if (!this.matrix[i][j].equals("_"))
            {
              int loc = Integer.parseInt(this.matrix[i][j]) - 1;
              if (loc/m_size != i | loc%m_size != j) {
                return false;
              }
            }
          }
      }
      return true;
    }
    public String matrix_to_string()
    {
      String str = "";
      for(int i = 0; i<this.n_size; i++)
      {
          for(int j = 0; j<this.m_size; j++)
          {
              str += this.matrix[i][j];
          }
      }
      return str;
    }
    public String toString()
    {
      //overriding the toString() method
    //   String print_matrix = "";
    //
    // for(int i = 0; i<this.n_size; i++)
    // {
    //     for(int j = 0; j<this.m_size; j++)
    //     {
    //       print_matrix += this.matrix[i][j] + " ";
    //     }
    //     print_matrix += "\n";
    // }
      // return String.join("-", this.path_to_here) + "\n" + print_matrix + "\ncost: " + this.cost + " is_goal: " + this.is_goal();
      return String.join("-", this.path_to_here);
    }
}
