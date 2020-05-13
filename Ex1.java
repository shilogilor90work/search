import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
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
import java.util.Comparator;





public class Ex1 {
  static int node_counter = 0;
  static long totalTime;
  static public class Heuristic_cost implements Comparator<Game> {
      @Override
      public int compare(Game x, Game y) {
          if (x.heuristic + x.cost < y.heuristic + y.cost) {
              return -1;
          }
          if (x.heuristic + x.cost > y.heuristic + y.cost) {
              return 1;
          }
          return 0;
      }
  }

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
        String black_raw_info = myReader.nextLine();
        black_info = black_raw_info.substring(black_raw_info.indexOf(":") + 1).replaceAll("\\s","").split(",");
        String red_raw_info = myReader.nextLine();
        red_info = red_raw_info.substring(red_raw_info.indexOf(":") + 1).replaceAll("\\s","").split(",");
        String size_info = myReader.nextLine();
        n_size =  Integer.parseInt(size_info.substring(0 , size_info.indexOf("x")));
        m_size =  Integer.parseInt(size_info.substring(size_info.indexOf("x") + 1));
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
 static public Game BFS(Game game)
 {
   long startTime = System.currentTimeMillis();
   Queue<Game> frontier = new LinkedList<Game>();
   Set<String> visited = new HashSet<String>();
   frontier.add(game);
   while (!frontier.isEmpty())
   {
     node_counter +=1;
     Game first_in_line = frontier.remove();
     visited.add(first_in_line.matrix_to_string());
     if (first_in_line.is_goal())
     {
       long endTime   = System.currentTimeMillis();
       totalTime = endTime - startTime;
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
   long endTime = System.currentTimeMillis();
   totalTime = endTime - startTime;
   return null;
 }






 static public Game DFID(Game game, int max_depth)
 {
   int depth_counter = 1;
   long startTime = System.currentTimeMillis();
   Stack<Game> frontier = new Stack<Game>();
   Set<String> visited = new HashSet<String>();
   frontier.push(game);
   while (!frontier.isEmpty())
   {
     node_counter +=1;
     Game first_in_line = frontier.pop();
     visited.add(first_in_line.matrix_to_string());
     if (first_in_line.is_goal())
     {
       long endTime   = System.currentTimeMillis();
       totalTime = endTime - startTime;
       return first_in_line;
     }
     if (first_in_line.path_to_here.size() < max_depth)
     {
       for (Game next_game : first_in_line.get_successors())
       {
         if (!visited.contains(next_game.matrix_to_string()))
         {
           frontier.push(next_game);
         }
       }
     }
   }
   long endTime = System.currentTimeMillis();
   totalTime = endTime - startTime;
   return DFID(game, max_depth + 1);
 }


 static public Game A_star(Game game)
 {
   long startTime = System.currentTimeMillis();
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
       long endTime   = System.currentTimeMillis();
       totalTime = endTime - startTime;
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
   long endTime = System.currentTimeMillis();
   totalTime = endTime - startTime;
   return null;
 }



 static public Game IDA_star(Game game, int max_depth)
 {
   long startTime = System.currentTimeMillis();
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
       long endTime   = System.currentTimeMillis();
       totalTime = endTime - startTime;
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
   long endTime = System.currentTimeMillis();
   totalTime = endTime - startTime;
   return DFID(game, max_depth + 1);
 }




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
      switch (data.algo)
      {
        case "BFS":
          solution = BFS(start_game);
          break;
        case "DFID":
          solution = DFID(start_game, 1);
          break;
        case "A*":
          solution = A_star(start_game);
          break;
        case "IDA*":
          solution = IDA_star(start_game, 1);
          break;
        case "DFBnB":
          // solution = DFBnB(start_game);
          break;
      }
      message = solution + "\nNum: " + node_counter + "\nCost: " + solution.cost + "\n" + totalTime/1000.0 + " seconds";
    }
    System.out.println(message);
 }
}
