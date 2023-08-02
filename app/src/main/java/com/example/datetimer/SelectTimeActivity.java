package com.example.datetimer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectTimeActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnDone; // Done 버튼
    private Button reStart; // 이 전페이지로 돌아가는 버튼
    private TextView tvSelectedDate;
    private TextView selectedDateTimeTextView; // Done 버튼을 누르면 나오는 날짜와 시간

    private List<String> selectedDates = new ArrayList<>(); // SongMe에서 추가. String 타입의 ArrayList selectedDates 선언 및 초기화
    private static ArrayList<String> accumulatedSelections = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // SongMe에서 추가. SimpleDateFormat 객체 dateFormat 선언 및 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        timePicker = findViewById(R.id.timePicker);
        btnDone = findViewById(R.id.btnDone);
        reStart = findViewById(R.id.reStart);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        selectedDateTimeTextView = findViewById(R.id.selectedDateTimeTextView);

        String selectedDate = getIntent().getStringExtra("selected_date"); // selectedDate는 선택한 날짜
        tvSelectedDate.setText("선택한 날짜: " + selectedDate);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();


                String selectedTime = String.format("%02d:%02d", hourOfDay, minute); // selectedTime는 선택한 시간
                /*
                showToast("Selected Time: " + selectedTime); // 밑에 잠깐 뜨고 마는 것
                 */


                String selectedDateTime = selectedDate + " " + hourOfDay + ":" + minute; // selectedDateTime는 선택한 날짜와 시간
                selectedDateTimeTextView.setText("선택한 날짜와 시간 : " + selectedDateTime);

                // 이전 액티비티로부터 선택한 날짜를 받아옵니다.
                String selectedDate = getIntent().getStringExtra("selected_date");

                /*
                // 선택한 날짜와 시간을 하나의 문자열로 합칩니다.
                String selectedDateTime = selectedDate + " " + selectedTime;
                 */

                // 선택한 날짜와 시간을 ArrayList에 추가합니다.
                accumulatedSelections.add(selectedDateTime);

                // 누적된 선택 내용을 화면에 표시합니다.
                StringBuilder stringBuilder = new StringBuilder();
                for (String dateTime : accumulatedSelections) {
                    stringBuilder.append(dateTime).append("\n");
                }
                selectedDateTimeTextView.setText("누적된 선택 내용:\n" + stringBuilder.toString());

                showToast("선택한 시간: " + selectedTime);
            }
        });

        reStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectTimeActivity.this, SelectDateActivity.class);
                intent.putStringArrayListExtra("accumulated_selections", accumulatedSelections);
                startActivity(intent);
            }
        });

        // ChatGPT로부터 추가된 코드를 여기에 추가합니다.

                // "날짜 선택" 버튼 클릭 시, showDatePickerDialog() 메서드를 호출하여 날짜를 선택할 수 있도록 합니다.
                        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog();
                    }
                });
    }

    // Chat Gpt 내용
    // 추가된 메서드들
    // showDatePickerDialog()와 updateSelectedDatesTextView() 메서드들은 위에 ChatGPT로부터 제공된 코드에 포함되어 있습니다.

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // 여기서부터 밑부분 SongMe에서 추가
    private void showDatePickerDialog() { // SongMe에서 추가될 부분. 날짜 선택 Dialog를 표시하는 메서드
        Calendar calendar = Calendar.getInstance(); // 현재 시간으로 Calendar 객체 생성
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog( // SongMe에서 추가될 부분. DatePickerDialog 객체 생성
                this,
                new DatePickerDialog.OnDateSetListener() { // 날짜 선택 Listener 설정
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) { // 날짜가 선택되면 호출되는 메서드
                        Calendar selectedDate = Calendar.getInstance(); // 선택된 날짜로 Calendar 객체 생성
                        selectedDate.set(year, month, dayOfMonth); // 선택된 날짜 설정
                        String formattedDate = dateFormat.format(selectedDate.getTime()); // 형식화된 날짜 문자열 생성
                        selectedDates.add(formattedDate); // 선택된 날짜를 selectedDates 리스트에 추가
                        updateSelectedDatesTextView(); // 텍스트뷰 업데이트 메서드 호출
                    }
                },
                year,
                month,
                dayOfMonth
        );

        // Allow multiple date selection
        datePickerDialog.getDatePicker().setOnDateChangedListener(null); // 다중 선택 가능하도록 설정

        datePickerDialog.show();
    }

    private void updateSelectedDatesTextView() { // SongMe에서 추가될 부분. 선택된 날짜들을 텍스트뷰에 업데이트하는 메서드
        StringBuilder message = new StringBuilder("Selected Dates :\n"); // 메시지 문자열 생성
        for (String date : selectedDates) { // selectedDates 리스트에 있는 날짜들을 반복하며
            message.append(date).append("\n"); // 메시지에 날자를 추가
        }
        tvSelectedDate.setText(message.toString()); // 텍스트뷰에 메시지를 설정하여 표시
    }
}