import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Game state of the tile puzzle
 */
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
    boolean cutoff;
    boolean isout;
    int f;


    /**
     * Game start state
     * @param data raw data to build start state of game
     */
    Game(Raw_input data)
    {
      this.do_heuristic = false;
      if (data.algo.equals("A*") || data.algo.equals("IDA*") || data.algo.equals("DFBnB"))
      {
        this.do_heuristic = true;
      }
      this.move_cost = data.move_cost;
      this.n_size = data.n_size;
      this.m_size = data.m_size;
      this.matrix = data.matrix;
      this.blank_row = data.blank_row;
      this.blank_column = data.blank_column;
      this.move_cost = data.move_cost;
      this.cost = 0;
      this.heuristic = 0;
      if (this.do_heuristic) {
        this.heuristic = calculate_heuristic();
      }
      this.last_direction = '_';
      this.cutoff = false;
      this.isout = false;
      this.f = this.heuristic + this.cost;
    }
    /**
     * consructor for next move
     * @param game      game state of last position
     * @param direction char of where to go
     */
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
      this.cutoff = false;
      this.isout = false;
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
        this.f = this.heuristic + this.cost;
    }
    /**
     * calculate heuristic value
     * @return int of the estamated cost to get to final
     */
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
              heuristic_value += Integer.parseInt(move_cost.get(this.matrix[i][j])) * (Math.abs(loc/this.m_size - i)  + Math.abs(loc%this.m_size - j));
            }
          }
      }
      return heuristic_value;
    }
    /**
     * add all options of movable peaces
     * @return list of Games of new options
     */
    public List<Game> get_successors()
    {
      List<Game> successors = new ArrayList<>();
      if (this.blank_column != this.m_size - 1 && !move_cost.get(this.matrix[this.blank_row][this.blank_column + 1]).equals("false") && last_direction != 'R')
      {
        successors.add(new Game(this, 'L'));
      }
      if (this.blank_row != this.n_size - 1 && !move_cost.get(this.matrix[this.blank_row + 1][this.blank_column]).equals("false") && last_direction != 'D')
      {
        successors.add(new Game(this, 'U'));
      }
      if (this.blank_column != 0 && !move_cost.get(this.matrix[this.blank_row][this.blank_column - 1]).equals("false") && last_direction != 'L')
      {
        successors.add(new Game(this, 'R'));
      }
      if (this.blank_row != 0 && !move_cost.get(this.matrix[this.blank_row - 1][this.blank_column]).equals("false") && last_direction != 'U')
      {
        successors.add(new Game(this, 'D'));
      }
      return successors;
    }
    /**
     * is goal state
     * @return boolean of if we are in the goal state.
     */
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
    /**
     * turning the matrix to a string that is unique to each setup of matrix build.
     * @return String of the matrix as string.
     */
    public String matrix_to_string()
    {
      String str = "";
      for(int i = 0; i<this.n_size; i++)
      {
          for(int j = 0; j<this.m_size; j++)
          {
              str += this.matrix[i][j] + "_";
          }
      }
      return str;
    }
    /**
     * set cutoff
     * @param bool true false
     */
    public void set_cutoff(boolean bool)
    {
      this.cutoff = bool;
    }
    /**
     * get cutoff
     * @return boolean
     */
    public boolean get_cutoff()
    {
      return this.cutoff;
    }
    /**
     * set isout
     * @param bool true false
     */
    public void set_isout(boolean bool)
    {
      this.isout = bool;
    }
    /**
     * get isout
     * @return bool
     */
    public boolean get_isout()
    {
      return this.isout;
    }
    /**
     * print the path to this node.
     * @return string of the path in the format for handing in this task
     */
    public String toString()
    {
      return String.join("-", this.path_to_here);
    }
}
