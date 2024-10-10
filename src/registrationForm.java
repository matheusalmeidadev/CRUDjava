import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class registrationForm extends JDialog {
    private JPanel Panel;
    private JTextField tfName;
    private JPasswordField pfSenha;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JTextField tfEmail;
    private JTextField tfTelefone;
    private JTextField tfEndereco;
    private JPasswordField pfConfirma;

    public registrationForm(JFrame parent){
        super(parent);
        setTitle("Crie uma nova conta");
        setContentPane(Panel);
        setMinimumSize(new Dimension(500,500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String nome = tfName.getText();
        String email = tfEmail.getText();
        String telefone = tfTelefone.getText();
        String endereco = tfEndereco.getText();
        String senha = String.valueOf(pfSenha.getPassword());
        String confirmaSenha =  String.valueOf(pfConfirma.getPassword());

        if(nome.isBlank() || email.isBlank() || telefone.isBlank() || endereco.isBlank() || endereco.isBlank() || senha.isBlank()){
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos.",
                    "tente novamente", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!senha.equals(confirmaSenha)) {
            JOptionPane.showMessageDialog(this,
                    "Senha confirmada não confere",
                    "tente novamente", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user =  addUserToDatabase(nome, email, endereco, telefone, senha);
        if (user != null) {
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Falha ao se registrar",
                    "Tente novamente", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;
    private User addUserToDatabase(String nome, String email, String telefone, String endereco, String senha) {
        User user = null;

        final String URL = "jdbc:mysql://localhost:3306/CRUD";
        final String USER = "root";
        final String PASSWORD = "matheusenrico22";

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // conectado corretamente

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (nome, email, telefone, endereco, senha)" + "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, telefone);
            preparedStatement.setString(4, endereco);
            preparedStatement.setString(5, senha);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.nome = nome;
                user.email = email;
                user.telefone = telefone;
                user.endereco = endereco;
                user.senha = senha;
            }

            stmt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        registrationForm myForm = new registrationForm(null);
        User user = myForm.user;
        if (user != null){
            System.out.println("Registro do usuário: " + user.nome + " completo");
        }
        else {
            System.out.println("Registro cancelado");
    }
}
}

