package com.juara.c_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juara.c_weather.model.WeatherModel;
import com.juara.c_weather.service.APIClient;
import com.juara.c_weather.service.APIInterfacesRest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView txtkota, txtSuhu, txtTanggal, txtWind, txtClouds, txtPreasure, txtHumadity, txtSunrise, txtSunset, txtGeoCoords;
    ImageView imgCuaca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather");
        txtSuhu = findViewById(R.id.txtSuhu);
        txtTanggal = findViewById(R.id.txtTanggal);
        txtWind = findViewById(R.id.txtWind);
        txtClouds = findViewById(R.id.txtClouds);
        txtPreasure = findViewById(R.id.txtPreasure);
        txtHumadity = findViewById(R.id.txtHumadity);
        txtSunrise = findViewById(R.id.txtSunrise);
        txtSunset = findViewById(R.id.txtSunset);
        txtGeoCoords = findViewById(R.id.txtGeoCoords);
        imgCuaca = findViewById(R.id.imgCuaca);
        txtkota = findViewById(R.id.txtKota);

    }


    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    public void callWeather(){

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<WeatherModel> call3 = apiInterface.getc_weather(35.0,139.0,"2cb8140ddd1c73c602d5b97a344d9808");
        call3.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                progressDialog.dismiss();
                WeatherModel dataWeather = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (dataWeather !=null) {
                    txtkota.setText(dataWeather.getName()+ ", " + dataWeather.getSys().getCountry());
                    txtWind.setText(dataWeather.getWeather().get(0).getDescription() + " , " + dataWeather.getWind().getSpeed().toString() + " m/s");
                    txtClouds.setText(dataWeather.getWeather().get(0).getMain());
//                    txtSunrise.setText(dataWeather.getName());
//                    txtTanggal.setText(new SimpleDateFormat("dd mm yyyy", Locale.ENGLISH));
                    txtPreasure.setText(dataWeather.getMain().getPressure() + " hpa");
                    txtHumadity.setText(dataWeather.getMain().getHumidity() + " %");
                    txtSunrise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(dataWeather.getSys().getSunset() * 1000 * (60 * 60 * 7 ) )));
                    txtSunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(dataWeather.getSys().getSunrise() * 1000 * (60 * 60 * 7))));
                    txtGeoCoords.setText("[ " +dataWeather.getCoord().getLat().toString() + " , " + dataWeather.getCoord().getLon().toString() + " ]");
                    txtSuhu.setText(new DecimalFormat("##.##").format(dataWeather.getMain().getTemp()-273.15) + "°C");

                    String image = "http://openweathermap.org/img/wn/"+ dataWeather.getWeather().get(0).getIcon()+"@2x.png";
                    Picasso.get().load(image).into(imgCuaca);








                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });




    }
}
