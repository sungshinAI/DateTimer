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
    private Button reStart; // 전(날짜 선택) page로 돌아가는 버튼
    private TextView tvSelectedDate; // "선택한 날짜 : "
    private TextView selectedDateTimeTextView; // "선택한 날짜와 시간"

    private List<String> selectedDates = new ArrayList<>(); // String 타입의 ArrayList selectedDates 선언 및 초기화
    private static ArrayList<String> accumulatedSelections = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // SimpleDateFormat 객체 dateFormat 선언 및 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        timePicker = findViewById(R.id.timePicker);
        btnDone = findViewById(R.id.btnDone);
        reStart = findViewById(R.id.reStart);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        selectedDateTimeTextView = findViewById(R.id.selectedDateTimeTextView);

        // "선택한 날짜 : " 띄우기
        String selectedDate = getIntent().getStringExtra("selected_date"); // selectedDate는 선택한 날짜
        tvSelectedDate.setText("선택한 날짜: " + selectedDate);

        // Done 버튼 눌렀을 때
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour(); // 시
                int minute = timePicker.getMinute(); // 분

                String selectedTime = String.format("%02d:%02d", hour, minute); // selectedTime는 선택한 시간

                String selectedDateTime = selectedDate + " " + hour + ":" + minute; // selectedDateTime는 선택한 날짜와 시간
                selectedDateTimeTextView.setText("선택한 날짜와 시간 : " + selectedDateTime);

                // 이전 액티비티로부터 선택한 날짜를 받아옵니다.
                String selectedDate = getIntent().getStringExtra("selected_date");

                // 선택한 날짜와 시간을 ArrayList에 추가합니다.
                accumulatedSelections.add(selectedDateTime);

                // 누적된 선택 내용을 화면에 표시합니다.
                StringBuilder stringBuilder = new StringBuilder();
                for (String dateTime : accumulatedSelections) {
                    stringBuilder.append(dateTime).append("\n");
                }
                selectedDateTimeTextView.setText("누적된 선택 내용:\n" + stringBuilder.toString());
            }
        });

        reStart.setOnClickListener(new View.OnClickListener() { // 날짜 선택 Page로 돌아가기 버튼
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectTimeActivity.this, SelectDateActivity.class); // Page 전환
                intent.putStringArrayListExtra("accumulated_selections", accumulatedSelections); // 누적되게 만들어주는 코드
                startActivity(intent);
            }
        });

        // "날짜 선택" 버튼 클릭 시, showDatePickerDialog() 메서드를 호출하여 날짜를 선택할 수 있도록 합니다.
         tvSelectedDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 showDatePickerDialog();
             }
         });

        // 누적된 선택 내용을 화면에 표시합니다.
        StringBuilder stringBuilder = new StringBuilder();
        for (String dateTime : accumulatedSelections) {
            stringBuilder.append(dateTime).append("\n");
        }
        selectedDateTimeTextView.setText("누적된 선택 내용:\n" + stringBuilder.toString());
    }

    private void showDatePickerDialog() { // 날짜 선택 Dialog를 표시하는 메서드
        Calendar calendar = Calendar.getInstance(); // 현재 시간으로 Calendar 객체 생성
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog( // DatePickerDialog 객체 생성
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

    private void updateSelectedDatesTextView() { // 선택된 날짜들을 텍스트뷰에 업데이트하는 메서드
        StringBuilder message = new StringBuilder("Selected Dates :\n"); // 메시지 문자열 생성
        for (String date : selectedDates) { // selectedDates 리스트에 있는 날짜들을 반복하며
            message.append(date).append("\n"); // 메시지에 날자를 추가
        }
        tvSelectedDate.setText(message.toString()); // 텍스트뷰에 메시지를 설정하여 표시
    }
}