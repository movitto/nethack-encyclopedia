package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.text.util.Linkify;
import android.widget.Button;
import android.widget.TextView;

import org.morsi.android.nethack.redux.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutDialog extends Dialog{
     private Button closeButton(){
        return (Button) findViewById(R.id.about_close);
    }

    private TextView title(){
        return (TextView) findViewById(R.id.about_title);
    }

    private TextView authors(){
        return (TextView) findViewById(R.id.about_authors);
    }

    private TextView license(){
        return (TextView) findViewById(R.id.about_license);
    }

    public AboutDialog(Activity activity){
        super(activity, R.style.About);
        setContentView(R.layout.about_dialog);
        setTitle(activity.getString(R.string.about_dialog_title));

        // wire up close button
        closeButton().setOnClickListener(new DialogListener(this));

        // wire up project homepage link
        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return "";
            }
        };
        Pattern pattern = Pattern.compile(activity.getString(R.string.app_name));
        String scheme = activity.getString(R.string.project_url);

        Linkify.addLinks(title(), pattern, scheme, null, filter);

        // wire up author url
        pattern = Pattern.compile(activity.getString(R.string.project_author));
        scheme = activity.getString(R.string.project_author_url);

        Linkify.addLinks(authors(), pattern, scheme, null, filter);

        // wire up license url
        pattern = Pattern.compile(activity.getString(R.string.project_license));
        scheme = activity.getString(R.string.project_license_url);

        Linkify.addLinks(license(), pattern, scheme, null, filter);
    }
}
