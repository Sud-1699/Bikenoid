#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <SimpleTimer.h>

TinyGPSPlus gps;

SimpleTimer timer;

static const int RXPin = 6, TXPin = 7;   // GPIO 4=D2(conneect Tx of GPS) and GPIO 5=D1(Connect Rx of GPS)
static const uint32_t GPSBaud = 9600; //if Baud rate 9600 didn't work in your case then use 4800

float latitude;
float longitude;
float spd;

String lat1;
String lon1;

int buzz=2;

const int x=A0;
const int y=A1;
const int z=A2;

int xsample=0;
int ysample=0;
int zsample=0;

int value1;
int value2;
int value3;

int xValue;
int yValue;
int zValue;

#define samples 10

#define minVal -300
#define MaxVal 300

String DEVICEID="Bikenoid_Client_1";

SoftwareSerial ss(RXPin, TXPin);
SoftwareSerial SerialAT(10, 11);

void setup() {
  Serial.begin(9600);
  ss.begin(GPSBaud);
  SerialAT.begin(19200);
  callAT();
  setupinit();
  timer.setInterval(5000L, checkGPS);
  pinMode(x,INPUT);
  pinMode(y,INPUT);
  pinMode(z,INPUT);
  sendAcc();
  pinMode(buzz,OUTPUT);
}

void loop() {
  ss.listen();
   while (ss.available() > 0){
     gps.encode(ss.read());
     callAT();
     callGPS();
   }
   accInfo();
   callAT();
}

void sendAcc(){

    for(int i=0;i<samples;i++)
  {
    xsample+=analogRead(x);
    ysample+=analogRead(y);
    zsample+=analogRead(z);
  }

  xsample/=samples;
  ysample/=samples;
  zsample/=samples;
  
  }

void checkGPS(){
  if (gps.charsProcessed() < 10)
  {
    Serial.println(F("No GPS detected: check wiring."));
  }
}

void callAT(){

  while (Serial.available()){
    SerialAT.write(Serial.read());  
  }
  while (SerialAT.available()>0){
    Serial.write(SerialAT.read());
  }
 }

void setupinit(){
  SerialAT.println("at");
  delay(200L);
  callAT();

  SerialAT.println("at+csq");
  delay(500L);
  callAT();

//  SerialAT.println("at+cops=1,2,\"40420\"");
//  delay(500L);
//  callAT();

  SerialAT.println("at+cops?");
  delay(500L);
  callAT();

  SerialAT.println("at+creg?");
  delay(500L);
  callAT();

//  SerialAT.println("AT+CMGF=1");
//  delay(500L);
//  callAT();
   
  SerialAT.println("at+sapbr=3,1,\"CONTYPE\",\"GPRS\"");
  delay(500L);
  callAT();
  
  SerialAT.println("at+sapbr=3,1,\"APN\",\"CMNET\"");
  delay(500L);
  callAT();

  SerialAT.println("at+sapbr=0,1");
  delay(500L);
  callAT();
  
  SerialAT.println("at+sapbr=1,1");
  delay(500L);
  callAT();
  
  SerialAT.println("at+sapbr=2,1");
  delay(500L);
  callAT();
  
  SerialAT.println("at+httpinit=?");
  delay(500L);
  callAT();

  SerialAT.println("at+httpterm");
  delay(500L);
  callAT();

  SerialAT.println("at+httpinit");
  delay(100L);
  callAT();

  SerialAT.println("at+httppara=\"cid\",1");
  delay(100L);
  callAT();
  
  }

void callGPS(){

  if (gps.location.isUpdated() && (gps.location.age()<3000))
  {
    latitude=(gps.location.lat());
    longitude=(gps.location.lng());
    spd=gps.speed.kmph();
    lat1=String(latitude,6);
    lon1=String(longitude,6);
    sendLoc(DEVICEID);
    if (spd > 40){
      int i=0;
      A:
      if (i<10){
        digitalWrite(buzz,HIGH);
        digitalWrite(buzz,LOW);
        i++;
        i-=10;
        goto A;
        }
      }
  }
 }

void sendLoc(String deviceId){
  String url="at+httppara=\"URL\",\"http://bikenoid1.000webhostapp.com/firebase.php?&deviceid=";
  url.concat(deviceId);
  url.concat("&lat=");
  url.concat(lat1);
  url.concat("&lon=");
  url.concat(lon1);
  url.concat("&spd=");
  url.concat(spd);
  url.concat("\"");
  SerialAT.begin(19200);
  SerialAT.println(url);   //,\"http://bikenoid1.000webhostapp.com/firebase.php?deviceid="
  delay(100L);
  callAT();
  SerialAT.println("at+httpaction=0");
  delay(3000L);
  callAT();
  SerialAT.println("at+httpread=1,5000");
  delay(3000L);
  callAT();
  }

void accInfo(){

  value1=analogRead(x);
  value2=analogRead(y);
  value3=analogRead(z);
  
  xValue=xsample-value1;
  yValue=ysample-value2;
  zValue=zsample-value3;
    
  if((xValue < minVal || xValue > MaxVal)  || (yValue < minVal || yValue > MaxVal)  || (zValue < minVal || zValue > MaxVal)){
        Serial.println("Accident is Occur!!");
        delay(5000L);
        Send();
     }
  }

void Send()
{ 
  SerialAT.begin(19200);
   SerialAT.println("AT");
   delay(500);
   callAT();
   SerialAT.println("AT+CMGF=1");
   delay(500);
   callAT();
   SerialAT.print("AT+CMGS=");
   SerialAT.print('"');
   SerialAT.print("+918828691940");
//   SerialAT.print("+919619521390");    //mobile no. for SMS alert
   SerialAT.println('"');
   delay(500);
   callAT();
   SerialAT.println("Accident has occur in this location");
   delay(500);
   callAT();
   SerialAT.print("http://maps.google.com/maps?&z=15&mrt=yp&t=k&q=");
   SerialAT.print(lat1);
   SerialAT.print("+");              //28.612953, 77.231545   //28.612953,77.2293563
   SerialAT.print(lon1);
   SerialAT.println((char)26);
   delay(500);
   callAT();
 }
