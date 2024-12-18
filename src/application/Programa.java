package application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;

public class Programa {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cadastro de Alunos");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        frame.getContentPane().setBackground(Color.GRAY);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        frame.add(lblNome);

        JTextField txtNome = new JTextField();
        txtNome.setBounds(120, 30, 200, 25);
        frame.add(txtNome);

        JLabel lblFaixa = new JLabel("Faixa:");
        lblFaixa.setBounds(30, 70, 100, 25);
        frame.add(lblFaixa);

        JTextField txtFaixa = new JTextField();
        txtFaixa.setBounds(120, 70, 200, 25);
        frame.add(txtFaixa);

        JLabel lblGrau = new JLabel("Grau:");
        lblGrau.setBounds(30, 110, 100, 25);
        frame.add(lblGrau);

        JTextField txtGrau = new JTextField();
        txtGrau.setBounds(120, 110, 200, 25);
        frame.add(txtGrau);

        JLabel lblIdade = new JLabel("Idade:");
        lblIdade.setBounds(30, 150, 100, 25);
        frame.add(lblIdade);

        JTextField txtIdade = new JTextField();
        txtIdade.setBounds(120, 150, 200, 25);
        frame.add(txtIdade);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(150, 200, 100, 30);
        frame.add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String faixa = txtFaixa.getText();
                String grau = txtGrau.getText();
                int idade;

                try {
                    idade = Integer.parseInt(txtIdade.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Idade deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                cadastrarAluno(nome, faixa, grau, idade, frame);
            }
        });

        frame.setVisible(true);
    }

    private static void cadastrarAluno(String nome, String faixa, String grau, int idade, JFrame frame) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DB.getConnection();
            String sql = "INSERT INTO alunos (nome, faixa, grau, idade) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setString(1, nome);
            ps.setString(2, faixa);
            ps.setString(3, grau);
            ps.setInt(4, idade);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Aluno cadastrado com sucesso!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao cadastrar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            DB.closeConnection();
        }
    }
}



