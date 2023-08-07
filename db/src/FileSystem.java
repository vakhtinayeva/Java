import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystem {
    // зчитування даних
    public String[][] read(int rows, int columns) {
        try {
            // визначаємо шлях до файлу
            String filename = System.getProperty("user.dir") + "/data/data.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String curLine = reader.readLine();

            String[][] matrix = new String[rows][columns];

            // лінія за лінією зчитуємо файл
            while (curLine != null) {
                for (int i = 0; i < rows; i++) {
                    String[] values = curLine.split(",");
                    for (int j = 0; j < columns; j++) {
                        matrix[i][j] = values[j];
                    }
                    curLine = reader.readLine();
                }
            }
            reader.close();

            return matrix;
        } catch (IOException e) {
            System.out.print("Error occured while reading data.txt");
        }

        return null;
    }

    // запис часу виконання
    public void write(String folder, String type, String content) {
        try {
            String path = System.getProperty("user.dir") + "/" + folder;
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }

            String filename = path + "/" + type + ".txt";
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(String.valueOf(content));
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.print("Error occured while writing " + type);
        }
    }
}
