import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;





/**
 * class Ex1 for search algorithm task
 * @author Shilo Gilor
 */
public class Ex1 {
  static int node_counter = 0;
  static long totalTime;
  /**
   *class comparator for comparing game states for the PriorityQueue
   */
  static public class Heuristic_cost implements Comparator<Game> {
      @Override
      public int compare(Game x, Game y) {
        return (x.f) - (y.f);
      }
  }
/**
 * create the start state from the raw input data
 * @param  data raw input data
 * @return game object
 */
  static public Game create_start_state(Raw_input data)
  {
    boolean do_heuristic = false;
    String algo = data.algo;
    if (algo.equals("A*") || algo.equals("IDA*") || algo.equals("DFBnB"))
    {
      do_heuristic = true;
    }
    return new Game(data.move_cost, data.matrix, data.n_size, data.m_size, data.blank_row, data.blank_column, do_heuristic);
  }
  /**
   * read the input file and turn in to the Raw_input object
   * @return Raw_input object
   */
  static public Raw_input read_input()
  {
    Hashtable<String, String> move_cost = new Hashtable<String, String>();
    String algo = "";
    String time_info = "";
    String open_info = "";
    String [] black_info = new String[0];
    String [] red_info = new String[0];
    int n_size = 0;
    int m_size = 0;
    String[][] matrix = new String[0][0];
    int blank_row = 0;
    int blank_column = 0;
    try {
        Scanner myReader = new Scanner(new File("input.txt"));
        algo = myReader.nextLine();
        time_info = myReader.nextLine();
        open_info = myReader.nextLine();
        String size_info = myReader.nextLine();
        n_size =  Integer.parseInt(size_info.substring(0 , size_info.indexOf("x")));
        m_size =  Integer.parseInt(size_info.substring(size_info.indexOf("x") + 1));
        String black_raw_info = myReader.nextLine();
        black_info = black_raw_info.substring(black_raw_info.indexOf(":") + 1).replaceAll("\\s","").split(",");
        String red_raw_info = myReader.nextLine();
        red_info = red_raw_info.substring(red_raw_info.indexOf(":") + 1).replaceAll("\\s","").split(",");
        matrix = new String[n_size][m_size];
        for(int i=0;i<n_size;i++)
        {
          String[] result = myReader.nextLine().split(",");
          for(int j=0;j<result.length;j++)
          {
            matrix[i][j] = result[j];
            if (Arrays.asList(red_info).contains(matrix[i][j]))
            {
              move_cost.put(matrix[i][j], "30");
            } else if (Arrays.asList(black_info).contains(matrix[i][j]))
            {
              move_cost.put(matrix[i][j], "false");
            } else
            {
              move_cost.put(matrix[i][j], "1");
            }
            if (matrix[i][j].equals("_"))
            {
              blank_row = i;
              blank_column = j;
            }
          }
        }
        myReader.close();
      } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    return new Raw_input(algo, time_info, open_info, move_cost, black_info, matrix, n_size, m_size, blank_row, blank_column);
}

/**
 * function to check if all non movable tiles are in their correct place.
 * @param  data the raw input info
 * @return boolean of if the tiles are ok failing being in the correct place
 */
  static public boolean pre_black_check_failure(Raw_input data)
  {
    if (data.black_info.length == 1 && data.black_info[0].equals(""))
    {
      return false;
    }
    for (String pos: data.black_info)
    {
      int loc = Integer.parseInt(pos)-1;
      if (!data.matrix[loc/data.m_size][loc%data.m_size].equals(pos))
      {
        return true;
      }
    }
    return false;
  }
  /**
   * BFS - Breafth First Search, algorithm
   * @param  game game state
   * @return the game state solved with the path to get there.
   */
 static public Game BFS(Game game)
 {
   Queue<Game> frontier = new LinkedList<Game>();
   Set<String> visited = new HashSet<String>();
   frontier.add(game);
   visited.add(game.matrix_to_string());
   while (!frontier.isEmpty())
   {
     node_counter +=1;
     Game first_in_line = frontier.remove();
     System.out.println(first_in_line.matrix_to_string());
     if (first_in_line.is_goal())
     {
       return first_in_line;
     }
     else
     {
       for (Game next_game : first_in_line.get_successors())
       {
         if (!visited.contains(next_game.matrix_to_string()))
         {
           frontier.add(next_game);
         }
       }
     }
   }
   return null;
 }





/**
 * DFID - Depth First Iterative Deepening, algorithm (recursive)
 * @param  game      game state.
 * @param  max_depth max depth to check if state is goal.
 * @return the game state solved with the path to get there.
 */
 static public Game DFID(Game game, int max_depth)
 {
   int depth_counter = 1;
   Stack<Game> frontier = new Stack<Game>();
   Set<String> visited = new HashSet<String>();
   frontier.push(game);
   while (!frontier.isEmpty())
   {
     node_counter +=1;
     Game first_in_line = frontier.pop();
     System.out.println(first_in_line);

     visited.add(first_in_line.matrix_to_string());
     if (first_in_line.is_goal())
     {
       return first_in_line;
     }
     if (first_in_line.path_to_here.size() <= max_depth)
     {
       List<Game> successors = first_in_line.get_successors();
       Collections.reverse(successors);
       for (Game next_game : successors)
       {
         if (!visited.contains(next_game.matrix_to_string()))
         {
           frontier.push(next_game);
         }
       }
     }
   }
   return DFID(game, max_depth + 1);
 }


