package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.text.util.Linkify;
import android.widget.Button;
import android.widget.TextView;

import org.morsi.android.nethack.redux.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutDialog {
    Dialog dialog;

    private Button closeButton(){
        return (Button) dialog.findViewById(R.id.about_close);
    }

    private TextView title(){
        return (TextView) dialog.findViewById(R.id.about_title);
    }

    private TextView authors(){
        return (TextView) dialog.findViewById(R.id.about_authors);
    }

    private TextView license(){
        return (TextView) dialog.findViewById(R.id.about_license);
    }

    public static Dialog create(Activity activity){
        AboutDialog about = new AboutDialog();
        about.dialog = new Dialog(activity, R.style.About);
        about.dialog.setContentView(R.layout.about_dialog);
        about.dialog.setTitle(activity.getString(R.string.about_dialog_title));

        // wire up close button
        about.closeButton().setOnClickListener(new DialogListener(about.dialog));

        // wire up project homepage link
        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return "";
            }
        };
        Pattern pattern = Pattern.compile(activity.getString(R.string.app_name));
        String scheme = activity.getString(R.string.project_url);

        Linkify.addLinks(about.title(), pattern, scheme, null, filter);

        // wire up author url
        pattern = Pattern.compile(activity.getString(R.string.project_author));
        scheme = activity.getString(R.string.project_author_url);

        Linkify.addLinks(about.authors(), pattern, scheme, null, filter);

        // wire up license url
        pattern = Pattern.compile(activity.getString(R.string.project_license));
        scheme = activity.getString(R.string.project_license_url);

        Linkify.addLinks(about.license(), pattern, scheme, null, filter);

        return about.dialog;
    }
}
