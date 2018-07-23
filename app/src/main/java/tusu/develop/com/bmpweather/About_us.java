package tusu.develop.com.bmpweather;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class About_us extends AppCompatActivity {

    Button btnMessage,btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        btnMessage=findViewById(R.id.btnMessage);
        btnSend=findViewById(R.id.btnConnect);

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.facebook.com/m.mehedihasanshuvo.in"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "bdmehedishuvo@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, " ");
                intent.putExtra(Intent.EXTRA_TEXT, " ");
                startActivity(Intent.createChooser(intent, ""));
            }
        });


    }
}
