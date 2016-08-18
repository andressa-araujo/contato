package br.com.teste.dao;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import br.com.teste.modelo.Contato;

public class ContatoDao {

	private Connection connection;

	public ContatoDao() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/contato", "root",
					"");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void adiciona(Contato contato) {
		try {
			String sql = "insert into contatos (nome, dataNascimento) values (?,?)";
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);

			stmt.setString(1, contato.getNome());
			stmt.setDate(2, new Date(contato.getDataNascimento().getTimeInMillis()));
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Contato> getLista() {
		try {
			List<Contato> contatos = new ArrayList<Contato>();
			PreparedStatement stmt = (PreparedStatement) this.connection.prepareStatement("select * from contatos");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				Contato contato = new Contato();
				contato.setId(rs.getLong("id"));
				contato.setNome(rs.getString("nome"));

				Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("dataNascimento"));
				contato.setDataNascimento(data);

				contatos.add(contato);

			}

			rs.close();
			stmt.close();
			return contatos;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void altera(Contato contatos) throws SQLException {
		PreparedStatement stmt = (PreparedStatement) connection
				.prepareStatement("update contatos set nome='" + contatos.getNome() + "', " + "dataNascimento='"
						+ contatos.getDataNascimento() + "' where id='" + contatos.getId() + "' ");

		stmt.executeUpdate();
		stmt.close();

	}

	public void remove(Contato contato) {
		  try {  
		  PreparedStatement stmt = (PreparedStatement) connection.prepareStatement("delete from contatos where id=?");
		  stmt.setLong(1, contato.getId());
		  stmt.execute();
		  stmt.close();
		  } catch (SQLException e) {
		  throw new RuntimeException(e);
		  }
	}
}