 static public Game DFID_2(Game game)
 {
   Game result;
   int depth = 0;
   node_counter +=1;
   while (true)
   {
     depth ++;
     Set<String> visited = new HashSet<String>();
     result = Limited_DFS(game,depth,visited);
     if (!result.get_cutoff())
     {
       return result;
     }
   }
 }
 static public Game Limited_DFS(Game game, int limit, Set<String> h)
 {
   Game result;
   boolean is_cutoff = false;
   if (game.is_goal())
   {
     return game;
   }
   else if (limit == 0)
   {
     game.set_cutoff(true);
     return game;
   } else
   {
     h.add(game.matrix_to_string());
     is_cutoff = false;

     for (Game next_game : game.get_successors())
     {
       if (!h.contains(next_game.matrix_to_string()))
       {
         node_counter +=1;
         result = Limited_DFS(next_game,limit-1,h);
         if (result.get_cutoff())
         {
           is_cutoff = true;
         } else
         {
           return result;
         }
       }
     }
     h.remove(game.matrix_to_string());
     if (is_cutoff)
     {
       game.set_cutoff(true);
       return game;
     } else
     {
       game.set_cutoff(false);
       return game;
     }
   }
 }




/**
 * A* algorithm uses PriorityQueue of logic of h(x) + c(x)
 * @param  game      game state.
 * @return the game state solved with the path to get there.
 */
 static public Game A_star(Game game)
 {
   Comparator<Game> comparator = new Heuristic_cost();
   PriorityQueue<Game> frontier = new PriorityQueue<Game>(100,comparator);
   Set<String> visited = new HashSet<String>();
   frontier.add(game);
   while (!frontier.isEmpty())
   {
     node_counter +=1;
     Game first_in_line = frontier.remove();
     visited.add(first_in_line.matrix_to_string());
     if (first_in_line.is_goal())
     {
       return first_in_line;
     }
     else
     {
       for (Game next_game : first_in_line.get_successors())
       {
         if (!visited.contains(next_game.matrix_to_string()))
         {
           frontier.add(next_game);
         }
       }
     }
   }
   return null;
 }


/**
 * IDA* - Iterative Deepening A* algorithm uses A* but has a max depth to check if in goal state.
 * @param  game      game state.
 * @param  max_depth max depth to check if state is goal.
 * @return the game state solved with the path to get there.
 */
 static public Game IDA_star(Game game, int max_depth)
 {

   Comparator<Game> comparator = new Heuristic_cost();
   PriorityQueue<Game> frontier = new PriorityQueue<Game>(100,comparator);
   Set<String> visited = new HashSet<String>();
   frontier.add(game);
   while (!frontier.isEmpty())
   {
     node_counter +=1;
     Game first_in_line = frontier.remove();
     visited.add(first_in_line.matrix_to_string());
     if (first_in_line.is_goal())
     {
       return first_in_line;
     }
     else
     {
       if (first_in_line.path_to_here.size() < max_depth)
       {
         for (Game next_game : first_in_line.get_successors())
         {
           if (!visited.contains(next_game.matrix_to_string()))
           {
             frontier.add(next_game);
           }
         }
       }
     }
   }
   return IDA_star(game, max_depth + 1);
 }

