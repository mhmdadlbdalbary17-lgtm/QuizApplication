package com.mycompany.quizapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class QuizApplication {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:quiz.db";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            
            // 1. إنشاء جدول الأسئلة لو مش موجود
            String createTableSQL = "CREATE TABLE IF NOT EXISTS questions (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "question TEXT NOT NULL, " +
                                    "option1 TEXT NOT NULL, " +
                                    "option2 TEXT NOT NULL, " +
                                    "option3 TEXT NOT NULL, " +
                                    "option4 TEXT NOT NULL, " +
                                    "correctAnswer INTEGER NOT NULL);";
            stmt.execute(createTableSQL);
            
            // 2. مسح الأسئلة القديمة عشان ميتكرروش، وإدخال 5 أسئلة جديدة ومنوعة
            stmt.execute("DELETE FROM questions;");
            
            stmt.execute("INSERT INTO questions(question, option1, option2, option3, option4, correctAnswer) VALUES " +
                         "('What is the capital of Egypt?', 'Cairo', 'Alexandria', 'Giza', 'Luxor', 1);");
            stmt.execute("INSERT INTO questions(question, option1, option2, option3, option4, correctAnswer) VALUES " +
                         "('Which language is used for Android development primarily?', 'Python', 'Java', 'C++', 'Ruby', 2);");
            stmt.execute("INSERT INTO questions(question, option1, option2, option3, option4, correctAnswer) VALUES " +
                         "('What does SQL stand for?', 'Structured Query Language', 'Simple Question List', 'System Quality Link', 'Store Quick Logic', 1);");
            stmt.execute("INSERT INTO questions(question, option1, option2, option3, option4, correctAnswer) VALUES " +
                         "('Which company developed the Java programming language?', 'Microsoft', 'Apple', 'Sun Microsystems', 'Google', 3);");
            stmt.execute("INSERT INTO questions(question, option1, option2, option3, option4, correctAnswer) VALUES " +
                         "('What is the extension of Java bytecode files?', '.java', '.class', '.exe', '.txt', 2);");
            
            // 3. جلب وعرض الأسئلة على المستخدم سؤال بـ سؤال
            ResultSet rs = stmt.executeQuery("SELECT * FROM questions;");
            Scanner scanner = new Scanner(System.in);
            int score = 0;
            int totalQuestions = 0;
            
            while (rs.next()) {
                totalQuestions++;
                System.out.println("\n--- Question " + rs.getInt("id") + " ---");
                System.out.println(rs.getString("question"));
                System.out.println("1. " + rs.getString("option1"));
                System.out.println("2. " + rs.getString("option2"));
                System.out.println("3. " + rs.getString("option3"));
                System.out.println("4. " + rs.getString("option4"));
                
                System.out.print("Enter your choice (1-4): ");
                int userAnswer = scanner.nextInt();
                int correctAnswer = rs.getInt("correctAnswer");
                
                if (userAnswer == correctAnswer) {
                    System.out.println("Correct! 🎉");
                    score++;
                } else {
                    System.out.println("Wrong! ❌ The correct answer was: " + correctAnswer);
                }
            }
            
            System.out.println("\nQuiz Finished! Your Total Score: " + score + " out of " + totalQuestions);
            
            rs.close();
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}