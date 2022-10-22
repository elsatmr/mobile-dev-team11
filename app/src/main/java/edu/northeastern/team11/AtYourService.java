package edu.northeastern.team11;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AtYourService extends AppCompatActivity {

    private ConstraintLayout constLayout;
    private Toolbar toolbar;
    private EditText searchInput;
    private ImageButton clearButton;
    private String textInputContents;
    protected List<String> searchList; // List of search strings
    private List<Integer> chipIDs; // List of the ids for the chips
    private int chipID; // counter used to dynamically create chip Ids
    private List<Chip> chipList;
    private FloatingActionButton searchButton;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_foods);
        textInputContents = "";
        searchList = new ArrayList<String>();
        searchInput = findViewById(R.id.searchInput);
        clearButton = findViewById(R.id.clearTextBtn);
        constLayout = findViewById(R.id.constLayoutView);
        searchButton = findViewById(R.id.searchButton);
        toolbar = findViewById(R.id.toolbar);
        searchButton.setEnabled(false);
        chipID = 0;
        chipIDs = new ArrayList<Integer>();
        chipList = new ArrayList<Chip>();
        handleTyping();
    }

    // When the user types in EditText store the string
    // When the user deletes all test in the search input, add the hint back
    private void handleTyping() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputContents = charSequence.toString().trim();
                if (textInputContents.length() > 0) {
                    clearButton.setVisibility(View.VISIBLE);
                    searchButton.setEnabled(true);
                } else {
                    clearButton.setVisibility(View.INVISIBLE);
                    searchButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        searchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    addChip();
                }
                return false;
            }
        });
    }

    // Clear the search EditText
    public void clearSearchInput(View view) {
        searchInput.setText("");
        textInputContents = "";
        searchInput.requestFocus();
    }

    // Add Chipgroup to hold the chips
    private void addChipGroup() {
        chipGroup = new ChipGroup(this);
        chipGroup.setId(R.id.chipGroup);
        constLayout.addView(chipGroup);
        ConstraintSet chipGroupConstraint = new ConstraintSet();
        chipGroupConstraint.constrainHeight(chipGroup.getId(), ConstraintSet.WRAP_CONTENT);
        chipGroupConstraint.constrainWidth(chipGroup.getId(), ConstraintSet.WRAP_CONTENT);
        chipGroupConstraint.connect(chipGroup.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        chipGroupConstraint.connect(chipGroup.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        chipGroupConstraint.connect(chipGroup.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
        chipGroupConstraint.applyTo(constLayout);
    }

    // Add a chip
    public void addChip(View view) {
        if (textInputContents.length() > 0) {
            addChip(textInputContents);
        }
    }

    // Add a chip
    private void addChip(){
        if (textInputContents.length() > 0) {
            addChip(textInputContents);
        }
    }

    // Add a chip
    // THIS SHOULD TRIGGER THE GET REQUEST
    private void addChip(String searchString) {
        if (chipGroup == null) {
            addChipGroup();
        }
        // Create the chip and give it an ID
        Chip newChip = new Chip(this);
        newChip.setId(chipID);
//        chipIDs.add(chipID);
        chipID++;
        searchList.add(searchString);
        newChip.setText(searchString);
        newChip.setTextColor(Color.parseColor("#FAF8F8"));
        newChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#19444C")));
        clearSearchInput(null);

        // Set the onClose listener for each chip
        newChip.setCloseIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_close_clear_cancel));
        newChip.setCloseIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        newChip.setCloseIconVisible(true);
        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(newChip);
                searchList.remove(newChip.getText());
                chipList.remove(newChip);

            }
        });
        chipList.add(newChip);
        chipGroup.addView(newChip);
        closeKeyboard();
        searchInput.clearFocus();
    }

    // Close the keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("chipID", chipID);
//        outState.putSerializable("chipIDs", (Serializable) chipIDs);
        outState.putSerializable("searchList", (Serializable) searchList);
        outState.putSerializable("chipList", (Serializable) chipList);
        chipGroup.removeAllViews();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        chipID = savedInstanceState.getInt("chipID");
//        chipIDs = (List<Integer>) savedInstanceState.getSerializable("chipIDs");
        searchList = (List<String>) savedInstanceState.getSerializable("searchList");
        chipList = (List<Chip>) savedInstanceState.getSerializable("chipList");
        addChipGroup();
        chipList.forEach(x -> {
            chipGroup.addView(x);
            x.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipGroup.removeView(x);
                    searchList.remove(x.getText());
                    chipList.remove(x);
                }
            });
        });
    }

    private void addChips() {

    }
}