 public static Stack<Game> remove_item(Stack<Game> stack, Game game)
 {
     Stack<Game> temp = new Stack<Game>();
     while (!stack.isEmpty())
     {
         Game check = stack.pop();
         if( ! check.matrix_to_string().equals(game.matrix_to_string()))
         {
             temp.push(check);
         }
     }

     return temp;
 }


 /**
  * IDA* - Iterative Deepening A* algorithm uses A* but has a max depth to check if in goal state.
  * @param  game      game state.
  * @param  max_depth max depth to check if state is goal.
  * @return the game state solved with the path to get there.
  */
  static public Game IDA_star_2(Game game)
  {
    Stack<Game> frontier = new Stack<Game>();
    Hashtable<String, Game> visited = new Hashtable<String, Game>();
    int minf;
    int t = game.heuristic;
    Game this_game;
    node_counter += 1;
    while (true)
    {
      minf = Integer.MAX_VALUE;
      frontier.push(game);
      visited.put(game.matrix_to_string(), game);
      while (!frontier.isEmpty())
      {
        this_game = frontier.pop();
        if (this_game.get_isout())
        {
          visited.remove(this_game.matrix_to_string());
        } else
        {
          this_game.set_isout(true);
          frontier.push(this_game);
          for (Game next_game : this_game.get_successors())
          {
            node_counter += 1;
            if (next_game.f > t)
            {
              minf = Math.min(minf, next_game.f);
              continue;
            }
            if (visited.contains(next_game.matrix_to_string()) && visited.get(next_game.matrix_to_string()).get_isout())
            {
              continue;
            }
            if (visited.contains(next_game.matrix_to_string()) && !visited.get(next_game.matrix_to_string()).get_isout())
            {
              Game old_game = visited.get(next_game.matrix_to_string());
              if (old_game.f > next_game.f)
              {
                visited.remove(old_game.matrix_to_string());
                frontier = remove_item(frontier, old_game);
              } else
              {
                continue;
              }
            }
            if (next_game.is_goal())
            {
              return next_game;
            }
            frontier.push(next_game);
            visited.put(next_game.matrix_to_string(), next_game);
          }
        }
      }
      t = minf;
    }
  }



  /**
   * IDA* - Iterative Deepening A* algorithm uses A* but has a max depth to check if in goal state.
   * @param  game      game state.
   * @param  max_depth max depth to check if state is goal.
   * @return the game state solved with the path to get there.
   */
   static public Game DFBnB_2(Game game)
   {
     Stack<Game> frontier = new Stack<Game>();
     Hashtable<String, Game> visited = new Hashtable<String, Game>();
     int minf;
     int t = Integer.MAX_VALUE;
     Game this_game;
     Game result = null;
     node_counter += 1;
     frontier.push(game);
     visited.put(game.matrix_to_string(), game);
     while (!frontier.isEmpty())
     {
       this_game = frontier.pop();
       if (this_game.get_isout())
       {
         visited.remove(this_game.matrix_to_string());
       } else
       {
         this_game.set_isout(true);
         frontier.push(this_game);
         List<Game> N = this_game.get_successors();
         Collections.sort(N, new Heuristic_cost());
         for (int index = 0;index<N.size();index++)
         {
           Game next_game = N.get(index);
           node_counter += 1;
           if (next_game.f >= t)
           {
             N.subList(index, N.size()).clear();
           }
           else if (visited.contains(next_game.matrix_to_string()) && visited.get(next_game.matrix_to_string()).get_isout())
           {
             N.remove(next_game);
             index --;
           }
           else if (visited.contains(next_game.matrix_to_string()) && !visited.get(next_game.matrix_to_string()).get_isout())
           {
             Game old_game = visited.get(next_game.matrix_to_string());
             if (old_game.f <= next_game.f)
             {
               N.remove(next_game);
               index --;

             } else
             {
               visited.remove(old_game.matrix_to_string());
               frontier = remove_item(frontier, old_game);
             }
           }
           else if (next_game.is_goal())
           {
             t = next_game.f;
             result = next_game;
             N.subList(index, N.size()).clear();
           }
         }
         Collections.reverse(N);
         for (Game next_game : N)
         {
           frontier.push(next_game);
           visited.put(next_game.matrix_to_string(), next_game);
         }
       }
     }
     return result;
   }

