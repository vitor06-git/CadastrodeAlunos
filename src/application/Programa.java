package application;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import db.DB;


public class Programa {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cadastro de Alunos");
        frame.setSize(700, 600);
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
        
        JLabel lblEmail = new JLabel("Email:");  
        lblEmail.setBounds(30, 190, 100, 25);  
        frame.add(lblEmail);  
        
        JTextField txtEmail = new JTextField();  
        txtEmail.setBounds(120, 190, 200, 25);  
        frame.add(txtEmail);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 230, 100, 25);
        frame.add(lblTelefone);

        JTextField txtTelefone = new JTextField();
        txtTelefone.setBounds(120, 230, 200, 25);
        frame.add(txtTelefone);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(30, 270, 100, 30);
        frame.add(btnSalvar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(150, 270, 100, 30);
        frame.add(btnAtualizar);

        JButton btnDeletar = new JButton("Deletar");
        btnDeletar.setBounds(270, 270, 100, 30);
        frame.add(btnDeletar);

        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Nome", "Faixa", "Grau", "Idade", "Email", "Telefone"});
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 300, 620, 200);
        frame.add(scrollPane);

        loadTableData(model);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String faixa = txtFaixa.getText();
                String grau = txtGrau.getText();
                String email = txtEmail.getText();
                String telefone = txtTelefone.getText();
                int idade;
                

                try {
                    idade = Integer.parseInt(txtIdade.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Idade deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                cadastrarAluno(nome, faixa, grau, idade, email, telefone, frame);
                loadTableData(model);
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Selecione um aluno para atualizar!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = (int) model.getValueAt(selectedRow, 0);
                String nome = txtNome.getText();
                String faixa = txtFaixa.getText();
                String grau = txtGrau.getText();
                String email = txtEmail.getText();
                String telefone = txtTelefone.getText();
                int idade;

                try {
                    idade = Integer.parseInt(txtIdade.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Idade deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                atualizarAluno(id, nome, faixa, grau, idade, email, telefone, frame);
                loadTableData(model);
            }
        });

        btnDeletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Selecione um aluno para deletar!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = (int) model.getValueAt(selectedRow, 0);
                deletarAluno(id, frame);
                loadTableData(model);
            }
        });

        frame.setVisible(true);
    }

    private static void cadastrarAluno(String nome, String faixa, String grau, int idade, String email, String telefone, JFrame frame) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO alunos (nome, faixa, grau, idade, email, telefone) VALUES (?, ?, ?, ?)");) {

            ps.setString(1, nome);
            ps.setString(2, faixa);
            ps.setString(3, grau);
            ps.setInt(4, idade);
            ps.setString(5, email);
            ps.setString(6, telefone);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Aluno cadastrado com sucesso!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao cadastrar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void atualizarAluno(int id, String nome, String faixa, String grau, int idade, String email, String telefone, JFrame frame) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE alunos SET nome = ?, faixa = ?, grau = ?, idade = ?, email = ?, telefone = ? WHERE id = ?");) {

            ps.setString(1, nome);
            ps.setString(2, faixa);
            ps.setString(3, grau);
            ps.setInt(4, idade);
			ps.setString(5, email);
			ps.setString(6, telefone);
            ps.setInt(7, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Aluno atualizado com sucesso!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao atualizar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void deletarAluno(int id, JFrame frame) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM alunos WHERE id = ?");) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Aluno deletado com sucesso!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao deletar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void loadTableData(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM alunos");
             ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("nome"), rs.getString("faixa"), rs.getString("grau"), rs.getInt("idade"), rs.getString("email"), rs.getString("telefone")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados da tabela: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
