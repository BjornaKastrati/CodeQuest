package com.example.codequest.controller;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import java.util.Arrays;


import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.model.BinaryChallenge;
import com.example.codequest.model.BossLogicChallenge;
import com.example.codequest.model.Challenge;
import com.example.codequest.model.CodeOrderChallenge;
import com.example.codequest.model.GameRepository;
import com.example.codequest.model.Level;
import com.example.codequest.model.LogicChallenge;
import com.example.codequest.model.MultipleChoiceChallenge;
import com.example.codequest.model.TextInputChallenge;
import com.example.codequest.model.CodeChoiceChallenge;
import com.example.codequest.model.ArrayChallenge;
import com.example.codequest.model.StackChallenge;
import com.example.codequest.model.QueueChallenge;
import com.example.codequest.model.TreeChallenge;

import com.example.codequest.views.TreeCanvasView;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends AppCompatActivity {

    private TextView txtLevelTitle, txtScenarioProgress, txtInstruction;
    private TextView txtInputA, txtInputB, txtOutput, txtGateLabel, txtFeedback;
    private TextView txtBossInputA, txtBossInputB, txtBossInputC, txtBossOutput;

    private FrameLayout layoutCircuit;
    private LinearLayout layoutBossCircuit, layoutBinaryPanel, layoutOptions, layoutTextInput;
    private LinearLayout layoutCodePanel, layoutCodeOrderPanel, codeArrangeZone, layoutCodeChoicePanel;

    private LinearLayout layoutArrayPanel, layoutArrayContainer;
    private TextView txtArrayPrompt;

    private final ArrayList<TextView> arrayBoxes = new ArrayList<>();
    private int selectedArrayIndex = -1;

    private LinearLayout layoutStackPanel;
    private FrameLayout layoutStackCanvas;
    private TextView txtStackPrompt;
    private TextView bookNew, bookTop, bookMiddle, bookBottom;
    private View dropTop, dropMiddle, dropBottom, dropUnder;
    private boolean stackAnswerLocked = false;

    private int selectedStackPosition = -1;

    private LinearLayout layoutQueuePanel;
    private FrameLayout layoutQueueCanvas;
    private TextView txtQueuePrompt;

    private ImageView personA, personB, personC, personD;
    private TextView labelA, labelB, labelC, labelD;

    private View dropBeforeA, dropBetweenAB, dropBetweenBC, dropAfterC;

    private int selectedQueuePosition = -1;
    private boolean queueAnswerLocked = false;

    private LinearLayout layoutTreePanel;
    private TextView txtTreePrompt, txtTreeHint, txtTreeSequence;
    private Button btnTreeReset;
    private TreeCanvasView treeCanvasView;

    private final ArrayList<Integer> tappedTreeOrder = new ArrayList<>();

    private TextView txtCodePanel, txtCodePrompt, txtCodeOrderPrompt;

    private TextView txtCodeChoicePrompt, txtCodeChoice1, txtCodeChoice2;
    private int selectedCodeChoice = 0;

    private ImageView imgBinaryBulb1, imgBinaryBulb2, imgBinaryBulb3, imgBinaryBulb4;
    private TextView txtBinaryBit1, txtBinaryBit2, txtBinaryBit3, txtBinaryBit4;

    private ImageView imgInputA, imgInputB, imgOutput, imgGate;
    private ImageView imgBossInputA, imgBossInputB, imgBossInputC, imgBossOutput;
    private ImageView imgBossGate1, imgBossGate2, imgBossGateNot;

    private Button btnOption1, btnOption2, btnOption3, btnOption4, btnCheck, btnNext;
    private EditText editAnswer;

    private int worldNumber;
    private int levelNumber;
    private int currentScenario = 0;
    private int score = 0;

    // Tracks whether each scenario is still eligible for a star.
    // If the user gets it wrong once, that scenario loses its star,
    // even if they later answer correctly.
    private boolean[] scenarioStarEligible;

    private Level currentLevel;
    private ArrayList<Challenge> challenges;

    private boolean selectedOutput = false;
    private String selectedOption = "";

    private final ArrayList<String> droppedCodeOrder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        worldNumber = getIntent().getIntExtra("WORLD", 1);
        levelNumber = getIntent().getIntExtra("LEVEL", 1);

        connectViews();
        setupOptionButtons();

        currentLevel = GameRepository.getLevel(worldNumber, levelNumber);
        challenges = currentLevel.getChallenges();

        scenarioStarEligible = new boolean[challenges.size()];
        Arrays.fill(scenarioStarEligible, true);

        showScenario();

        imgOutput.setOnClickListener(v -> {
            Challenge challenge = getCurrentChallenge();

            if (challenge instanceof LogicChallenge) {
                selectedOutput = !selectedOutput;
                updateSimpleOutputBulb();
            }
        });

        imgBossOutput.setOnClickListener(v -> {
            Challenge challenge = getCurrentChallenge();

            if (challenge instanceof BossLogicChallenge) {
                selectedOutput = !selectedOutput;
                updateBossOutputBulb();
            }
        });

        btnCheck.setOnClickListener(v -> checkAnswer());

        btnNext.setOnClickListener(v -> {
            currentScenario++;

            if (currentScenario < challenges.size()) {
                showScenario();
            } else {
                openResultScreen();
            }
        });
    }

    private void connectViews() {
        txtLevelTitle = findViewById(R.id.txtLevelTitle);
        txtScenarioProgress = findViewById(R.id.txtScenarioProgress);
        txtInstruction = findViewById(R.id.txtInstruction);

        txtInputA = findViewById(R.id.txtInputA);
        txtInputB = findViewById(R.id.txtInputB);
        txtOutput = findViewById(R.id.txtOutput);
        txtGateLabel = findViewById(R.id.txtGateLabel);
        txtFeedback = findViewById(R.id.txtFeedback);

        layoutBinaryPanel = findViewById(R.id.layoutBinaryPanel);
        layoutArrayPanel = findViewById(R.id.layoutArrayPanel);
        layoutArrayContainer = findViewById(R.id.layoutArrayContainer);
        txtArrayPrompt = findViewById(R.id.txtArrayPrompt);

        imgBinaryBulb1 = findViewById(R.id.imgBinaryBulb1);
        imgBinaryBulb2 = findViewById(R.id.imgBinaryBulb2);
        imgBinaryBulb3 = findViewById(R.id.imgBinaryBulb3);
        imgBinaryBulb4 = findViewById(R.id.imgBinaryBulb4);

        txtBinaryBit1 = findViewById(R.id.txtBinaryBit1);
        txtBinaryBit2 = findViewById(R.id.txtBinaryBit2);
        txtBinaryBit3 = findViewById(R.id.txtBinaryBit3);
        txtBinaryBit4 = findViewById(R.id.txtBinaryBit4);

        txtBossInputA = findViewById(R.id.txtBossInputA);
        txtBossInputB = findViewById(R.id.txtBossInputB);
        txtBossInputC = findViewById(R.id.txtBossInputC);
        txtBossOutput = findViewById(R.id.txtBossOutput);

        imgInputA = findViewById(R.id.imgInputA);
        imgInputB = findViewById(R.id.imgInputB);
        imgOutput = findViewById(R.id.imgOutput);
        imgGate = findViewById(R.id.imgGate);

        imgBossInputA = findViewById(R.id.imgBossInputA);
        imgBossInputB = findViewById(R.id.imgBossInputB);
        imgBossInputC = findViewById(R.id.imgBossInputC);
        imgBossOutput = findViewById(R.id.imgBossOutput);

        imgBossGate1 = findViewById(R.id.imgBossGate1);
        imgBossGate2 = findViewById(R.id.imgBossGate2);
        imgBossGateNot = findViewById(R.id.imgBossGateNot);

        layoutCircuit = findViewById(R.id.layoutCircuit);
        layoutBossCircuit = findViewById(R.id.layoutBossCircuit);
        layoutOptions = findViewById(R.id.layoutOptions);
        layoutTextInput = findViewById(R.id.layoutTextInput);

        layoutStackPanel = findViewById(R.id.layoutStackPanel);
        layoutStackCanvas = findViewById(R.id.layoutStackCanvas);
        txtStackPrompt = findViewById(R.id.txtStackPrompt);

        layoutQueuePanel = findViewById(R.id.layoutQueuePanel);
        layoutQueueCanvas = findViewById(R.id.layoutQueueCanvas);
        txtQueuePrompt = findViewById(R.id.txtQueuePrompt);

        personA = findViewById(R.id.personA);
        personB = findViewById(R.id.personB);
        personC = findViewById(R.id.personC);
        personD = findViewById(R.id.personD);

        labelA = findViewById(R.id.labelA);
        labelB = findViewById(R.id.labelB);
        labelC = findViewById(R.id.labelC);
        labelD = findViewById(R.id.labelD);

        dropBeforeA = findViewById(R.id.dropBeforeA);
        dropBetweenAB = findViewById(R.id.dropBetweenAB);
        dropBetweenBC = findViewById(R.id.dropBetweenBC);
        dropAfterC = findViewById(R.id.dropAfterC);

        bookNew = findViewById(R.id.bookNew);
        bookTop = findViewById(R.id.bookTop);
        bookMiddle = findViewById(R.id.bookMiddle);
        bookBottom = findViewById(R.id.bookBottom);

        dropTop = findViewById(R.id.dropTop);
        dropMiddle = findViewById(R.id.dropMiddle);
        dropBottom = findViewById(R.id.dropBottom);
        dropUnder = findViewById(R.id.dropUnder);

        layoutTreePanel = findViewById(R.id.layoutTreePanel);
        txtTreePrompt = findViewById(R.id.txtTreePrompt);
        txtTreeHint = findViewById(R.id.txtTreeHint);
        txtTreeSequence = findViewById(R.id.txtTreeSequence);
        btnTreeReset = findViewById(R.id.btnTreeReset);
        treeCanvasView = findViewById(R.id.treeCanvasView);

        layoutCodePanel = findViewById(R.id.layoutCodePanel);
        txtCodePanel = findViewById(R.id.txtCodePanel);
        txtCodePrompt = findViewById(R.id.txtCodePrompt);

        layoutCodeOrderPanel = findViewById(R.id.layoutCodeOrderPanel);
        txtCodeOrderPrompt = findViewById(R.id.txtCodeOrderPrompt);
        codeArrangeZone = findViewById(R.id.codeArrangeZone);

        layoutCodeChoicePanel = findViewById(R.id.layoutCodeChoicePanel);
        txtCodeChoicePrompt = findViewById(R.id.txtCodeChoicePrompt);
        txtCodeChoice1 = findViewById(R.id.txtCodeChoice1);
        txtCodeChoice2 = findViewById(R.id.txtCodeChoice2);

        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);

        btnCheck = findViewById(R.id.btnCheck);
        btnNext = findViewById(R.id.btnNext);

        editAnswer = findViewById(R.id.editAnswer);
    }

    private void setupOptionButtons() {
        btnOption1.setOnClickListener(v -> selectOption(btnOption1.getText().toString()));
        btnOption2.setOnClickListener(v -> selectOption(btnOption2.getText().toString()));
        btnOption3.setOnClickListener(v -> selectOption(btnOption3.getText().toString()));
        btnOption4.setOnClickListener(v -> selectOption(btnOption4.getText().toString()));
    }

    private void selectOption(String option) {
        selectedOption = option;

        resetOptionButtonStyles();

        if (btnOption1.getText().toString().equals(option)) {
            btnOption1.setBackgroundResource(R.drawable.option_card_selected);
        } else if (btnOption2.getText().toString().equals(option)) {
            btnOption2.setBackgroundResource(R.drawable.option_card_selected);
        } else if (btnOption3.getText().toString().equals(option)) {
            btnOption3.setBackgroundResource(R.drawable.option_card_selected);
        } else if (btnOption4.getText().toString().equals(option)) {
            btnOption4.setBackgroundResource(R.drawable.option_card_selected);
        }
    }

    private void resetOptionButtonStyles() {
        btnOption1.setBackgroundResource(R.drawable.option_card);
        btnOption2.setBackgroundResource(R.drawable.option_card);
        btnOption3.setBackgroundResource(R.drawable.option_card);
        btnOption4.setBackgroundResource(R.drawable.option_card);
    }

    private Challenge getCurrentChallenge() {
        return challenges.get(currentScenario);
    }

    private void showScenario() {
        selectedOutput = false;
        selectedOption = "";
        selectedCodeChoice = 0;
        selectedArrayIndex = -1;
        selectedStackPosition = -1;
        selectedQueuePosition = -1;
        queueAnswerLocked = false;
        stackAnswerLocked = false;
        tappedTreeOrder.clear();
        droppedCodeOrder.clear();
        editAnswer.setText("");

        Challenge challenge = getCurrentChallenge();

        txtLevelTitle.setText(currentLevel.getTitle());
        txtScenarioProgress.setText("Scenario " + (currentScenario + 1) + "/" + challenges.size());
        txtFeedback.setText("");
        txtFeedback.setTextColor(getColor(R.color.cq_text));
        resetOptionButtonStyles();
        btnCheck.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);

        if (challenge instanceof LogicChallenge) {
            showLogicScenario((LogicChallenge) challenge);

        } else if (challenge instanceof BossLogicChallenge) {
            showBossLogicScenario((BossLogicChallenge) challenge);

        } else if (challenge instanceof BinaryChallenge) {
            showBinaryScenario((BinaryChallenge) challenge);

        } else if (challenge instanceof CodeOrderChallenge) {
            showCodeOrderScenario((CodeOrderChallenge) challenge);

        } else if (challenge instanceof CodeChoiceChallenge) {
            showCodeChoiceScenario((CodeChoiceChallenge) challenge);

        } else if (challenge instanceof ArrayChallenge) {
            showArrayScenario((ArrayChallenge) challenge);

        } else if (challenge instanceof StackChallenge) {
            showStackScenario((StackChallenge) challenge);

        } else if (challenge instanceof QueueChallenge) {
            showQueueScenario((QueueChallenge) challenge);

        } else if (challenge instanceof TreeChallenge) {
            showTreeScenario((TreeChallenge) challenge);

        } else if (challenge instanceof MultipleChoiceChallenge) {
            showMultipleChoiceScenario((MultipleChoiceChallenge) challenge);

        } else if (challenge instanceof TextInputChallenge) {
            showTextInputScenario((TextInputChallenge) challenge);
        }
    }

    private void showLogicScenario(LogicChallenge challenge) {
        hideAllChallengeLayouts();
        layoutCircuit.setVisibility(View.VISIBLE);

        txtInstruction.setText(currentLevel.getRuleText() + "\n\n" + challenge.getQuestion());
        txtInputA.setText("A: " + onOff(challenge.isInputA()));
        txtInputB.setText("B: " + onOff(challenge.isInputB()));

        imgInputA.setImageResource(challenge.isInputA() ? R.drawable.bulb_on : R.drawable.bulb_off);
        imgInputB.setImageResource(challenge.isInputB() ? R.drawable.bulb_on : R.drawable.bulb_off);

        setGateDrawable(imgGate, challenge.getGateType());
        txtGateLabel.setText(challenge.getGateType() + " Gate");

        updateSimpleOutputBulb();
    }

    private void showBinaryScenario(BinaryChallenge challenge) {
        hideAllChallengeLayouts();

        layoutBinaryPanel.setVisibility(View.VISIBLE);
        layoutOptions.setVisibility(View.VISIBLE);

        txtInstruction.setText(challenge.getQuestion());

        boolean[] bulbs = challenge.getBulbs();

        setBinaryColumn(imgBinaryBulb1, txtBinaryBit1, bulbs[0]);
        setBinaryColumn(imgBinaryBulb2, txtBinaryBit2, bulbs[1]);
        setBinaryColumn(imgBinaryBulb3, txtBinaryBit3, bulbs[2]);
        setBinaryColumn(imgBinaryBulb4, txtBinaryBit4, bulbs[3]);

        String[] options = challenge.getOptions();

        btnOption1.setText(options[0]);
        btnOption2.setText(options[1]);
        btnOption3.setText(options[2]);
        btnOption4.setText(options[3]);
    }

    private void showBossLogicScenario(BossLogicChallenge challenge) {
        hideAllChallengeLayouts();

        layoutBossCircuit.setVisibility(View.VISIBLE);

        txtInstruction.setText(currentLevel.getRuleText() + "\n\n" + challenge.getQuestion());

        imgBossInputA.setImageResource(challenge.isInputA() ? R.drawable.bulb_on : R.drawable.bulb_off);
        imgBossInputB.setImageResource(challenge.isInputB() ? R.drawable.bulb_on : R.drawable.bulb_off);
        imgBossInputC.setImageResource(challenge.isInputC() ? R.drawable.bulb_on : R.drawable.bulb_off);

        txtBossInputA.setText("A: " + onOff(challenge.isInputA()));
        txtBossInputB.setText("B: " + onOff(challenge.isInputB()));
        txtBossInputC.setText("C: " + onOff(challenge.isInputC()));

        setGateDrawable(imgBossGate1, challenge.getFirstGate());
        setGateDrawable(imgBossGate2, challenge.getSecondGate());
        imgBossGateNot.setImageResource(R.drawable.gate_not);

        updateBossOutputBulb();
    }

    private void showCodeChoiceScenario(CodeChoiceChallenge challenge) {
        hideAllChallengeLayouts();

        layoutCodeChoicePanel.setVisibility(View.VISIBLE);

        txtInstruction.setText(currentLevel.getRuleText());
        txtCodeChoicePrompt.setText(challenge.getQuestion());

        txtCodeChoice1.setText(challenge.getFirstCode());
        txtCodeChoice2.setText(challenge.getSecondCode());

        resetCodeChoiceSelection();

        txtCodeChoice1.setOnClickListener(v -> selectCodeChoice(1));
        txtCodeChoice2.setOnClickListener(v -> selectCodeChoice(2));
    }

    private void showArrayScenario(ArrayChallenge challenge) {
        hideAllChallengeLayouts();

        layoutArrayPanel.setVisibility(View.VISIBLE);

        txtInstruction.setText(currentLevel.getRuleText());
        txtArrayPrompt.setText(challenge.getQuestion());

        buildArrayView(challenge);

        if (challenge.getScenarioType() == 3) {
            layoutOptions.setVisibility(View.VISIBLE);

            String[] options = challenge.getOptions();

            btnOption1.setText(options[0]);
            btnOption2.setText(options[1]);
            btnOption3.setText(options[2]);
            btnOption4.setText(options[3]);

            setOptionButtonsMonospace(false);
        }
    }

    private void showStackScenario(StackChallenge challenge) {
        hideAllChallengeLayouts();

        layoutStackPanel.setVisibility(View.VISIBLE);
        txtInstruction.setText(currentLevel.getRuleText());
        txtStackPrompt.setText(challenge.getQuestion());

        selectedStackPosition = -1;

        if (challenge.getStackAction() == StackChallenge.PUSH) {
            setupStackPushScenario();
        } else {
            setupStackPopScenario();
        }
    }

    private void setupStackPushScenario() {
        stackAnswerLocked = false;

        bookNew.setVisibility(View.VISIBLE);
        bookTop.setVisibility(View.VISIBLE);
        bookMiddle.setVisibility(View.VISIBLE);
        bookBottom.setVisibility(View.VISIBLE);

        bookNew.setText("");
        bookTop.setText("");
        bookMiddle.setText("");
        bookBottom.setText("");

        bookNew.setBackgroundResource(R.drawable.book_blue);
        bookTop.setBackgroundResource(R.drawable.book_pink);
        bookMiddle.setBackgroundResource(R.drawable.book_purple);
        bookBottom.setBackgroundResource(R.drawable.book_cyan);

        positionBook(bookNew, 20, 125);
        positionBook(bookTop, 205, 75);
        positionBook(bookMiddle, 205, 120);
        positionBook(bookBottom, 205, 165);

        positionDropZone(dropTop, 190, 45);
        positionDropZone(dropMiddle, 190, 105);
        positionDropZone(dropBottom, 190, 150);
        positionDropZone(dropUnder, 190, 200);

        bookNew.setOnLongClickListener(v -> {
            if (stackAnswerLocked) return false;

            ClipData data = ClipData.newPlainText("book", "NEW");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, shadow, v, 0);
            v.setAlpha(0.45f);
            return true;
        });

        setupStackDropZone(dropTop, 0);
        setupStackDropZone(dropMiddle, 1);
        setupStackDropZone(dropBottom, 2);
        setupStackDropZone(dropUnder, 3);
    }

    private void setupStackDropZone(View dropZone, int position) {
        dropZone.setOnDragListener((v, event) -> {
            if (stackAnswerLocked) return true;

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    previewPushPlacement(position);
                    return true;

                case DragEvent.ACTION_DROP:
                    selectedStackPosition = position;
                    stackAnswerLocked = true;
                    previewPushPlacement(position);
                    bookNew.setAlpha(1f);
                    txtFeedback.setText("Book placed. Check your answer.");
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    bookNew.setAlpha(1f);
                    return true;
            }
            return true;
        });
    }

    private void previewPushPlacement(int position) {
        if (position == 0) {
            positionBook(bookNew, 205, 45);
            positionBook(bookTop, 205, 90);
            positionBook(bookMiddle, 205, 135);
            positionBook(bookBottom, 205, 180);
        } else if (position == 1) {
            positionBook(bookTop, 205, 45);
            positionBook(bookNew, 205, 90);
            positionBook(bookMiddle, 205, 135);
            positionBook(bookBottom, 205, 180);
        } else if (position == 2) {
            positionBook(bookTop, 205, 45);
            positionBook(bookMiddle, 205, 90);
            positionBook(bookNew, 205, 135);
            positionBook(bookBottom, 205, 180);
        } else {
            positionBook(bookTop, 205, 45);
            positionBook(bookMiddle, 205, 90);
            positionBook(bookBottom, 205, 135);
            positionBook(bookNew, 205, 180);
        }
    }

    private void setupStackPopScenario() {
        stackAnswerLocked = false;

        bookNew.setVisibility(View.VISIBLE);
        bookTop.setVisibility(View.VISIBLE);
        bookMiddle.setVisibility(View.VISIBLE);
        bookBottom.setVisibility(View.VISIBLE);

        bookNew.setText("");
        bookTop.setText("");
        bookMiddle.setText("");
        bookBottom.setText("");

        bookNew.setBackgroundResource(R.drawable.book_blue);
        bookTop.setBackgroundResource(R.drawable.book_pink);
        bookMiddle.setBackgroundResource(R.drawable.book_purple);
        bookBottom.setBackgroundResource(R.drawable.book_cyan);

        // centered
        positionBook(bookNew, 105, 45);
        positionBook(bookTop, 105, 90);
        positionBook(bookMiddle, 105, 135);
        positionBook(bookBottom, 105, 180);

        bookNew.setOnClickListener(v -> popBook(bookNew, 0));
        bookTop.setOnClickListener(v -> popBook(bookTop, 1));
        bookMiddle.setOnClickListener(v -> popBook(bookMiddle, 2));
        bookBottom.setOnClickListener(v -> popBook(bookBottom, 3));
    }

    private void popBook(TextView book, int position) {
        if (stackAnswerLocked) return;

        selectedStackPosition = position;
        stackAnswerLocked = true;

        book.setVisibility(View.GONE);
        compactVisibleStack();

        txtFeedback.setText("Book removed. Check your answer.");
    }

    private void compactVisibleStack() {
        int y = 45;

        if (bookNew.getVisibility() == View.VISIBLE) {
            positionBook(bookNew, 105, y);
            y += 45;
        }

        if (bookTop.getVisibility() == View.VISIBLE) {
            positionBook(bookTop, 105, y);
            y += 45;
        }

        if (bookMiddle.getVisibility() == View.VISIBLE) {
            positionBook(bookMiddle, 105, y);
            y += 45;
        }

        if (bookBottom.getVisibility() == View.VISIBLE) {
            positionBook(bookBottom, 105, y);
        }
    }

    private void positionBook(View book, int startDp, int topDp) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) book.getLayoutParams();
        params.leftMargin = dpToPx(startDp);
        params.topMargin = dpToPx(topDp);
        book.setLayoutParams(params);
    }

    private void positionDropZone(View zone, int startDp, int topDp) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) zone.getLayoutParams();
        params.leftMargin = dpToPx(startDp);
        params.topMargin = dpToPx(topDp);
        zone.setLayoutParams(params);
    }

    private void showQueueScenario(QueueChallenge challenge) {
        hideAllChallengeLayouts();

        layoutQueuePanel.setVisibility(View.VISIBLE);
        txtInstruction.setText(currentLevel.getRuleText());
        txtQueuePrompt.setText(challenge.getQuestion());

        selectedQueuePosition = -1;
        queueAnswerLocked = false;

        if (challenge.getQueueAction() == QueueChallenge.ENQUEUE) {
            setupQueueEnqueueScenario();
        } else {
            setupQueueDequeueScenario();
        }
    }

    private void setupQueueEnqueueScenario() {
        showQueuePeople(true);

        // Initial line: checkout stand, then A-B-C. D waits below.
        positionQueuePerson(personA, labelA, 100, 72);
        positionQueuePerson(personB, labelB, 152, 72);
        positionQueuePerson(personC, labelC, 204, 72);
        positionQueuePerson(personD, labelD, 204, 215);

        positionQueueDropZone(dropBeforeA, 74, 62);
        positionQueueDropZone(dropBetweenAB, 128, 62);
        positionQueueDropZone(dropBetweenBC, 180, 62);
        positionQueueDropZone(dropAfterC, 232, 62);

        personD.setOnLongClickListener(v -> {
            if (queueAnswerLocked) return false;

            ClipData data = ClipData.newPlainText("person", "D");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, shadow, v, 0);
            v.setAlpha(0.45f);
            labelD.setAlpha(0.45f);
            return true;
        });

        setupQueueDropZone(dropBeforeA, 0);
        setupQueueDropZone(dropBetweenAB, 1);
        setupQueueDropZone(dropBetweenBC, 2);
        setupQueueDropZone(dropAfterC, 3);
    }

    private void setupQueueDropZone(View dropZone, int position) {
        dropZone.setOnDragListener((v, event) -> {
            if (queueAnswerLocked) return true;

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    previewEnqueuePlacement(position);
                    return true;

                case DragEvent.ACTION_DROP:
                    selectedQueuePosition = position;
                    queueAnswerLocked = true;
                    previewEnqueuePlacement(position);
                    personD.setAlpha(1f);
                    labelD.setAlpha(1f);
                    txtFeedback.setText("Person D placed. Check your answer.");
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    personD.setAlpha(1f);
                    labelD.setAlpha(1f);
                    return true;
            }

            return true;
        });
    }

    private void previewEnqueuePlacement(int position) {
        int y = 72;

        if (position == 0) {
            positionQueuePerson(personD, labelD, 100, y);
            positionQueuePerson(personA, labelA, 152, y);
            positionQueuePerson(personB, labelB, 204, y);
            positionQueuePerson(personC, labelC, 256, y);

        } else if (position == 1) {
            positionQueuePerson(personA, labelA, 100, y);
            positionQueuePerson(personD, labelD, 152, y);
            positionQueuePerson(personB, labelB, 204, y);
            positionQueuePerson(personC, labelC, 256, y);

        } else if (position == 2) {
            positionQueuePerson(personA, labelA, 100, y);
            positionQueuePerson(personB, labelB, 152, y);
            positionQueuePerson(personD, labelD, 204, y);
            positionQueuePerson(personC, labelC, 256, y);

        } else {
            positionQueuePerson(personA, labelA, 100, y);
            positionQueuePerson(personB, labelB, 152, y);
            positionQueuePerson(personC, labelC, 204, y);
            positionQueuePerson(personD, labelD, 256, y);
        }
    }

    private void setupQueueDequeueScenario() {
        showQueuePeople(true);

        // D is already added at the back.
        positionQueuePerson(personA, labelA, 100, 72);
        positionQueuePerson(personB, labelB, 152, 72);
        positionQueuePerson(personC, labelC, 204, 72);
        positionQueuePerson(personD, labelD, 256, 72);

        personA.setOnClickListener(v -> removeQueuePerson(personA, labelA, 0));
        personB.setOnClickListener(v -> removeQueuePerson(personB, labelB, 1));
        personC.setOnClickListener(v -> removeQueuePerson(personC, labelC, 2));
        personD.setOnClickListener(v -> removeQueuePerson(personD, labelD, 3));
    }

    private void removeQueuePerson(ImageView person, TextView label, int position) {
        if (queueAnswerLocked) return;

        selectedQueuePosition = position;
        queueAnswerLocked = true;

        person.setVisibility(View.GONE);
        label.setVisibility(View.GONE);

        compactQueueAfterRemoval();

        txtFeedback.setText("Person served. Check your answer.");
    }

    private void compactQueueAfterRemoval() {
        int x = 100;
        int y = 72;

        if (personA.getVisibility() == View.VISIBLE) {
            positionQueuePerson(personA, labelA, x, y);
            x += 52;
        }

        if (personB.getVisibility() == View.VISIBLE) {
            positionQueuePerson(personB, labelB, x, y);
            x += 52;
        }

        if (personC.getVisibility() == View.VISIBLE) {
            positionQueuePerson(personC, labelC, x, y);
            x += 52;
        }

        if (personD.getVisibility() == View.VISIBLE) {
            positionQueuePerson(personD, labelD, x, y);
        }
    }

    private void showQueuePeople(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;

        personA.setVisibility(visibility);
        personB.setVisibility(visibility);
        personC.setVisibility(visibility);
        personD.setVisibility(visibility);

        labelA.setVisibility(visibility);
        labelB.setVisibility(visibility);
        labelC.setVisibility(visibility);
        labelD.setVisibility(visibility);

        personA.setAlpha(1f);
        personB.setAlpha(1f);
        personC.setAlpha(1f);
        personD.setAlpha(1f);

        labelA.setAlpha(1f);
        labelB.setAlpha(1f);
        labelC.setAlpha(1f);
        labelD.setAlpha(1f);
    }

    private void positionQueuePerson(ImageView person, TextView label, int startDp, int topDp) {
        FrameLayout.LayoutParams personParams = (FrameLayout.LayoutParams) person.getLayoutParams();
        personParams.leftMargin = dpToPx(startDp);
        personParams.topMargin = dpToPx(topDp);
        person.setLayoutParams(personParams);

        FrameLayout.LayoutParams labelParams = (FrameLayout.LayoutParams) label.getLayoutParams();
        labelParams.leftMargin = dpToPx(startDp);
        labelParams.topMargin = dpToPx(topDp + 72);
        label.setLayoutParams(labelParams);
    }

    private void positionQueueDropZone(View zone, int startDp, int topDp) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) zone.getLayoutParams();
        params.leftMargin = dpToPx(startDp);
        params.topMargin = dpToPx(topDp);
        zone.setLayoutParams(params);
    }

    private void showTreeScenario(TreeChallenge challenge) {
        hideAllChallengeLayouts();

        layoutTreePanel.setVisibility(View.VISIBLE);

        txtInstruction.setText(currentLevel.getRuleText());
        txtTreePrompt.setText(challenge.getQuestion());
        txtTreeHint.setText(challenge.getHint());

        resetTreeAttempt();

        treeCanvasView.setOnNodeTapListener((nodeValue, nodeIndex) -> {
            tappedTreeOrder.add(nodeValue);
            treeCanvasView.addTappedIndex(nodeIndex);
            updateTreeSequenceText();
        });

        btnTreeReset.setOnClickListener(v -> resetTreeAttempt());
    }

    private void resetTreeAttempt() {
        tappedTreeOrder.clear();
        treeCanvasView.clearTapped();
        txtTreeSequence.setText("Your sequence: tap nodes above");
        txtFeedback.setText("");
    }

    private void updateTreeSequenceText() {
        StringBuilder builder = new StringBuilder("Your sequence: ");

        for (int i = 0; i < tappedTreeOrder.size(); i++) {
            builder.append(tappedTreeOrder.get(i));

            if (i < tappedTreeOrder.size() - 1) {
                builder.append(" → ");
            }
        }

        txtTreeSequence.setText(builder.toString());
    }

    private boolean checkTreeAnswer(TreeChallenge challenge) {
        int[] correctSequence = challenge.getCorrectSequence();

        if (tappedTreeOrder.size() != correctSequence.length) {
            return false;
        }

        for (int i = 0; i < correctSequence.length; i++) {
            if (tappedTreeOrder.get(i) != correctSequence[i]) {
                return false;
            }
        }

        return true;
    }

    private void selectCodeChoice(int choice) {
        selectedCodeChoice = choice;

        txtCodeChoice1.setBackgroundResource(
                choice == 1 ? R.drawable.code_snippet_selected : R.drawable.code_snippet_normal
        );

        txtCodeChoice2.setBackgroundResource(
                choice == 2 ? R.drawable.code_snippet_selected : R.drawable.code_snippet_normal
        );

        txtFeedback.setText("Selected snippet " + choice);
    }

    private void resetCodeChoiceSelection() {
        selectedCodeChoice = 0;
        txtCodeChoice1.setBackgroundResource(R.drawable.code_snippet_normal);
        txtCodeChoice2.setBackgroundResource(R.drawable.code_snippet_normal);
    }

    private void showMultipleChoiceScenario(MultipleChoiceChallenge challenge) {
        hideAllChallengeLayouts();

        if (worldNumber == 2) {
            layoutCodePanel.setVisibility(View.VISIBLE);
            txtCodePanel.setText(challenge.getQuestion());
            txtCodePrompt.setText("▶ Select the correct answer:");
            txtInstruction.setText(currentLevel.getRuleText());
        } else {
            txtInstruction.setText(currentLevel.getRuleText() + "\n\n" + challenge.getQuestion());
        }

        layoutOptions.setVisibility(View.VISIBLE);

        String[] options = challenge.getOptions();

        btnOption1.setText(options[0]);
        btnOption2.setText(options[1]);
        btnOption3.setText(options[2]);
        btnOption4.setText(options[3]);

        setOptionButtonsMonospace(worldNumber == 2);
    }

    private void showTextInputScenario(TextInputChallenge challenge) {
        hideAllChallengeLayouts();

        layoutCodePanel.setVisibility(View.VISIBLE);
        layoutTextInput.setVisibility(View.VISIBLE);

        txtInstruction.setText(currentLevel.getRuleText());
        txtCodePanel.setText(challenge.getQuestion());
        txtCodePrompt.setText("▶ Type and run the correct Java statement:");

        editAnswer.setTypeface(Typeface.MONOSPACE);
    }

    private void showCodeOrderScenario(CodeOrderChallenge challenge) {
        hideAllChallengeLayouts();

        layoutCodeOrderPanel.setVisibility(View.VISIBLE);

        txtInstruction.setText(currentLevel.getRuleText());
        txtCodeOrderPrompt.setText(challenge.getQuestion());

        codeArrangeZone.removeAllViews();
        droppedCodeOrder.clear();

        String[] blocks = challenge.getCodeBlocks();
        int[] shuffledOrder = buildSimpleShuffledOrder(blocks.length);

        for (int index : shuffledOrder) {
            TextView block = createCodeBlock(blocks[index]);
            codeArrangeZone.addView(block);
        }

        setupArrangeZone();
        rebuildDroppedCodeOrder();
    }

    private void buildArrayView(ArrayChallenge challenge) {
        layoutArrayContainer.removeAllViews();
        arrayBoxes.clear();

        TextView prefix = createArrayText("int arr[] = {");
        layoutArrayContainer.addView(prefix);

        int[] values = challenge.getArrayValues();

        for (int i = 0; i < values.length; i++) {
            TextView box = createArrayBox(values[i], i, challenge);
            arrayBoxes.add(box);
            layoutArrayContainer.addView(box);

            if (i < values.length - 1) {
                layoutArrayContainer.addView(createArrayText(","));
            }
        }

        TextView suffix = createArrayText("}");
        layoutArrayContainer.addView(suffix);
    }

    private TextView createArrayText(String text) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(0xFFFFFFFF); // FIX: actual white color, not resource id
        view.setTextSize(14f);
        view.setTypeface(Typeface.MONOSPACE);
        view.setPadding(2, 0, 2, 0);
        return view;
    }

    private TextView createArrayBox(int value, int index, ArrayChallenge challenge) {
        TextView box = new TextView(this);

        box.setText(String.valueOf(value));
        box.setTextColor(0xFFFFFFFF);
        box.setTextSize(16f);
        box.setTypeface(Typeface.DEFAULT_BOLD);
        box.setGravity(android.view.Gravity.CENTER);
        box.setBackgroundResource(R.drawable.array_element_normal);
        box.setTag(value);

        int boxSize = dpToPx(40);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(boxSize, boxSize);
        params.setMargins(dpToPx(4), 0, dpToPx(4), 0);
        box.setLayoutParams(params);

        if (challenge.getScenarioType() == 1 || challenge.getScenarioType() == 2) {
            box.setOnClickListener(v -> selectArrayElement(index, challenge));
        }

        return box;
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void selectArrayElement(int index, ArrayChallenge challenge) {
        selectedArrayIndex = index;

        int[] originalValues = challenge.getArrayValues();

        for (int i = 0; i < arrayBoxes.size(); i++) {
            TextView box = arrayBoxes.get(i);
            box.setBackgroundResource(R.drawable.array_element_normal);
            box.setText(String.valueOf(originalValues[i]));
        }

        TextView selectedBox = arrayBoxes.get(index);
        selectedBox.setBackgroundResource(R.drawable.array_element_selected);

        if (challenge.getScenarioType() == 2) {
            selectedBox.setText("99");
        }
        
    }

    private int[] buildSimpleShuffledOrder(int length) {
        if (length == 4) {
            return new int[]{1, 3, 0, 2};
        }

        int[] order = new int[length];

        for (int i = 0; i < length; i++) {
            order[i] = length - 1 - i;
        }

        return order;
    }

    private TextView createCodeBlock(String code) {
        TextView block = new TextView(this);

        block.setText(code);
        block.setTextColor(0xFF80CBC4);
        block.setTextSize(14f);
        block.setTypeface(Typeface.MONOSPACE);
        block.setPadding(18, 14, 18, 14);
        block.setBackgroundResource(R.drawable.code_snippet_normal);
        block.setTag(code);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 8, 0, 8);
        block.setLayoutParams(params);

        block.setOnLongClickListener(v -> {
            ClipData data = ClipData.newPlainText("code", code);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, shadowBuilder, v, 0);
            v.setAlpha(0.35f);
            return true;
        });

        block.setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent == codeArrangeZone) {
                parent.removeView(v);
                v.setAlpha(1f);
                codeArrangeZone.addView(v);
                rebuildDroppedCodeOrder();
            }
        });

        block.setOnDragListener((targetView, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    targetView.setBackgroundResource(R.drawable.code_snippet_selected);
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    targetView.setBackgroundResource(R.drawable.code_snippet_normal);
                    return true;

                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();

                    if (draggedView != null && draggedView != targetView) {
                        moveDraggedBlockBeforeTarget(draggedView, targetView);
                    }

                    targetView.setBackgroundResource(R.drawable.code_snippet_normal);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    targetView.setBackgroundResource(R.drawable.code_snippet_normal);
                    View endedView = (View) event.getLocalState();
                    if (endedView != null) endedView.setAlpha(1f);
                    return true;
            }

            return true;
        });

        return block;
    }

    private void setupArrangeZone() {
        codeArrangeZone.setOnDragListener((view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();

                    if (draggedView != null) {
                        ViewGroup parent = (ViewGroup) draggedView.getParent();

                        if (parent != null) {
                            parent.removeView(draggedView);
                        }

                        draggedView.setAlpha(1f);
                        codeArrangeZone.addView(draggedView);
                        rebuildDroppedCodeOrder();
                    }

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    View endedView = (View) event.getLocalState();
                    if (endedView != null) endedView.setAlpha(1f);
                    return true;
            }

            return true;
        });
    }

    private void moveDraggedBlockBeforeTarget(View draggedView, View targetView) {
        ViewGroup parent = (ViewGroup) draggedView.getParent();

        if (parent != null) {
            parent.removeView(draggedView);
        }

        int targetIndex = codeArrangeZone.indexOfChild(targetView);
        draggedView.setAlpha(1f);
        codeArrangeZone.addView(draggedView, targetIndex);

        rebuildDroppedCodeOrder();
    }


    private void rebuildDroppedCodeOrder() {
        droppedCodeOrder.clear();

        for (int i = 0; i < codeArrangeZone.getChildCount(); i++) {
            View child = codeArrangeZone.getChildAt(i);
            Object tag = child.getTag();

            if (tag != null) {
                droppedCodeOrder.add(tag.toString());
            }
        }
    }

    private void hideAllChallengeLayouts() {
        layoutCircuit.setVisibility(View.GONE);
        layoutBossCircuit.setVisibility(View.GONE);
        layoutBinaryPanel.setVisibility(View.GONE);
        layoutOptions.setVisibility(View.GONE);
        layoutTextInput.setVisibility(View.GONE);
        layoutCodePanel.setVisibility(View.GONE);
        layoutCodeOrderPanel.setVisibility(View.GONE);
        layoutCodeChoicePanel.setVisibility(View.GONE);
        layoutArrayPanel.setVisibility(View.GONE);
        layoutStackPanel.setVisibility(View.GONE);
        layoutQueuePanel.setVisibility(View.GONE);
        layoutTreePanel.setVisibility(View.GONE);
    }

    private void updateSimpleOutputBulb() {
        imgOutput.setImageResource(selectedOutput ? R.drawable.bulb_on : R.drawable.bulb_off);
        txtOutput.setText("OUT: " + onOff(selectedOutput));
    }

    private void updateBossOutputBulb() {
        imgBossOutput.setImageResource(selectedOutput ? R.drawable.bulb_on : R.drawable.bulb_off);
        txtBossOutput.setText("OUT: " + onOff(selectedOutput));
    }

    private void setBinaryColumn(ImageView bulbView, TextView bitView, boolean isOn) {
        bulbView.setImageResource(isOn ? R.drawable.bulb_on : R.drawable.bulb_off);
        bitView.setText(isOn ? "1" : "0");
    }

    private void setGateDrawable(ImageView imageView, String gateType) {
        if (gateType.equals("AND")) {
            imageView.setImageResource(R.drawable.gate_and);
        } else if (gateType.equals("OR")) {
            imageView.setImageResource(R.drawable.gate_or);
        } else if (gateType.equals("XOR")) {
            imageView.setImageResource(R.drawable.gate_xor);
        } else if (gateType.equals("NOT")) {
            imageView.setImageResource(R.drawable.gate_not);
        }
    }

    private void setOptionButtonsMonospace(boolean monospace) {
        Typeface typeface = monospace ? Typeface.MONOSPACE : Typeface.DEFAULT_BOLD;

        btnOption1.setTypeface(typeface);
        btnOption2.setTypeface(typeface);
        btnOption3.setTypeface(typeface);
        btnOption4.setTypeface(typeface);
    }

    private void checkAnswer() {
        Challenge challenge = getCurrentChallenge();
        boolean correct = false;

        if (challenge instanceof LogicChallenge) {
            LogicChallenge logicChallenge = (LogicChallenge) challenge;
            correct = selectedOutput == logicChallenge.isCorrectOutput();

        } else if (challenge instanceof BossLogicChallenge) {
            BossLogicChallenge bossChallenge = (BossLogicChallenge) challenge;
            correct = selectedOutput == bossChallenge.isCorrectOutput();

        } else if (challenge instanceof BinaryChallenge) {
            BinaryChallenge binaryChallenge = (BinaryChallenge) challenge;
            correct = selectedOption.equals(binaryChallenge.getCorrectAnswer());

        } else if (challenge instanceof CodeOrderChallenge) {
            CodeOrderChallenge codeOrderChallenge = (CodeOrderChallenge) challenge;
            correct = checkCodeOrderAnswer(codeOrderChallenge);

        } else if (challenge instanceof CodeChoiceChallenge) {
            CodeChoiceChallenge codeChoiceChallenge = (CodeChoiceChallenge) challenge;
            correct = selectedCodeChoice == codeChoiceChallenge.getCorrectChoice();

        } else if (challenge instanceof ArrayChallenge) {
            ArrayChallenge arrayChallenge = (ArrayChallenge) challenge;

            if (arrayChallenge.getScenarioType() == 1 || arrayChallenge.getScenarioType() == 2) {
                correct = selectedArrayIndex == arrayChallenge.getTargetIndex();
            } else {
                correct = selectedOption.equals(arrayChallenge.getCorrectAnswer());
            }

        } else if (challenge instanceof StackChallenge) {
            correct = selectedStackPosition == 0;

        } else if (challenge instanceof QueueChallenge) {
            QueueChallenge queueChallenge = (QueueChallenge) challenge;

            if (queueChallenge.getQueueAction() == QueueChallenge.ENQUEUE) {
                correct = selectedQueuePosition == 3; // after C
            } else {
                correct = selectedQueuePosition == 0; // A leaves first
            }

        } else if (challenge instanceof TreeChallenge) {
            TreeChallenge treeChallenge = (TreeChallenge) challenge;
            correct = checkTreeAnswer(treeChallenge);

            if (correct) {
                treeCanvasView.setAllCorrect();
            }

        } else if (challenge instanceof MultipleChoiceChallenge) {
            MultipleChoiceChallenge multipleChoiceChallenge = (MultipleChoiceChallenge) challenge;
            correct = selectedOption.equals(multipleChoiceChallenge.getCorrectAnswer());

        } else if (challenge instanceof TextInputChallenge) {
            TextInputChallenge textInputChallenge = (TextInputChallenge) challenge;
            String userAnswer = normalize(editAnswer.getText().toString());
            String expectedAnswer = normalize(textInputChallenge.getCorrectAnswer());
            correct = userAnswer.equals(expectedAnswer);
        }

        if (correct) {
            if (scenarioStarEligible[currentScenario]) {
                score++;
            }

            txtFeedback.setTextColor(getColor(R.color.cq_success));
            txtFeedback.setText("Correct! " + getCorrectFeedback());

            btnCheck.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);

        } else {
            scenarioStarEligible[currentScenario] = false;

            btnCheck.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);

            String wrongMessage = "Wrong. Try again! " + getWrongFeedback();

            if (challenge instanceof StackChallenge) {
                showStackScenario((StackChallenge) challenge);
            } else if (challenge instanceof QueueChallenge) {
                resetQueueAfterWrong((QueueChallenge) challenge);
            } else if (challenge instanceof TreeChallenge) {
                resetTreeAttempt();
            }

            txtFeedback.setTextColor(getColor(R.color.cq_error));
            txtFeedback.setText(wrongMessage);
        }
    }

    private void resetQueueAfterWrong(QueueChallenge challenge) {
        selectedQueuePosition = -1;
        queueAnswerLocked = false;

        if (challenge.getQueueAction() == QueueChallenge.ENQUEUE) {
            setupQueueEnqueueScenario();
        } else {
            setupQueueDequeueScenario();
        }
    }

    private boolean checkCodeOrderAnswer(CodeOrderChallenge challenge) {
        String[] blocks = challenge.getCodeBlocks();
        int[] correctOrder = challenge.getCorrectOrder();

        if (droppedCodeOrder.size() != blocks.length) {
            txtFeedback.setText("Place all code blocks in the answer zone first.");
            return false;
        }

        List<String> expectedOrder = new ArrayList<>();

        for (int index : correctOrder) {
            expectedOrder.add(blocks[index]);
        }

        return droppedCodeOrder.equals(expectedOrder);
    }

    private String getCorrectFeedback() {
        if (worldNumber == 1 && levelNumber == 1) return "AND only turns ON when both inputs are ON.";
        if (worldNumber == 1 && levelNumber == 2) return "OR turns ON if at least one input is ON.";
        if (worldNumber == 1 && levelNumber == 3) return "XOR turns ON when inputs are different.";
        if (worldNumber == 1 && levelNumber == 4) return "Each light represents a binary digit.";
        if (worldNumber == 1 && levelNumber == 5) return "You followed the full circuit correctly.";
        if (worldNumber == 2 && levelNumber == 2) return "That is the correct print syntax.";
        if (worldNumber == 2 && levelNumber == 3) return "The code is in the correct order.";
        if (worldNumber == 2 && levelNumber == 4) return "That version is cleaner and easier to read.";
        if (worldNumber == 3 && levelNumber == 1) return "You understood the array index.";
        if (worldNumber == 3 && levelNumber == 3) return "Queue uses FIFO: First In, First Out.";
        if (worldNumber == 3 && levelNumber == 4) return "You followed the tree order properly.";
        return "Great work!";
    }

    private String getWrongFeedback() {
        if (worldNumber == 1 && levelNumber == 1) return "AND needs both inputs to be ON.";
        if (worldNumber == 1 && levelNumber == 2) return "OR is OFF only when both inputs are OFF.";
        if (worldNumber == 1 && levelNumber == 3) return "XOR is ON only for different inputs.";
        if (worldNumber == 1 && levelNumber == 4) return "Check place values: 8, 4, 2, 1.";
        if (worldNumber == 1 && levelNumber == 5) return "Trace the gates from left to right, including NOT.";
        if (worldNumber == 2 && levelNumber == 2) return "Remember: strings need quotation marks and the statement must end with ;";
        if (worldNumber == 2 && levelNumber == 3) return "Drag or tap the code blocks into the correct order.";
        if (worldNumber == 2 && levelNumber == 4) return "Look for the version that is shorter, clearer, or avoids repeated code.";
        if (worldNumber == 3 && levelNumber == 1) return "Remember: array indexes start from 0.";
        if (worldNumber == 3 && levelNumber == 3) return "Remember: enqueue adds at the back, dequeue removes from the front.";
        if (worldNumber == 3 && levelNumber == 4) return "Check the traversal rule or path and try again.";
        return "Review the concept and try again.";
    }

    private String normalize(String text) {
        return text.replace(" ", "")
                .replace("\n", "")
                .replace("\t", "")
                .trim();
    }

    private String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    private void openResultScreen() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("WORLD", worldNumber);
        intent.putExtra("LEVEL", levelNumber);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL", challenges.size());
        startActivity(intent);
        finish();
    }
}