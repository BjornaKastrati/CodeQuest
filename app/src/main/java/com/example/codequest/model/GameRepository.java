package com.example.codequest.model;

public class GameRepository {

    public static World getWorld(int worldNumber) {
        if (worldNumber == 1) {
            return createWorld1();
        } else if (worldNumber == 2) {
            return createWorld2();
        } else {
            return createWorld3();
        }
    }

    public static Level getLevel(int worldNumber, int levelNumber) {
        World world = getWorld(worldNumber);

        for (Level level : world.getLevels()) {
            if (level.getLevelNumber() == levelNumber) {
                return level;
            }
        }

        return world.getLevels().get(0);
    }

    private static World createWorld1() {
        World world = new World(
                1,
                "Digital Logic Lab",
                "Turn logic into light."
        );

        Level level1 = new Level(
                1,
                "AND Gate",
                "AND turns ON only when both inputs are ON."
        );

        level1.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the AND result.",
                true,
                true,
                true,
                "AND"
        ));

        level1.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the AND result.",
                true,
                false,
                false,
                "AND"
        ));

        level1.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the AND result.",
                false,
                false,
                false,
                "AND"
        ));

        world.addLevel(level1);

        Level level2 = new Level(
                2,
                "OR Gate",
                "OR turns ON when at least one input is ON."
        );

        level2.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the OR result.",
                true,
                false,
                true,
                "OR"
        ));

        level2.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the OR result.",
                false,
                true,
                true,
                "OR"
        ));

        level2.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the OR result.",
                false,
                false,
                false,
                "OR"
        ));

        world.addLevel(level2);

        Level level3 = new Level(
                3,
                "XOR Gate",
                "XOR turns ON only when the inputs are different."
        );

        level3.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the XOR result.",
                true,
                false,
                true,
                "XOR"
        ));

        level3.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the XOR result.",
                false,
                true,
                true,
                "XOR"
        ));

        level3.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the XOR result.",
                true,
                true,
                false,
                "XOR"
        ));

        level3.addChallenge(new LogicChallenge(
                "Tap the OUT bulb to match the XOR result.",
                false,
                false,
                false,
                "XOR"
        ));

        world.addLevel(level3);

        Level level4 = new Level(
                4,
                "Binary Lights",
                ""
        );

        level4.addChallenge(new BinaryChallenge(
                "What are the light bulbs trying to tell you? Convert this binary number to decimal.",
                new boolean[]{true, false, true, false},
                new String[]{"8", "10", "12", "14"},
                "10"
        ));

        level4.addChallenge(new BinaryChallenge(
                "Convert this binary number to decimal.",
                new boolean[]{false, true, true, false},
                new String[]{"4", "6", "8", "10"},
                "6"
        ));

        level4.addChallenge(new BinaryChallenge(
                "Convert this binary number to decimal.",
                new boolean[]{true, true, false, true},
                new String[]{"11", "12", "13", "15"},
                "13"
        ));

        world.addLevel(level4);

        Level level5 = new Level(
                5,
                "Logic Circuit Boss",
                "Solve the final output of each connected circuit."
        );

        level5.addChallenge(new BossLogicChallenge(
                "A and B go through AND. Then that result and C go through OR. Then NOT determines the final OUT bulb.",
                true,
                true,
                false,
                false,
                "AND",
                "OR",
                true
        ));

        level5.addChallenge(new BossLogicChallenge(
                "A and B go through OR. Then that result and C go through XOR. Then NOT determines the final OUT bulb.",
                true,
                false,
                true,
                true,
                "OR",
                "XOR",
                true
        ));

        level5.addChallenge(new BossLogicChallenge(
                "A and B go through OR. Then that result and C go through XOR. Then NOT determines the final OUT bulb.",
                false,
                false,
                false,
                true,
                "OR",
                "XOR",
                true
        ));

        level5.addChallenge(new BossLogicChallenge(
                "A and B go through AND. Then that result and C go through XOR. Then NOT determines the final OUT bulb.",
                true,
                true,
                false,
                false,
                "AND",
                "XOR",
                true
        ));

        world.addLevel(level5);

        return world;
    }

    private static World createWorld2() {
        World world = new World(
                2,
                "Programming Temple",
                "Read, write, order, and optimize code."
        );

        Level level1 = new Level(
                1,
                "Output Challenge",
                "Read the Java code and choose the correct output."
        );

        level1.addChallenge(new MultipleChoiceChallenge(
                "int x = 3;\nSystem.out.println(x + 2);",
                new String[]{"3", "5", "32", "Error"},
                "5"
        ));

        level1.addChallenge(new MultipleChoiceChallenge(
                "int a = 4;\nint b = 2;\nSystem.out.println(a * b);",
                new String[]{"6", "8", "42", "Error"},
                "8"
        ));

        level1.addChallenge(new MultipleChoiceChallenge(
                "int x = 10;\nSystem.out.println(x - 3);",
                new String[]{"13", "7", "3", "Error"},
                "7"
        ));

        world.addLevel(level1);

        Level level2 = new Level(
                2,
                "Print Statement",
                "Write the correct Java print statement."
        );

        level2.addChallenge(new TextInputChallenge(
                "Task: Print the word Hello.",
                "System.out.println(\"Hello\");"
        ));

        level2.addChallenge(new TextInputChallenge(
                "Task: Print the number 10.",
                "System.out.println(10);"
        ));

        level2.addChallenge(new TextInputChallenge(
                "Task: Print the variable x.",
                "System.out.println(x);"
        ));

        world.addLevel(level2);

        Level level3 = new Level(
                3,
                "Code Ordering",
                "Drag the code blocks into the correct order."
        );

        level3.addChallenge(new CodeOrderChallenge(
                "Build a correct if statement.",
                new String[]{
                        "if (age >= 18) {",
                        "System.out.println(\"Access granted\");",
                        "}",
                        "else { System.out.println(\"Too young\"); }"
                },
                new int[]{0, 1, 2, 3}
        ));

        level3.addChallenge(new CodeOrderChallenge(
                "Build a simple for loop.",
                new String[]{
                        "for (int i = 1; i <= 3; i++) {",
                        "System.out.println(i);",
                        "}",
                        "// loop finished"
                },
                new int[]{0, 1, 2, 3}
        ));

        level3.addChallenge(new CodeOrderChallenge(
                "Build a small method.",
                new String[]{
                        "public static void greet() {",
                        "System.out.println(\"Hello\");",
                        "}",
                        "greet();"
                },
                new int[]{0, 1, 2, 3}
        ));

        world.addLevel(level3);

        Level level4 = new Level(
                4,
                "Code Optimization",
                "Choose the cleaner code snippet."
        );

        level4.addChallenge(new CodeChoiceChallenge(
                "Which version is cleaner?",
                "int x = 5;\nx = x + 1;",
                "int x = 5;\nx++;",
                2
        ));

        level4.addChallenge(new CodeChoiceChallenge(
                "Which condition is cleaner?",
                "if (x == true) {\n    System.out.println(\"Yes\");\n}",
                "if (x) {\n    System.out.println(\"Yes\");\n}",
                2
        ));

        level4.addChallenge(new CodeChoiceChallenge(
                "Which version avoids repeated code?",
                "System.out.println(\"Hello\");\nSystem.out.println(\"Hello\");",
                "for (int i = 0; i < 2; i++) {\n    System.out.println(\"Hello\");\n}",
                2
        ));

        world.addLevel(level4);

        Level level5 = new Level(
                5,
                "Programming Boss",
                "Mixed programming challenge."
        );

        level5.addChallenge(new MultipleChoiceChallenge(
                "int x = 2;\nSystem.out.println(x * 3);",
                new String[]{"5", "6", "23", "Error"},
                "6"
        ));

        level5.addChallenge(new MultipleChoiceChallenge(
                "Fix this code:\nSystem.out.println Hello;",
                new String[]{
                        "System.out.println(\"Hello\");",
                        "System.out.println Hello;",
                        "print(Hello)",
                        "System.out(Hello);"
                },
                "System.out.println(\"Hello\");"
        ));

        level5.addChallenge(new MultipleChoiceChallenge(
                "int x = 7;\nif(x > 5)\n    System.out.println(\"Big\");\n\nWill it print?",
                new String[]{"Yes", "No", "Only if x is 5", "Error"},
                "Yes"
        ));

        world.addLevel(level5);

        return world;
    }

    private static World createWorld3() {
        World world = new World(
                3,
                "Data Structures Forest",
                "Master how data is stored and moved."
        );

        Level level1 = new Level(
                1,
                "Array Explorer",
                "Explore arrays and understand indexing."
        );

        level1.addChallenge(new ArrayChallenge(
                "Tap the element at index 2.",
                new int[]{4, 1, 3, 2},
                1,
                2,
                null,
                ""
        ));

        level1.addChallenge(new ArrayChallenge(
                "Change index 1 to 99. What will the array look like?",
                new int[]{4, 6, 3, 2},
                2,
                1,
                null,
                ""
        ));

        level1.addChallenge(new ArrayChallenge(
                "What is the value stored at index 3?",
                new int[]{7, 5, 8, 10},
                3,
                -1,
                new String[]{"5", "7", "8", "10"},
                "10"
        ));

        level1.addChallenge(new ArrayChallenge(
                "What happens if we try to access arr[4]?",
                new int[]{2, 4, 6, 8},
                3,
                -1,
                new String[]{
                        "It returns 8",
                        "It returns null",
                        "It throws ArrayIndexOutOfBoundsException",
                        "It compiles but does nothing"
                },
                "It throws ArrayIndexOutOfBoundsException"
        ));

        world.addLevel(level1);

        Level level2 = new Level(
                2,
                "Stack Master",
                "Stack uses LIFO: Last In, First Out."
        );

        level2.addChallenge(new StackChallenge(
                "push(): Drag the NEW book into the stack. Where should it go?",
                StackChallenge.PUSH
        ));

        level2.addChallenge(new StackChallenge(
                "pop(): Tap the book that should be removed first from the stack.",
                StackChallenge.POP
        ));

        level2.addChallenge(new MultipleChoiceChallenge(
                "Which item is removed first in a stack?",
                new String[]{"First added", "Last added", "Random", "Middle"},
                "Last added"
        ));

        world.addLevel(level2);

        Level level3 = new Level(
                3,
                "Queue Flow",
                "Queue uses FIFO: First In, First Out."
        );

        level3.addChallenge(new QueueChallenge(
                "enqueue(): Drag person D into the queue. Where should D wait?",
                QueueChallenge.ENQUEUE
        ));

        level3.addChallenge(new QueueChallenge(
                "dequeue(): Tap the person who should be served first.",
                QueueChallenge.DEQUEUE
        ));

        level3.addChallenge(new MultipleChoiceChallenge(
                "Who gets served first in a queue?",
                new String[]{"Last person", "First person", "Random", "Middle"},
                "First person"
        ));

        world.addLevel(level3);

        Level level4 = new Level(
                4,
                "Tree Explorer",
                "Tap tree nodes in the correct order."
        );

        level4.addChallenge(new TreeChallenge(
                "In-order traversal: tap the nodes in LNR order.",
                TreeChallenge.INORDER,
                new int[]{2, 4, 6, 8, 12, 14},
                "In-order means: Left → Root → Right."
        ));

        level4.addChallenge(new TreeChallenge(
                "Pre-order traversal: tap the nodes in NLR order.",
                TreeChallenge.PREORDER,
                new int[]{8, 4, 2, 6, 12, 14},
                "Pre-order means: Root → Left → Right."
        ));

        level4.addChallenge(new TreeChallenge(
                "Post-order traversal: tap the nodes in LRN order.",
                TreeChallenge.POSTORDER,
                new int[]{2, 6, 4, 14, 12, 8},
                "Post-order means: Left → Right → Root."
        ));

        level4.addChallenge(new TreeChallenge(
                "Path finding: tap the path from root 8 to node 6.",
                TreeChallenge.PATH,
                new int[]{8, 4, 6},
                "Start at the root, then follow the correct branch."
        ));

        world.addLevel(level4);

        Level level5 = new Level(
                5,
                "Data Boss",
                "Mixed data structures challenge."
        );

        level5.addChallenge(new MultipleChoiceChallenge(
                "Stack<Integer> s;\nWhich operation removes the top?",
                new String[]{"push", "pop", "add", "removeLast"},
                "pop"
        ));

        level5.addChallenge(new MultipleChoiceChallenge(
                "Queue<String> q;\nWho leaves first?",
                new String[]{"Last", "First", "Random", "Middle"},
                "First"
        ));

        level5.addChallenge(new MultipleChoiceChallenge(
                "Which structure uses LIFO?",
                new String[]{"Array", "Queue", "Stack", "Tree"},
                "Stack"
        ));

        level5.addChallenge(new MultipleChoiceChallenge(
                "Which structure uses FIFO?",
                new String[]{"Stack", "Queue", "Tree", "Array"},
                "Queue"
        ));

        world.addLevel(level5);

        return world;
    }
}