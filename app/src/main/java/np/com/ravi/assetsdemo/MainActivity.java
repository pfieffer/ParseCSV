package np.com.ravi.assetsdemo;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView mainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTextView = (TextView) findViewById(R.id.main_textView);

        try {
            AssetManager manager = getAssets();
            InputStream in = manager.open("Book.csv");

            ArrayList<Employee> employeeArrayList = parse(in);
            StringBuilder builder = new StringBuilder();
            for (Employee employee : employeeArrayList) {
                builder.append(String.format(Locale.US, "%s is %d years old, works as %s and earns %d per month.",
                        employee.name, employee.age, employee.position, employee.salary));
                builder.append('\n');
            }
            mainTextView.setText(builder.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Simple CSV Parser */
    private static final int COL_NAME = 0;
    private static final int COL_AGE = 1;
    private static final int COL_POSITION = 2;
    private static final int COL_SALARY = 3;

    private ArrayList<Employee> parse(InputStream in) throws IOException {
        ArrayList<Employee> results = new ArrayList<Employee>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String nextLine = null;
        while ((nextLine = reader.readLine()) != null) {
            String[] tokens = nextLine.split(",");
            if (tokens.length != 4) {
                Log.w("CSVParser", "Skipping Bad CSV Row");
                continue;
            }
            //Add new parsed result
            Employee current = new Employee();
            current.name = tokens[COL_NAME];
            current.age = Integer.parseInt(tokens[COL_AGE]);
            current.position = tokens[COL_POSITION];
            current.salary = Integer.parseInt(tokens[COL_SALARY]);


            results.add(current);
        }
        in.close();
        return results;
    }
}
