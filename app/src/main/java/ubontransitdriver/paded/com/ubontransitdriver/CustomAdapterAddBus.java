package ubontransitdriver.paded.com.ubontransitdriver;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterAddBus extends ArrayAdapter<AddBusDataItem> {
    Context context;
    int layoutResourceId;
    List<AddBusDataItem> data=null;

    public CustomAdapterAddBus(@NonNull Context context, @LayoutRes int resource, @NonNull List<AddBusDataItem> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data=objects;
    }

}
