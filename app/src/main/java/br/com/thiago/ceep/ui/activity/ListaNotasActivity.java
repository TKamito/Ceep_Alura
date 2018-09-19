package br.com.thiago.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.thiago.ceep.R;
import br.com.thiago.ceep.dao.NotaDAO;
import br.com.thiago.ceep.model.Nota;
import br.com.thiago.ceep.ui.recycler.adapter.ListaNotasAdapter;
import br.com.thiago.ceep.ui.recycler.adapter.listener.OnItemClickListener;
import br.com.thiago.ceep.ui.recycler.helper.callback.NotaItemTouchHelperCallBack;

import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.thiago.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;
    private final String TITULO_APPBAR = "Notas";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNotas = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoInsereNota(requestCode, data)) {

            if (resultadoOK(resultCode)){

                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adciona(notaRecebida);
            }
        }

        if (ehResultadoAlteraNota(requestCode, data)) {

            if (resultadoOK(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                if (ehPosicaoValida(posicaoRecebida)){
                    altera(notaRecebida, posicaoRecebida);

                }
            }
        }
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return CodigoRequisicaoAlteraNota(requestCode) && temNota(data);
    }

    private void adciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adciona(nota);
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) { return CodigoRequisicaoInsereNota(requestCode) && temNota(data); }

    private boolean temNota(Intent data) { return data != null && data.hasExtra(CHAVE_NOTA); }

    private boolean resultadoOK(int resultCode) { return resultCode == Activity.RESULT_OK; }

    private boolean CodigoRequisicaoInsereNota(int requestCode) { return requestCode == CODIGO_REQUISICAO_INSERE_NOTA; }

    private boolean CodigoRequisicaoAlteraNota(int requestCode) { return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;  }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallBack(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int position) {
                vaiParaFormularioNotaActivityAltera(nota, position);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int position) {
        Intent notaEnviada = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        notaEnviada.putExtra(CHAVE_NOTA, nota);
        notaEnviada.putExtra(CHAVE_POSICAO, position);
        startActivityForResult(notaEnviada, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}
