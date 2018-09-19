package br.com.thiago.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.thiago.ceep.R;
import br.com.thiago.ceep.model.Nota;

import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;
    private final String TITULO_APPBAR_INSERE = "Insere nota";
    private final String TITULO_APPBAR_ALTERA = "Altera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR_INSERE);
        inicializaCampos();

        Intent dadosRecebidos = getIntent();
        if (dadosRecebidos.hasExtra(CHAVE_NOTA)){
            setTitle(TITULO_APPBAR_ALTERA + " nota");
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            preencheCampos(notaRecebida);
        }
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(MenuSalvaNota(item)){
            Nota notaCriada = CriaNota();
            RetornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void RetornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    @NonNull
    private Nota CriaNota() {
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean MenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_salva_ic;
    }
}
