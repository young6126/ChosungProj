package com.example.chosungproj;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class ChosungGameActivity2 extends AppCompatActivity {
    XmlPullParser xpp;
    String key = "86F3B8C49B9C4187A83294E47AA9058F";
    Boolean data;
    String[] wordlist = new String[]{"가랫밥","가맛밥","가윗밥","가첨밥",
            "간질밥","갈큇밥","감자밥","감투밥","강정밥","강조밥"};
    String[] chosungList1 = new String[]{"ㄱ","ㄱ","ㄱ","ㄱ",
            "ㄱ","ㄱ","ㄱ","ㄱ","ㄱ","ㄱ"};
    String[] chosungList2 = new String[]{"ㄹ","ㅁ","ㅇ","ㅊ",
            "ㅈ","ㅋ","ㅈ","ㅌ","ㅈ","ㅈ"};
    String[] chosungList3 = new String[]{"ㅂ","ㅂ","ㅂ","ㅂ",
            "ㅂ","ㅂ","ㅂ","ㅂ","ㅂ","ㅂ"};
    Button button1;
    TextView textView,chosung1,chosung2,chosung3;
    EditText edittext1;
    int answerint = 0;
    int numberint = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.chosunggame_view);*/

        button1 = (Button) findViewById(R.id.button1);
        textView = (TextView) findViewById(R.id.chosung_score);
        edittext1 = (EditText) findViewById(R.id.edittext1);
        chosung1 = (TextView) findViewById(R.id.chosung1);
        chosung2 = (TextView) findViewById(R.id.chosung2);
        chosung3 = (TextView) findViewById(R.id.chosung3);
        textView.setText("정답 수 : "+ answerint +" / "+ numberint);
        chosung1.setText(chosungList1[0]);
        chosung2.setText(chosungList2[0]);
        chosung3.setText(chosungList3[0]);
    }

    public void mOnClick(View v) { // 클릭 시 이벤트 설정
        switch (v.getId()) {
            case R.id.button1: // 버튼 클릭 시
                CheckAnswer(edittext1.getText().toString());
                if(numberint == 10) { // 10이라면
                }
                edittext1.setText(null);
                textView.setText(String.valueOf(answerint)+ numberint); // 정답 값 수정
                chosung1.setText(chosungList1[numberint]); // 초성칸 1,2,3 수정
                chosung2.setText(chosungList2[numberint]);
                chosung3.setText(chosungList3[numberint]);
                break;
        }
    }
    void CheckAnswer(String s) { // 스레드를 이용하여 구성
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data=getXmlData(s);
                    if(data.equals(true)) { //국어사전에 실제로 존재하는 단어일 경우
                        answerint += 1; //answer 값을 1 증가시켜 답의 값을 1 증가시킴
                    }
                    numberint += 1; // 정답에 상관없이 number값은 1 증가
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                    }
                }).start();
    }

    Boolean getXmlData(String search) throws UnsupportedEncodingException { //xml 데이터 파싱
        String tosearch = URLEncoder.encode(search, "UTF-8");
        String queryUrl="https://stdict.korean.go.kr/api/search.do"
                +"?key="+key
                +"&q="+tosearch;
        try {
            URL url=new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(url.openStream(),null);

            String tag=null;

            xpp.next();
            int eventType=xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag=xpp.getName();
                        if (tag.equals("total")) {
                            xpp.next();
                            String what = xpp.getText();
                            if (!what.equals("0")) {
                                return true;
                            }
                        }
                        break;
                }
                eventType=xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
