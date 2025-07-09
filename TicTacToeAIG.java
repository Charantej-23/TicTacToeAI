import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;G
import javax.swing.Timer;

public class TicTacToeAI extends JFrame implements ActionListener {
    JButton[][] buttons = new JButton[3][3];
    char currentPlayer = 'X';
    JLabel statusLabel = new JLabel("Your Turn (X)");
    JButton resetButton = new JButton("Reset");

    /**
     * 
     */
    public TicTacToeAI() {
        setTitle("Tic Tac Toe - VS AI");
        setSize(400, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel board = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 60);

        // Initialize buttons
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].addActionListener(this);
                board.add(buttons[i][j]);
            }

        add(statusLabel, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        resetButton.setFont(new Font("Arial", Font.PLAIN, 20));
        resetButton.addActionListener(e -> resetGame()); // ✅ Action listener
        add(resetButton, BorderLayout.SOUTH);            // ✅ Add to frame bottom
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
    JButton clicked = (JButton) e.getSource();

    if (!clicked.getText().equals("") || currentPlayer != 'X') return;

    clicked.setText("X");
    clicked.setEnabled(false);  // 🔥 This line is important!

    if (checkGameEnd()) return;

    currentPlayer = 'O';
    statusLabel.setText("AI's Turn (O)");

    // Delay AI move for UX
    Timer timer = new Timer(500, evt -> {
        makeAIMove();
        checkGameEnd();
        currentPlayer = 'X';
        statusLabel.setText("Your Turn (X)");
    });
    timer.setRepeats(false);
    timer.start();
}
    class Move {
      int row, col;
      Move(int row, int col) {
         this.row = row;
         this.col = col;
    }
}


    void makeAIMove() {
    Move best = findBestMove();
    if (best != null) {
        buttons[best.row][best.col].setText("O");
        buttons[best.row][best.col].setEnabled(false);
    }
}
Move findBestMove() {
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (buttons[i][j].getText().equals("")) {
                buttons[i][j].setText("O");
                int score = minimax(0, false);
                buttons[i][j].setText("");
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new Move(i, j);
                }
            }
        }
    }
    return bestMove;
}
int minimax(int depth, boolean isMaximizing) {
    if (hasWon("O")) return 10 - depth;
    if (hasWon("X")) return depth - 10;
    if (isDraw()) return 0;

    if (isMaximizing) {
        int best = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText("O");
                    int score = minimax(depth + 1, false);
                    buttons[i][j].setText("");
                    best = Math.max(score, best);
                }
            }
        }
        return best;
    } else {
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText("X");
                    int score = minimax(depth + 1, true);
                    buttons[i][j].setText("");
                    best = Math.min(score, best);
                }
            }
        }
        return best;
    }
}

    boolean checkGameEnd() {
        if (hasWon("X")) {
            statusLabel.setText("🎉 You Win!");
            disableBoard();
            return true;
        } else if (hasWon("O")) {
            statusLabel.setText("🤖 AI Wins!");
            disableBoard();
            return true;
        } else if (isDraw()) {
            statusLabel.setText("😅 It's a Draw!");
            return true;
        }
        return false;
    }

    boolean hasWon(String player) {
        for (int i = 0; i < 3; i++) {
            if (checkLine(player, buttons[i][0], buttons[i][1], buttons[i][2])) return true;
            if (checkLine(player, buttons[0][i], buttons[1][i], buttons[2][i])) return true;
        }
        return checkLine(player, buttons[0][0], buttons[1][1], buttons[2][2]) ||
               checkLine(player, buttons[0][2], buttons[1][1], buttons[2][0]);
    }

    boolean checkLine(String player, JButton b1, JButton b2, JButton b3) {
        return b1.getText().equals(player) &&
               b2.getText().equals(player) &&
               b3.getText().equals(player);
    }

    boolean isDraw() {
        for (JButton[] row : buttons)
            for (JButton b : row)
                if (b.getText().equals(""))
                    return false;
        return true;
    }

    void disableBoard() {
        for (JButton[] row : buttons)
            for (JButton b : row)
                b.setEnabled(false);
    }

    public static void main(String[] args) {
        new TicTacToeAI();
    }
    void resetGame() {
    for (int i = 0; i < 3; i++)
        for (int j = 0; j < 3; j++) {
            buttons[i][j].setText("");
            buttons[i][j].setEnabled(true);
        }

    currentPlayer = 'X';
    statusLabel.setText("Your Turn (X)");
}
}