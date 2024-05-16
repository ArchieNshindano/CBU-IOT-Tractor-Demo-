# CBU-IOT-Tractor-Demo-
CBU IOT Tractor Demo  is an exciting Internet of Things (IoT) project that allows you to control two LEDs remotely using Firebase plus retrieving temperature. 

Here is the ESP code below

```c++

#include "addons/RTDBHelper.h"

#include <DHT.h>
#define DHT11_PIN 32

// Insert your network credentials
#define WIFI_SSID "your wifi name"
#define WIFI_PASSWORD "your wifi password"

// Insert Firebase project API Key
#define API_KEY "your firebase api key"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "your firebase url" 

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;



DHT dht11(DHT11_PIN, DHT11);

int ledPin1 = 22;
int ledPin2 = 23;
int temperaturePin = 3;


unsigned long sendDataPrevMillis = 0;
int count = 0;
bool signupOK = false;


bool led1State;
bool led2State;

void setup(){

  pinMode(ledPin1,OUTPUT);
  pinMode(ledPin2,OUTPUT);

   dht11.begin(); // initialize the sensor




  Serial.begin(115200);

  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");

  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }

  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();





  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
  
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);




}

void loop(){

  // read humidity
  float humidity  = dht11.readHumidity();
  // read temperature as Celsius
  float temperatureCelcuis = dht11.readTemperature();
  // read temperature as Fahrenheit
  float tempFahrenheit = dht11.readTemperature(true);

  // check if any reads failed
  if (isnan(humidity) || isnan(temperatureCelcuis) || isnan(tempFahrenheit)) {
    Serial.println("Failed to read from DHT11 sensor!");
  }
  
   else {
    Serial.print("DHT11# Humidity: ");
    Serial.print(humidity);
    Serial.print("%");

    Serial.print("  |  "); 

    Serial.print("Temperature: ");
    Serial.print(temperatureCelcuis);
    Serial.print("°C ~ ");
    Serial.print(tempFahrenheit);
    Serial.println("°F");
  }

  
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 500 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    // Write an Int number on the database path test/int

     if ( Firebase.RTDB.setFloat(&fbdo, "Temperature", temperatureCelcuis) ){
      Serial.println("PASSED Temperature");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("TYPE: " + fbdo.dataType());
    }

    else {
      Serial.println("FAILED");
      Serial.println("REASON: " + fbdo.errorReason());
    }  // SENDING TEMPERATURE ENDS 







    
    if (Firebase.RTDB.getBool(&fbdo,"/LED1")){

       led1State = fbdo.boolData();
      Serial.println("Gotten");
      Serial.println("Value1: ");
      Serial.println(led1State);

     
     } // FETCHING VALUE1 ENDS
    
  
    else {
      Serial.println("FAILED");
      Serial.println("REASON: " + fbdo.errorReason());
    } // ERROR MESSAGE ENDS
 
    
    // Write an Float number on the database path test/float
     if (Firebase.RTDB.getBool(&fbdo, "/LED2")){
      led2State = fbdo.boolData();
      Serial.println("Gotten");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("TYPE: " + fbdo.dataType());
      Serial.println("Value2: ");
      Serial.println(led2State);

      } // FETCHING VALUE2 ENDS

     
    else {
      Serial.println("FAILED");
      Serial.println("REASON: " + fbdo.errorReason());
    } // ERROR MESSAGE ENDS


  } // FIREBASE QUERY ENDS



   if(led1State){

         digitalWrite(ledPin1,HIGH);
      }

      else{

        digitalWrite(ledPin1,LOW);
      }  // LED ENDS
       
   




     if(led2State){

         digitalWrite(ledPin2,HIGH);
      }

      else{

        digitalWrite(ledPin2,LOW);
      }   // LED ENDS

  

  
}

```
