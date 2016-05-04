/**********************************
 * Nethack Encyclopedia - Quick Stats Activity
 * <p/>
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.QuickStat;
import org.morsi.android.nethack.redux.util.QuickStatCategory;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;

// User friendly view to various lists of builtin Nethack stats
public class QuickStatsActivity extends Activity {
    private LinearLayout layout(){
        return (LinearLayout) findViewById(R.id.quick_stats_layout);
    }

    private Spinner spinner() {
        return (Spinner) findViewById(R.id.quick_stats_spinner);
    }

    private String selectedQuickStatName() {
        return spinner().getSelectedItem().toString();
    }

    // Returns the current stat category selected in the spinner
    private QuickStatCategory selectedQuickStatCategory() {
        String selected = selectedQuickStatName();
        for (QuickStatCategory category : stats)
            if (category.name.equals(selected))
                return category;
        return null;
    }

    private ArrayAdapter<CharSequence> spinnerAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.quick_stats_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private TableLayout statsTable(){
        return (TableLayout) layout().findViewById(R.id.quick_stats_table);
    }

    private int statsTableIndex(View row){
        for (int index = 0; index < statsTable().getChildCount(); ++index)
            if (statsTable().getChildAt(index) == row)
                return index;
        return -1;
    }

    private QuickStat selectedQuickStat(View row){
        return selectedQuickStatCategory().get_stats().get(statsTableIndex(row) - 2); // need to offset header row and border row
    }

    // Override menu / about_dialog dialog handlers
    @Override
    public boolean onSearchRequested() {
        return AndroidMenu.onSearchRequested(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return AndroidMenu.onCreateOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return AndroidMenu.onOptionsItemSelected(this, item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return AndroidMenu.onCreateDialog(this, id);
    }

    // List of quick stat categories containing stats
    ArrayList<QuickStatCategory> stats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_stats);

        // retrieve stats from xml resources
        createStats();

        // populate spinner and wire up changes
        initSpinner();
    }

    private void createStats(){
        stats = new ArrayList<QuickStatCategory>();
        stats.add(QuickStatCategory.fromXML(this.getString(R.string.gems_quick_stat),    getResources().getXml(R.xml.gems)));
        stats.add(QuickStatCategory.fromXML(this.getString(R.string.scrolls_quick_stat), getResources().getXml(R.xml.scrolls)));
        stats.add(QuickStatCategory.fromXML(this.getString(R.string.armor_quick_stat),   getResources().getXml(R.xml.armor)));
        stats.add(QuickStatCategory.fromXML(this.getString(R.string.rings_quick_stat),   getResources().getXml(R.xml.rings)));
        stats.add(QuickStatCategory.fromXML(this.getString(R.string.tools_quick_stat),   getResources().getXml(R.xml.tools)));
        stats.add(QuickStatCategory.fromXML(this.getString(R.string.wands_quick_stat),   getResources().getXml(R.xml.wands)));
        stats.add(QuickStatCategory.fromXML(this.getString(R.string.corpses_quick_stat), getResources().getXml(R.xml.corpses)));
    }

    private void initSpinner(){
        spinner().setAdapter(spinnerAdapter());
        spinner().setOnItemSelectedListener(new QuickStatsSelectedListener());
    }

    // Handles quick stats spinner changes
    class QuickStatsSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            clearStats();
            if (pos != 0) displayStats(selectedQuickStatCategory());
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    // Clears the quick stats table
    private void clearStats() {
        statsTable().removeAllViews();
    }

    // Display the specified stat on the table in the view
    private void displayStats(QuickStatCategory category) {
        List<QuickStat> rstats = category.get_stats();

        // add header row to table
        statsTable().addView(tableHeader(category), tableHeaderLayout());

        // add border row beneath header
        statsTable().addView(headerBorder(), headerBorderLayout());

        // add each quick stat to table
        for (QuickStat stat : rstats)
            statsTable().addView(statRow(category, stat), statRowLayout());
    }

    private TableRow tableHeader(QuickStatCategory category){
        List<String> columns   = category.get_columns();
        List<Float> weights    = category.get_weights();

        TableRow row = new TableRow(layout().getContext());
        for (int i = 0; i < columns.size(); ++i) {
            TextView label = new TextView(layout().getContext());
            label.setText(columns.get(i));
            label.setEllipsize(TruncateAt.MARQUEE);

            // wire up click listener to stort columns
            label.setOnClickListener(new ColumnClickedListener());
            label.setTextColor(getResources().getColor(R.color.light_blue));

            // if no weight specified, weigh all columns equally
            float weight = weights.get(i);
            if (weight == -1) weights.set(i, new Double(1.0 / weights.size()).floatValue());

            label.setGravity(Gravity.CENTER);
            label.setLayoutParams(new LayoutParams(0, LayoutParams.FILL_PARENT, weights.get(i)));
            row.addView(label);
        }

        row.setLayoutParams(tableHeaderLayout());
        return row;
    }

    private TableLayout.LayoutParams tableHeaderLayout(){
       return new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    }

    private TableRow headerBorder(){
        TableRow border = new TableRow(layout().getContext());
        border.setPadding(1, 1, 1, 2);
        border.setBackgroundResource(R.color.white);
        border.setLayoutParams(headerBorderLayout());
        return border;
    }

    private LayoutParams headerBorderLayout(){
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(1, 1, 1, 2);
        return lp;
    }

    private TableRow statRow(QuickStatCategory category, QuickStat stat){
        List<String> columns   = category.get_columns();
        List<Float> weights    = category.get_weights();

        TableRow row = new TableRow(layout().getContext());
        row.setLayoutParams(statRowLayout());

        boolean alternate_col = false;
        for (int i = 0; i < columns.size(); ++i) {
            String value = stat.get_value(i);
            if (value != null) {
                TextView col = new TextView(layout().getContext());
                col.setText(value);
                col.setEllipsize(TruncateAt.MARQUEE);

                // alternate background colors
                col.setBackgroundResource((alternate_col = !alternate_col) ? R.color.gray : R.color.light_gray);

                // wire up click listener to view row details
                RowClickedListener listener = new RowClickedListener();
                listener.activity = this;
                col.setOnClickListener(listener);

                col.setGravity(Gravity.CENTER);
                col.setLayoutParams(new LayoutParams(0, LayoutParams.FILL_PARENT, weights.get(i)));
                row.addView(col);
            }
        }

        // fill in empty columns
        for (int i = stat.num_values(); i < columns.size(); ++i) {
            TextView blank = new TextView(layout().getContext());
            blank.setLayoutParams(new LayoutParams(0, LayoutParams.FILL_PARENT, weights.get(i)));
            blank.setBackgroundResource((alternate_col = !alternate_col) ? R.color.gray : R.color.light_gray);
            row.addView(blank);
        }

        return row;
    }

    private TableLayout.LayoutParams statRowLayout(){
        return new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    }

    // Handles clicks to the column headers by sorting the table
    class ColumnClickedListener implements OnClickListener {
        public void onClick(View v) {
            QuickStatCategory selected = selectedQuickStatCategory();
            String sort_column = ((TextView) v).getText().toString();
            selected.sort_stats(sort_column);
            clearStats();
            displayStats(selected);
        }
    }

    // Handles clicks to the rows by popuping w/ more more details
    class RowClickedListener implements OnClickListener {
        QuickStatsActivity activity;

        public void onClick(View v) {
            // get the quick stat corresponding to the row
            new QuickStatsPopup(activity, selectedQuickStatCategory(), selectedQuickStat((View) v.getParent()));
        }
    }
}
