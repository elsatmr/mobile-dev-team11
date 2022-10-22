package edu.northeastern.team11;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AtYourService extends AppCompatActivity {

    private ConstraintLayout constLayout;
    private Toolbar toolbar;
    private EditText searchInput;
    private ImageButton clearButton;
    private String textInputContents;
    protected List<String> searchList;
    private List<Chip> chips;
    private List<Integer> chipIDs;
    private int chipID;

    private FloatingActionButton searchButton;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_foods);
        textInputContents = "";
        searchList = null;
        searchInput = findViewById(R.id.searchInput);
        clearButton = findViewById(R.id.clearTextBtn);
        constLayout = findViewById(R.id.constLayoutView);
        searchButton = findViewById(R.id.searchButton);
        toolbar = findViewById(R.id.toolbar);
        searchButton.setEnabled(false);
        chipID = 0;
        chipIDs = new ArrayList<Integer>();
        chips = new ArrayList<Chip>();

        enableRemoveSearchFocus(); // When user clicks off search input, remove focus
        handleTyping();
    }

    // When the user types in search input, remove the hint and store the string
    // When the user deletes all test in the search input, add the hint back
    private void handleTyping() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputContents = charSequence.toString();
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
                Log.i("inputContents:", textInputContents.toString());
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
    // THIS SHOULD TRIGGER THE GET REQUEST
    public void addChip(View view) {
        if (chipGroup == null) {
            addChipGroup();
        }
        // Create the chip and give it an ID
        Chip newChip = new Chip(this);
        newChip.setId(chipID);
        chipIDs.add(chipID);
        chipID++;

        newChip.setText(textInputContents);
        newChip.setTextColor(Color.parseColor("#FAF8F8"));
        newChip.setBackgroundColor(Color.parseColor("#19444C"));
        newChip.setVisibility(View.VISIBLE);

        clearSearchInput(null);

        // onClose listener
        newChip.setCloseIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_close_clear_cancel));
        newChip.setCloseIconVisible(true);
        newChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(newChip);
                chipIDs.remove(chipIDs.indexOf(newChip.getId()));
//                chips.remove(newChip.getId());
            }
        });

        // Add chip to the view and Flow
//        constLayout.addView(newChip);
        chipGroup.addView(newChip);
//        flow.setReferencedIds(chipIDs.stream().mapToInt(i -> i).toArray());

    }




    // When the searchInput has focus and the user clicks off of it, remove the focus
    private void enableRemoveSearchFocus() {
        constLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (searchInput.hasFocus() == true) {
                    searchInput.clearFocus();
                }
                return true;
            }
        });

 }



}
