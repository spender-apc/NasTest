package novozhilova.nastest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import java.util.LinkedList;
import android.widget.ListView;
import java.util.Map;
import android.view.View;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    int sum=0;//хр суммы
    // объект для доступа к постоянному хранилищу. xml
    SharedPreferences sharedPref;

    // список для хранения элементов истории трат
    public LinkedList<String> expensesHistory = new LinkedList();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//получаем доступ к постоянному хранилищу
        sharedPref = getSharedPreferences("date", MODE_PRIVATE);
        // вынимаем от туда сумму
        sum = sharedPref.getInt("MySum",1);
        setContentView(R.layout.activity_main);//изначально экран активности пуст ,нужен для размещ польз интерфейса

        final Button button = (Button) findViewById(R.id.button);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final TextView textView  = (TextView) findViewById(R.id.textView2);
        final ListView listView = (ListView)findViewById(R.id.listView);
        //чтение истории из постоянного хранилища и сохранение в списке
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            expensesHistory.add(" " + entry.getKey() + " " + entry.getValue().toString());
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }

// задать соответствие между списком на экране и списке в памяти
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, expensesHistory);

        listView.setAdapter(adapter);

        // добавить текст на элементы интерфейса на экране
        textView.setText("The total sum is: " + sum);
        button.setText("Add expenses");
        editText.setText("");
//метод срабатывает при нажатии на экран
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //считать текущее значение из поля ввода и сохранить
                int current = Integer.parseInt((editText.getText()).toString());
                sum = sum + current;

                //сохранить сумму в постоянном хранилище
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("MySum", sum);

                // получить текущую дату
                Date d = new Date();
                d.getTime();
                //сохранить запись "дата траты" в постоянном хранилище
                editor.putInt(d.toString(), current);
                editor.commit();

                //добавить запсить в список
                expensesHistory.add(d.toString() + " " + current);
                adapter.notifyDataSetChanged();

                //отобразить сумму на экране
                textView.setText("The total sum is: " + sum);
            }
        });
    }
}