 static public Game DFBnB(Game game)
 {
   Comparator<Game> comparator = new Heuristic_cost();
   Stack<Game> frontier = new Stack<Game>();
   Set<String> loop_avoidance = new HashSet<String>();
   Game solution = null;
   Integer min_cost = Integer.MAX_VALUE;
   frontier.add(game);
   loop_avoidance.add(game.matrix_to_string());
   while (!frontier.isEmpty())
   {
     node_counter +=1;
     Game first_in_line = frontier.pop();
     if (first_in_line.is_goal() && min_cost>first_in_line.cost)
     {
       solution = first_in_line;
       min_cost = first_in_line.cost;
     }
     else
     {
       for (Game next_game : first_in_line.get_successors())
       {

         if (!loop_avoidance.contains(next_game.matrix_to_string()) && next_game.cost < min_cost)
         {
           System.out.println(next_game.matrix_to_string());
           frontier.push(next_game);
           loop_avoidance.add(next_game.matrix_to_string());
         }
       }
     }
   }
   return solution;
 }


/**
 * Main
 * @param args
 */
  public static void main(String[] args)
  {
    String message = null;
    Game solution = null;
    Raw_input data = read_input();
    Game start_game = create_start_state(data);
    if (pre_black_check_failure(data))
    {
      message = "no path\nNum: 1";
    }
    else
    {
      long startTime = System.currentTimeMillis();
      long endTime = System.currentTimeMillis();
      switch (data.algo)
      {
        case "BFS":
          startTime = System.currentTimeMillis();
          solution = BFS(start_game);
          endTime = System.currentTimeMillis();
          break;
        case "DFID":
          startTime = System.currentTimeMillis();
          solution = DFID(start_game, 1);
          endTime = System.currentTimeMillis();
          break;
        case "A*":
          startTime = System.currentTimeMillis();
          solution = A_star(start_game);
          endTime = System.currentTimeMillis();
          break;
        case "IDA*":
          startTime = System.currentTimeMillis();
          // solution = IDA_star(start_game, 1);
          solution = IDA_star_2(start_game);

          endTime = System.currentTimeMillis();
          break;
        case "DFBnB":
          startTime = System.currentTimeMillis();
          // solution = DFBnB(start_game);
          solution = DFBnB_2(start_game);
          endTime = System.currentTimeMillis();
          break;
        case "DFID_2":
          startTime = System.currentTimeMillis();
          solution = IDA_star_2(start_game);
          endTime = System.currentTimeMillis();
      }
      totalTime = endTime - startTime;
      message = solution + "\nNum: " + node_counter + "\nCost: " + solution.cost + "\n";
    }
    if (data.time_info)
    {
      message += totalTime/1000.0 + " seconds";
    }
    if (data.open_info)
    {
      System.out.println(message);
    }
    else
    {
      try {
        FileWriter myWriter = new FileWriter("output.txt");
         myWriter.write(message);
         myWriter.close();
       } catch (IOException e) {
         System.out.println("An error occurred.");
         e.printStackTrace();
       }
    }
 }
}
