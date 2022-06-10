import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class TaskGenerator {
    String task = "";
    String answer;
    Logger logger = Logger.getLogger(TaskGenerator.class.getName());

    public String getAnswer() {
        return answer;
    }

    public String getTask() {
        return task;
    }

    public void generate(int taskNumber) {
        task = "";
        answer = "";
        Random random = new Random();
        String number;
        int n;
        try {
            FileReader reader = new FileReader("task" + taskNumber +  ".txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String s;
            switch (taskNumber) {
                case 4:
                    n = random.nextInt(41);
                    number = Integer.toString(n); // целочисленный тип переводим в строчный, чтобы сравнивать его со строкой из файла
                    while((s = bufferedReader.readLine()) != null) {
                        if (s.equals(number)){
                            for (int i = 0; i < 6; i++) {
                                if (i < 5) {
                                    task += bufferedReader.readLine() + "\n";
                                } else {
                                    answer = bufferedReader.readLine();
                                }
                            }
                        }
                    }
                    break;
                case 6:
                    n = random.nextInt(31);
                    number = Integer.toString(n);
                    while ((s = bufferedReader.readLine()) != null) {
                        if (s.equals(number)) {
                            for (int i = 0; i < 2; i++) {
                                if (i == 0) {
                                    task += bufferedReader.readLine() + "\n";
                                } else {
                                    answer = bufferedReader.readLine();
                                }
                            }
                        }
                    }
                    break;
                case 7:
                    n = random.nextInt(31);
                    number = Integer.toString(n);
                    while((s = bufferedReader.readLine()) != null) {
                        if (s.equals(number)){
                            for (int i = 0; i < 6; i++) {
                                if (i < 5) {
                                    task += bufferedReader.readLine() + "\n";
                                } else {
                                    answer = bufferedReader.readLine();
                                }
                            }
                        }
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

