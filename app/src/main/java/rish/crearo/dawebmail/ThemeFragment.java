package rish.crearo.dawebmail;

import java.util.ArrayList;

import rish.crearo.R;
import rish.crearo.colorpicker.ColorPickerDialog;
import rish.crearo.colorpicker.ColorPickerDialog.OnColorSelectedListener;
import rish.crearo.dawebmail.fragments.FragmentOne;
import rish.crearo.utils.ColorScheme;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThemeFragment extends Fragment {

    String username = "", pwd = "";

    public ThemeFragment(String type, String username, String pwd) {
        this.username = username;
        this.type = type;
        if (savethembtn != null)
            if (!(type.equals("Customise")))
                savethembtn.setText("Choose One");
            else
                savethembtn.setText("Save Theme");

        setHasOptionsMenu(true);
    }

    ArrayList<String> tochangelist;
    ListView mListView;
    ThemeListAdapter mListAdapter;
    String type = "";
    static int positionThemeFrag = 0;

    Button savethembtn;

    static Boolean themecolorchanged = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_theme_fragment, container, false);
        getActivity().getActionBar().setTitle("Choose Theme");

        tochangelist = new ArrayList<>();

        setList();

        mListView = (ListView) rootView.findViewById(R.id.theme_listview);
        savethembtn = (Button) rootView.findViewById(R.id.theme_savebtn);
        mListAdapter = new ThemeListAdapter();

        mListView.setAdapter(mListAdapter);

        if (!(type.equals("Customise")))
            savethembtn.setText("Choose One");
        else
            savethembtn.setText("Save Theme");

        if (savethembtn.getText().equals("Save Theme")) {
            savethembtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    System.out.println("clicked");
                    ColorScheme scheme = new ColorScheme(getActivity());
                    scheme.saveUserColor();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Saved", Toast.LENGTH_SHORT).show();
                }
            });
        }

        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (type.equals("Customise")) {
                    positionThemeFrag = position;
                    showColorPickerDialogDemo(position);

                } else {// if preset
                    ColorScheme coloring = new ColorScheme(getActivity());
                    switch (position) {
                        case 0:
                            coloring.simpleColorPreset();
                            System.out.println("Setting simple");
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame,
                                            new FragmentOne(username, pwd))
                                    .commit();
                            break;
                        case 1:
                            coloring.classicColorPreset();
                            System.out.println("Setting classic");
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame,
                                            new FragmentOne(username, pwd))
                                    .commit();
                            break;
                        case 2:
                            coloring.groovyColorPreset();
                            System.out.println("Setting groovy");
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame,
                                            new FragmentOne(username, pwd))
                                    .commit();
                            break;
                        case 3:
                            coloring.userColorPreset();
                            System.out.println("Setting user");
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame,
                                            new FragmentOne(username, pwd))
                                    .commit();
                            break;

                    }
                }
            }
        });

        return rootView;
    }

    public void setList() {
        if (type.equals("Customise")) {
            tochangelist.add("color_readmessage");
            tochangelist.add("color_readtextsubject");
            tochangelist.add("color_readtextsender");
            tochangelist.add("color_unreadmessage");
            tochangelist.add("color_unreadtextsubject");
            tochangelist.add("color_unreadtextsender");
            tochangelist.add("color_datetextcolor");
            tochangelist.add("color_actionbarcolor");
            tochangelist.add("color_actionbartextcolor");
        } else {
            tochangelist.add("Simple");
            tochangelist.add("Classic");
            tochangelist.add("Groovy");
            tochangelist.add(username + "'s Theme");
        }
    }

    class ThemeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tochangelist.size();
        }

        @Override
        public String getItem(int position) {
            return tochangelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View
                        .inflate(getActivity().getApplicationContext(),
                                R.layout.theme_listitem, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String text = getItem(position);

            holder.theme_listtv.setText(text);

            if (type.equals("Customise")) {
                switch (position) {
                    case 0:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_readmessage));
                        break;
                    case 1:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_readtextsubject));
                        break;
                    case 2:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_readtextsender));
                        break;
                    case 3:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_unreadmessage));
                        break;
                    case 4:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_unreadtextsubject));
                        break;
                    case 5:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_unreadtextsender));
                        break;
                    case 6:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_datetextcolor));
                        break;
                    case 7:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_actionbarcolor));
                        break;
                    case 8:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_actionbartextcolor));
                        break;
                }
            } else {
                switch (position) {
                    case 0:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_presetsimple));
                        break;
                    case 1:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_presetclassic));
                        break;
                    case 2:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_presetgroovy));
                        break;

                    case 3:
                        holder.theme_listicon.setBackgroundColor(Color
                                .parseColor(ColorScheme.color_presetuser));
                        break;

                }
            }
            return convertView;
        }

        class ViewHolder {
            ImageView theme_listicon;
            TextView theme_listtv;

            public ViewHolder(View view) {
                theme_listicon = (ImageView) view
                        .findViewById(R.id.theme_image);
                theme_listtv = (TextView) view
                        .findViewById(R.id.theme_textView);
                view.setTag(this);
            }
        }
    }

    public void showColorPickerDialogDemo(final int position) {

        int initialColor = Color.WHITE;
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                getActivity(), initialColor, new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                System.out.println("Color selected " + color);
                String hexColor = String.format("#%02x%02x%02x",
                        Color.red(color), Color.green(color),
                        Color.blue(color));
                System.out.println("changing - "
                        + tochangelist.get(position) + " to "
                        + hexColor);
                ColorScheme colorScheme = new ColorScheme(getActivity());
                colorScheme.editColorScheme(tochangelist.get(position),
                        hexColor);
                themecolorchanged = true;
                getFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.content_frame,
                                new ThemeFragment("Customise",
                                        username, pwd)).commit();
            }
        });
        colorPickerDialog.show();
    }
}