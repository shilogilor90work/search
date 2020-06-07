import java.io.FileWriter;
import java.io.IOException;


/**
 * class Ex1 for search algorithm task
 * @author Shilo Gilor
 */
public class Ex1 {

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
 * Main
 * @param args main args
 */
  public static void main(String[] args)
  {

    String message = "";
    Game solution = null;
    Raw_input data = new Raw_input();
    Game start_game = new Game(data);
    long startTime = System.currentTimeMillis();
    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    int moveable_tiles = data.m_size*data.n_size - 1 - data.black_info.length;
    Algorithm algo = new Algorithm(data.algo, start_game, data.open_info);
    // check if all black tiles are in thier spot
    if (pre_black_check_failure(data))
    {
      message = "no path\nNum: 1";
    }
    else
    {
      startTime = System.currentTimeMillis();
      solution = algo.run(moveable_tiles);
      endTime = System.currentTimeMillis();
      totalTime = endTime - startTime;
      if (solution == null) {
        message = "no path\nNum: " + algo.node_counter + "\n";
      } else
      {
        message = solution + "\nNum: " + algo.node_counter + "\nCost: " + solution.cost + "\n";
      }
    }
    if (data.time_info)
    {
      message += totalTime/1000.0 + " seconds";
    }
    try {
      FileWriter myWriter = new FileWriter("output.txt");
       myWriter.write(message);
       myWriter.close();
     } catch (IOException e) {
       System.out.println("An error occurred.");
       e.printStackTrace();
     }
    System.out.println(message);
 }
}
