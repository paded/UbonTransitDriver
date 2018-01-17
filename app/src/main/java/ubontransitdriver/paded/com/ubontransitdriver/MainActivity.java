package ubontransitdriver.paded.com.ubontransitdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);



        MaterialShadowContainerView shadowView =
                (MaterialShadowContainerView) findViewById(R.id.shadow_item_container);

        float density = getResources().getDisplayMetrics().density;

        shadowView.setShadowTranslationZ(density * 2.0f); // 2.0 dp
        shadowView.setShadowElevation(density * 4.0f); // 4.0 dp


    }
}
