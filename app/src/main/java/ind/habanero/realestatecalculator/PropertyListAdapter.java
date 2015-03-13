package ind.habanero.realestatecalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.PropertyDataSource;
import models.Property;

/**
 * Created by michaelborglin on 2/14/15.
 */
public class PropertyListAdapter extends ArrayAdapter<Property> {

    private Context context;
    private int resource;
    private ArrayList<Property> properties;
    public static final String TAG = EstimatePropertyActivity.class.getSimpleName();

    public PropertyListAdapter(Context context, int resource, ArrayList<Property> properties) {
        super(context, resource, properties);
        this.context = context;
        this.resource = resource;
        this.properties=properties;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AtomHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(resource, parent, false);

        holder = new AtomHolder();
        holder.property = properties.get(position);

        Log.i(TAG, position+"");
        Log.i(TAG, holder.property.toString());
        holder.description = (TextView)row.findViewById(R.id.propertyDescription);

        row.setTag(holder.property.get_id());
        setupItem(holder);

        TextView propertyText = (TextView) row.findViewById(R.id.propertyDescription);
        propertyText.setTag(position);
        propertyText.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer index = (Integer) v.getTag();
                        Property property = properties.get(index.intValue());

                        Intent i = new Intent(context.getApplicationContext(), PropertyInfoActivity.class);
                        ArrayList<Property> toSend = new ArrayList<Property>();
                        toSend.add(property);
                        i.putParcelableArrayListExtra("property", toSend);
                        context.startActivity(i);
                        //overridePendingTransition(R.anim.slide_in_left, 0);

                    }
                });



        propertyText.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(final View view) {
                final AlertDialog.Builder b = new AlertDialog.Builder(
                        context);
                Integer index = (Integer) view.getTag();
                Property property = properties.get(index.intValue());

                b.setIcon(android.R.drawable.ic_dialog_alert);
                b.setMessage("Are you sure you want to delete this property '" + property.toStringLong() + "'?");
                b.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                Integer index = (Integer) view.getTag();
                                Property property = properties.remove(index.intValue());
                                PropertyDataSource ds = new PropertyDataSource(context);
                                ds.delete(property);
                                notifyDataSetChanged();

                            }
                        });
                b.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                            }
                        });

                b.show();
                return true;
            }
        });

        return row;
    }

    private void setupItem(AtomHolder holder) {
        holder.description.setText(holder.property.toString());
    }


    public static class AtomHolder {
        Property property;
        TextView description;
        ImageButton delete;
    }
}
