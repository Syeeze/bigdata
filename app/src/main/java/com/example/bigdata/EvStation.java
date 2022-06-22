package com.example.bigdata;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class EvStation  {

    private static String ServiceKey = "API키"; //공공데이터 사이트를 통해 발급 받은 API키
    public EvStation() {
        try {
            apiParserSearch();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<MapPoint> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<MapPoint> mapPoint = new ArrayList<MapPoint>();

        String facility_name = null, longitude= null,latitude=null;
        boolean bfacility_name = false, blatitude = false, blongitude = false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if(tag.equals("addr")) {
                    bfacility_name = true;
                }
                if(tag.equals("lat")) {
                    blatitude = true;
                }
                if(tag.equals("longi")){
                    blongitude = true;
                }
            } else if (event_type == XmlPullParser.TEXT) {
                if(bfacility_name == true){
                    facility_name = xpp.getText();
                    bfacility_name = false;
                } else if(blatitude == true){
                    latitude = xpp.getText();
                    blatitude = false;
                }else if(blongitude ==true){
                    longitude = xpp.getText();
                    blongitude = false;
                }
            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("row")) {
                    MapPoint entity = new MapPoint();
                    entity.setName(facility_name);
                    entity.setLatitude(Double.valueOf(latitude));
                    entity.setLongitude(Double.valueOf(longitude));
                    mapPoint.add(entity);
                    System.out.println(mapPoint.size());
                }
            }
            event_type = xpp.next();
        }
        System.out.println(mapPoint.size());

        return mapPoint;
    }

    private String getURLParam(String search){
        String url = "http://apis.data.go.kr/1741000/CivilDefenseShelter2/getCivilDefenseShelterList?ServiceKey=" + ServiceKey + "&type=xml&pageNo=1&numOfRows=300&flag=Y";
        return url;
    }

    public static void main(String[] args) {
        new ShelterApi();
    }

/*
    EditText edit;
    TextView text;
    XmlPullParser xpp;

    String key="wYJ9TitTjQ342xmm2kBGdOgT7m%2B4YhbgB3Rpr7b05C%2BlboqM%2B15lsCMzXZC31EC91y3ee3Rg1QCcsiE4j8JxyA%3D%3D";
    String data;
    String lat;
    String longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ev_station);

        edit= (EditText)findViewById(R.id.edit);
        text= (TextView)findViewById(R.id.result);


    }
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data=getXmlData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }



    String getXmlData(){
        StringBuffer buffer=new StringBuffer();
        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";

        String queryUrl="http://openapi.kepco.co.kr/service/EvInfoServiceV2/getEvSearchList?serviceKey=wYJ9TitTjQ342xmm2kBGdOgT7m%2B4YhbgB3Rpr7b05C%2BlboqM%2B15lsCMzXZC31EC91y3ee3Rg1QCcsiE4j8JxyA%3D%3D&pageNo=1&numOfRows=10&addr="+location+"";
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("addr")){

                           // buffer.append("주소 : ");
                            xpp.next();
                           // buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                           // buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("chargeTp")){
                            //buffer.append("충전소타입 : ");
                            xpp.next();
                          //  buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                           // buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("cpId")){
                          //  buffer.append("충전소ID :");
                            xpp.next();
                          //  buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                          //  buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("cpNm")){
                          //  buffer.append("충전기 명칭 :");
                            xpp.next();
                          //  buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                           // buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("cpStat")){
                         //   buffer.append("충전기 상태 코드 :");
                            xpp.next();
                          //  buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                          //  buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("cpTp")){
                            buffer.append("충전 방식 :");
                            xpp.next();
                         //   buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                         //   buffer.append("  ,  "); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("csId")){
                            buffer.append("충전소 ID :");
                            xpp.next();
                        //    buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                         //   buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("cpNm")){
                         //   buffer.append("충전소 명칭 :");
                            xpp.next();
                         //   buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                         //   buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("lat")){
                         //   buffer.append("위도 :");
                            get
                            xpp.next();
                         //   buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                          //  buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("longi")){
                        //    buffer.append("경도 :");
                            xpp.next();
                         //   buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                         //   buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("statUpdateDatetime")){
                          //  buffer.append("충전기상태갱신시각 :");
                            xpp.next();
                          //  buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                          //  buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        buffer.append("파싱 끝\n");
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....*/
}
