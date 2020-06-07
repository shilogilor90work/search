import java.util.List;
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
 * Algorithm for the tile puzzle
 */
public class Algorithm {
  Game game;
  String type;
  int node_counter;
  boolean open;
  Algorithm (String type, Game game, boolean open)
  {
    this.game = game;
    this.type = type;
    this.node_counter = 0;
    this.open = open;
  }

    /**
     *class comparator for comparing game states for the PriorityQueue
     */
    public class Heuristic_cost implements Comparator<Game> {
        @Override
        public int compare(Game x, Game y) {
          return x.f - y.f;
        }
    }

    /**
     * BFS - Breafth First Search, algorithm
     * @param  game game state
     * @return the game state solved with the path to get there.
     */
   public Game BFS(Game game)
   {
     Queue<Game> frontier = new LinkedList<Game>();
     Set<String> visited = new HashSet<String>();
     frontier.add(game);
     while (!frontier.isEmpty())
     {
       if (this.open)
       {
         for (Game open_list : frontier)
         {
           System.out.print(open_list.matrix_to_string() + " ");
         }
         System.out.print("\n");

       }
       this.node_counter++;
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

   public Game DFID(Game game)
   {
     Game result;
     int depth = 0;
     this.node_counter++;
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

   public Game Limited_DFS(Game game, int limit, Set<String> h)
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
           this.node_counter++;
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
   public Game A_star(Game game)
   {
     Comparator<Game> comparator = new Heuristic_cost();
     PriorityQueue<Game> frontier = new PriorityQueue<Game>(100,comparator);
     Set<String> visited = new HashSet<String>();
     frontier.add(game);
     this.node_counter++;
     while (!frontier.isEmpty())
     {
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
             this.node_counter++;
             frontier.add(next_game);
             if (this.open)
             {
               for (Game open_list : frontier)
               {
                 System.out.print(open_list.matrix_to_string() + " ");
               }
               System.out.print("\n");

             }
         }
       }
     }
   }
     return null;
   }

   public Stack<Game> remove_item(Stack<Game> stack, Game game)
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
    * @return the game state solved with the path to get there.
    */
    public Game IDA_star(Game game)
    {
      Stack<Game> frontier = new Stack<Game>();
      Hashtable<String, Game> visited = new Hashtable<String, Game>();
      int minf;
      int t = game.heuristic;
      Game this_game;
      this.node_counter++;
      while (true)
      {
        game.set_isout(false);
        minf = Integer.MAX_VALUE;

        frontier.push(game);
        visited.put(game.matrix_to_string(), game);
        while (!frontier.isEmpty())
        {
          if (this.open)
          {
            for (Game open_list : frontier)
            {
              System.out.println(open_list.matrix_to_string() + " ");
            }
            System.out.println("\n");
          }
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
              this.node_counter++;
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
     * DFBnB -
     * @param  game      game state.
     * @param moveable_tiles the amount of movable tiles
     * @return the game state solved with the path to get there.
     */
     public Game DFBnB(Game game, int moveable_tiles)
     {
       Stack<Game> frontier = new Stack<Game>();
       Hashtable<String, Game> visited = new Hashtable<String, Game>();
       int minf;
       // int t = factorial(moveable_tiles);
       int t = game.heuristic*4;
       Game this_game;
       Game result = null;
       this.node_counter++;
       frontier.push(game);
       visited.put(game.matrix_to_string(), game);
       while (!frontier.isEmpty())
       {
         if (this.open)
         {
           for (Game open_list : frontier)
           {
             System.out.print(open_list.matrix_to_string() + " ");
           }
           System.out.print("\n");
         }
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
           for (int index = 0;index<N.size();++index)
           {
             Game next_game = N.get(index);
             this.node_counter++;
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
           for (int index = N.size()-1;index>=0;--index)
           {
             Game next_game = N.get(index);
             frontier.push(next_game);
             visited.put(next_game.matrix_to_string(), next_game);
           }
         }
       }
       return result;
     }
/**
 * run the algorithm to get the path
 * @param moveable_tiles the amount of movable tiles
 * @return Game state of goal where the path is listed in it.
 */
  public Game run(int moveable_tiles)
  {
    Game solution = null;
    switch (this.type)
    {
      case "BFS":
        solution = BFS(this.game);
        break;
      case "DFID":
        solution = DFID(this.game);
        break;
      case "A*":
        solution = A_star(this.game);
        break;
      case "IDA*":
        solution = IDA_star(this.game);
        break;
      case "DFBnB":
        solution = DFBnB(this.game, moveable_tiles);
        break;
  }
  return solution;
}
}
